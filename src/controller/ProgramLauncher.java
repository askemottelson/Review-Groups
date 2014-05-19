package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import view.MessageSystem;
import model.*;
import java.util.ArrayList;



public class ProgramLauncher {
    
    private ArrayList<Group> groups = new ArrayList<>();
    private MessageSystem ms = new MessageSystem();
    private DataBaseManager db = new DataBaseManager();
    private FileManager fm = new FileManager();

    public void run(String[] args){
        initFileSystem();
        initDataBaseSystem();
        
        /*
         * args:
         * setup
         * group (view|add|rm)
         * student (add|rm)
         * review (add|edit|send)
         * help
         */
        
        if(args.length == 0)
            ms.err(1);
        
        switch(args[0]){
            case "setup":
                ms.setup(db);
                break;
            case "group":
                this.group(args);
                break;
            case "student":
                this.student(args);
                break;
            case "assignment":
                this.assignment(args);
                break;
            case "review":
                this.review(args);
                break;
            case "help":
                ms.help();
                break;
            default:
                ms.err(-1);
                break;
        }
    }
    
    
    private void group(String[] args){
        /*
         * view
         * add <groupname>
         * rm <groupname>
         */
        String name;
        
        switch(args[1]){
            case "view":
                this.checkArgs(args,2,2);
                ms.listGroups(this.groups);
                break;
            case "add":
                this.checkArgs(args,3,2);
                name = args[2];
                db.createGroup(name);
                break;
            case "rm":
                this.checkArgs(args,3,2);
                name = args[2];
                Group g = this.findGroup(name);
                if(g == null)
                    ms.err(4);
                db.removeGroup(g);
                break;
            default:
                ms.err(3);
                break;
        }
    }
    
    
    private void student(String[] args){
        /*
         * add <name>
         * rm <name>
         */
        String group;
        String name;
        String email;
        
        switch(args[1]){
            case "add":
                this.checkArgs(args,5,2);
                group = args[2];
                name = args[3];
                email = args[4];
                Group g = this.findGroup(group);
                if(g == null)
                    ms.err(4);
                db.createUser(g,name,email);
                break;
            case "rm":
                this.checkArgs(args,3,2);
                email = args[2];
                User u = this.findUser(email);
                if(u == null)
                    ms.err(5);
                
                db.removeUser(email);
                break;
            default:
                ms.err(3);
                break;
        }
    }
    
    
    private void assignment(String[] args){
        /*
         * add <group_name> <assignment_no> <file_url> 
         * rm <group_name> <assignment_no>
         */
        String groupname;
        int assno;
        String fileurl;
        
        switch(args[1]){
            case "add":
                this.checkArgs(args,5,2);
                groupname = args[2];
                Group g = this.findGroup(groupname);
                if(g == null)
                    ms.err(4);
                
                assno = Integer.parseInt(args[3]);
                fileurl = args[4];
                this.fm.createAssigment(g, assno, fileurl);
                
                break;
            case "rm":
                this.checkArgs(args,4,2);
                groupname = args[2];
                assno = Integer.parseInt(args[3]);
                
                break;
            default:
                ms.err(3);
                break;
        }
    }
    
    
    private void review(String[] args){
        /*
         * send <assignment_no>
         */
        
        switch(args[1]){
            case "send":
                /*
                 * go through all groups
                 * get assignemtn at <ass_no>
                 * check all groups has ass <ass_no>
                 * shuffle reviews
                 * send each one
                 * 
                 */
                
                /*
                 * first review:
                 * 1->2
                 * 2->3
                 * 3->1
                 * 
                 * general:
                 * i-> ((i + ass_no) % group_numbers)
                 */
                
                this.checkArgs(args,3,2);
                int assignment_no = Integer.parseInt(args[2]);
                
                for(Group g : this.groups){
                    File assignment = fm.getFirstFile("./review-data/assignments/" + g.getName() + "/" + assignment_no + "/");
                    if(assignment == null){
                        ms.err(6);
                    }
                    g.setAssignment(assignment);
                }
                
                MailManager mm = new MailManager();
                    
                Settings s = db.getSettings();
                mm.setUsername(s.getUsername());
                mm.setPassword(s.getPassword());
                
                String message = fm.getEmailMessage();
                
                int i = 0;
                for(Group g : this.groups){
                    // group i should have group ((i + ass_no) % group_numbers) 's assignment
                    int ith = (i + assignment_no) % this.groups.size();
                    
                    // dont get your own group's review
                    if(ith == i)
                        ith++;
                    
                    File review = this.groups.get(ith).getAssignment();
                    i++;
                    
                    for(User u : g.getUsers()){
                        // update variables
                        message = message.replaceAll("\\$student\\$", u.getName());
                        message = message.replaceAll("\\$group\\$", g.getName());
                        message = message.replaceAll("\\$filename\\$", review.getName());
                        message = message.replaceAll("\\$teacher\\$", s.getTeacher());
                        
                        String name = u.getName();
                        String email = u.getEmail();
                        
                        Boolean sent = mm.send(email, "Review", message, review, review.getName());
                        if(!sent){
                            ms.err(7);
                        }
                    }
                }
                
                break;
            default:
                ms.err(3);
                break;
        }
    }
    
    
    private Group findGroup(String groupname){
        for (Group group : this.groups) {
            if (group.getName().equals(groupname)) {
                return group;
            }
        }
        return null; 
    }
    
    private User findUser(String email){
        for (Group group : this.groups) {
            for(User u : group.getUsers()){
                if (u.getEmail().equals(email)) {
                    return u;
                }
            }
            
        }
        return null; 
    }
    
    
    
    
    private void checkArgs(String[] args, int numArgs, int err){
        if(args.length != numArgs){
            ms.err(err);
        }
    }
    
    private void initFileSystem(){
        FileManager fm = new FileManager();
        
        Boolean s1 = true;
        Boolean s2 = true;
        
        if(!fm.hasBeenInit()){
            s1 = fm.createDir();
            s2 = fm.createDB();
        }
        
        if(!s1 || !s2){
            System.out.println("initFileSystem: Could not write to folder. Check permissions");
            System.exit(1);
        }
    }
    
    private void initDataBaseSystem(){
        this.groups = this.db.getGroups();
    }
}

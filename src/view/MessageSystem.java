package view;


import model.*;
import controller.DataBaseManager;
import java.io.Console;
import java.util.ArrayList;


public class MessageSystem {

    public MessageSystem() {
    
    }
    
    public void setup(DataBaseManager db){
        Console cons = System.console();
        
        System.out.print("Enter your name: ");
        String name = cons.readLine();
        
        System.out.print("Enter gmail username: ");
        String mail = cons.readLine();

        System.out.print("Enter gmail password: ");
        String pass = String.valueOf( cons.readPassword() );
        
        db.setAppInfo(name,mail,pass);
    }
    
    
    public void help(){
        System.out.println("Available arguments:");
        System.out.println("setup");
        System.out.println("group (view | add <groupname> |rm <groupname>)");
        System.out.println("student (add <group_name> <name> <email> | rm <email>)");
        System.out.println("assignment (add <group_name> <assignment_no> <file_url> | rm <group_name> <assignment_no>)");
        System.out.println("review send <assignment_no>");
        System.out.println("help");
    }
    
    public void err(int err){
        switch(err){
            case 1:
                System.out.println("Err: no arguments given");
                break;
            case 2:
                System.out.println("Err: wrong number of arguments given");
                break;
            case 3:
                System.out.println("Err: unknown command");
                break;
            case 4:
                System.out.println("Err: group name not found");
                break;
            case 5:
                System.out.println("Err: user's email not found");
                break;
            case 6:
                System.out.println("Err: not all groups has assignment for this review");
                break;
            case 7:
                System.out.println("Err: no settings found. Please run program with setup as argument");
                break;
            default:
                System.out.println("Err: unkown err");
                break;
        }
        System.exit(err);
    }
    
    public void listGroups(ArrayList<Group> list){
        for(Group g : list){
           System.out.println(g.getName() + ":");
           
           for(User u : g.getUsers()){
               System.out.println("   " + u.getName() + " (" + u.getEmail() + ")");
           }
        }
    }
    
    
}
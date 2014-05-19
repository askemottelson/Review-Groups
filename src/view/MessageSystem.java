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
        
        System.out.print("Enter gmail username: ");
        String mail = cons.readLine();

        System.out.print("Enter gmail password: ");
        String pass = cons.readPassword().toString();
        
        db.setAppInfo(mail,pass);
    }
    
    
    
    public void help(){
        
        System.out.println("1. view groups");
        System.out.println("2. add group");
        System.out.println("3. edit group");
        System.out.println("4. add review");
        System.out.println("0. exit");

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
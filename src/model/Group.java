package model;

import java.io.File;
import java.util.ArrayList;


public class Group {
    
    private int id;
    private String name;
    private ArrayList<User> users = new ArrayList<User>();
    private File assignment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void addUser(User u){
        users.add(u);
    }
    
    public ArrayList<User> getUsers(){
        return this.users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getAssignment() {
        return assignment;
    }

    public void setAssignment(File assignment) {
        this.assignment = assignment;
    }
    
    
    
}

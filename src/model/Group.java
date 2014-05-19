package model;

import java.util.ArrayList;


public class Group {
    
    private int id;
    private String name;
    private ArrayList<User> users = new ArrayList<User>();

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
    
    
}

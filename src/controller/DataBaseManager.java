package controller;

import java.util.ArrayList;
import model.*;
import java.sql.*;


public class DataBaseManager {
        
    public DataBaseManager() {
        // init DB
     

    }
    
    
    
    private void prepareQuery(String q, String[] args){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:review-data/data.db");
                       
            // update info
            PreparedStatement pstmt =  c.prepareStatement(q);
            for(int i = 0; i < args.length; i++){
                pstmt.setString(i+1, args[i]);
            }
            
            pstmt.addBatch();
            pstmt.executeBatch();
            
            pstmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    private void query(String q){
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:review-data/data.db");

            Statement stmt = c.createStatement();
            stmt.executeUpdate(q);
            stmt.close();

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    
    
    public void setAppInfo(String teacher, String user, String pass){
        this.query("DELETE FROM app_info;");
        String[] args = {teacher, user, pass};
        this.prepareQuery("INSERT INTO app_info(teacher,username,password) VALUES(?,?,?);", args);
    }
    
    public ArrayList<Group> getGroups(){
        ArrayList<Group> list = new ArrayList<>();
        
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:review-data/data.db");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
             /*
               "SELECT groups.id gid, groups.name gname, users.name uname, users.email uemail "
             + "FROM groups, users "
             + "WHERE groups.id = users.group_id;"
             */
               "SELECT * FROM groups LEFT JOIN users on users.group_id = groups.id ORDER BY groups.id;"
            );
            
            Group last_group = new Group();
            last_group.setId(-1);
            
            while (rs.next()) {
                String gname = rs.getString(2);
                int gid = rs.getInt(1);
                
                Group g;
                
                if(last_group.getId() != gid){
                    g = new Group();
                    g.setId(gid);
                    g.setName(gname);
                    list.add(g);
                } else {
                    g = last_group;
                }
                
                String uname = rs.getString(4);
                String uemail = rs.getString(5);
                if(uname != null){
                    User u = new User();
                    u.setGroup_id(g.getId());
                    u.setName(uname);
                    u.setEmail(uemail);

                    g.addUser(u);
                }
                last_group = g;
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return list;
    }
    
    public void createGroup(String name){
        String[] args = {name};
        this.prepareQuery("INSERT INTO groups(name) VALUES(?);", args);
    }
    
    public void removeGroup(Group group){
        String[] args = {group.getId() + ""};
        this.prepareQuery("DELETE FROM users WHERE group_id=?;", args);
        
        String[] args2 = {group.getName()};
        this.prepareQuery("DELETE FROM groups WHERE name=?;", args2);
    }
    
    public void removeUser(String email){
        String[] args = {email};
        this.prepareQuery("DELETE FROM users WHERE email=?;", args);
    }
    
    public void createUser(Group group, String name, String email){
        String[] args = {group.getId() + "", name, email};
        this.prepareQuery("INSERT INTO users(group_id,name,email) VALUES(?,?,?);", args);
    }
    
    public Settings getSettings(){
        ArrayList<Group> list = new ArrayList<>();
        
        Settings s = new Settings();
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:review-data/data.db");

            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(
               "SELECT * FROM app_info;"
            );
            
            while (rs.next()) {
                String teacher = rs.getString("teacher");
                String user = rs.getString("username");
                String pass = rs.getString("password");
                s.setTeacher(teacher);
                s.setUsername(user);
                s.setPassword(pass);
                break;
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        return s;
    }
}

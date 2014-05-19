package controller;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import model.Group;


public class FileManager {
    
    
    public Boolean hasBeenInit(){
        File f = new File("./review-data/");
        return f.isDirectory();
    }
    
    public Boolean createDir(){
        Boolean success = (new File("./review-data/")).mkdirs();
        return success;
    }
    
    public void createAssigment(Group g, int assignment_no, String fileurl){
        File from = new File(fileurl);
        String filename = from.getName();
        String url = "./review-data/assignments/" + g.getName() + "/" + assignment_no + "/";
        
        File to = new File(url + filename);

        File mkdirs = new File(url);
        Boolean success = mkdirs.mkdirs();
        if(!success){
            System.out.println("createAssigment err: Could not write to folder. Check permissions");
            System.exit(1);
        }
        
        try{
            Files.copy( from.toPath(), to.toPath() );
        } catch(IOException e){
            System.out.println("createAssigment err: " + e.getMessage());
        }
    }
    
    
    public File getFirstFile(String path){
        
        final File folder = new File(path);
        if(!folder.exists())
            return null;
        
        for (final File fileEntry : folder.listFiles()) {
            if (!fileEntry.isDirectory()) {
                return fileEntry;
            }
        }
        
        return null;
    }
    
    public String getEmailMessage(){
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream stream = cl.getResourceAsStream("etc/email.txt");

        BufferedReader input = new BufferedReader(new InputStreamReader(stream));
        String message = "";

        try{
            String thisline;
            while ((thisline = input.readLine()) != null) {
                message += thisline + "\n";
            }
        } catch(IOException e){
            return "";
        }

        return message;
    }
    
    public Boolean createDB(){
        try{
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream stream = cl.getResourceAsStream("etc/data.db");
            
            File file = new File("review-data/data.db");
            OutputStream out = new FileOutputStream(file);

            int readBytes = 0;
            byte[] buffer = new byte[4096];

            while ((readBytes = stream.read(buffer)) > 0) {
                out.write(buffer, 0, readBytes);
            }

            stream.close();
            out.close();

        } catch(Exception e){
            System.out.println("DB:" + e.getMessage());
            return false;
        }
        
        return true;
    }
}

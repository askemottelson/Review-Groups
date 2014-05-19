package controller;

import java.io.File;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

public class MailManager {

    private String username;
    private String password;

    
    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    public Boolean send(String to, String subject, String text, File file, String filename){
        if(this.username == null || this.password == null){
            return false;
        }
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(text, "utf-8");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            DataSource source = new FileDataSource(file.getPath()); // maybe file.path
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            System.out.println("Sending to: " + to);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            System.out.print("Could not connect to gmail. Maybe your password is wrong, or no internet connection?");
            System.exit(1);
        }
        return true;
    }
}
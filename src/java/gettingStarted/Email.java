/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 *
 * @author luilly
 */
public class Email {
    private String username = "luillyfe89@gmail.com";
    private String password = "pjfcpknyepponxoo";
    
    private Properties  propiedades;
    private Session     session;
    private MimeMessage message;
    private InternetAddress     from;
    private InternetAddress     to;

    public Email() {
        /*
         * Connection Properties
         */
        propiedades = new Properties();
        
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.socketFactory.port", "465");
        propiedades.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.port", "465");
        
        /*
         * To preparate the session
         */
        session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
    }
    
    public void sendEmail(String subject, String text) {
        /*
         * Wil building the message
         */
        message = new MimeMessage(session);
        try {
            from = new InternetAddress("luillyfe89@gmail.com");
            to   = new InternetAddress("luillyfe89@hotmail.com");
            
            message.setFrom(from);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(subject);
            message.setText(text);
        } catch (MessagingException ex) { System.err.println("error" + ex); }
        
        /*
         * To send message
         */
        try {
            Transport.send(message);
        } catch (NoSuchProviderException ex) { System.err.println("eror2" + ex); } 
          catch (MessagingException ex) { System.err.println("error3" + ex); }
    }
    
    public static void main(String[] args) {
        Email ac = new Email();
        ac.sendEmail("", "");
        System.out.println("finish!!");
        
    }
}

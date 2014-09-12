/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

/**
 *
 * @author luilly
 */
public class ControlAction {
    private Email email;
    private SMS   sms;

    public ControlAction() {
    }

    public void sendEmail(String subject, String text) {
        Email e = new Email();
        e.sendEmail(subject, text);
    }

    void sendSms() {
        throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }

    void sendSignalFirewall() {
        throw new UnsupportedOperationException("Not supported yet."); 
        //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

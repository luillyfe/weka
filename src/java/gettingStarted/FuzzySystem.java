/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.util.Map;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.LinguisticTerm;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 *
 * @author luilly
 */
public class FuzzySystem {
    private String fileName;
    private FunctionBlock fb;
    private FIS fis;
    private ControlAction ca;

    public FuzzySystem() {
        /**
         * Load Fuzzy Control Languaje
         **/
        fileName = "C:\\Users\\luilly\\Documents\\NetBeansProjects"
                                                    + "\\lib\\fcl\\ids.fcl";
        fis = FIS.load(fileName);
        
        /**
         * Throw if the FCL not is found 
         **/
        if( fis == null ) {
            System.err.println("FCL not found !!");
            return;
        }
    }
    
    /**
     * Get fuzzy output
     **/
    public LinguisticTerm getFuzzyOutput(Variable output) {
        LinguisticTerm FuzzyOutput = output.getLinguisticTerm("email");
        
        for(LinguisticTerm t: output) {
            if(t.getMembershipFunction().membership( output.getValue() ) > 
                    FuzzyOutput.getMembershipFunction().membership( output.getValue() ))
                FuzzyOutput = t;
        }
        
        return FuzzyOutput;
    }
    
    /**
     * 
     **/
    public void SetControlAction(Map<String, Integer> alertCount) {
        if( alertCount.get("block") != 0  ) {
            String subject = "New DoS attack detected ("+ alertCount.get("block") +")";
            String text = "Hoston we have a problem !!";
            
            ca = new ControlAction();
            ca.sendEmail(subject, text);
        }
        
        /*if( alertCount.get("average") != 0  )
            ca.sendSms();
        
        if( alertCount.get("serious") != 0 )
            ca.sendSignalFirewall();*/
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the fb
     */
    public FunctionBlock getFb() {
        return fb;
    }

    /**
     * @param fb the fb to set
     */
    public void setFb(FunctionBlock fb) {
        this.fb = fb;
    }

    /**
     * @return the fis
     */
    public FIS getFis() {
        return fis;
    }

    /**
     * @param fis the fis to set
     */
    public void setFis(FIS fis) {
        this.fis = fis;
    }
    
    
    public static void main(String[] args) {
        FuzzySystem fl = new FuzzySystem();
        /**
         * Show the function block
         **/
        fl.setFb(fl.getFis().getFunctionBlock("ids"));
        //JFuzzyChart.get().chart(fl.getFb());
        
        /**
         * Set input variables
         **/
        fl.getFis().setVariable("duration", 15000);
        
        /**
         * Fuzzy Control System
         **/
        fl.getFis().evaluate();
        
        /**
         * Show output variable 
         **/
        Variable alert = fl.getFb().getVariable("alert");
        JFuzzyChart.get().chart(alert, alert.getDefuzzifier(), true);
        
        /**
         * Get fuzzy output
         **/
        System.out.println( fl.getFuzzyOutput(alert).getTermName() );
        
        /**
         * Print the ruleset
         **/
        //System.out.println(fl.getFis());
        
    }
}

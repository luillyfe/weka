/*
 * 
 */
package gettingStarted;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.RuleBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * Inicia el Sistema de Deteccion de Intrusos, carga el modelo. 
 * El primer paso en la función del IBAS es escanear el directorio 
 * 'c:\Users\luilly\Documents\Data' en busca de estadisticas 
 * a nivel de conexion sobre la interfaz de una red informatica. 
 * Es importante aclarar que esta version del IBAS no se encarga de
 * la recolección, ni de la estructuacion de los datos, tampoco de
 * colocarlos en el directorio antes indicado para su analisis,
 * se espera que de alguna manera (ya sea manualmente) estos datos 
 * se ubiquen allí.
 * Seguidamente analizamos estas conexiones a fin de 
 * clasificarlas entre intrusivas y normales.
 * @author luilly
 */
public class IBAS {

    public static void main(String[] args) throws IOException, Exception {
        
        /**
         * Vigilance Systems
         **/
        new Thread(new VigilanceSystem()).start();
        /**   * ******* *    **/

        /**
         * Neural System 
         **/
        Path model = Paths.get( "C:\\Users\\luilly\\Documents\\NetBeansProjects"
                + "\\weka\\models\\AttackTypeFold290E.model" );
        NeuralSystem analyze = new NeuralSystem();
        analyze.load(model);
        
        /**  * ******* * */
        
        /**
         *  Fuzzy System
         **/
        FuzzySystem fl = new FuzzySystem();
        fl.setFb(fl.getFis().getFunctionBlock("ids"));
        Variable ControlAction = fl.getFb().getVariable("ControlAction");
        /**   * ******* *    **/
        
        
        int index = 0;
        Map<String, Integer> alertCount = new HashMap<String, Integer>();
        alertCount.put("email", 0);
        alertCount.put("sms", 0);
        alertCount.put("block", 0);
        LinkedList<Integer> duration = new LinkedList(); 
        for(;;) {
            try {
                if(VigilanceSystem.dataSets.size()>index) {
                    duration = analyze.analyze(VigilanceSystem.dataSets.get(index));
                    
                    /** To chance an control action  **/
                    for( int i: duration ) {
                        fl.getFis().setVariable("duration", i);
                        fl.getFis().evaluate();
                        //JFuzzyChart.get().chart(ControlAction, ControlAction.getDefuzzifier(), true);
                        //System.out.println( fl.getFuzzyOutput(ControlAction).getTermName() );
                        if( fl.getFuzzyOutput(ControlAction).getTermName().equalsIgnoreCase("block") ) {
                            alertCount.put("block", alertCount.get("block")+1 );
                        }
                    }
                    
                    fl.SetControlAction( alertCount );
                    alertCount.put("email", 0);
                    alertCount.put("sms", 0);
                    alertCount.put("block", 0);
                            
                    index++;
                }
            } catch(NullPointerException npe) { continue; }
        }
    }
}

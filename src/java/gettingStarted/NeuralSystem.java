/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.io.InvalidClassException;
import java.nio.file.Path;
import java.util.LinkedList;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Analiza la data en busca de caracteristicas que construyan conexiones
 * de red sospechosas de intentar comprometer las politicas de seguridad
 * del sistema.
 * @author luilly
 */
public class NeuralSystem {
    private MultilayerPerceptron  model;

    /** Cargamos el modelo */
    public void load(Path model) throws Exception {
        try {
        this.model = (MultilayerPerceptron) 
                weka.core.SerializationHelper.read(model.toString());
        
        } catch(InvalidClassException ice) { 
            System.err.println("Error garrafal\t" + ice); }
    }
    
    /** Analizamos cada conexion en busca de condiciones sospechosas  */
    public LinkedList analyze(Path test) throws Exception {
        
        /** Cargamos las conexiones que van a ser analizadas */
        DataSource   aData    = new DataSource( test.toString() );
        Instances data = aData.getDataSet();
        data.setClassIndex( data.numAttributes()-1 );
        
        /** Save the Dos Attack */
        LinkedList<Integer> dosAttack = new LinkedList();
        for( int i=100;i<data.numInstances();i++ ){
            double pred = model.classifyInstance(data.instance(i));
            
            if(pred == 1.0 || pred == 5.0 || pred == 8.0 || pred == 9.0 || 
                    pred == 10.0 || pred == 19.0) {
                dosAttack.add( (int) data.instance(i).value(0) );
                
                /*System.out.println( data.instance(i).classAttribute()
                                        .value( (int) pred ) + pred );*/
            }

        }
        
        return dosAttack;
    }    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.io.InvalidClassException;
import java.nio.file.Path;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 * Analiza la data en busca de caracteristicas que construyan conexiones
 * de red sospechosas de intentar comprometer las politicas de seguridad
 * del sistema.
 * @author luilly
 */
public class Analyze {
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
    public void analyze(Path test) throws Exception {
        
        /** Cargamos las conexiones que van a ser analizadas */
        DataSource   aData    = new DataSource( test.toString() );
        Instances data = aData.getDataSet();
        data.setClassIndex( 42 );
        
        /** Mostramos el ID de las conexiones intrusivas */
        int anomaly = 0;
        for( int i=0;i<data.numInstances();i++ ){
            double pred = model.classifyInstance(data.instance(i));
            
            if( pred == 1.0 ){
                System.out.println(data.instance(i).value(0));
                anomaly++;
            }
        }
        System.out.println(anomaly);
    }    
}

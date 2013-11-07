/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.trees.J48;
import weka.filters.Filter;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.filters.supervised.instance.StratifiedRemoveFolds;
import weka.filters.unsupervised.instance.RemovePercentage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author luilly
 * Se lee un archivo ARFF. No se realiza preprocesamiento sobre los datos
 * y se define su propiedad clase. Se elige un algoritmo basado en 
 * funciones con el objetivo de construir una red neuronal capaz de 
 * encontrar diferencias entre conexiones de red normales y de tipo 
 * intrusivas, esta vez se utiliza el perceptron multicapa.
 * lo entremamos con el 66% de los datos y el  
 * 34% restante se utiliza para evaluación. 
 */
public class gettingStarted {
    
    public static void main(String[] args) 
            throws FileNotFoundException, IOException, Exception {
        
        /* Cargamos en memoria el archivo ARFF
         * Total de datos: 125973 */
        BufferedReader rDataSet = new BufferedReader( 
                                            new FileReader("NSL-KDD.arff") );        
        
        /* Convertimos la data a un objeto Instances */
        Instances iDataSet = new Instances(rDataSet);
        rDataSet.close();
         
        /* Configuramos la clase de la data */
        iDataSet.setClassIndex( iDataSet.numAttributes() - 1 );
        
        /* Filtro */
            /* Data de entrenamiento */
        RemovePercentage rp = new RemovePercentage();
        rp.setPercentage(34.0);
        rp.setInputFormat(iDataSet);
        Instances iTrain = Filter.useFilter(iDataSet, rp);
        
            /* Data de evaluacion */
        rp.setInputFormat(iDataSet);
        rp.setInvertSelection(true);
        Instances iTest = Filter.useFilter(iDataSet, rp);
        
        /* Clasificador */
        MultilayerPerceptron mp = new MultilayerPerceptron();
        String[] oMultilayerPerceptron = weka.core.Utils.splitOptions( 
                                "-L 0.3 -M 0.2 -N 1 -V 0 -S 0 -E 20 -H a" );
        mp.setOptions(oMultilayerPerceptron);
        
        /* Entrenar */
        System.out.println(iDataSet.numInstances());
        System.out.println(iTrain.numInstances());
        System.out.println(iTest.numInstances());

        mp.buildClassifier(iTrain);
        
        /* Realizando predicciones */
        for(int i=0;i<iTest.numInstances();i++ ){
            double pred = mp.classifyInstance(iTest.instance(i));
            System.out.print("ID: "+iTest.instance(i).value(0)+" \t");
            System.out.print("Actual: "+iTest.classAttribute().value(
                                    (int)iTest.instance(i).classValue() )+" \t");
            System.out.println("Estimada: "+iTest.classAttribute().value( (int)pred ));
        }

    }
    
}

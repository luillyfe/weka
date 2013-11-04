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
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/**
 *
 * @author luilly
 * Leer dos archivos ARFF, uno para entrenamiento del clasificador y 
 * otro a fin de evaluar su precision. Se establece la propiedad objetivo
 * del clasificador, es decir su clase. Instanciamos un algoritmo de 
 * clasificaión, esta vez usaremos el c4.5. Construimos el clasificador 
 * con el ARFF  de entrenamiento. Seguidamente hacemos una serie de 
 * estimaciones para el ARFF de prueba.
 */
public class gettingStarted {
    
    public static void main(String[] args) 
            throws FileNotFoundException, IOException, Exception {
        
        /* Cargamos en memoria el archivo ARFF */
        BufferedReader readerTrain = new BufferedReader( 
                                            new FileReader("NSL-train.arff") );
        BufferedReader readerTest  = new BufferedReader( 
                                            new FileReader("NSL-test.arff") );
        Instances train = new Instances(readerTrain);
        Instances test  = new Instances(readerTest);
        readerTrain.close();
        readerTest.close();
        
        /* Configuramos la propiedad clase */
        train.setClassIndex( train.numAttributes() - 1 );
        test.setClassIndex( test.numAttributes() - 1 );
        
        /* Filtro */
        String[] options = weka.core.Utils.splitOptions( "-R 1" );
        Remove rm = new Remove();
        rm.setOptions(options);
        rm.setInputFormat(train);
        //Instances filterTrain = Filter.useFilter(train, rm);
        
        /* Clasificador */
        J48 j48 = new J48();
        j48.setUnpruned(true);
        
        /* Meta-Clasificador */
        FilteredClassifier fc = new FilteredClassifier();
        //fc.setFilter(rm);
        fc.setClassifier(j48);
        
        /* Entrenar */
        fc.buildClassifier(train);
        
        /* Realizando predicciones */
        for(int i=0;i<test.numInstances();i++ ){
            double pred = fc.classifyInstance(test.instance(i));
            System.out.print("ID: "+test.instance(i).value(0)+" \t");
            System.out.print("Actual: "+test.classAttribute().value(
                                    (int)test.instance(i).classValue() )+" \t");
            System.out.println("Estimada: "+test.classAttribute().value( (int)pred ));
        }
    }
    
}

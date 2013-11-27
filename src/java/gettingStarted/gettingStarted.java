/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.filters.Filter;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.filters.unsupervised.instance.RemovePercentage;
import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author luilly
 * A travez de la clase DataSource, leemos un archivo ARFF. 
 * No se realiza preprocesamiento sobre los datos
 * y se define su propiedad clase. Se elige un algoritmo basado en 
 * funciones con el objetivo de construir una red neuronal capaz de 
 * encontrar diferencias entre conexiones de red normales y de tipo 
 * intrusivas, esta vez se utiliza el perceptron multicapa.
 * lo entremamos con el 66% de los datos y el  
 * 34% restante se utiliza para evaluación. 
 */
public class gettingStarted {
    
    public static void main(String[] args) throws Exception {
        
        /* Cargamos en memoria el archivo ARFF
         * La clase DataSource nos ahorra 2 lineas de codigo,
         * ademas de facilitar la legibilidad del codigo.
         * Total de datos: 125973 */
        DataSource  oDataSet = new DataSource( "Pliegue 1.arff" );        
        
        /* Convertimos la data a un objeto Instances */
        Instances iDataSet = oDataSet.getDataSet();
         
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
        
        /** */
        System.out.println(iDataSet.numInstances());
        System.out.println(iTrain.numInstances());
        System.out.println(iTest.numInstances());
        
        /* Entrenar */
        mp.buildClassifier(iTrain);
        weka.core.SerializationHelper.write("model.model", mp);
        
        Evaluation eval = new Evaluation(iTrain);
        eval.evaluateModel(mp, iTest);
        
        System.out.println( eval.toSummaryString("Fermin%", false) );
        
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

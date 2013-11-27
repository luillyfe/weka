/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.nio.file.Path;
import java.nio.file.Paths;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemovePercentage;

/**
 * Construye un modelo de deteccion de intrusos inteligente a patir de
 * un algoritmo de aprendizaje automatico, las redes neuronales artificiales
 * que utiliza un conjunto de datos para entrenamiento del modelo.
 * Tambien permite evaluar su rendimiento en terminos de precision a la hora 
 * clasificar
 * @author luilly
 */
public class Classifier {

    private DataSource Dataset;
    private Instances  iTrain;
    private MultilayerPerceptron mp;
    private RemovePercentage rp;
    private Instances iDataset;

    public Classifier(Path data, Path model) {
        toEvaluateModel(data, model);
    }
    
    public Classifier(Path data) {
        toFormatData(data);
        toTrain();
        toSaveModel();
    }
    
    /** Prepara los datos para el clasificador */
    private void toFormatData(Path data) {
        try {
            /** Se carga en memoria la data de entrenamiento */
            Dataset = new DataSource( data.toString() );
            
            /** Se estructura la data para que pueda ser entregada al clasificador */
            iDataset = Dataset.getDataSet();
            
            /** Y se declara su clase -la de los datos- */
            iDataset.setClassIndex( iDataset.numAttributes()-1 );
            
            /** Decidimos un filtro y lo aplicamos */
            rp = new RemovePercentage();
            rp.setPercentage(34.0);
            rp.setInputFormat(iDataset);
            iTrain = Filter.useFilter(iDataset, rp);
            
        } catch (Exception ex) { System.err.println("Data no valida\t" + ex); }
    }
    
    /** Se construye un modelo inteligente basado en redes neuronales artificales */
    private void toTrain() {
        mp = new MultilayerPerceptron();
        String[] oMultilayerPerceptron;
        
        try {
            /** Se establecen las opciones del clasificador */
            oMultilayerPerceptron = weka.core.Utils.splitOptions( 
                           "-L 0.3 -M 0.2 -N 120 -V 0 -S 0 -E 20 -H 6" );
            mp.setOptions(oMultilayerPerceptron);
            
            /* Entrenar */
            mp.buildClassifier(iTrain);
        
        } catch (Exception ex) { System.err.println("Entrenamiento error" + ex); }
    }
    
    private void toEvaluateModel(Path data, Path model) {
        try {
            if(iDataset == null) { toFormatData(data); }
                
            /** Evaluamos el modelo con el resto de los datos que no se usaron
                par entrenamiento */
            rp.setInputFormat(iDataset);
            rp.setInvertSelection(true);
            Instances iTest = Filter.useFilter(iDataset, rp);
            
            if(mp == null ) {
                mp = (MultilayerPerceptron) 
                    weka.core.SerializationHelper.read( model.toString() ); } 
            
            Evaluation eval = new Evaluation(iTrain);
            eval.evaluateModel(mp, iTest);
            System.out.println( eval.toSummaryString("Fermin%", false) );
            
        } catch (Exception ex) {System.err.println("Error evaluando el modelo "+ ex);}
    }
    
    private void toSaveModel() {
        try {
            /** Salvar el modelo */
            weka.core.SerializationHelper.write("models/120E.model", mp);
        } catch (Exception ex) {
            System.err.println("Error al salvar el modelo" + ex);}
    }
    
    public static void main(String[] args) {
        Path data = Paths.get( "Pliegue 1.arff" );
        Path model = Paths.get( "models/120E.model" );
        
        new Classifier(data, model);
    }
}

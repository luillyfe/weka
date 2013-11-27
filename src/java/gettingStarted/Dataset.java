/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;

/**
 *
 * @author luilly
 */
public class Dataset {
    private Instances  iDataset;
    private ArffSaver saver;

    public Dataset(Path data) {
        try {
            DataSource Dataset = new ConverterUtils.DataSource( data.toString() );
            iDataset = Dataset.getDataSet();
            
        } catch (Exception ex) { System.err.println("Data no encontrada " + ex); }
    }
    
    private void addAttribute(String index, String name, int data) {
        try {
            Add filter = new Add();
            filter.setAttributeIndex(index);
            filter.setNominalLabels("normal,anomaly");
            filter.setAttributeName(name);
            filter.setInputFormat(iDataset);
            iDataset = Filter.useFilter(iDataset, filter);
            
            //for(int i=0;i<iDataset.numInstances();i++)
                //iDataset.instance(i).setValue(0, i+1);
            
            saver = new ArffSaver();
            saver.setInstances(iDataset);
            saver.setFile( new File("C:/Users/luilly/Documents/Data/"+data+".arff") );
            saver.writeBatch();
            
        } catch (Exception ex) { System.err.println("Error agregando atributo " + ex); }
        
    }
    
    private void rmAttribute(String index) {
        try {
            Remove filter = new Remove();
            filter.setAttributeIndices(index);
            filter.setInputFormat(iDataset);
            iDataset = Filter.useFilter(iDataset, filter);
            
            saver = new ArffSaver();
            saver.setInstances(iDataset);
            saver.setFile( new File("C:/Users/luilly/Documents/Data/2.arff") );
            saver.writeBatch();
            
        } catch (Exception ex) { System.err.println("Error eliminando atributo" + ex); }
    
    }
    
    public static void main(String[] args) {
        //Path data = Paths.get( "C:/Users/luilly/Documents/Data/data10.arff" );
        //Path uno  = Paths.get( "C:/Users/luilly/Documents/Data/data1.arff" );
        
        for(int i= 2;i<10;i++) {
            Path data = Paths.get( "C:/Users/luilly/Documents/Data/data"+i+".arff" );
            new Dataset( data ).addAttribute("last", "class", i);
        }
        //new Dataset( uno ).rmAttribute(43+"");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gettingStarted;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author luilly
 */
public class ClassifierInstance {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        
        Instances unlabeled = new Instances(
                new BufferedReader(
                new FileReader("C:/Users/luilly/Documents/Data/Pliegue 1.arff")));

        unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
        
        Instances labeled = new Instances(unlabeled);
        
        String[] options = new String[1];
        options[0] = "-U";            // unpruned tree
        J48 tree = new J48();         // new instance of tree
        tree.setOptions(options);     // set the options
        tree.buildClassifier(unlabeled);

        for (int i = 0; i < unlabeled.numInstances(); i++) {
            double clsLabel = tree.classifyInstance(unlabeled.instance(i));
            labeled.instance(i).setClassValue(clsLabel);
            System.out.print(unlabeled.instance(i).classIndex() + "\t");
            System.out.print(unlabeled.instance(i).classAttribute().toString() + "\t");
            System.out.print(unlabeled.instance(i).getRevision() + "\n");
        }
        
        BufferedWriter writer = new BufferedWriter(
                new FileWriter("C:/Users/luilly/Documents/Data/labeled.arff"));
        writer.write(labeled.toString());
        writer.newLine();
        writer.flush();
        writer.close();
    }
}

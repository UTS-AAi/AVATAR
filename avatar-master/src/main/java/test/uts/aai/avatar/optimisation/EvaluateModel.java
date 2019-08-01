/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.pbmn.WekaExecutor;

/**
 *
 * @author ntdun
 */
public class EvaluateModel {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String modelPath = "C:\\experiments\\tools\\avatar\\output\\m1.model";
        String validationSet = "C:\\experiments\\datasets\\arff\\abalone_train.arff";
        WekaExecutor wekaExecutor = new WekaExecutor();
        Double accuracy = wekaExecutor.evaluateModel(modelPath, validationSet);
        
        if (accuracy!=null) {
            System.out.println("accuracy: " + accuracy);
            
        } else {
            System.out.println("NULL");
        }
        
    }
    
}

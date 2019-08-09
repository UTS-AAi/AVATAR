/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.pbmn;

import uts.aai.feature.configuration.MLComponentConfiguration;
import uts.aai.feature.model.MLComponent;
import uts.aai.pbmn.WekaExecutor;

/**
 *
 * @author ntdun
 */
public class ExecuteSingleComponent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String componentId = "weka.filters.unsupervised.instance.PeriodicSampling";
        String input = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/testing/unbalanced.arff";
        String output = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/testing/output/temp-out.arff";
        
        MLComponentConfiguration.initConfiguration();
        
        MLComponent mLComponent = MLComponentConfiguration.getComponentByID(componentId);
        
        String script = mLComponent.getComponentExecutionScriptSingleComponentWeka();
        
        WekaExecutor wekaExecutor = new WekaExecutor();
        
        //wekaExecutor.executePredictor(input, output, script);
        wekaExecutor.executeFilter(input, output, script);
        
        
    }
    
}

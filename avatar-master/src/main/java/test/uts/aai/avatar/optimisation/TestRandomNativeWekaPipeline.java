/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import java.util.ArrayList;
import uts.aai.avatar.optimisation.RandomPipelineGenerator;
import uts.aai.feature.configuration.MLComponentConfiguration;
import uts.aai.feature.model.MLComponent;

/**
 *
 * @author ntdun
 */
public class TestRandomNativeWekaPipeline {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        //String trainingData = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/cpu.arff";
        String trainingData = "C:/DATA/Projects/eclipse-workspace/aai_aw/weka-3-7-7/data/iris.arff";
        String outputModel = "C:/experiments/tmp/temp_model.model";
        
        MLComponentConfiguration.initConfiguration();
        ArrayList<MLComponent> orderedPipelineComponents = new ArrayList<>();
        orderedPipelineComponents.add(MLComponentConfiguration.getComponentByID("weka.classifiers.functions.SimpleLogistic"));
        
        RandomPipelineGenerator pipelineGenerator = new RandomPipelineGenerator();
        String wekaCommand = pipelineGenerator.generateNativeWekaPipeline(trainingData, outputModel, orderedPipelineComponents);
        System.out.println("\n" + wekaCommand);
        
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.avatar.optimisation.RandomPipelineGenerator;
import uts.aai.avatar.optimisation.RandomSearch;
import uts.aai.pbmn.WekaExecutor;

/**
 *
 * @author ntdun
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String datasetPath = "C:/experiments/datasets/arff/abalone_train.arff";
        
        
//        RandomPipelineGenerator randomPipelineGenerator = new RandomPipelineGenerator(datasetPath);
//        String bpmnPipeline = randomPipelineGenerator.generate();
//        
//        System.out.println(bpmnPipeline);
        


        long timeBudgetInMinutes = 5;
            RandomSearch randomSearch = new RandomSearch(datasetPath, timeBudgetInMinutes);
            randomSearch.start();
            randomSearch.finalise();
      
    }
    
}

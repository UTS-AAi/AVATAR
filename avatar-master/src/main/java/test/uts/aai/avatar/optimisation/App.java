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

    public static void main(String[] args) {
     
       
        String datasetPath = "C:/experiments/datasets/arff/allModels-convex-12h.arff";
        String outputFolder= "C:/experiments/tools/avatar/output/";
      
        boolean isAvatar = true;
        long timeBudgetInMinutes = 720;
        
            RandomSearch randomSearch = new RandomSearch(datasetPath, timeBudgetInMinutes, isAvatar,outputFolder);
            randomSearch.start();
            randomSearch.finalise();
      
    }
    
}

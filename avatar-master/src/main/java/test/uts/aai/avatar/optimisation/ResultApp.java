/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.avatar.optimisation.RandomSearchResultAnalyse;

/**
 *
 * @author ntdun
 */
public class ResultApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        RandomSearchResultAnalyse randomSearchResultAnalyse = new RandomSearchResultAnalyse();
//        randomSearchResultAnalyse.writeTheLastPipeline(
//                "C:/experiments/results/tmp/allModels.json", 
//                "C:/experiments/results/tmp/issue.bpmn");
        
        
        randomSearchResultAnalyse.analyse(
                "C:/experiments/tools/avatar/log/allModels.json",
                "C:/experiments/results/tmp/tmp-bestPipeline.bpmn");
    }
    
}

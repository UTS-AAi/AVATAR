/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.optimisation;

import java.util.ArrayList;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.EvaluationResult;
import uts.aai.avatar.model.pipeline.*;

/**
 *
 * @author ntdun
 */
public class EvolutionSearch {
    
    private String datasetPath;
    private long timeBudgetInMinutes;
    private boolean isAvatar;
    private ArrayList<EvaluationResult> evaluationResultList;
    private String outputFolder;
    private int counter;
    
    private ArrayList<MLPipeline> evolGeneration;
    private int maxIndividual;
   

    public EvolutionSearch() {
    }
    
    public EvolutionSearch(String datasetPath, long timeBudgetInMinutes, boolean isAvatar, String outputFolder) {
        this.datasetPath = datasetPath;
        this.timeBudgetInMinutes = timeBudgetInMinutes;
        this.isAvatar = isAvatar;
        this.outputFolder = outputFolder;
    }

    private void initEvolutionSearch() {
        counter = 0;
        evaluationResultList = new ArrayList<EvaluationResult>();
        MLComponentConfiguration.initDefault();
    }
    
    private void optimise(){
        
        initPopulation();
        
        
        
        
        
        
        
        
        
        
    }
    
    private void initPopulation(){
        // random generate 
        // check classification or regression problem
        
        
        for (int i=0;i<maxIndividual;i++) {
            
            
            MLComponentConfiguration.getListOfMLComponents();
            
        }
    }
    
    private MLPipeline generateMLPipeline(){
        
        
        
        
        
        
        return null;
    }
    
    
    
    
    
}

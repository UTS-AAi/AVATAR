/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.api;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */
public class SMACWrapperToPetriNet {
    
    
    public void toPetriNet() {
        
        ArrayList<String> listOfFilters = new ArrayList<>();
    	ArrayList<String> listOfPredictors = new ArrayList<>();
        
        
        listOfFilters.add("weka.filters.unsupervised.instance.RemoveOutliers");
        listOfFilters.add("weka.filters.unsupervised.attribute.InterquartileRange");
        
        listOfPredictors.add("weka.classifiers.bayes.NaiveBayes");
        
        
        
        
        
        
        
        
        
    }
    
    
    
    
    
    
    
}

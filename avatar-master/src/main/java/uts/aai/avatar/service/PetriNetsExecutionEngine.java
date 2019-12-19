/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.service;

import java.util.List;
import uts.aai.avatar.model.MLComponent;
import uts.aai.pn.model.Transition;
import uts.aai.pn.model.Parameter;
import uts.aai.pn.model.Arc;
import uts.aai.pn.model.Place;
import uts.aai.pn.model.PetriNetsPipeline;
import uts.aai.pn.model.TransitionFunction;
import uts.aai.pn.model.Token;
//import uts.aai.avatar.configuration.Configuration;

/**
 *
 * @author ntdun
 */
public class PetriNetsExecutionEngine {
    
    private PetriNetsPipeline petriNetsPipeline;
    private boolean isInvalidPipeline;

    private List<MLComponent> loadedListOfMLComponents;
    public PetriNetsExecutionEngine(PetriNetsPipeline petriNetsPipeline,List<MLComponent> loadedListOfMLComponents) {
        this.petriNetsPipeline = petriNetsPipeline;
        isInvalidPipeline = false;
        this.loadedListOfMLComponents = loadedListOfMLComponents;
    }
    
    
    
    public boolean execute(){
        
        Place currentPlace = getStartPlace();
        
        
        
        boolean isFinished =false;
        while (!isFinished && !isInvalidPipeline) {
            
            Place nextPlace = moveToNextPlace(currentPlace);
            //printToken(nextPlace.getToken());
            if (nextPlace==null) {
                isFinished = true;
            } else {
                currentPlace = nextPlace;
            }
        }
        
        if (isInvalidPipeline) {
            System.out.println("Invalid Pipeline\n");
            return false;
        } else {
            System.out.println("Valid Pipeline\n");
            
            return true;
        }
        
        
           
    }
    
    private void printToken(Token token){
        if (token!=null) {
        
        for (Parameter param : token.getParameterList()) {
            
            System.out.println(param.getParamName() + " : " + param.getParamValue());
            
        }
        }
    }
    
    private Place moveToNextPlace(Place currentPlace){
       
        Place nextPlace = null;
        Transition transition = getConnectedTransitionFromPlace(currentPlace);
       
        
        if (transition!=null) {
            
            nextPlace = getConnectedPlaceFromTransition(transition);
            
            TransitionFunction transitionFunction = transition.getTransitionFunction();
            //Token outputToken = transitionFunction.fire(currentPlace.getToken());
            System.out.println("--------------------------");
            currentPlace.getToken().toString();
            Token outputToken = transitionFunction.fireAlg(currentPlace.getToken(), transition.getId(),loadedListOfMLComponents);
            
           
            
            if (outputToken!=null) {
                //printToken(outputToken);
                nextPlace.setToken(outputToken);
                outputToken.toString();
            } else {
                System.out.println("NULL TOKEN");
                isInvalidPipeline = true;
            }
            
            
            
            System.out.println("--------------------------");
        }
        
        
        
        return nextPlace;
    }
    
    private Place getConnectedPlaceFromTransition(Transition transition) {
        Place foundPlace = null;
        
        for (Arc arc : petriNetsPipeline.getArcList()) {
            if (arc.getTransitionId().equals(transition.getId()) && !arc.isIsPlaceToTransition()) {
                foundPlace = getPlaceById(arc.getPlaceId());
            }
        }
        return foundPlace;
    }
    
    private Transition getConnectedTransitionFromPlace(Place place){
        
        Transition foundTransition = null;
        for (Arc arc : petriNetsPipeline.getArcList()) {
            if (arc.getPlaceId().equals(place.getId()) && arc.isIsPlaceToTransition()) {
                foundTransition = getTransitionById(arc.getTransitionId());
            }
        }
        
        return foundTransition;
    }
    
    private Transition getTransitionById(String transitionId){
        
        for (Transition transition : petriNetsPipeline.getTransitionList()) {
            if (transition.getId().equals(transitionId)) {
                return transition;
            }
        }
        return null;
    }
    
    private Place getPlaceById(String placeId) {
        for (Place place : petriNetsPipeline.getPlaceList()) {
            if (place.getId().equals(placeId)) {
                return place;
            }
        }
        return null;
    }
    
    
    
    private Place getStartPlace(){
        
        Place startPlace = null;
        for (Place place : petriNetsPipeline.getPlaceList()) {
            if (place.getToken()!=null) {
                startPlace = place;
            }
        }
        
        return startPlace;
    }
    
    
    
    
    
}

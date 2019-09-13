/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model.pipeline;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */
public class MLPipelineComponent {
    
    private String componentId;
    private String algorithmId;
    private ArrayList<MLComponentHyperparameter> listOfHyperparameters;

    public MLPipelineComponent() {
    }

    public MLPipelineComponent(String componentId, String algorithmId, ArrayList<MLComponentHyperparameter> listOfHyperparameters) {
        this.componentId = componentId;
        this.algorithmId = algorithmId;
        this.listOfHyperparameters = listOfHyperparameters;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getAlgorithmId() {
        return algorithmId;
    }

    public void setAlgorithmId(String algorithmId) {
        this.algorithmId = algorithmId;
    }

    public ArrayList<MLComponentHyperparameter> getListOfHyperparameters() {
        return listOfHyperparameters;
    }

    public void setListOfHyperparameters(ArrayList<MLComponentHyperparameter> listOfHyperparameters) {
        this.listOfHyperparameters = listOfHyperparameters;
    }
    
    
    
}

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
public class MLPipeline {
    
    private MLPipelineStructure mLPipelineStructure;
    private ArrayList<MLPipelineComponent> listOfMLComponents;
    private ArrayList<MLPipelineUtilityComponent> listOfUtilityComponents;

    public MLPipeline() {
    }

    public MLPipeline(MLPipelineStructure mLPipelineStructure, ArrayList<MLPipelineComponent> listOfMLComponents, ArrayList<MLPipelineUtilityComponent> listOfUtilityComponents) {
        this.mLPipelineStructure = mLPipelineStructure;
        this.listOfMLComponents = listOfMLComponents;
        this.listOfUtilityComponents = listOfUtilityComponents;
    }

    public MLPipelineStructure getmLPipelineStructure() {
        return mLPipelineStructure;
    }

    public void setmLPipelineStructure(MLPipelineStructure mLPipelineStructure) {
        this.mLPipelineStructure = mLPipelineStructure;
    }

    public ArrayList<MLPipelineComponent> getListOfMLComponents() {
        return listOfMLComponents;
    }

    public void setListOfMLComponents(ArrayList<MLPipelineComponent> listOfMLComponents) {
        this.listOfMLComponents = listOfMLComponents;
    }

    public ArrayList<MLPipelineUtilityComponent> getListOfUtilityComponents() {
        return listOfUtilityComponents;
    }

    public void setListOfUtilityComponents(ArrayList<MLPipelineUtilityComponent> listOfUtilityComponents) {
        this.listOfUtilityComponents = listOfUtilityComponents;
    }
    
    
    
}

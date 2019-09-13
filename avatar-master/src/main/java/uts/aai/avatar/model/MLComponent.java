/*
 * Copyright 2019 camunda services GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uts.aai.avatar.model;

import java.util.List;

/**
 *
 * @author ntdun
 */
public class MLComponent {
    
    private String componentId;
    private String componentName;
    private String componentFullClassName;
    private MLComponentType mLComponentType;
    private String componentExecutionScriptSingleComponentWeka;
    private String componentExecutionScriptFilteredClassifierWeka;
    private List<MLComponentIO> listOfCapabilities;
    private List<MLComponentIO> listOfEffects;
    private List<MLHyperparameter> listOfMLHyperparameters;
    
    

    public MLComponent() {
    }

    public MLComponent(String componentId, String componentName, String componentFullClassName, MLComponentType mLComponentType, String componentExecutionScriptSingleComponentWeka, String componentExecutionScriptFilteredClassifierWeka, List<MLComponentIO> listOfCapabilities, List<MLComponentIO> listOfEffects, List<MLHyperparameter> listOfMLHyperparameters) {
        this.componentId = componentId;
        this.componentName = componentName;
        this.componentFullClassName = componentFullClassName;
        this.mLComponentType = mLComponentType;
        this.componentExecutionScriptSingleComponentWeka = componentExecutionScriptSingleComponentWeka;
        this.componentExecutionScriptFilteredClassifierWeka = componentExecutionScriptFilteredClassifierWeka;
        this.listOfCapabilities = listOfCapabilities;
        this.listOfEffects = listOfEffects;
        this.listOfMLHyperparameters = listOfMLHyperparameters;
    }

    public String getComponentExecutionScriptFilteredClassifierWeka() {
        return componentExecutionScriptFilteredClassifierWeka;
    }

    public void setComponentExecutionScriptFilteredClassifierWeka(String componentExecutionScriptFilteredClassifierWeka) {
        this.componentExecutionScriptFilteredClassifierWeka = componentExecutionScriptFilteredClassifierWeka;
    }
    
    

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentFullClassName() {
        return componentFullClassName;
    }

    public void setComponentFullClassName(String componentFullClassName) {
        this.componentFullClassName = componentFullClassName;
    }

    public MLComponentType getmLComponentType() {
        return mLComponentType;
    }

    public void setmLComponentType(MLComponentType mLComponentType) {
        this.mLComponentType = mLComponentType;
    }

    public String getComponentExecutionScriptSingleComponentWeka() {
        return componentExecutionScriptSingleComponentWeka;
    }

    public void setComponentExecutionScriptSingleComponentWeka(String componentExecutionScriptSingleComponentWeka) {
        this.componentExecutionScriptSingleComponentWeka = componentExecutionScriptSingleComponentWeka;
    }

    public List<MLComponentIO> getListOfCapabilities() {
        return listOfCapabilities;
    }

    public void setListOfCapabilities(List<MLComponentIO> listOfCapabilities) {
        this.listOfCapabilities = listOfCapabilities;
    }

    public List<MLComponentIO> getListOfEffects() {
        return listOfEffects;
    }

    public void setListOfEffects(List<MLComponentIO> listOfEffects) {
        this.listOfEffects = listOfEffects;
    }

    public List<MLHyperparameter> getListOfMLHyperparameters() {
        return listOfMLHyperparameters;
    }

    public void setListOfMLHyperparameters(List<MLHyperparameter> listOfMLHyperparameters) {
        this.listOfMLHyperparameters = listOfMLHyperparameters;
    }

    
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model.pipeline;

/**
 *
 * @author ntdun
 */
public class MLPipelineUtilityComponent {
    
    private String componentId;
    private MLUtilityComponentType componentType;

    public MLPipelineUtilityComponent() {
    }

    public MLPipelineUtilityComponent(String componentId, MLUtilityComponentType componentType) {
        this.componentId = componentId;
        this.componentType = componentType;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public MLUtilityComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(MLUtilityComponentType componentType) {
        this.componentType = componentType;
    }
    
    
    
}

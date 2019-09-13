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
public class MLPipelineConnection {
    private String source;
    private String target;

    public MLPipelineConnection() {
    }

    public MLPipelineConnection(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
    
    
    
}

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
public class MLPipelineStructure {
    
    private ArrayList<MLPipelineConnection> listOfConnections;

    public MLPipelineStructure() {
    }

    public MLPipelineStructure(ArrayList<MLPipelineConnection> listOfConnections) {
        this.listOfConnections = listOfConnections;
    }

    public ArrayList<MLPipelineConnection> getListOfConnections() {
        return listOfConnections;
    }

    public void setListOfConnections(ArrayList<MLPipelineConnection> listOfConnections) {
        this.listOfConnections = listOfConnections;
    }
    
    public MLPipelineConnection getFirstComponent(){
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getSource()==null) {
                return mLPipelineConnection;
            }
        }
        return null;
    }
    
    public MLPipelineConnection getLastComponent(){
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getTarget()==null) {
                return mLPipelineConnection;
            }
        }
        return null;
    }
    
    public MLPipelineConnection getNextComponent(String sourceId){
        for (MLPipelineConnection mLPipelineConnection : listOfConnections) {
            if (mLPipelineConnection.getTarget()==null && mLPipelineConnection.getSource().equals(sourceId)) {
                return mLPipelineConnection;
            }
        }
        return null;
    }
    
}

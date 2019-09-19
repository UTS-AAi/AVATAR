/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */
public class TreeNode {
    private int nodeId;
    private String componentId;
    private ArrayList<TreeNode> listOfChildNodes;

    public TreeNode() {
    }

    public TreeNode(int nodeId, String componentId, ArrayList<TreeNode> listOfChildNodes) {
        this.nodeId = nodeId;
        this.componentId = componentId;
        this.listOfChildNodes = listOfChildNodes;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public ArrayList<TreeNode> getListOfChildNodes() {
        return listOfChildNodes;
    }

    public void setListOfChildNodes(ArrayList<TreeNode> listOfChildNodes) {
        this.listOfChildNodes = listOfChildNodes;
    }

    
    
    
}

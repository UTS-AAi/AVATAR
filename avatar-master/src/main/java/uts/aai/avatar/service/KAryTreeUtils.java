/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.service;

import java.util.ArrayList;
import java.util.UUID;
import uts.aai.avatar.model.KAryTree;
import uts.aai.avatar.model.TreeNode;
import uts.aai.avatar.model.pipeline.MLPipeline;

/**
 *
 * @author ntdun
 */
public class KAryTreeUtils {
    
    private KAryTree kAryTree;
    private int nodeCounter;
   
    public KAryTreeUtils() {
        initTree();
    }
    
    private void initTree() {
        nodeCounter=0;
        ArrayList<TreeNode> listOfChildNodes = new ArrayList<>();
        TreeNode rootNode = new TreeNode(nodeCounter, null, listOfChildNodes);
        kAryTree = new KAryTree(rootNode, listOfChildNodes);
        
    }
    
    public void addPAth(MLPipeline mLPipeline){
        
        
    }
    
    public void addChildNode(TreeNode parentNode, TreeNode childNode) {
        
    }
    
    public void randomPath(){
        
    }
    
    private String randUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
    
    
}

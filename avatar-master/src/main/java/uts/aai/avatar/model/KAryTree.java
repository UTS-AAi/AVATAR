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
public class KAryTree {
    
    private TreeNode rootNode;
    private ArrayList<TreeNode> listOfTreeNodes;

    public KAryTree() {
    }

    public KAryTree(TreeNode rootNode, ArrayList<TreeNode> listOfTreeNodes) {
        this.rootNode = rootNode;
        this.listOfTreeNodes = listOfTreeNodes;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public ArrayList<TreeNode> getListOfTreeNodes() {
        return listOfTreeNodes;
    }

    public void setListOfTreeNodes(ArrayList<TreeNode> listOfTreeNodes) {
        this.listOfTreeNodes = listOfTreeNodes;
    }
    
    
    
    
    
}

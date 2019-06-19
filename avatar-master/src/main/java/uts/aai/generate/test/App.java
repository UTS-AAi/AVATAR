/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.generate.test;

import uts.aai.generate.RandomPipelineCombination;



/**
 *
 * @author ntdun
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        
        System.out.println();
        RandomPipelineCombination rpc = new RandomPipelineCombination();
        rpc.permute(new String[]{ "M", "O", "T", "R", "S"});
        System.out.println("Number of Pipeline: " +rpc.counter);
        
    }

}

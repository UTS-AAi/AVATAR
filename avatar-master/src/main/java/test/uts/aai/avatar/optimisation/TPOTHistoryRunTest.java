/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.resultanalysis.TPOTHistoryRunAnalyse;

/**
 *
 * @author ntdun
 */
public class TPOTHistoryRunTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        
        

    
            String filePath = "C:\\experiments\\results\\tpot-results-16august2019\\car\\log-car-seed0.txt";
            TPOTHistoryRunAnalyse analyse = new TPOTHistoryRunAnalyse();
            analyse.analyse(filePath);
            
    }
    
}

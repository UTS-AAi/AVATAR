/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.avatar.optimisation;

import uts.aai.resultanalysis.AutoSklearnHistoryRunAnalyse;

/**
 *
 * @author ntdun
 */
public class AutosklearnHistoryRunTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String filePath = "C:\\experiments\\results\\autosklearn\\winequalitywhite_seed4\\smac3-output\\run_1\\runhistory.json";
    	AutoSklearnHistoryRunAnalyse analyse = new AutoSklearnHistoryRunAnalyse();
    	analyse.analyse(filePath);
    }
    
}

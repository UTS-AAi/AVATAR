/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.resultanalysis;

/**
 *
 * @author ntdun
 */
public class AutoWekaWastedTime {
    //runs_and_results....
    
    // number of invalid pipelines / total pipelines
    // =TEXT(COUNTIF(N:N,"CRASHED"),"0")&"/"&TEXT(COUNTA(N:N)-1,"0")
    
    // AVERAGE TIME TO EVALUATE INVALID PIPELINE
    // =SUMIF(N:N,"CRASHED",H:H)*1000/COUNTIF(N:N,"CRASHED")
    
    // wasted time in percentage
    //    =SUMIF(N:N,"CRASHED",H:H)/SUM(H:H)*100
    
}

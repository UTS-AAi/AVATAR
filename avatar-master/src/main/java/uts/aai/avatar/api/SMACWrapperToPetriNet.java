/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.api;

import java.util.ArrayList;

/**
 *
 * @author ntdun
 */
public class SMACWrapperToPetriNet {
    
    
    public void toPetriNet(ArrayList<String> wrapperArgs) {
        for (String wrapperArg: wrapperArgs) {
    		if(wrapperArg.contains("weka."))
    		System.out.println("AVATAR -" + wrapperArg.trim()+"-");
    	}
    }
    
    
}

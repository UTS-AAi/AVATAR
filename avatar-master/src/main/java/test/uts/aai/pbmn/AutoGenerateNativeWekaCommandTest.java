/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.uts.aai.pbmn;

/**
 *
 * @author ntdun
 */
public class AutoGenerateNativeWekaCommandTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String folderName = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\combination\\dataset_1";

        AutoGenerateNativeWekaCommand agnwc = new AutoGenerateNativeWekaCommand();
        agnwc.readAllFiles(folderName);
    }
    
}

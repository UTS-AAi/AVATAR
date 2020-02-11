/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ntdun
 */
public class CSVUtils {
    
    public List<String[]> readCSVFile(String fileName) {
        
        List<String[]> listOfRows = new ArrayList<>();
            BufferedReader br = null;
            String line = "";
            String cvsSplitBy = ",";

            try {

                br = new BufferedReader(new FileReader(fileName) );
                while ((line = br.readLine()) != null) {
                    String[] attributes = line.split(cvsSplitBy);
                    listOfRows.add(attributes);
                }
        
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        return listOfRows;
    } 
    
}

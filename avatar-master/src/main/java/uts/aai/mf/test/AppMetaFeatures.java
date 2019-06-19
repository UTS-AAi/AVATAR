/*
 * Copyright 2019 camunda services GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uts.aai.mf.test;

import uts.aai.mf.model.MLComponentIO;
import uts.aai.mf.service.DatasetMetaFeatures;
import java.util.List;

/**
 *
 * @author ntdun
 */
public class AppMetaFeatures {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String filePath = "C:/Users/ntdun/Desktop/data/abalone-small.csv";
        DatasetMetaFeatures datasetMetaFeatures = new DatasetMetaFeatures(filePath);
        
        List<MLComponentIO> listOfMetaFeatures = datasetMetaFeatures.analyseMetaFeatures();
        System.out.println("");
        for (MLComponentIO mLComponentIO : listOfMetaFeatures) {
            System.out.println(mLComponentIO.getmLComponentCapability() + " : " + mLComponentIO.getValue());
        }
        
        
    }
    
}

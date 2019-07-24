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
package uts.aai.mf.service;

import uts.aai.mf.model.MLMetafeature;
import uts.aai.mf.model.MLComponentIO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.experiment.Stats;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author ntdun
 */
public class DatasetMetaFeatures {

    private String filePath;
    private List<MLComponentIO> listOfMetaFeatures;

    public DatasetMetaFeatures(String filePath) {
        this.filePath = filePath;

        listOfMetaFeatures = new ArrayList<>();
        listOfMetaFeatures.addAll(getAllAttributesCapabilities());
        listOfMetaFeatures.addAll(getAllClassesCapabilities());

    }

    public List<MLComponentIO> analyseMetaFeaturesArff() {

        DataSource source = null;
        try {
            source = new DataSource(filePath);

            Instances data = source.getDataSet();

            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            
            for (int i = 0; i < data.numAttributes(); i++) {
                
                if (data.attributeStats(i).missingCount>0) {
                    
                    if (i==data.numAttributes()-1) {
                        setComponentIO(MLMetafeature.MISSING_CLASS_VALUES, 1);
                    } else {
                        setComponentIO(MLMetafeature.MISSING_VALUES, 1);
                    }
                    
                }
                
                
                if (data.attribute(i).isNumeric()) {
                    if (i==data.numAttributes()-1) {
                        setComponentIO(MLMetafeature.NUMERIC_CLASS, 1);
                    } else {
                        setComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES, 1);
                    }
                }
                
                if (data.attribute(i).isNominal()) {
                    if (i==data.numAttributes()-1) {
                        if (data.attributeStats(i).distinctCount==2) {
                            //setComponentIO(MLMetafeature.BINARY_CLASS, 1);
                        }
                        
                        setComponentIO(MLMetafeature.NOMINAL_CLASS, 1);
                        
                    } else {
                        if (data.attributeStats(i).distinctCount==2) {
                            //setComponentIO(MLMetafeature.BINARY_ATTRIBUTES, 1);
                        }
                        
                        setComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES, 1);
                        
                    }
                }
                
                
                 if (data.attribute(i).isDate()) {
                    if (i==data.numAttributes()-1) {
                        setComponentIO(MLMetafeature.DATE_CLASS, 1);
                    } else {
                        setComponentIO(MLMetafeature.DATE_ATTRIBUTES, 1);
                    }
                }
                
                
            }
            
           

        } catch (Exception ex) {
            Logger.getLogger(DatasetMetaFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

        return listOfMetaFeatures;
    }

    public List<MLComponentIO> analyseMetaFeaturesCSV() {

        List<List<String>> datasetByRow = readCSV();
        List<List<String>> datasetByColumns = transformToColumns(datasetByRow);

        if (containAttributeMissingValues(datasetByRow)) {
            setComponentIO(MLMetafeature.MISSING_VALUES, 1);
        }

        if (containClassMissingValues(datasetByRow)) {
            setComponentIO(MLMetafeature.MISSING_CLASS_VALUES, 1);
        }

        if (isContainNumericAttribute(datasetByColumns)) {
            setComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES, 1);
        }

        if (isContainNumericClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.NUMERIC_CLASS, 1);
        }

        if (isContainBinaryClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.BINARY_CLASS, 1);
        }

        if (!isContainNumericClass(datasetByColumns) && !isContainBinaryClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.NOMINAL_CLASS, 1);
        }

        if (isContainNominalAttribute(datasetByColumns)) {
            setComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES, 1);
        }

        if (isContainBinaryAttribute(datasetByColumns)) {
            setComponentIO(MLMetafeature.BINARY_ATTRIBUTES, 1);
        }

        if (isContainDateAttribute(datasetByColumns)) {
            setComponentIO(MLMetafeature.DATE_ATTRIBUTES, 1);
        }

        if (isContainDateClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.DATE_CLASS, 1);
        }

        if (isContainEmptyAttribute(datasetByColumns)) {
            setComponentIO(MLMetafeature.EMPTY_NOMINAL_ATTRIBUTES, 1);
        }

        if (isContainEmptyClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.EMPTY_NOMINAL_CLASS, 1);
        }

        if (isImbalancedClass(datasetByColumns)) {
            setComponentIO(MLMetafeature.IMBALANCE_CLASS, 1);
        }

        return listOfMetaFeatures;
    }

    private void setComponentIO(MLMetafeature mLComponentCapability, int value) {
        for (MLComponentIO mLComponentIO : listOfMetaFeatures) {
            if (mLComponentIO.getmLComponentCapability().equals(mLComponentCapability)) {
                mLComponentIO.setValue(value);
                break;
            }
        }
    }

    private boolean containAttributeMissingValues(List<List<String>> dataset) {

        for (List<String> row : dataset) {
            for (int i = 0; i < row.size() - 1; i++) {

                if (row.get(i).equals("?")) {
                    return true;
                }
            }
        }

        return false;

    }

    private boolean containClassMissingValues(List<List<String>> dataset) {

        for (List<String> row : dataset) {
            if (row.get(row.size() - 1).equals("?")) {
                return true;
            }

        }

        return false;

    }

    private boolean isNumeric(String str) {

        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isContainNumericClass(List<List<String>> datasetByColumns) {

        List<String> columns = datasetByColumns.get(datasetByColumns.size() - 1);

        boolean hasNumericAttributeSingleColumn = true;
        for (String cell : columns) {

            if (!isNumeric(cell) && !cell.equals("?")) {
                hasNumericAttributeSingleColumn = false;
                break;
            }

        }

        return hasNumericAttributeSingleColumn;

    }

    private boolean isImbalancedClass(List<List<String>> datasetByColumns) {

        List<String> columns = datasetByColumns.get(datasetByColumns.size() - 1);

        boolean hasNumericAttributeSingleColumn = false;

        Map<String, Integer> counter = new HashMap<>();

        for (String cell : columns) {
            if (counter.containsKey(cell) && !cell.equals("?")) {

                counter.put(cell, counter.get(cell) + 1);

            } else if (!cell.equals("?")) {
                counter.put(cell, 1);
            }

        }

        List<Integer> mapValues = new ArrayList<>(counter.values());
        Collections.sort(mapValues);
        int sum = 0;
        for (Integer val : mapValues) {
            sum += val;
        }

        double imbalancedRate = 1.0 * mapValues.get(mapValues.size() - 1) / sum;

        if (imbalancedRate >= 0.8) {
            hasNumericAttributeSingleColumn = true;
        }

        return hasNumericAttributeSingleColumn;

    }

    private boolean isContainBinaryAttribute(List<List<String>> datasetByColumns) {

        boolean hasBinaryAttribute = false;

        for (int i = 0; i < datasetByColumns.size() - 1; i++) {
            List<String> columns = datasetByColumns.get(i);

            boolean hasBinaryAttributeSingleColumn = true;
            for (String cell : columns) {
                if (!((isInteger(cell) && (Integer.parseInt(cell) == 1 || Integer.parseInt(cell) == 0)) || cell.equals("?"))) {
                    hasBinaryAttributeSingleColumn = false;
                    break;
                }

            }

            if (hasBinaryAttributeSingleColumn) {
                hasBinaryAttribute = true;
                break;
            }

        }

        return hasBinaryAttribute;
    }

    private List<List<String>> transformToColumns(List<List<String>> dataset) {

        List<List<String>> datasetByColumns = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            List<String> rowList = dataset.get(i);
            for (int j = 0; j < rowList.size(); j++) {

                // first row
                if (i == 0) {
                    List<String> column = new ArrayList<>();
                    column.add(rowList.get(j));
                    datasetByColumns.add(column);
                } else {
                    List<String> column = datasetByColumns.get(j);
                    column.add(rowList.get(j));
                }

            }
        }

        return datasetByColumns;
    }

    private boolean isContainNumericAttribute(List<List<String>> datasetByColumns) {

        boolean hasNumericAttribute = false;

        for (int i = 0; i < datasetByColumns.size() - 1; i++) {
            List<String> columns = datasetByColumns.get(i);

            boolean hasNumericAttributeSingleColumn = true;
            for (String cell : columns) {
                if (!isNumeric(cell) && !cell.equals("?")) {

                    hasNumericAttributeSingleColumn = false;
                    break;
                }

            }

            if (hasNumericAttributeSingleColumn) {
                hasNumericAttribute = true;
                break;
            }

        }

        return hasNumericAttribute;
    }

    private boolean isContainNominalAttribute(List<List<String>> datasetByColumns) {

        boolean hasNominalAttribute = false;

        for (int i = 0; i < datasetByColumns.size() - 1; i++) {
            List<String> columns = datasetByColumns.get(i);

            boolean hasNumericAttributeSingleColumn = true;
            boolean hasBinaryAttributeSingleColumn = true;
            for (String cell : columns) {
                if (!isNumeric(cell) && !cell.equals("?")) {
                    hasNumericAttributeSingleColumn = false;

                }

                if (!((isInteger(cell) && (Integer.parseInt(cell) == 1 || Integer.parseInt(cell) == 0)) || cell.equals("?"))) {
                    hasBinaryAttributeSingleColumn = false;

                }

            }

            if (!hasNumericAttributeSingleColumn && !hasBinaryAttributeSingleColumn) {
                hasNominalAttribute = true;
                break;
            }

        }

        return hasNominalAttribute;
    }

    private boolean isContainBinaryClass(List<List<String>> datasetByColumns) {

        List<String> columns = datasetByColumns.get(datasetByColumns.size() - 1);

        boolean hasBinaryAttributeSingleColumn = true;
        for (String cell : columns) {
            if (!((isInteger(cell) && (Integer.parseInt(cell) == 1 || Integer.parseInt(cell) == 0)) || cell.equals("?"))) {
                hasBinaryAttributeSingleColumn = false;
                break;
            }

        }

        return hasBinaryAttributeSingleColumn;
    }

    private boolean isContainDateAttribute(List<List<String>> datasetByColumns) {

        boolean hasDateAttribute = false;

        for (int i = 0; i < datasetByColumns.size() - 1; i++) {
            List<String> columns = datasetByColumns.get(i);

            boolean hasDateAttributeSingleColumn = true;
            for (String cell : columns) {
                if (!(isValidDateFormat("dd-MM-yyyy", cell, Locale.ENGLISH)
                        || isValidDateFormat("dd/MM/yyyy", cell, Locale.ENGLISH)
                        || isValidDateFormat("dd.MM.yyyy", cell, Locale.ENGLISH))) {
                    hasDateAttributeSingleColumn = false;
                    break;
                }
            }

            if (hasDateAttributeSingleColumn) {
                hasDateAttribute = true;
                break;
            }

        }

        return hasDateAttribute;

    }

    private boolean isContainEmptyAttribute(List<List<String>> datasetByColumns) {

        boolean hasEmptyAttribute = false;

        for (int i = 0; i < datasetByColumns.size() - 1; i++) {
            List<String> columns = datasetByColumns.get(i);

            boolean hasEmptyAttributeSingleColumn = true;
            for (String cell : columns) {
                if (!(cell.trim().equals("") || cell.trim().equals("?"))) {
                    hasEmptyAttributeSingleColumn = false;
                    break;
                }
            }

            if (hasEmptyAttributeSingleColumn) {
                hasEmptyAttribute = true;
                break;
            }

        }

        return hasEmptyAttribute;

    }

    private boolean isContainEmptyClass(List<List<String>> datasetByColumns) {

        List<String> columns = datasetByColumns.get(datasetByColumns.size() - 1);

        boolean hasEmptyAttributeSingleColumn = true;
        for (String cell : columns) {

            if (!(cell.trim().equals("") || cell.trim().equals("?"))) {
                hasEmptyAttributeSingleColumn = false;
                break;
            }
        }

        return hasEmptyAttributeSingleColumn;

    }

    private boolean isContainDateClass(List<List<String>> datasetByColumns) {

        List<String> columns = datasetByColumns.get(datasetByColumns.size() - 1);

        boolean hasDateAttributeSingleColumn = true;
        for (String cell : columns) {
            if (!(isValidDateFormat("dd-MM-yyyy", cell, Locale.ENGLISH)
                    || isValidDateFormat("dd/MM/yyyy", cell, Locale.ENGLISH)
                    || isValidDateFormat("dd.MM.yyyy", cell, Locale.ENGLISH))) {
                hasDateAttributeSingleColumn = false;
                break;
            }
        }

        return hasDateAttributeSingleColumn;

    }

    private boolean isValidDateFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, fomatter);
            String result = ldt.format(fomatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, fomatter);
                String result = ld.format(fomatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, fomatter);
                    String result = lt.format(fomatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }

    private List<List<String>> readCSV() {

        String line = "";

        List<List<String>> dataset = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                //System.out.println("" + line);
                List<String> oneRow = new ArrayList<>();
                String[] dataRow = line.split(",");
                //String[] dataRow = split(line,',');

                for (int i = 0; i < dataRow.length; i++) {
                    oneRow.add(dataRow[i]);
                }
                dataset.add(oneRow);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataset;

    }

    public String[] split(String line, char delimiter) {
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter, 0); // first substring

        while (j >= 0) {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }

    private List<MLComponentIO> getAllClassesCapabilities() {

        List<MLComponentIO> listOfMLComponentIOs = new ArrayList<>();

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.BINARY_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.DATE_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.EMPTY_NOMINAL_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.MISSING_CLASS_VALUES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NOMINAL_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NO_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NUMERIC_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.RELATIONAL_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.STRING_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.UNARY_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        return listOfMLComponentIOs;
    }

    private List<MLComponentIO> getAllAttributesCapabilities() {

        List<MLComponentIO> listOfMLComponentIOs = new ArrayList<>();

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.BINARY_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.DATE_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.EMPTY_NOMINAL_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.MISSING_VALUES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.ONLY_MULTIINSTANCE, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.RELATIONAL_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.STRING_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.UNARY_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.OUTLIER_ATTRIBUTES, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.IMBALANCE_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        return listOfMLComponentIOs;
    }

}

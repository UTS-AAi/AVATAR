/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.metaknowledge.generator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uts.aai.mf.configuration.MLComponentConfiguration;
import uts.aai.mf.model.MLComponent;
import uts.aai.mf.model.MLComponentIO;
import uts.aai.mf.model.MLComponentType;
import uts.aai.mf.model.MLMetafeature;
import uts.aai.mf.model.MetafeatureType;
import uts.aai.mf.model.MetafeatureTypeConfig;
import uts.aai.mf.service.DatasetMetaFeatures;
import uts.aai.pbmn.SurrogatePipelineMapping;
import uts.aai.pbmn.WekaExecutor;
import uts.aai.pn.utils.IOUtils;
import uts.aai.pn.utils.JSONUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class MetaKnowledgeGenerator {

    public void run() {
        String outputPath = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\output\\metadata-temp-out";
        String dataFolderName = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\testing\\synthetic";
        String metaKnowledgeFile = "C:\\DATA\\Projects\\eclipse-workspace\\aai_aw\\weka-3-7-7\\data\\meta_knowledge\\meta_knowledge.json";
        cleanOutput(outputPath);
        init();

        for (MLComponent mLComponent : MLComponentConfiguration.getListOfMLComponents()) 
        {

            //MLComponent mLComponent = MLComponentConfiguration.getComponentByID("weka.filters.unsupervised.attribute.PrincipalComponents");

            List<MLComponentIO> listOfCapabilities = disableAllMetaFeatures();;
            List<MLComponentIO> listOfEffects = disableAllMetaFeatures();;

            String algorithmId = mLComponent.getComponentId();

            if (listOfCapabilities == null) {
                System.out.println("NULL listOfCapabilities");
            }

            File folder = new File(dataFolderName);
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory() && fileEntry.getName().contains(".arff")) {
                    System.out.println(fileEntry.getName());

                    String filePathWithoutExtension = fileEntry.getAbsolutePath();
                    filePathWithoutExtension = filePathWithoutExtension.substring(0, filePathWithoutExtension.length() - 5);
                    findAlgorithmCapabilitiesAndEffectsFromOneDataset(filePathWithoutExtension, outputPath, algorithmId, listOfCapabilities, listOfEffects);
                    cleanOutput(outputPath);
                }
            }

            printMLComponentIOList(listOfCapabilities, "Capabilities");
            printMLComponentIOList(listOfEffects, "Effects");

            mLComponent.setListOfCapabilities(listOfCapabilities);
            mLComponent.setListOfEffects(listOfEffects);
        }

        saveMetaKnowledge(metaKnowledgeFile);

    }

    private void cleanOutput(String outputPath) {
        File file = new File(outputPath + ".arff");
        file.delete();

        file = new File(outputPath + ".csv");
        file.delete();
    }

    private void findAlgorithmCapabilitiesAndEffectsFromOneDataset(String datasetPath, String outputPath, String algorithmId, List<MLComponentIO> listOfCapabilities, List<MLComponentIO> listOfEffects) {

        List<MLComponentIO> listOfInputMetaFeatures = calculateDatasetMetafeatures(datasetPath);

        boolean isSuccess = executeAlgorithm(algorithmId, datasetPath, outputPath);

        if (isSuccess) {
            findCapabilities(listOfInputMetaFeatures, listOfCapabilities);
            List<MLComponentIO> listOfOutputMetaFeatures = null;
            if (!MLComponentConfiguration.getComponentByID(algorithmId).getmLComponentType().equals(MLComponentType.CLASSIFIER)) {
                fromArffToCSV(outputPath + ".arff", outputPath + ".csv");
                listOfOutputMetaFeatures = calculateDatasetMetafeatures(outputPath);
            }

            printCompare(listOfInputMetaFeatures, listOfOutputMetaFeatures);
            findEffects(algorithmId, listOfInputMetaFeatures, listOfOutputMetaFeatures, listOfEffects);

        }

//            printMLComponentIOList(listOfCapabilities,"Capabilities");
//            printMLComponentIOList(listOfEffects,"Effects");
    }

    private void init() {

        MLComponentConfiguration.initConfiguration();

    }

    private void findCapabilities(List<MLComponentIO> listOfInputMetaFeatures, List<MLComponentIO> listOfCapabilities) {
        for (MLComponentIO capability : listOfCapabilities) {
            for (MLComponentIO inputMetafeature : listOfInputMetaFeatures) {
                if (capability.getmLComponentCapability().equals(inputMetafeature.getmLComponentCapability())
                        && inputMetafeature.getValue().equals(1)) {
                    capability.setValue(1);
                }
            }

        }
    }

    private void findEffects(String algorithmId, List<MLComponentIO> listOfInputMetaFeatures, List<MLComponentIO> listOfOutputMetaFeatures, List<MLComponentIO> listOfEffects) {
        // for finding effects
        if (MLComponentConfiguration.getComponentByID(algorithmId).getmLComponentType().equals(MLComponentType.CLASSIFIER)) {

            for (MLComponentIO effect : listOfEffects) {

                if (effect.getmLComponentCapability().equals(MLMetafeature.PREDICTIVE_MODEL)) {
                    effect.setValue(1);
                    break;
                }
            }

        } else {
            for (MLComponentIO effect : listOfEffects) {

                MLComponentIO inputMetafeature = findMLComponentIOInList(listOfInputMetaFeatures, effect.getmLComponentCapability());
                MLComponentIO outputMetafeature = findMLComponentIOInList(listOfOutputMetaFeatures, effect.getmLComponentCapability());

                if (inputMetafeature != null && outputMetafeature != null && effect.getValue().equals(0)) {
                    int changedValue = outputMetafeature.getValue() - inputMetafeature.getValue();
                    System.out.println("Effect: " + effect.getmLComponentCapability() + " - OLD: " + effect.getValue() + " - NEW: " + changedValue);
                    effect.setValue(changedValue);

                } else {
                    System.out.println("Find Effects - NULL - Metafeature");
                }
            }

        }
    }

    private MLComponentIO findMLComponentIOInList(List<MLComponentIO> listOfMLComponentIO, MLMetafeature metafeature) {

        MLComponentIO foundMLComponentIO = null;
        for (MLComponentIO componentIO : listOfMLComponentIO) {
            if (componentIO.getmLComponentCapability().equals(metafeature)) {
                foundMLComponentIO = componentIO;
                break;
            }
        }

        return foundMLComponentIO;
    }

    private List<MLComponentIO> calculateDatasetMetafeatures(String syntheticDatasetFilePath) {

        DatasetMetaFeatures datasetMetaFeatures = new DatasetMetaFeatures(syntheticDatasetFilePath + ".csv");
        List<MLComponentIO> listOfMetafeatures = datasetMetaFeatures.analyseMetaFeatures();

        return listOfMetafeatures;

    }

    private boolean executeAlgorithm(String algorithmID, String inputPath, String outputPath) {

        WekaExecutor wekaExecutor = new WekaExecutor();
        boolean result = wekaExecutor.executeAlgorithm(inputPath + ".arff", outputPath + ".arff", algorithmID);
        return result;
    }

    private void saveMetaKnowledge(String metaKnowledgeFile) {
        try {

            AlgorithmMetaKnowledge algorithmMetaKnowledge = new AlgorithmMetaKnowledge(MLComponentConfiguration.getListOfMLComponents());

            String metaKnowledgeJSON = JSONUtils.marshal(algorithmMetaKnowledge, AlgorithmMetaKnowledge.class);
            System.out.println("");
            System.out.println(metaKnowledgeJSON);

            IOUtils iou = new IOUtils();
            iou.overWriteData(metaKnowledgeJSON, metaKnowledgeFile);

            AlgorithmMetaKnowledge metaKnowledge2 = JSONUtils.unmarshal(metaKnowledgeJSON, AlgorithmMetaKnowledge.class);

            for (MLComponent mLComponent : metaKnowledge2.getListOfMLComponents()) {
                System.out.println("JSON Unmarshall: \n" + mLComponent.getComponentId());
            }

        } catch (JAXBException ex) {
            Logger.getLogger(MetaKnowledgeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void printMLComponentIOList(List<MLComponentIO> listOfMLComponentIOs, String listName) {
        System.out.println("\nLIST --- " + listName);
        for (MLComponentIO mLComponentIO : listOfMLComponentIOs) {
            System.out.println(mLComponentIO.getmLComponentCapability() + ": " + mLComponentIO.getValue());
        }

    }

    private void printCompare(List<MLComponentIO> list1, List<MLComponentIO> list2) {
        System.out.println("\nCOMPARE --- ");
        for (MLComponentIO mLComponentIO : list1) {

            if (list2 != null) {
                MLComponentIO foundCorrespondingObject = null;
                for (MLComponentIO correspondingObject : list2) {
                    if (mLComponentIO.getmLComponentCapability().equals(correspondingObject.getmLComponentCapability())) {
                        foundCorrespondingObject = correspondingObject;
                        break;
                    }
                }

                System.out.println(mLComponentIO.getmLComponentCapability() + ": " + mLComponentIO.getValue() + " - " + foundCorrespondingObject.getValue());

            } else {
                System.out.println(mLComponentIO.getmLComponentCapability() + ": " + mLComponentIO.getValue() + " - ?");
            }
        }

    }

    private List<MLComponentIO> disableAllMetaFeatures() {

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

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.IMBALANCE_CLASS, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }

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
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.PREDICTIVE_MODEL, 0);
            listOfMLComponentIOs.add(mLComponentIO);
        }
        return listOfMLComponentIOs;
    }

    private void fromArffToCSV(String sourceArff, String outputCSV) {

        ArffLoader loader = new ArffLoader();
        try {
            loader.setSource(new File(sourceArff));
            Instances data = loader.getDataSet();

            CSVSaver saver = new CSVSaver();
            saver.setInstances(data);

            saver.setFile(new File(outputCSV));
            saver.writeBatch();
        } catch (IOException ex) {
            Logger.getLogger(SurrogatePipelineMapping.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

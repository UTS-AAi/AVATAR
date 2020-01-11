/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uts.aai.avatar.configuration.AppConst;
import uts.aai.avatar.configuration.MLComponentConfiguration;
import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.avatar.model.MLComponentType;
import uts.aai.avatar.model.MLHyperparameter;
import uts.aai.avatar.model.MLHyperparameterType;
import uts.aai.avatar.model.MLMetafeature;
import uts.aai.avatar.model.MetafeatureType;
import uts.aai.avatar.model.MetafeatureTypeConfig;
import uts.aai.avatar.service.DatasetMetaFeatures;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author ntdun
 */
public class MetaKnowledgeGenerator {

    private String outputPath;
    private String dataFolderName;
    private String metaKnowledgeFile;
    private List<MLComponent> loadedListOfMLComponents;

    public MetaKnowledgeGenerator() {
    }

    public MetaKnowledgeGenerator(String outputPath, String dataFolderName, String metaKnowledgeFile) {
        this.outputPath = outputPath;
        this.dataFolderName = dataFolderName;
        this.metaKnowledgeFile = metaKnowledgeFile;
        MLComponentConfiguration.initDefault();
        loadedListOfMLComponents = MLComponentConfiguration.getListOfMLComponents();
    }

    public void run() {

        cleanOutput();
        init();

        for (MLComponent mLComponent : MLComponentConfiguration.getListOfMLComponents()) {

            System.out.println("mLComponent: " + mLComponent.getComponentId());
            //MLComponent mLComponent = MLComponentConfiguration.getComponentByID("weka.filters.unsupervised.attribute.PrincipalComponents");
            // List<MLComponentIO> listOfCapabilities = disableAllMetaFeatures();;
            // List<MLComponentIO> listOfEffects = disableAllMetaFeatures();;

            // String algorithmId = mLComponent.getComponentId();
            // if (listOfCapabilities == null) {
            //    System.out.println("NULL listOfCapabilities");
            // }
            File folder = new File(dataFolderName);
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory() && fileEntry.getName().contains(".arff")) {
                    System.out.println(fileEntry.getName());

                    String filePathWitExtension = fileEntry.getAbsolutePath();

                    findAlgorithmCapabilitiesAndEffectsFromOneDataset(filePathWitExtension, mLComponent);
                    cleanOutput();
                }
            }

//            printMLComponentIOList(listOfCapabilities, "Capabilities");
//            printMLComponentIOList(listOfEffects, "Effects");
//
//            mLComponent.setListOfCapabilities(listOfCapabilities);
//            mLComponent.setListOfEffects(listOfEffects);
        }

        saveMetaKnowledge(metaKnowledgeFile,MLComponentConfiguration.getListOfMLComponents());

    }

    public void runWithSelectedAlgorithms(List<String> listOfSelectedAlgorithms) {

        cleanOutput();
        init();
        List<MLComponent> listOfMLComponents = preprocessComponentConfig(listOfSelectedAlgorithms);

        for (MLComponent mLComponent : listOfMLComponents) {

            System.out.println("mLComponent: " + mLComponent.getComponentId());

            File folder = new File(dataFolderName);
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory() && fileEntry.getName().contains(".arff")) {
                    System.out.println(fileEntry.getName());

                    String filePathWitExtension = fileEntry.getAbsolutePath();

                    findAlgorithmCapabilitiesAndEffectsFromOneDataset(filePathWitExtension, mLComponent);
                    cleanOutput();
                }
            }

        }

        saveMetaKnowledge(metaKnowledgeFile,listOfMLComponents);
    }

    public List<MLComponent> preprocessComponentConfig(List<String> selectedMLComponents) {

        List<MLComponent> listOfPredictors = MLComponentConfiguration.initClassifier();

        List<MLComponent> listOfMLComponents = new ArrayList<>();
        for (String selectedComponent : selectedMLComponents) {

            MLComponent mLComponent = MLComponentConfiguration.getComponentByID(selectedComponent);

            if (mLComponent.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {

                //  for (MLHyperparameter mLHyperparameter : listOfMLHyperparameters) {
                //  if (mLHyperparameter.getHyperparameterType().equals(MLHyperparameterType.PREDICTORS)) {
                for (MLComponent mLComponentClassifier : listOfPredictors) {

                    if (!mLComponentClassifier.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {

                        String classifierScript = mLComponentClassifier.getComponentExecutionScriptSingleComponentWeka();
                        String[] scriptConfigs = classifierScript.split(" ");
                        String configStr = "";
                        if (scriptConfigs.length > 1) {

                            for (int i = 1; i < scriptConfigs.length; i++) {

                                if (i == scriptConfigs.length - 1) {
                                    configStr += scriptConfigs[i];
                                } else {
                                    configStr += scriptConfigs[i] + " ";
                                }

                            }
                            configStr = scriptConfigs[0] + " -- " + configStr;
                        } else {
                            configStr = scriptConfigs[0];
                        }

                        System.out.println("configStr: " + configStr);

                        List<MLHyperparameter> listOfHyperparametersC = new ArrayList<>();
                        
//                        for (MLHyperparameter mLHyperparameter : mLComponent.getListOfMLHyperparameters()) {
//                            
//                            MLHyperparameter mLHyperparameterC = new MLHyperparameter(mLHyperparameter.getCommand(), mLHyperparameter.getHyperparameterType());
//                            mLHyperparameterC.setDefaultBoolValue(mLHyperparameter.getDefaultBoolValue());
//                            mLHyperparameterC.setDefaultIntValue(mLHyperparameter.getDefaultIntValue());
//                            mLHyperparameterC.setDefaultNominalValue(mLHyperparameter.getDefaultNominalValue());
//                            mLHyperparameterC.setDefaultNumericValue(mLHyperparameter.getDefaultNumericValue());
//                            mLHyperparameterC.setMaxIntValue(mLHyperparameter.getMaxIntValue());
//                            mLHyperparameterC.setMaxNumericValue(mLHyperparameter.getMaxNumericValue());
//                            mLHyperparameterC.setMinIntValue(mLHyperparameter.getMinIntValue());
//                            mLHyperparameterC.setMinNumericValue(mLHyperparameter.getMinNumericValue());
//                            mLHyperparameterC.setDefaultConfigString(mLHyperparameter.getDefaultConfigString());
//                        }
//                        
                        
                        List<MLComponentIO> listOfInputs = new ArrayList<>();
                        List<MLComponentIO> listOfOutputs = new ArrayList<>();
                        listOfOutputs.add(new MLComponentIO(MLMetafeature.PREDICTIVE_MODEL, 1));

                        MLComponent cMLComponent = new MLComponent(
                                mLComponent.getComponentId(),
                                mLComponent.getComponentName(),
                                mLComponent.getComponentFullClassName(),
                                mLComponent.getmLComponentType(),
                                mLComponent.getComponentExecutionScriptSingleComponentWeka(),
                                mLComponent.getComponentExecutionScriptFilteredClassifierWeka(),
                                listOfInputs,
                                listOfOutputs,
                                listOfHyperparametersC);

                        String templateConfig = cMLComponent.getComponentExecutionScriptSingleComponentWeka();
                        templateConfig = templateConfig.replace("#PREDICTOR_CONFIG#", configStr);
                        cMLComponent.setComponentExecutionScriptSingleComponentWeka(templateConfig);
                        cMLComponent.setComponentExecutionScriptFilteredClassifierWeka(scriptConfigs[0]);
                        listOfMLComponents.add(cMLComponent);
                    }
              //      break;
                }

                // }
                //}
            }
            //break;
        }

        for (MLComponent mLComponent : listOfMLComponents) {
            System.out.println("MLComponent 1: " + mLComponent.getComponentExecutionScriptSingleComponentWeka());
            System.out.println("MLComponent 2: " + mLComponent.getComponentExecutionScriptFilteredClassifierWeka());
            System.out.println(" ");
        }

        return listOfMLComponents;

    }

    public void update(String newDataSetPath) {
        cleanOutput();
        MLComponentConfiguration.initDefault();

        for (MLComponent mLComponent : MLComponentConfiguration.getListOfMLComponents()) {

            findAlgorithmCapabilitiesAndEffectsFromOneDataset(newDataSetPath, mLComponent);
            cleanOutput();

        }

        saveMetaKnowledge(metaKnowledgeFile,MLComponentConfiguration.getListOfMLComponents());

    }

    private void cleanOutput() {
        File file = new File(outputPath);
        file.delete();

    }

    private void findAlgorithmCapabilitiesAndEffectsFromOneDataset(String datasetPath, MLComponent mLComponent) {

        mLComponent.setListOfCapabilities(disableAllMetaFeatures());
        mLComponent.setListOfEffects(disableAllMetaFeatures());

        String algorithmId = mLComponent.getComponentId();
        List<MLComponentIO> listOfCapabilities = mLComponent.getListOfCapabilities();
        List<MLComponentIO> listOfEffects = mLComponent.getListOfEffects();
        List<MLComponentIO> listOfInputMetaFeatures = calculateDatasetMetafeatures(datasetPath);

        boolean isSuccess = executeAlgorithm(mLComponent, datasetPath);

        if (isSuccess) {
            findCapabilities(listOfInputMetaFeatures, listOfCapabilities);
            List<MLComponentIO> listOfOutputMetaFeatures = null;
            if (!mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER)
                    && !mLComponent.getmLComponentType().equals(MLComponentType.REGRESSOR)
                    && !mLComponent.getmLComponentType().equals(MLComponentType.CLASSIFIER_REGRESSOR)
                    && !mLComponent.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {
                //fromArffToCSV(outputPath + ".arff", outputPath + ".csv");
                listOfOutputMetaFeatures = calculateDatasetMetafeatures(outputPath);
            }

            printCompare(listOfInputMetaFeatures, listOfOutputMetaFeatures);
            findEffects(algorithmId, listOfInputMetaFeatures, listOfOutputMetaFeatures, listOfEffects);
            System.out.println("SUCCESS .................................................");
        } else {
            System.out.println("FAIL .................................................");
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
        if (MLComponentConfiguration.getComponentByID(algorithmId, loadedListOfMLComponents).getmLComponentType().equals(MLComponentType.CLASSIFIER)
                || MLComponentConfiguration.getComponentByID(algorithmId, loadedListOfMLComponents).getmLComponentType().equals(MLComponentType.REGRESSOR)
                || MLComponentConfiguration.getComponentByID(algorithmId, loadedListOfMLComponents).getmLComponentType().equals(MLComponentType.CLASSIFIER_REGRESSOR)
                || MLComponentConfiguration.getComponentByID(algorithmId, loadedListOfMLComponents).getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {

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

        DatasetMetaFeatures datasetMetaFeatures = new DatasetMetaFeatures(syntheticDatasetFilePath);
        List<MLComponentIO> listOfMetafeatures = datasetMetaFeatures.analyseMetaFeaturesArff();

        return listOfMetafeatures;

    }

    private boolean executeAlgorithm(MLComponent mLComponent, String inputPath) {

        WekaExecutor wekaExecutor = new WekaExecutor();
        boolean result = wekaExecutor.executeAlgorithm(inputPath, outputPath, mLComponent);
        return result;
    }

    private void saveMetaKnowledge(String metaKnowledgeFile, List<MLComponent> listOfMLComponents) {
        try {

            AlgorithmMetaKnowledge algorithmMetaKnowledge = new AlgorithmMetaKnowledge(listOfMLComponents);

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

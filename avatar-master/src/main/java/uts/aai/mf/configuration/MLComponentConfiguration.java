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
package uts.aai.mf.configuration;

import uts.aai.mf.model.MLComponent;
import uts.aai.mf.model.MLMetafeature;
import uts.aai.mf.model.MLComponentIO;
import uts.aai.mf.model.MLComponentType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uts.aai.global.AppConst;
import uts.aai.metaknowledge.generator.AlgorithmMetaKnowledge;
import uts.aai.pn.utils.IOUtils;
import uts.aai.pn.utils.JSONUtils;

/**
 *
 * @author ntdun
 */
public class MLComponentConfiguration {

    private static List<MLComponent> listOfMLComponents;

    public static List<MLComponent> initConfiguration() {

        listOfMLComponents = new ArrayList<>();

        listOfMLComponents.addAll(initClassifier());
        listOfMLComponents.addAll(initMissingValueComponent());
        listOfMLComponents.addAll(initDataBalancerComponent());
        listOfMLComponents.addAll(initDataSamplingComponent());
        listOfMLComponents.addAll(initDataTransformationComponent());
        listOfMLComponents.addAll(initDimensionalityReductionComponent());
        listOfMLComponents.addAll(initOutlierRemovalComponent());

        return listOfMLComponents;
    }

    public static void initDefault() {

        IOUtils iou = new IOUtils();

        String metaKnowledgeJSON = iou.readData(AppConst.META_KNOWLEDGE_PATH);
        try {
            AlgorithmMetaKnowledge metaKnowledge = JSONUtils.unmarshal(metaKnowledgeJSON, AlgorithmMetaKnowledge.class);

            listOfMLComponents = metaKnowledge.getListOfMLComponents();
        } catch (JAXBException ex) {
            Logger.getLogger(MLComponentConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static List<MLComponent> getListOfMLComponents() {
        return listOfMLComponents;
    }

    public static void setListOfMLComponents(List<MLComponent> listOfMLComponents) {
        MLComponentConfiguration.listOfMLComponents = listOfMLComponents;
    }

    public static List<MLComponent> initClassifier() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();
        {
            String componentId = "weka.classifiers.bayes.NaiveBayes";
            String componentName = "NaiveBayes";
            String componentFullClassName = "weka.classifiers.bayes.NaiveBayes";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.bayes.NaiveBayes";
            String componentExecutionScriptFilteredClassifierWeka = "-W weka.classifiers.bayes.NaiveBayes --";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));

            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.BINARY_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.IMBALANCE_CLASS));

            List<MLComponentIO> listOfOutputs = new ArrayList<>();
            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.LinearRegression";
            String componentName = "LinearRegression";
            String componentFullClassName = "weka.classifiers.functions.LinearRegression";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.functions.LinearRegression -S 0 -R 1.0E-8";
            String componentExecutionScriptFilteredClassifierWeka = "-W weka.classifiers.functions.LinearRegression -- -S 0 -R 1.0E-8";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));

            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.IMBALANCE_CLASS));

            List<MLComponentIO> listOfOutputs = new ArrayList<>();
            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;
    }

    public static List<MLComponent> initMissingValueComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();

        {
            String componentId = "weka.filters.unsupervised.attribute.ReplaceMissingValues";
            String componentName = "ReplaceMissingValues";
            String componentFullClassName = "weka.filters.unsupervised.attribute.ReplaceMissingValues";
            MLComponentType mLComponentType = MLComponentType.MISSING_VALUE_HANDLER;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.ReplaceMissingValues";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.ReplaceMissingValues \\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();
            listOfOutputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));
            listOfOutputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.EMImputation";
            String componentName = "EMImputation";
            String componentFullClassName = "weka.filters.unsupervised.attribute.EMImputation";
            MLComponentType mLComponentType = MLComponentType.MISSING_VALUE_HANDLER;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.EMImputation -N -1 -E 1.0E-4 -Q 1.0E-8";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.EMImputation -N -1 -E 1.0E-4 -Q 1.0E-8\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();
            listOfOutputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));
            listOfOutputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;

    }

    public static List<MLComponent> initDataBalancerComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();

//        {
//            String componentId = "weka.filters.supervised.instance.ClassBalancer";
//            String componentName= "ClassBalancer";
//            String componentFullClassName= "weka.filters.supervised.instance.ClassBalancer";
//            MLComponentType mLComponentType = MLComponentType.DATA_BALANCER;
//            String componentExecutionScript = "weka.filters.supervised.instance.ClassBalancer";
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//            listOfInputs.addAll(getAllAttributesCapabilities());
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_CLASS));
//            
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId, 
//                    componentName, 
//                    componentFullClassName,
//                    mLComponentType, 
//                    componentExecutionScript,
//                    listOfInputs,
//                    listOfOutputs);
//            listOfMLComponentByType.add(mLComponent);
//        }
//        {
//            String componentId = "weka.filters.supervised.instance.SpreadSubsample";
//            String componentName= "SpreadSubsample";
//            String componentFullClassName= "weka.filters.supervised.instance.SpreadSubsample";
//            MLComponentType mLComponentType = MLComponentType.DATA_BALANCER;
//            String componentExecutionScript = "weka.filters.supervised.instance.SpreadSubsample -M 1.0 -X 0.0 -S 1";
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//            listOfInputs.addAll(getAllClassesCapabilities());
//            listOfInputs.addAll(getAllAttributesCapabilities());
//            
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId, 
//                    componentName, 
//                    componentFullClassName,
//                    mLComponentType, 
//                    componentExecutionScript,
//                    listOfInputs,
//                    listOfOutputs);
//            listOfMLComponentByType.add(mLComponent);
//        }
        return listOfMLComponentByType;
    }

    public static List<MLComponent> initDimensionalityReductionComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();
        {
            String componentId = "weka.filters.unsupervised.attribute.PrincipalComponents";
            String componentName = "PrincipalComponents";
            String componentFullClassName = "weka.filters.unsupervised.attribute.PrincipalComponents";
            MLComponentType mLComponentType = MLComponentType.DIMENTIONALITY_REDUCTION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.PrincipalComponents -R 0.95 -A 5 -M -1 -c last";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.PrincipalComponents -R 0.95 -A 5 -M -1\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_ATTRIBUTES));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));
//            
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_CLASS));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_CLASS));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_CLASS));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.NO_CLASS));
//            listOfInputs.add(getMLComponentIO(MLMetafeature.IMBALANCE_CLASS));
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            listOfOutputs.add(getMLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;

    }

    public static List<MLComponent> initDataSamplingComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();

        {
            String componentId = "weka.filters.unsupervised.instance.Resample";
            String componentName = "Resample";
            String componentFullClassName = "weka.filters.unsupervised.instance.Resample";
            MLComponentType mLComponentType = MLComponentType.DATA_SAMPLING;
            String componentExecutionScript = "weka.filters.unsupervised.instance.Resample -S 1 -Z 50.0";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.instance.Resample -S 1 -Z 50.0\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.instance.ReservoirSample";
            String componentName = "ReservoirSample";
            String componentFullClassName = "weka.filters.unsupervised.instance.ReservoirSample";
            MLComponentType mLComponentType = MLComponentType.DATA_SAMPLING;
            String componentExecutionScript = "weka.filters.unsupervised.instance.ReservoirSample -S 1 -Z 50";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.instance.ReservoirSample -S 1 -Z 50\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.filters.unsupervised.instance.PeriodicSampling";
//            String componentName= "PeriodicSampling";
//            String componentFullClassName= "weka.filters.unsupervised.instance.PeriodicSampling";
//            MLComponentType mLComponentType = MLComponentType.DATA_SAMPLING;
//            String componentExecutionScript = "weka.filters.unsupervised.instance.PeriodicSampling -N 2";
//            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.instance.PeriodicSampling -N 2\\\" ";
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//            listOfInputs.addAll(getAllClassesCapabilities());
//            listOfInputs.addAll(getAllAttributesCapabilities());
//            
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId, 
//                    componentName, 
//                    componentFullClassName,
//                    mLComponentType, 
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs);
//            listOfMLComponentByType.add(mLComponent);
//        }
        return listOfMLComponentByType;

    }

    public static List<MLComponent> initOutlierRemovalComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();

        {
            String componentId = "weka.filters.unsupervised.instance.PeriodicSampling";
            String componentName = "PeriodicSampling";
            String componentFullClassName = "weka.filters.unsupervised.instance.PeriodicSampling";
            MLComponentType mLComponentType = MLComponentType.OUTLIER_REMOVAL;
            String componentExecutionScript = "weka.filters.unsupervised.instance.RemoveOutliers -O \"weka.filters.unsupervised.attribute.InterquartileRange -R first-last -O 3.0 -E 6.0\"";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.instance.RemoveOutliers -O \\\\\\\"weka.filters.unsupervised.attribute.InterquartileRange -R first-last -O 3.0 -E 6.0\\\\\\\"\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();
            listOfOutputs.add(getMLComponentIO(MLMetafeature.OUTLIER_ATTRIBUTES));

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;
    }

    public static List<MLComponent> initDataTransformationComponent() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();

        {
            String componentId = "weka.filters.unsupervised.attribute.Center";
            String componentName = "Center";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Center";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Center";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.Center \\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.Standardize";
            String componentName = "Standardize";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Standardize";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Standardize";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.Standardize \\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.Normalize";
            String componentName = "Normalize";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Normalize";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Normalize -S 1.0 -T 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.Normalize -S 1.0 -T 0.0\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.addAll(getAllClassesCapabilities());
            listOfInputs.addAll(getAllAttributesCapabilities());

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.IndependentComponents";
            String componentName = "IndependentComponents";
            String componentFullClassName = "weka.filters.unsupervised.attribute.IndependentComponents";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.IndependentComponents -W -A -1 -N 200 -T 1.0E-4";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.IndependentComponents -W -A -1 -N 200 -T 1.0E-4\\\" ";

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_ATTRIBUTES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_VALUES));

            listOfInputs.add(getMLComponentIO(MLMetafeature.NOMINAL_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.UNARY_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.NUMERIC_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.DATE_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.MISSING_CLASS_VALUES));
            listOfInputs.add(getMLComponentIO(MLMetafeature.NO_CLASS));
            listOfInputs.add(getMLComponentIO(MLMetafeature.IMBALANCE_CLASS));

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;

    }

    private static MLComponent getMLComponentById(String componentId) {

        for (MLComponent mLComponent : listOfMLComponents) {
            if (mLComponent.getComponentId().equals(componentId)) {
                return mLComponent;
            }
        }

        return null;
    }

    private static List<MLComponentIO> getAllClassesCapabilities() {

        List<MLComponentIO> listOfMLComponentIOs = new ArrayList<>();

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.BINARY_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.DATE_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.EMPTY_NOMINAL_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.MISSING_CLASS_VALUES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NOMINAL_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NO_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NUMERIC_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.RELATIONAL_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.STRING_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.UNARY_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.IMBALANCE_CLASS, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        return listOfMLComponentIOs;
    }

    private static List<MLComponentIO> getAllAttributesCapabilities() {

        List<MLComponentIO> listOfMLComponentIOs = new ArrayList<>();

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.BINARY_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.DATE_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.EMPTY_NOMINAL_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.MISSING_VALUES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NOMINAL_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.NUMERIC_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.ONLY_MULTIINSTANCE, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.RELATIONAL_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.STRING_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.UNARY_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

        {
            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.OUTLIER_ATTRIBUTES, 1);
            listOfMLComponentIOs.add(mLComponentIO);
        }

//        {
//            MLComponentIO mLComponentIO = new MLComponentIO(MLMetafeature.PREDICTIVE_MODEL, 1);
//            listOfMLComponentIOs.add(mLComponentIO);
//        }
        return listOfMLComponentIOs;
    }

    private static MLComponentIO getMLComponentIO(MLMetafeature mLComponentCapability) {
        return new MLComponentIO(mLComponentCapability, 1);
    }

    public static MLComponent getComponentByID(String componentId) {

        for (MLComponent mLComponent : listOfMLComponents) {
            if (mLComponent.getComponentId().equals(componentId)) {
                return mLComponent;
            }
        }
        return null;
    }

    public static List<MLComponentIO> getInputByComponentID(String componentId) {

        for (MLComponent mLComponent : listOfMLComponents) {
            if (mLComponent.getComponentId().equals(componentId)) {
                return mLComponent.getListOfCapabilities();
            }
        }
        return null;
    }

    public static List<MLComponentIO> getOutputByComponentID(String componentId) {

        for (MLComponent mLComponent : listOfMLComponents) {
            if (mLComponent.getComponentId().equals(componentId)) {
                return mLComponent.getListOfEffects();
            }
        }
        return null;
    }

}

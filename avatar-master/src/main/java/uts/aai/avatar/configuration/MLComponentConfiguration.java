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
package uts.aai.avatar.configuration;

import uts.aai.avatar.model.MLComponent;
import uts.aai.avatar.model.MLMetafeature;
import uts.aai.avatar.model.MLComponentIO;
import uts.aai.avatar.model.MLComponentType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import uts.aai.avatar.service.AlgorithmMetaKnowledge;
import uts.aai.avatar.model.MLHyperparameter;
import uts.aai.avatar.model.MLHyperparameterType;
import uts.aai.pn.model.AlgorithmConfiguration;
import uts.aai.pn.model.Parameter;
import uts.aai.utils.IOUtils;
import uts.aai.utils.JSONUtils;

/**
 *
 * @author ntdun
 */
public class MLComponentConfiguration {

    private static List<MLComponent> listOfMLComponents;
    private static List<String> listOfNotAutoFinaliseAlgorithm;

    public static List<MLComponent> initConfiguration() {

        initNotAutoFinaliseAlgorithm();
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

        initNotAutoFinaliseAlgorithm();

    }

    public static List<MLComponent> getListOfMLComponents() {
        return listOfMLComponents;
    }

    public static void setListOfMLComponents(List<MLComponent> listOfMLComponents) {
        MLComponentConfiguration.listOfMLComponents = listOfMLComponents;
    }

    public static void initNotAutoFinaliseAlgorithm() {
        listOfNotAutoFinaliseAlgorithm = new ArrayList<>();
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.functions.MultilayerPerceptron");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.AdaBoostM1");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.Bagging");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.LogitBoost");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.MultiClassClassifier");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.RandomCommittee");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.meta.RandomSubSpace");
        listOfNotAutoFinaliseAlgorithm.add("weka.classifiers.trees.REPTree");
    }

    public static List<MLComponent> initClassifier() {

        List<MLComponent> listOfMLComponentByType = new ArrayList<>();
        
        
        
        ////////////////////////////////////////////////
        // BASE LEARNERS
        /////////////////////////////////////////////// 
        
        {
            String componentId = "weka.classifiers.functions.supportVector.PolyKernel";
            String componentName = "PolyKernel";
            String componentFullClassName = "weka.classifiers.functions.supportVector.PolyKernel";
            MLComponentType mLComponentType = MLComponentType.BASE_LEARNER;
            String componentExecutionScript = "weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-E", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(5.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs,
                    listOfHyperparameters);

            listOfMLComponentByType.add(mLComponent);
        }
        
        {
            String componentId = "weka.classifiers.functions.supportVector.NormalizedPolyKernel";
            String componentName = "NormalizedPolyKernel";
            String componentFullClassName = "weka.classifiers.functions.supportVector.NormalizedPolyKernel";
            MLComponentType mLComponentType = MLComponentType.BASE_LEARNER;
            String componentExecutionScript = "weka.classifiers.functions.supportVector.NormalizedPolyKernel -C 250007 -E 2.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-E", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(5.0);
                hp.setDefaultNumericValue(2.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs,
                    listOfHyperparameters);

            listOfMLComponentByType.add(mLComponent);
        }
        
        
        {
            String componentId = "weka.classifiers.functions.supportVector.Puk";
            String componentName = "Puk";
            String componentFullClassName = "weka.classifiers.functions.supportVector.Puk";
            MLComponentType mLComponentType = MLComponentType.BASE_LEARNER;
            String componentExecutionScript = "weka.classifiers.functions.supportVector.Puk -C 250007 -O 1.0 -S 1.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-O", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(100.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(5.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs,
                    listOfHyperparameters);

            listOfMLComponentByType.add(mLComponent);
        }
        
        {
            String componentId = "weka.classifiers.functions.supportVector.RBFKernel";
            String componentName = "RBFKernel";
            String componentFullClassName = "weka.classifiers.functions.supportVector.RBFKernel";
            MLComponentType mLComponentType = MLComponentType.BASE_LEARNER;
            String componentExecutionScript = "weka.classifiers.functions.supportVector.RBFKernel -C 250007 -G 10.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-G", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(10.0);
                hp.setDefaultNumericValue(0.01);
                listOfHyperparameters.add(hp);
            }
            
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();
            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs,
                    listOfHyperparameters);

            listOfMLComponentByType.add(mLComponent);
        }
        
        
        
        
        

        ////////////////////////////////////////////////
        // BAYES
        ///////////////////////////////////////////////
        {
            String componentId = "weka.classifiers.bayes.NaiveBayes";
            String componentName = "NaiveBayes";
            String componentFullClassName = "weka.classifiers.bayes.NaiveBayes";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.bayes.NaiveBayes";
            String componentExecutionScriptFilteredClassifierWeka = "-W weka.classifiers.bayes.NaiveBayes --";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }

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
                    listOfOutputs,
                    listOfHyperparameters);

            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.bayes.NaiveBayesUpdateable";
            String componentName = "NaiveBayesUpdateable";
            String componentFullClassName = "weka.classifiers.bayes.NaiveBayesUpdateable";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.bayes.NaiveBayesUpdateable";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        ////////////////////////////////////////////////
        // FUNCTIONS
        ///////////////////////////////////////////////
        {
            String componentId = "weka.classifiers.functions.LinearRegression";
            String componentName = "LinearRegression";
            String componentFullClassName = "weka.classifiers.functions.LinearRegression";
            MLComponentType mLComponentType = MLComponentType.REGRESSOR;
            String componentExecutionScript = "weka.classifiers.functions.LinearRegression -S 0 -R 1.0E-8";
            String componentExecutionScriptFilteredClassifierWeka = "-W weka.classifiers.functions.LinearRegression -- -S 0 -R 1.0E-8";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-minimal", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(1.0);
                hp.setDefaultNumericValue(1.0E-8);
                listOfHyperparameters.add(hp);
            }
            
            
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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.Logistic";
            String componentName = "Logistic";
            String componentFullClassName = "weka.classifiers.functions.Logistic";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.functions.Logistic -R 1.0E-8 -M -1";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(1.0);
                hp.setDefaultNumericValue(1.0E-8);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.SimpleLogistic";
            String componentName = "SimpleLogistic";
            String componentFullClassName = "weka.classifiers.functions.SimpleLogistic";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.functions.SimpleLogistic -I 0 -M 500 -H 50 -W 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-H", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(50);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(100);
                hp.setMaxIntValue(500);
                hp.setDefaultIntValue(500);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(99);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-A", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(0.0);
                hp.setDefaultNumericValue(0.0);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.GaussianProcesses";
            String componentName = "GaussianProcesses";
            String componentFullClassName = "weka.classifiers.functions.GaussianProcesses";
            MLComponentType mLComponentType = MLComponentType.REGRESSOR;
            String componentExecutionScript = "weka.classifiers.functions.GaussianProcesses -L 1.0 -N 0 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(5.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.BASE_LEARNER);
                hp.setDefaultBaseLearner("weka.classifiers.functions.supportVector.PolyKernel");
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.MultilayerPerceptron";
            String componentName = "MultilayerPerceptron";
            String componentFullClassName = "weka.classifiers.functions.MultilayerPerceptron";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER_REGRESSOR;
            String componentExecutionScript = "weka.classifiers.functions.MultilayerPerceptron -L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            {
                MLHyperparameter hp = new MLHyperparameter("-H", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("a", "i", "o", "t")));
                hp.setDefaultNominalValue("a");
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(0.99);
                hp.setDefaultNumericValue(0.3);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(0.99);
                hp.setDefaultNumericValue(0.2);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-B", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.FALSE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(100);
                hp.setMaxIntValue(1000);
                hp.setDefaultIntValue(500);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-V", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-E", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(20);
                hp.setMaxIntValue(20);
                hp.setDefaultIntValue(20);
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.SimpleLinearRegression";
            String componentName = "SimpleLinearRegression";
            String componentFullClassName = "weka.classifiers.functions.SimpleLinearRegression";
            MLComponentType mLComponentType = MLComponentType.REGRESSOR;
            String componentExecutionScript = "weka.classifiers.functions.SimpleLinearRegression";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.functions.SMOreg";
            String componentName = "SMOreg";
            String componentFullClassName = "weka.classifiers.functions.SMOreg";
            MLComponentType mLComponentType = MLComponentType.REGRESSOR;
            String componentExecutionScript = "weka.classifiers.functions.SMOreg -C 1.0 -N 0 -I \"weka.classifiers.functions.supportVector.RegSMOImproved -L 0.001 -W 1 -P 1.0E-12 -T 0.001 -V\" -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(100.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("\"weka.classifiers.functions.supportVector.RegSMOImproved -L 0.001 -W 1 -P 1.0E-12 -T 0.001 -V\"")));
                hp.setDefaultNominalValue("\"weka.classifiers.functions.supportVector.RegSMOImproved -L 0.001 -W 1 -P 1.0E-12 -T 0.001 -V\"");
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.BASE_LEARNER);
                hp.setDefaultBaseLearner("weka.classifiers.functions.supportVector.PolyKernel");
                listOfHyperparameters.add(hp);
            }
            
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }
       

        {
            String componentId = "weka.classifiers.functions.SMO";
            String componentName = "SMO";
            String componentFullClassName = "weka.classifiers.functions.SMO";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.functions.SMO -C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(100.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.001);
                hp.setMaxNumericValue(0.001);
                hp.setDefaultNumericValue(0.001);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0E-12);
                hp.setMaxNumericValue(1.0E-12);
                hp.setDefaultNumericValue(1.0E-12);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-V", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.BASE_LEARNER);
                hp.setDefaultBaseLearner("weka.classifiers.functions.supportVector.PolyKernel");
                listOfHyperparameters.add(hp);
            }
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        ////////////////////////////////////////////////
        // LAZY
        ///////////////////////////////////////////////
        {
            String componentId = "weka.classifiers.lazy.IBk";
            String componentName = "IBk";
            String componentFullClassName = "weka.classifiers.lazy.IBk";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.lazy.IBk -K 1 -W 0 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\"";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-A", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("\"weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\"\""
                        ,"\"weka.core.neighboursearch.BallTree -A \"weka.core.EuclideanDistance -R first-last\" -C \"weka.core.neighboursearch.balltrees.TopDownConstructor -N 40 -S weka.core.neighboursearch.balltrees.PointsClosestToFurthestChildren\"\""
                        ,"\"weka.core.neighboursearch.CoverTree -A \"weka.core.EuclideanDistance -R first-last\" -B 1.3\""
                        ,"\"weka.core.neighboursearch.KDTree -A \"weka.core.EuclideanDistance -R first-last\" -S weka.core.neighboursearch.kdtrees.SlidingMidPointOfWidestSide -W 0.01 -L 40 -N\"")));
                hp.setDefaultNominalValue("\"weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\"\"");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.lazy.KStar";
            String componentName = "KStar";
            String componentFullClassName = "weka.classifiers.lazy.KStar";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.lazy.KStar -B 20 -M a";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-B", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(20);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("d","m","n","a")));
                hp.setDefaultNominalValue("d");
                listOfHyperparameters.add(hp);
            }
            
          
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.lazy.LWL";
            String componentName = "LWL";
            String componentFullClassName = "weka.classifiers.lazy.LWL";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.lazy.LWL -U 0 -K -1 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" -W weka.classifiers.trees.DecisionStump";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-U", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(5);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-A", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("\"weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\"\""
                        ,"\"weka.core.neighboursearch.BallTree -A \"weka.core.EuclideanDistance -R first-last\" -C \"weka.core.neighboursearch.balltrees.TopDownConstructor -N 40 -S weka.core.neighboursearch.balltrees.PointsClosestToFurthestChildren\"\""
                        ,"\"weka.core.neighboursearch.CoverTree -A \"weka.core.EuclideanDistance -R first-last\" -B 1.3\""
                        ,"\"weka.core.neighboursearch.KDTree -A \"weka.core.EuclideanDistance -R first-last\" -S weka.core.neighboursearch.kdtrees.SlidingMidPointOfWidestSide -W 0.01 -L 40 -N\"")));
                hp.setDefaultNominalValue("\"weka.core.neighboursearch.LinearNNSearch -A \"weka.core.EuclideanDistance -R first-last\"\"");
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.DecisionStump");
                listOfHyperparameters.add(hp);
            }
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        ////////////////////////////////////////////////
        // META
        ///////////////////////////////////////////////
        {
            String componentId = "weka.classifiers.meta.AdaBoostM1";
            String componentName = "AdaBoostM1";
            String componentFullClassName = "weka.classifiers.meta.AdaBoostM1";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.meta.AdaBoostM1 -P 100 -S 1 -I 10 -W weka.classifiers.trees.DecisionStump";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(500);
                hp.setDefaultIntValue(100);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.DecisionStump");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.classifiers.meta.AttributeSelectedClassifier";
//            String componentName = "AttributeSelectedClassifier";
//            String componentFullClassName = "weka.classifiers.meta.AttributeSelectedClassifier";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.AttributeSelectedClassifier -E \"weka.attributeSelection.CfsSubsetEval \" -S \"weka.attributeSelection.BestFirst -D 1 -N 5\" -W weka.classifiers.trees.J48 -- -C 0.25 -M 2";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

        {
            String componentId = "weka.classifiers.meta.Bagging";
            String componentName = "Bagging";
            String componentFullClassName = "weka.classifiers.meta.Bagging";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.meta.Bagging -P 100 -S 1 -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(50);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(100);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-num-slots", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(1);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.REPTree");
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.meta.ClassificationViaRegression";
            String componentName = "ClassificationViaRegression";
            String componentFullClassName = "weka.classifiers.meta.ClassificationViaRegression";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.meta.ClassificationViaRegression -W weka.classifiers.trees.M5P -- -M 4.0";
            String componentExecutionScriptFilteredClassifierWeka = "";
            
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.M5P");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.classifiers.meta.CVParameterSelection";
//            String componentName = "CVParameterSelection";
//            String componentFullClassName = "weka.classifiers.meta.CVParameterSelection";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.CVParameterSelection -X 10 -S 1 -W weka.classifiers.rules.ZeroR";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

        {
            String componentId = "weka.classifiers.meta.LogitBoost";
            String componentName = "LogitBoost";
            String componentFullClassName = "weka.classifiers.meta.LogitBoost";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.meta.LogitBoost -P 100 -F 0 -R 1 -L -1.7976931348623157E308 -H 1.0 -S 1 -I 10 -W weka.classifiers.trees.DecisionStump";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(500);
                hp.setDefaultIntValue(100);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-F", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(1);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(-1.7976931348623157E308);
                hp.setMaxNumericValue(-1.7976931348623157E308);
                hp.setDefaultNumericValue(-1.7976931348623157E308);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-H", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(1.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.M5P");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.classifiers.meta.MultiClassClassifier";
//            String componentName = "MultiClassClassifier";
//            String componentFullClassName = "weka.classifiers.meta.MultiClassClassifier";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.MultiClassClassifier -M 0 -R 2.0 -S 1 -W weka.classifiers.functions.Logistic -- -R 1.0E-8 -M -1";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

//        {
//            String componentId = "weka.classifiers.meta.MultiScheme";
//            String componentName = "MultiScheme";
//            String componentFullClassName = "weka.classifiers.meta.MultiScheme";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.MultiScheme -X 0 -S 1 -B \"weka.classifiers.rules.ZeroR \"";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

//        {
//            String componentId = "weka.classifiers.meta.RandomCommittee";
//            String componentName = "RandomCommittee";
//            String componentFullClassName = "weka.classifiers.meta.RandomCommittee";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.RandomCommittee -S 1 -num-slots 1 -I 10 -W weka.classifiers.trees.RandomTree -- -K 0 -M 1.0 -S 1";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

        {
            String componentId = "weka.classifiers.meta.RandomSubSpace";
            String componentName = "RandomSubSpace";
            String componentFullClassName = "weka.classifiers.meta.RandomSubSpace";
            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
            String componentExecutionScript = "weka.classifiers.meta.RandomSubSpace -P 0.5 -S 1 -num-slots 1 -I 10 -W weka.classifiers.trees.REPTree -- -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-P", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(0.99);
                hp.setDefaultNumericValue(0.5);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-num-slots", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(1);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.PREDICTORS);
                hp.setDefaultPredictor("weka.classifiers.trees.M5P");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.classifiers.meta.Stacking";
//            String componentName = "Stacking";
//            String componentFullClassName = "weka.classifiers.meta.Stacking";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.Stacking -X 10 -M \"weka.classifiers.rules.ZeroR \" -S 1 -num-slots 1 -B \"weka.classifiers.rules.ZeroR \"";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }
//
//        {
//            String componentId = "weka.classifiers.meta.Vote";
//            String componentName = "Vote";
//            String componentFullClassName = "weka.classifiers.meta.Vote";
//            MLComponentType mLComponentType = MLComponentType.META_PREDICTOR;
//            String componentExecutionScript = "weka.classifiers.meta.Vote -S 1 -B \"weka.classifiers.rules.ZeroR \" -R AVG";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//
//            List<MLComponentIO> listOfOutputs = new ArrayList<>();
//            listOfOutputs.add(getMLComponentIO(MLMetafeature.PREDICTIVE_MODEL));
//
//            MLComponent mLComponent = new MLComponent(
//                    componentId,
//                    componentName,
//                    componentFullClassName,
//                    mLComponentType,
//                    componentExecutionScript,
//                    componentExecutionScriptFilteredClassifierWeka,
//                    listOfInputs,
//                    listOfOutputs,
//                    listOfHyperparameters);
//            listOfMLComponentByType.add(mLComponent);
//        }

        ////////////////////////////////////////////////
        // RULES
        ///////////////////////////////////////////////
        {
            String componentId = "weka.classifiers.rules.DecisionTable";
            String componentName = "DecisionTable";
            String componentFullClassName = "weka.classifiers.rules.DecisionTable";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.rules.DecisionTable -X 1 -S \"weka.attributeSelection.BestFirst -D 1 -N 5\"";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            
            {
                MLHyperparameter hp = new MLHyperparameter("-X", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(1);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("\"weka.attributeSelection.BestFirst -D 1 -N 5\""
                        ,"\"weka.attributeSelection.GreedyStepwise -T -1.7976931348623157E308 -N -1\""
                        )));
                hp.setDefaultNominalValue("\"weka.attributeSelection.BestFirst -D 1 -N 5\"");
                listOfHyperparameters.add(hp);
            }
            
            
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.rules.JRip";
            String componentName = "JRip";
            String componentFullClassName = "weka.classifiers.rules.JRip";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.rules.JRip -F 3 -N 2.0 -O 2 -S 1";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-F", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(10);
                hp.setDefaultIntValue(2);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(2.0);
                hp.setMaxNumericValue(100.0);
                hp.setDefaultNumericValue(2.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-O", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(2);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.rules.OneR";
            String componentName = "OneR";
            String componentFullClassName = "weka.classifiers.rules.OneR";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.rules.OneR -B 6";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-B", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(6);
                listOfHyperparameters.add(hp);
            }
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.rules.PART";
            String componentName = "";
            String componentFullClassName = "weka.classifiers.rules.PART";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.rules.PART -M 2 -C 0.25 -Q 1";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(2);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(0.6);
                hp.setDefaultNumericValue(0.25);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-Q", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs,
                    listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.rules.ZeroR";
            String componentName = "ZeroR";
            String componentFullClassName = "weka.classifiers.rules.ZeroR";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.rules.ZeroR";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        ////////////////////////////////////////////////
        // TREES
        ///////////////////////////////////////////////        
        {
            String componentId = "weka.classifiers.trees.DecisionStump";
            String componentName = "DecisionStump";
            String componentFullClassName = "weka.classifiers.trees.DecisionStump";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.DecisionStump";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.trees.J48";
            String componentName = "J48";
            String componentFullClassName = "weka.classifiers.trees.J48";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.J48 -C 0.25 -M 2";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-C", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.01);
                hp.setMaxNumericValue(0.6);
                hp.setDefaultNumericValue(0.25);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(2);
                listOfHyperparameters.add(hp);
            }
            
            
            
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.trees.LMT";
            String componentName = "LMT";
            String componentFullClassName = "weka.classifiers.trees.LMT";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.LMT -I -1 -M 15 -W 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(15);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(0.0);
                hp.setDefaultNumericValue(0.0);
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.trees.RandomForest";
            String componentName = "RandomForest";
            String componentFullClassName = "weka.classifiers.trees.RandomForest";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.RandomForest -I 10 -K 0 -S 1 -num-slots 1";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(10);
                hp.setMaxIntValue(1000);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-num-slots", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(1);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.trees.RandomTree";
            String componentName = "RandomTree";
            String componentFullClassName = "weka.classifiers.trees.RandomTree";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.RandomTree -K 0 -M 1.0 -S 1";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-K", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(0);
                hp.setMaxIntValue(0);
                hp.setDefaultIntValue(0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(1.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.classifiers.trees.REPTree";
            String componentName = "REPTree";
            String componentFullClassName = "weka.classifiers.trees.REPTree";
            MLComponentType mLComponentType = MLComponentType.CLASSIFIER;
            String componentExecutionScript = "weka.classifiers.trees.REPTree -M 2 -V 0.001 -N 3 -S 1 -L -1 -I 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "";
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(2);
                hp.setMaxIntValue(2);
                hp.setDefaultIntValue(2);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-V", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.001);
                hp.setMaxNumericValue(0.001);
                hp.setDefaultNumericValue(0.001);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(3);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(3);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-L", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-I", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(0.0);
                hp.setDefaultNumericValue(0.0);
                listOfHyperparameters.add(hp);
            }
            
            

            List<MLComponentIO> listOfInputs = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
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
            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.EMImputation";
            String componentName = "EMImputation";
            String componentFullClassName = "weka.filters.unsupervised.attribute.EMImputation";
            MLComponentType mLComponentType = MLComponentType.MISSING_VALUE_HANDLER;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.EMImputation -N -1 -E 1.0E-4 -Q 1.0E-8";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.EMImputation -N -1 -E 1.0E-4 -Q 1.0E-8\\\" ";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-E", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0E-4);
                hp.setMaxNumericValue(1.0E-4);
                hp.setDefaultNumericValue(1.0E-4);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-Q", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0E-8);
                hp.setMaxNumericValue(1.0E-8);
                hp.setDefaultNumericValue(1.0E-8);
                listOfHyperparameters.add(hp);
            }
            
            
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
                    listOfOutputs, listOfHyperparameters);
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

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.95);
                hp.setMaxNumericValue(0.95);
                hp.setDefaultNumericValue(0.95);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-A", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(5);
                hp.setMaxIntValue(5);
                hp.setDefaultIntValue(5);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(50);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();
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
                    listOfOutputs, listOfHyperparameters);
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

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-Z", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(100);
                listOfHyperparameters.add(hp);
            }

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.instance.ReservoirSample";
            String componentName = "ReservoirSample";
            String componentFullClassName = "weka.filters.unsupervised.instance.ReservoirSample";
            MLComponentType mLComponentType = MLComponentType.DATA_SAMPLING;
            String componentExecutionScript = "weka.filters.unsupervised.instance.ReservoirSample -S 1 -Z 50";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.instance.ReservoirSample -S 1 -Z 50\\\" ";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(3);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-Z", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(100);
                hp.setMaxIntValue(1000);
                hp.setDefaultIntValue(100);
                listOfHyperparameters.add(hp);
            }
            
            
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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.instance.RemovePercentage";
            String componentName = "RemovePercentage";
            String componentFullClassName = "weka.filters.unsupervised.instance.RemovePercentage";
            MLComponentType mLComponentType = MLComponentType.DATA_SAMPLING;
            String componentExecutionScript = "weka.filters.unsupervised.instance.RemovePercentage -P 50.0";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(99.0);
                hp.setDefaultNumericValue(50.0);
                listOfHyperparameters.add(hp);
            }
            
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
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
            String componentExecutionScript = "weka.filters.unsupervised.instance.PeriodicSampling -N 1";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(1);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(1);
                listOfHyperparameters.add(hp);
            }

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
                    listOfOutputs, listOfHyperparameters);
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

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.Standardize";
            String componentName = "Standardize";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Standardize";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Standardize";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.Standardize \\\" ";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.Normalize";
            String componentName = "Normalize";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Normalize";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Normalize -S 1.0 -T 0.0";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.Normalize -S 1.0 -T 0.0\\\" ";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            
            {
                MLHyperparameter hp = new MLHyperparameter("-S", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0);
                hp.setMaxNumericValue(1.0);
                hp.setDefaultNumericValue(1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-T", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(0.0);
                hp.setMaxNumericValue(0.0);
                hp.setDefaultNumericValue(0.0);
                listOfHyperparameters.add(hp);
            }
            
            
            
            
            
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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.IndependentComponents";
            String componentName = "IndependentComponents";
            String componentFullClassName = "weka.filters.unsupervised.attribute.IndependentComponents";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.IndependentComponents -W -A -1 -N 200 -T 1.0E-4";
            String componentExecutionScriptFilteredClassifierWeka = "-F \\\"weka.filters.unsupervised.attribute.IndependentComponents -W -A -1 -N 200 -T 1.0E-4\\\" ";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-W", MLHyperparameterType.BOOLEAN);
                hp.setDefaultBoolValue(Boolean.TRUE);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-A", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(-1);
                hp.setMaxIntValue(-1);
                hp.setDefaultIntValue(-1);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-N", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(100);
                hp.setMaxIntValue(500);
                hp.setDefaultIntValue(200);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-T", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(1.0E-4);
                hp.setMaxNumericValue(1.0E-4);
                hp.setDefaultNumericValue(1.0E-4);
                listOfHyperparameters.add(hp);
            }

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
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.Discretize";
            String componentName = "Discretize";
            String componentFullClassName = "weka.filters.unsupervised.attribute.Discretize";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.Discretize -B 10 -M -1.0 -R first-last";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            {
                MLHyperparameter hp = new MLHyperparameter("-B", MLHyperparameterType.INTEGER);
                hp.setMinIntValue(5);
                hp.setMaxIntValue(100);
                hp.setDefaultIntValue(10);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-M", MLHyperparameterType.NUMERIC);
                hp.setMinNumericValue(-1.0);
                hp.setMaxNumericValue(-1.0);
                hp.setDefaultNumericValue(-1.0);
                listOfHyperparameters.add(hp);
            }
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("first-last")));
                hp.setDefaultNominalValue("first-last");
                listOfHyperparameters.add(hp);
            }
            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

//        {
//            String componentId = "weka.filters.unsupervised.attribute.NominalToBinary";
//            String componentName = "NominalToBinary";
//            String componentFullClassName = "weka.filters.unsupervised.attribute.NominalToBinary";
//            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
//            String componentExecutionScript = "weka.filters.unsupervised.attribute.NominalToBinary -R first-last";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//          
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
//        {
//            String componentId = "weka.filters.unsupervised.attribute.NominalToString";
//            String componentName = "NominalToString";
//            String componentFullClassName = "weka.filters.unsupervised.attribute.NominalToString";
//            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
//            String componentExecutionScript = "weka.filters.unsupervised.attribute.NominalToString -C last";
//            String componentExecutionScriptFilteredClassifierWeka = "";
//
//            List<MLComponentIO> listOfInputs = new ArrayList<>();
//          
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
        {
            String componentId = "weka.filters.unsupervised.attribute.NumericToBinary";
            String componentName = "NumericToBinary";
            String componentFullClassName = "weka.filters.unsupervised.attribute.NumericToBinary";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.NumericToBinary";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();

            
            List<MLComponentIO> listOfInputs = new ArrayList<>();

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.NumericToNominal";
            String componentName = "NumericToNominal";
            String componentFullClassName = "weka.filters.unsupervised.attribute.NumericToNominal";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.NumericToNominal -R first-last";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("first-last")));
                hp.setDefaultNominalValue("first-last");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        {
            String componentId = "weka.filters.unsupervised.attribute.StringToNominal";
            String componentName = "";
            String componentFullClassName = "weka.filters.unsupervised.attribute.StringToNominal";
            MLComponentType mLComponentType = MLComponentType.DATA_TRANSFORMATION;
            String componentExecutionScript = "weka.filters.unsupervised.attribute.StringToNominal -R last";
            String componentExecutionScriptFilteredClassifierWeka = "";

            List<MLHyperparameter> listOfHyperparameters = new ArrayList<>();
            
            {
                MLHyperparameter hp = new MLHyperparameter("-R", MLHyperparameterType.NOMINAL);
                hp.setListOfNomnialValues(new ArrayList<>( Arrays.asList("first-last")));
                hp.setDefaultNominalValue("first-last");
                listOfHyperparameters.add(hp);
            }

            List<MLComponentIO> listOfInputs = new ArrayList<>();

            List<MLComponentIO> listOfOutputs = new ArrayList<>();

            MLComponent mLComponent = new MLComponent(
                    componentId,
                    componentName,
                    componentFullClassName,
                    mLComponentType,
                    componentExecutionScript,
                    componentExecutionScriptFilteredClassifierWeka,
                    listOfInputs,
                    listOfOutputs, listOfHyperparameters);
            listOfMLComponentByType.add(mLComponent);
        }

        return listOfMLComponentByType;

    }

//    private static MLComponent getMLComponentById(String componentId) {
//
//        System.out.println("listOfMLComponents: " + listOfMLComponents.size());
//        
//        for (MLComponent mLComponent : listOfMLComponents) {
//            if (mLComponent.getComponentId().equals(componentId)) {
//                return mLComponent;
//            }
//        }
//
//        return null;
//    }

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
        System.out.println("listOfMLComponents: " + listOfMLComponents.size());
        
        
        for (MLComponent mLComponent : listOfMLComponents) {
            if (mLComponent.getComponentId().equals(componentId)) {
                return mLComponent;
            }
        }
        return null;
    }
    
    public static MLComponent getComponentByID(String componentId, List<MLComponent> loadedListOfMLComponents) {
      
        for (MLComponent mLComponent : loadedListOfMLComponents) {
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

    public static boolean isNotAutoFinaliseAlgorithm(String algorithmId) {

        return listOfNotAutoFinaliseAlgorithm.contains(algorithmId);
    }
    
     public static AlgorithmConfiguration getAlgorithmConfiguration(String algorithm, List<MLComponent> loadedListOfMLComponents) {

        System.out.println("Finding .. " + algorithm);
       
        MLComponent mLComponent = getComponentByID(algorithm,loadedListOfMLComponents);

        if (mLComponent != null) {
            
         
            String id = mLComponent.getComponentId();
            String name = mLComponent.getComponentName();
            String path = mLComponent.getComponentId();

            List<Parameter> capabilityList = new ArrayList<>();

            for (MLComponentIO mLComponentIO : mLComponent.getListOfCapabilities()) {
                Parameter capability = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
                capabilityList.add(capability);

            }

            List<Parameter> effectList = new ArrayList<>();
            for (MLComponentIO mLComponentIO : mLComponent.getListOfEffects()) {
                Parameter effect = new Parameter(mLComponentIO.getmLComponentCapability().name(), mLComponentIO.getValue());
                effectList.add(effect);

            }

            AlgorithmConfiguration algorithmConfiguration = new AlgorithmConfiguration(id, name, path, capabilityList, effectList);

            return algorithmConfiguration;

        } else {
            
            
            
            
            
            return null;
        }
    }
     
     
     public static List<MLComponent> getListOfBaseLearners() {
         List<MLComponent> listOfBaseLearners = new ArrayList<>();
         
         for (MLComponent component : listOfMLComponents) {
             if (component.getmLComponentType().equals(MLComponentType.BASE_LEARNER)) {
                 listOfBaseLearners.add(component);
             }
         }
         
         
         return listOfBaseLearners;
     }
     
     public static List<MLComponent> getListOfClassifiers() {
         List<MLComponent> listOfBaseLearners = new ArrayList<>();
         
         for (MLComponent component : listOfMLComponents) {
             if (component.getmLComponentType().equals(MLComponentType.CLASSIFIER)
                     || component.getmLComponentType().equals(MLComponentType.CLASSIFIER_REGRESSOR)
                     || component.getmLComponentType().equals(MLComponentType.REGRESSOR)
                     || component.getmLComponentType().equals(MLComponentType.META_PREDICTOR)) {
                 listOfBaseLearners.add(component);
             }
         }
         
         
         return listOfBaseLearners;
     }
     
     public static List<MLComponent> getListOfPredictors() {
         List<MLComponent> listOfBaseLearners = new ArrayList<>();
         
         for (MLComponent component : listOfMLComponents) {
             if (component.getmLComponentType().equals(MLComponentType.CLASSIFIER)
                     || component.getmLComponentType().equals(MLComponentType.REGRESSOR)) {
                 listOfBaseLearners.add(component);
             }
         }
         
         
         return listOfBaseLearners;
     }


}

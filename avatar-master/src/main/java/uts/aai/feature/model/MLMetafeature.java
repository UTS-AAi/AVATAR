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
package uts.aai.feature.model;

/**
 *
 * @author ntdun
 */
public enum MLMetafeature {
    
    BINARY_ATTRIBUTES, BINARY_CLASS, DATE_ATTRIBUTES, DATE_CLASS, 
    EMPTY_NOMINAL_ATTRIBUTES, EMPTY_NOMINAL_CLASS, MISSING_CLASS_VALUES, 
    MISSING_VALUES, NO_CLASS, NOMINAL_ATTRIBUTES, NOMINAL_CLASS, NUMERIC_ATTRIBUTES, 
    NUMERIC_CLASS, ONLY_MULTIINSTANCE, RELATIONAL_ATTRIBUTES, RELATIONAL_CLASS,
    STRING_ATTRIBUTES, STRING_CLASS, UNARY_ATTRIBUTES, UNARY_CLASS, OUTLIER_ATTRIBUTES
    , PREDICTIVE_MODEL, IMBALANCE_CLASS
    
    
    
    
}

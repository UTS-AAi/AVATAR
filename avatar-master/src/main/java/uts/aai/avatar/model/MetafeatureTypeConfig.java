/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.aai.avatar.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ntdun
 */
public class MetafeatureTypeConfig {

    private static final Map<MLMetafeature, MetafeatureType> METAFEATURE_CONFIG_LIST = new HashMap<MLMetafeature, MetafeatureType>() {
        {
            put(MLMetafeature.BINARY_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.BINARY_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.DATE_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.DATE_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.EMPTY_NOMINAL_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.EMPTY_NOMINAL_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
  
            put(MLMetafeature.NOMINAL_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.NOMINAL_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.NO_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.NUMERIC_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.NUMERIC_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.ONLY_MULTIINSTANCE, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.PREDICTIVE_MODEL, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.RELATIONAL_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.RELATIONAL_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.STRING_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.STRING_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
            put(MLMetafeature.UNARY_ATTRIBUTES, MetafeatureType.ATTRIBUTE_TRANSFORMATION);
            put(MLMetafeature.UNARY_CLASS, MetafeatureType.CLASS_TRANSFORMATION);
           
            
            put(MLMetafeature.IMBALANCE_CLASS, MetafeatureType.QUALITY);
            put(MLMetafeature.MISSING_CLASS_VALUES, MetafeatureType.QUALITY);
            put(MLMetafeature.MISSING_VALUES, MetafeatureType.QUALITY);
            put(MLMetafeature.OUTLIER_ATTRIBUTES, MetafeatureType.QUALITY);

        }
    };

    public static MetafeatureType getMetafeatureType(MLMetafeature metafeature) {

        return METAFEATURE_CONFIG_LIST.get(metafeature);

    }

}

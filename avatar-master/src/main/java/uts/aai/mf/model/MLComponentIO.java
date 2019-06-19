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
package uts.aai.mf.model;

/**
 *
 * @author ntdun
 */
public class MLComponentIO {
    
    private MLMetafeature mLComponentCapability;
    private Integer value;

    public MLComponentIO() {
    }

    public MLComponentIO(MLMetafeature mLComponentCapability, Integer value) {
        this.mLComponentCapability = mLComponentCapability;
        this.value = value;
    }

    public MLMetafeature getmLComponentCapability() {
        return mLComponentCapability;
    }

    public void setmLComponentCapability(MLMetafeature mLComponentCapability) {
        this.mLComponentCapability = mLComponentCapability;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
    
    
    
}
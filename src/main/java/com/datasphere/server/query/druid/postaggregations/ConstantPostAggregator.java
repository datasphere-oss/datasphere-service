/*
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

package com.datasphere.server.query.druid.postaggregations;

import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.constraints.NotNull;

import com.datasphere.server.query.druid.PostAggregation;

/**
 * Created by aladin on 2016. 3. 24..
 */

@JsonTypeName("constant")
public class ConstantPostAggregator implements PostAggregation {

    @NotNull
    String name;

    @NotNull
    double value;


    public ConstantPostAggregator(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

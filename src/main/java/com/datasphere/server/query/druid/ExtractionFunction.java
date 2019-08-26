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

package com.datasphere.server.query.druid;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.datasphere.server.query.druid.extractionfns.ExpressionFunction;
import com.datasphere.server.query.druid.extractionfns.JavaScriptFunction;
import com.datasphere.server.query.druid.extractionfns.LookupFunction;
import com.datasphere.server.query.druid.extractionfns.PartialFunction;
import com.datasphere.server.query.druid.extractionfns.RegExFunction;
import com.datasphere.server.query.druid.extractionfns.SearchQueryFunction;
import com.datasphere.server.query.druid.extractionfns.TimeFormatFunction;
import com.datasphere.server.query.druid.extractionfns.TimeParsingFunction;

/**
 * Created by i1befree on 2016. 1. 12..
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include= JsonTypeInfo.As.EXTERNAL_PROPERTY, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = JavaScriptFunction.class, name = "javascript"),
    @JsonSubTypes.Type(value = LookupFunction.class, name = "lookup"),
    @JsonSubTypes.Type(value = PartialFunction.class, name = "partial"),
    @JsonSubTypes.Type(value = RegExFunction.class, name = "regex"),
    @JsonSubTypes.Type(value = SearchQueryFunction.class, name = "searchquery"),
    @JsonSubTypes.Type(value = TimeFormatFunction.class, name = "timeFormat"),
    @JsonSubTypes.Type(value = TimeParsingFunction.class, name = "time"),
    @JsonSubTypes.Type(value = ExpressionFunction.class, name = "expression")
})

public interface ExtractionFunction {
}

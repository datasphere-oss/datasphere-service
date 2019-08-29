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

package com.datasphere.server.spec.druid.ingestion;

import javax.validation.constraints.NotNull;

import com.datasphere.server.spec.druid.ingestion.io.IoConfig;
import com.datasphere.server.spec.druid.ingestion.tuning.TuningConfig;

/**
 * Created by aladin on 2019. 6. 17..
 */
public class IngestionSpec {

  @NotNull
  DataSchema dataSchema;

  @NotNull
  IoConfig ioConfig;

  TuningConfig tuningConfig;

  public IngestionSpec() {
  }

  public IngestionSpec(DataSchema dataSchema, IoConfig ioConfig) {
    this.dataSchema = dataSchema;
    this.ioConfig = ioConfig;
  }

  public IngestionSpec(DataSchema dataSchema, IoConfig ioConfig, TuningConfig tuningConfig) {
    this.dataSchema = dataSchema;
    this.ioConfig = ioConfig;
    this.tuningConfig = tuningConfig;
  }

  public DataSchema getDataSchema() {
    return dataSchema;
  }

  public void setDataSchema(DataSchema dataSchema) {
    this.dataSchema = dataSchema;
  }

  public IoConfig getIoConfig() {
    return ioConfig;
  }

  public void setIoConfig(IoConfig ioConfig) {
    this.ioConfig = ioConfig;
  }

  public TuningConfig getTuningConfig() {
    return tuningConfig;
  }

  public void setTuningConfig(TuningConfig tuningConfig) {
    this.tuningConfig = tuningConfig;
  }

}

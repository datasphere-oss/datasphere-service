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

package com.datasphere.server.datasource;

import static com.datasphere.server.datasource.DataSourceErrorCodes.INGESTION_COMMON_ERROR;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.datasphere.engine.common.exception.ErrorCodes;
import com.datasphere.engine.common.exception.MetatronException;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="DataSource Ingestion Error")
public class DataSourceIngestionException extends Exception{

	ErrorCodes codes;
  public DataSourceIngestionException(String message) {
    this(INGESTION_COMMON_ERROR, message);
  }

  public DataSourceIngestionException(String message, Throwable cause) {
	  this(INGESTION_COMMON_ERROR, message, cause);
  }

  public DataSourceIngestionException(ErrorCodes codes, Throwable cause) {
	  this.codes = codes;
  }

  public DataSourceIngestionException(ErrorCodes codes, String message) {
	  this.codes = codes;
  }

  public DataSourceIngestionException(ErrorCodes codes, String message, Throwable cause) {
	  this.codes = codes;
  }
  
  public ErrorCodes getCode() {
	return this.codes;
  }
  
}

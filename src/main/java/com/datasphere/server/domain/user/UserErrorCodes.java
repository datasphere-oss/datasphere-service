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

package app.metatron.discovery.domain.user;

import app.metatron.discovery.common.exception.ErrorCodes;

public enum UserErrorCodes implements ErrorCodes {

  USER_COMMON_ERROR_CODE("UR0001"),
  DUPLICATED_USERNAME_CODE("UR0002"),
  DUPLICATED_EMAIL_CODE("UR0003");

  String errorCode;

  UserErrorCodes(String errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public String getCode() {
    return errorCode;
  }
}

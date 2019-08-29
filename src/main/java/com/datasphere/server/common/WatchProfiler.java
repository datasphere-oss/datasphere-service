/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.server.common;

import org.springframework.util.StopWatch;

/**
 * Created by aladin on 2019. 10. 4..
 */
public class WatchProfiler {

  private static final ThreadLocal<StopWatch> watchThreadLocal =
          new ThreadLocal<StopWatch>() {
            protected StopWatch initialValue() {
              return new StopWatch();
            }
          };

  public static void start(String phase) {
    if(!watchThreadLocal.get().isRunning()) {
      watchThreadLocal.get().start(phase);
    }
  }

  public static void stop() {
    if(watchThreadLocal.get().isRunning()) {
      watchThreadLocal.get().stop();
    }
  }

  public static long totalElapsed() {
    return watchThreadLocal.get().getTotalTimeMillis();
  }

  public static long lastElapsed() {
    return watchThreadLocal.get().getLastTaskTimeMillis();
  }

  public static String report() {
    return watchThreadLocal.get().toString();
  }
}

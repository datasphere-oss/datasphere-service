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

package com.datasphere.server.common.cache;

import org.infinispan.notifications.Listener;
import org.infinispan.notifications.cachemanagerlistener.annotation.ViewChanged;
import org.infinispan.notifications.cachemanagerlistener.event.ViewChangedEvent;

import java.util.concurrent.CountDownLatch;

/**
 * Created by aladin on 2019. 3. 13..
 * TODO: should be switch to hazelcast
 */
@Listener
public class InfinispanClusterListener {

  CountDownLatch clusterFormedLatch = new CountDownLatch(1);
  CountDownLatch shutdownLatch = new CountDownLatch(1);
  private final int expectedNodes;

  public InfinispanClusterListener(int expectedNodes) {
    this.expectedNodes = expectedNodes;
  }

  @ViewChanged
  public void viewChanged(ViewChangedEvent event) {
    System.out.printf("---- View changed: %s ----\n", event.getNewMembers());

    if (event.getCacheManager().getMembers().size() == expectedNodes) {
      clusterFormedLatch.countDown();
    } else if (event.getNewMembers().size() < event.getOldMembers().size()) {
      shutdownLatch.countDown();
    }
  }
}

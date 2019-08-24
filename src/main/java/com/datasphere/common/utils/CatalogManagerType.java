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

package com.datasphere.common.utils;

/**
 * status
 *
 */
public enum CatalogManagerType {
	
	ALL("所有", -1),
    NOSUBMIT("未提交/已编制", 0),
    SUBMIT("提交/未审核", 1),
    AGREE("同意/未发布", 2),
	REFUSE("拒绝/被驳回", 3),
    PUBLISH("发布/可订阅", 4),
	AGREEPUBLISH("审核并发布", 4),
	//订阅信息
	SUBSCRIBE("可订阅",5),
    AGREESUBSCRIBE("已订阅",6),
	SUBSCRIBREFUSE("订阅驳回",7),
    //未编目
    NO_BIANMU("未编目",8),
    YES_BIANMU("已编目/未编制",9),

	//资源来源
    ESTABLISHMENT("编制",1),
    SORTING("梳理",2);

    private String name ;
    private int index ; CatalogManagerType( String name , int index ){
        this.name = name ;
        this.index = index ;
    }
     
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }

}

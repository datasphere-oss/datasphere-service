package com.datasphere.engine.manager.resource.provider.model;

import com.google.common.base.MoreObjects;

/**
 * 测试返回结果
 */
public class ResultInfo {
    private boolean data;

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .toString();
    }
}

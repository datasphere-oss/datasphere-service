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

package com.datasphere.engine.shaker.processor.prep;

import java.io.Serializable;

import com.datasphere.engine.shaker.processor.prep.constant.ReturnConst;

public class ReturnData<T> implements Serializable {
    private static final long serialVersionUID = 7429938332852688523L;
    private static Integer Error = ReturnConst.Error;
    private static Integer Success = ReturnConst.Success;
    /**
     * @Fields data : 返回数据主体内容
     */
    private T data;
    /**
     * @Fields code : 返回结果代码, 0 成功,1 失败
     */
    private Integer code;
    /**
     * @Fields message : 返回结果信息
     */
    private String message;

    /* Constructor */
    public ReturnData() {
        super();
        this.setCode(Success);
        this.setMessage(ReturnConst.get(Success));
    }

    public ReturnData(Integer code) {
        super();
        this.setCode(code);
        this.setMessage(ReturnConst.get(code));
    }

    public ReturnData(Integer code, String message) {
        super();
        this.setCode(code);
        this.setMessage(message);
    }

    public T getData() {
        return data;
    }

    public ReturnData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public ReturnData<T> setCode(Integer code) {
        this.code = code;
        this.message = ReturnConst.get(code);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ReturnData<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "ReturnData{" +
                "data=" + data +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
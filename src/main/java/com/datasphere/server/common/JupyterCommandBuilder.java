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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aladin on 2019. 11. 1..
 */
public class JupyterCommandBuilder extends AbstractCommandBuilder {

    static final String JUPYTER = "jupyter";
    static final String TO = "--to";
    static final String OUTPUT = "--output";

    String nbFormat;
    String source;
    String target;
    String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getNbFormat() {
        return nbFormat;
    }

    public void setNbFormat(String nbFormat) {
        this.nbFormat = nbFormat;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public JupyterCommandBuilder() {
    }

    /**
     * e.g. jupyter action(nbconvert) --to script notebookPath.ipynb --output notebookPath(w/fileName)
     */
    private void buildCommand() {
        List<String> appArgs = new ArrayList<>();
        appArgs.add(JUPYTER);
        appArgs.add(this.action);
        appArgs.add(TO);
        appArgs.add(this.nbFormat);
        appArgs.add(this.source);
        appArgs.add(OUTPUT);
        appArgs.add(this.target);
        super.commandArgs = appArgs;
    }

    /**
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void run() throws Exception {
        buildCommand();
        super.run();
    }

}

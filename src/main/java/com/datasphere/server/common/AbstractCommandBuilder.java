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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by aladin on 2019. 7. 20..
 */
public abstract class AbstractCommandBuilder implements CommandAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommandBuilder.class);

    protected List<String> commandArgs;

    public AbstractCommandBuilder() {
    }

    /**
     * execute command line
     *
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        final ProcessBuilder builder = new ProcessBuilder(this.commandArgs);
        final Process process = builder.start();
        process.getErrorStream().close();
        process.getInputStream().close();
        process.getOutputStream().close();
        process.waitFor();
    }

    /**
     * return stdout after executing command line
     *
     * @return
     * @throws Exception
     */
    @Override
    public String runRedirectOutput() throws Exception {
        String printOut = "";
        final File tmp = File.createTempFile("out", null);
        try {
            tmp.deleteOnExit();
            final ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(this.commandArgs).redirectErrorStream(true)
                    .redirectOutput(tmp);
            final Process process = processBuilder.start();
            process.waitFor();

            final StringBuilder out = new StringBuilder();
            try (final InputStream is = new FileInputStream(tmp)) {
                int c;
                while ((c = is.read()) != -1) {
                    out.append((char) c);
                }
            }
            printOut = out.toString();
            if(printOut.isEmpty()) {
                LOGGER.debug("redirectOutput is empty.");
            }
        } finally {
            tmp.delete();
            return printOut;
        }
    }
}

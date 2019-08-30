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

package com.datasphere.server.domain.notebook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import com.datasphere.server.domain.notebook.connector.HttpRepository;

/**
 * Created by aladin on 2019. 8. 4..
 */
@RepositoryRestController
public class NotebookConnectorController {

    private static Logger LOGGER = LoggerFactory.getLogger(NotebookConnectorController.class);

    @Autowired
    NotebookConnectorRepository notebookConnectorRepository;

    @Autowired
    HttpRepository httpRepository;

    /**
     * Kill all of notebook kernels
     */
    @RequestMapping(path = "/connectors/kernels", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> killNotebookKernels() {
        Page<NotebookConnector> connectors = notebookConnectorRepository.findByType("jupyter", new PageRequest(0, Integer.MAX_VALUE));
        for(NotebookConnector connector : connectors) {
            connector.setHttpRepository(httpRepository);
            connector.killAllKernels();
        }
        LOGGER.info("Completed kill every alive notebook [jupyter] kernels.");
        return ResponseEntity.noContent().build();
    }

    /**
     * 워크스페이스 > 목록 > 노트북 일괄 삭제
     *
     * @param ids
     * @return
     */
    @RequestMapping(path = "/connectors/{ids}", method = RequestMethod.DELETE)
    public @ResponseBody
    ResponseEntity<?> deleteConnectors(@PathVariable("ids") List<String> ids) {
        for(String id : ids) {
            NotebookConnector connector = notebookConnectorRepository.findById(id).get();
            if(connector == null) {
                return ResponseEntity.notFound().build();
            }
            for(Notebook notebook : connector.getNotebooks()) {
                connector.deleteNotebook(notebook.getaLink());
            }
            notebookConnectorRepository.delete(connector);
        }
        return ResponseEntity.noContent().build();
    }

}

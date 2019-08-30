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

package com.datasphere.server.domain.dataprep.service;

import com.datasphere.server.domain.dataprep.PrepParamDatasetIdList;
import com.datasphere.server.domain.dataprep.PrepSwapRequest;
import com.datasphere.server.domain.dataprep.PrepUpstream;
import com.datasphere.server.domain.dataprep.entity.PrDataflow;
import com.datasphere.server.domain.dataprep.entity.PrDataflowProjections;
import com.datasphere.server.domain.dataprep.entity.PrDataset;
import com.datasphere.server.domain.dataprep.entity.PrTransformRule;
import com.datasphere.server.domain.dataprep.exceptions.PrepErrorCodes;
import com.datasphere.server.domain.dataprep.exceptions.PrepException;
import com.datasphere.server.domain.dataprep.exceptions.PrepMessageKey;
import com.datasphere.server.domain.dataprep.repository.PrDataflowRepository;
import com.datasphere.server.domain.dataprep.repository.PrDatasetRepository;
import com.datasphere.server.domain.dataprep.repository.PrTransformRuleRepository;
import com.datasphere.server.domain.dataprep.transform.PrepTransformResponse;
import com.datasphere.server.domain.dataprep.transform.PrepTransformService;
import com.google.common.collect.Lists;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value = "/preparationdataflows")
@RepositoryRestController
public class PrDataflowController {

    private static Logger LOGGER = LoggerFactory.getLogger(PrDataflowController.class);

    @Autowired
    ProjectionFactory projectionFactory;

    @Autowired
    private PrDataflowRepository dataflowRepository;

    @Autowired
    private PrDatasetRepository datasetRepository;

    @Autowired
    private PrTransformRuleRepository transformRuleRepository;

    @Autowired
    private PrDataflowService dataflowService;

    @Autowired
    private PrepTransformService transformService;

    @RequestMapping(value = "/{dfId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getDataflow(
            @PathVariable("dfId") String dfId,
            PersistentEntityResourceAssembler persistentEntityResourceAssembler
    ) throws PrepException {
        PrDataflow dataflow = null;
        Resource<PrDataflowProjections.DefaultProjection> projectedDataflow = null;
        try {
            dataflow = this.dataflowRepository.findById(dfId).get();
            if(dataflow!=null) {
            } else {
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, dfId);
            }

            PrDataflowProjections.DefaultProjection projection = projectionFactory.createProjection(PrDataflowProjections.DefaultProjection.class, dataflow);
            projectedDataflow = new Resource<>(projection);
        } catch (Exception e) {
            LOGGER.error("getDataflow(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(projectedDataflow);
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public @ResponseBody
    PersistentEntityResource postDataflow(
            @RequestBody Resource<PrDataflow> dataflowResource,
            PersistentEntityResourceAssembler resourceAssembler
    ) throws PrepException {
        PrDataflow dataflow = null;
        PrDataflow savedDataflow = null;

        try {
            dataflow = dataflowResource.getContent();
            savedDataflow = dataflowRepository.save(dataflow);
            LOGGER.debug(savedDataflow.toString());

            this.dataflowService.afterCreate(savedDataflow);

            this.dataflowRepository.flush();
        } catch (Exception e) {
            LOGGER.error("postDataflow(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, e.getMessage());
        }

        return resourceAssembler.toResource(savedDataflow);
    }

    @RequestMapping(value = "/{dfId}", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity<?> patchDataflow(
            @PathVariable("dfId") String dfId,
            @RequestBody Resource<PrDataflow> dataflowResource,
            PersistentEntityResourceAssembler persistentEntityResourceAssembler
    ) throws PrepException {

        PrDataflow dataflow = null;
        PrDataflow patchDataflow = null;
        PrDataflow savedDataflow = null;
        Resource<PrDataflowProjections.DefaultProjection> projectedDataflow = null;

        try {
            dataflow = this.dataflowRepository.findById(dfId).get();
            patchDataflow = dataflowResource.getContent();

            this.dataflowService.patchAllowedOnly(dataflow, patchDataflow);

            savedDataflow = dataflowRepository.save(dataflow);
            LOGGER.debug(savedDataflow.toString());

            this.dataflowRepository.flush();
        } catch (Exception e) {
            LOGGER.error("postDataflow(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        PrDataflowProjections.DefaultProjection projection = projectionFactory.createProjection(PrDataflowProjections.DefaultProjection.class, savedDataflow);
        projectedDataflow = new Resource<>(projection);
        return ResponseEntity.status(HttpStatus.SC_OK).body(projectedDataflow);
    }

    @RequestMapping(value = "/{dfId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteDataflow(
            @PathVariable("dfId") String dfId
    ) throws PrepException {
        try {
            PrDataflow dataflow = this.dataflowRepository.findById(dfId).get();
            if (null != dataflow) {
                ArrayList<PrDataset> datasets = Lists.newArrayList();
                datasets.addAll(dataflow.getDatasets());
                for(PrDataset ds : datasets) {
                    ds.deleteDataflow(dataflow);
                    dataflow.deleteDataset(ds);
                    if( ds.getDsType() == PrDataset.DS_TYPE.WRANGLED) {
                        List<PrTransformRule> transformRules = ds.getTransformRules();
                        if(null!=transformRules) {
                            for(PrTransformRule transformRule : transformRules) {
                                this.transformRuleRepository.delete(transformRule);
                            }
                        }
                        this.datasetRepository.deleteById(ds.getDsId());
                    }
                }
                this.datasetRepository.flush();

                this.dataflowRepository.deleteById(dataflow.getDfId());
                this.dataflowRepository.flush();
            }
        } catch (Exception e) {
            LOGGER.error("deleteDataflow(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dfId);
    }

    @RequestMapping(value = "/delete_chain/{dfId}/{dsId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteChain(
            @PathVariable("dfId") String dfId,
            @PathVariable("dsId") String dsId
    ) throws PrepException {

        List<String> deleteDsIds = Lists.newArrayList();
        try {
            List<String> upstreamDsIds = Lists.newArrayList();
            List<PrepUpstream> upstreams = Lists.newArrayList();
            upstreamDsIds.add(dsId);

            PrDataflow dataflow = this.dataflowRepository.findById(dfId).get();
            if (null != dataflow) {
                List<PrDataset> datasets = dataflow.getDatasets();
                if (null != datasets) {
                    for (PrDataset ds : datasets) {
                        String dId = ds.getDsId();
                        List<String> uIds = this.transformService.getUpstreamDsIds(ds.getDsId());
                        for(String uDsId : uIds) {
                            PrepUpstream upstream = new PrepUpstream();
                            upstream.setDfId(dataflow.getDfId());
                            upstream.setDsId(dId);
                            upstream.setUpstreamDsId(uDsId);
                            upstreams.add(upstream);
                        }
                    }
                }
            }
            while(0<upstreamDsIds.size()) {
                List<String> downDsIds = Lists.newArrayList();
                for(PrepUpstream upstream : upstreams) {
                    String uDsId = upstream.getUpstreamDsId();
                    if(true==upstreamDsIds.contains(uDsId)) {
                        downDsIds.add( upstream.getDsId() );
                    }
                }
                for(String uDsId : upstreamDsIds) {
                    if(false==deleteDsIds.contains(uDsId)) {
                        deleteDsIds.add(uDsId);
                    }
                }
                upstreamDsIds.clear();
                upstreamDsIds.addAll(downDsIds);
            }

            for(String deleteDsId : deleteDsIds) {
                PrDataset delDs = this.datasetRepository.findById(deleteDsId).get();
                if(delDs!=null) {
                    if(null!=dataflow) {
                        dataflow.deleteDataset(delDs);
                    }
                    if(delDs.getDsType() == PrDataset.DS_TYPE.WRANGLED) {
                        this.datasetRepository.delete(delDs);
                    } else {
                        delDs.deleteDataflow(dataflow);
                        this.datasetRepository.flush();
                        this.dataflowRepository.flush();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("deleteChain(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATASET_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(deleteDsIds);
    }

    @RequestMapping(value = "/{dfId}/upstreammap", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ResponseEntity<?> getStreams (
            @PathVariable("dfId") String dfId,
            @RequestParam(value = "forUpdate", required = false, defaultValue = "false") String forUpdate
    ) throws PrepException {
        List<PrepUpstream> upstreams = Lists.newArrayList();
        try {
            PrDataflow dataflow = dataflowRepository.findById(dfId).get();
            if (null != dataflow) {
                List<PrDataset> datasets = dataflow.getDatasets();
                if (null != datasets) {
                    for (PrDataset dataset : datasets) {
                        String dsId = dataset.getDsId();

                        if(dataset.getDsType()== PrDataset.DS_TYPE.WRANGLED) {
                            boolean forUpdateBoolean = forUpdate.equalsIgnoreCase("true") ? true : false;
                            List<String> upstreamDsIds = this.transformService.getUpstreamDsIds(dataset.getDsId());
                            if(null!=upstreamDsIds) {
                                for(String upstreamDsId : upstreamDsIds) {
                                    PrepUpstream upstream = new PrepUpstream();
                                    upstream.setDfId(dfId);
                                    upstream.setDsId(dsId);
                                    upstream.setUpstreamDsId(upstreamDsId);
                                    upstreams.add(upstream);
                                }
                            }
                        }
                    }
                }
            } else {
                String errorMsg = "No dataflow ["+dfId+"]";
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, errorMsg);
            }
        } catch (Exception e) {
            LOGGER.error("getStreams(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_CREATED).body(upstreams);
    }

    @RequestMapping(value = "/{dfId}/add/{dsId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<?> addDataset (
            @PathVariable("dfId") String dfId,
            @PathVariable("dsId") String dsId
    ) throws PrepException {
        PrDataflow dataflow = dataflowRepository.findById(dfId).get();
        try {
            if( dataflow!=null ) {
                PrDataset dataset = datasetRepository.findById(dsId).get();
                if( dataset!=null ) {
                    dataflow.addDataset(dataset);
                    dataset.addDataflow(dataflow);
                    dataflowRepository.saveAndFlush(dataflow);
                } else {
                    String errorMsg = new String("dataset[" + dsId + "] was not found");
                    throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATASET, errorMsg);
                }
            } else {
                String errorMsg = new String("dataflow[" + dfId + "] was not found");
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, errorMsg);
            }
        }
        catch (Exception e) {
            LOGGER.error("addDataset(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }

    @RequestMapping(value = "/{dfId}/update_datasets", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody ResponseEntity<?> updateDatasets (
            @PathVariable("dfId") String dfId,
            @RequestBody PrepParamDatasetIdList dsIds
    ) throws PrepException {
        // If an I.DS is new to the dataflow, we create a corresponding W.DS, except the case of dataset swapping.
        boolean autoCreate = (dsIds.getForSwap() != null && dsIds.getForSwap() == true) ? false : true;

        PrDataflow dataflow = dataflowRepository.findById(dfId).get();
        try {
            if( dataflow!=null ) {
                if(dsIds!=null) {
                    List<PrDataset> removeList = new ArrayList<PrDataset>();
                    List<PrDataset> datasets = dataflow.getDatasets();
                    List<String> oldIds = Lists.newArrayList();
                    List<String> newIds = Lists.newArrayList();
                    if(datasets!=null) {
                        for (PrDataset dataset : datasets) {
                            oldIds.add(dataset.getDsId());
                            if ( false == dsIds.getDsIds().contains(dataset.getDsId()) ) {
                                removeList.add(dataset);
                            }
                        }
                        for(PrDataset removeDataset : removeList) {
                            dataflow.deleteDataset(removeDataset);
                            removeDataset.deleteDataflow(dataflow);
                        }
                    }
                    for (String dsId : dsIds.getDsIds() ) {
                        PrDataset dataset = datasetRepository.findById(dsId).get();
                        if( dataset!=null ) {
                            if( PrDataset.DS_TYPE.IMPORTED ==dataset.getDsType() && false==oldIds.contains(dsId) ) {
                                newIds.add(dsId);
                            }
                            dataflow.addDataset(dataset);
                            dataset.addDataflow(dataflow);
                        }
                    }
                    dataflowRepository.saveAndFlush(dataflow);

                    if (autoCreate) {
                        for (String newId : newIds) {
                            PrepTransformResponse response = this.transformService.create(newId, dataflow.getDfId(), null);
                        }
                    }
                }
            } else {
                String errorMsg = new String("dataflow[" + dfId + "] was not found");
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, errorMsg);
            }
        }
        catch (Exception e) {
            LOGGER.error("addDataset(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }

    @RequestMapping(value = "/{dfId}/add_datasets", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<?> addDatasets (
            @PathVariable("dfId") String dfId,
            @RequestBody PrepParamDatasetIdList dsIds
    ) throws PrepException {
        PrDataflow dataflow = dataflowRepository.findById(dfId).get();
        try {
            if( dataflow!=null ) {
                if(dsIds!=null) {
                    for (String dsId : dsIds.getDsIds() ) {
                        PrDataset dataset = datasetRepository.findById(dsId).get();
                        if( dataset!=null ) {
                            dataflow.addDataset(dataset);
                            dataset.addDataflow(dataflow);
                        }
                    }
                    dataflowRepository.saveAndFlush(dataflow);
                }
            } else {
                String errorMsg = new String("dataflow[" + dfId + "] was not found");
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, errorMsg);
            }
        }
        catch (Exception e) {
            LOGGER.error("addDataset(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }

    @RequestMapping(value = "/{dfId}/remove/{dsId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody ResponseEntity<?> removeDataset (
            @PathVariable("dfId") String dfId,
            @PathVariable("dsId") String dsId
    ) throws PrepException {
        PrDataflow dataflow = dataflowRepository.findById(dfId).get();
        try {
            if( dataflow!=null ) {
                PrDataset dataset = datasetRepository.findById(dsId).get();
                if( dataset!=null ) {
                    dataflow.deleteDataset(dataset);
                    dataset.deleteDataflow(dataflow);
                    dataflowRepository.saveAndFlush(dataflow);
                } else {
                    String errorMsg = new String("dataset[" + dsId + "] was not found");
                    throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATASET, errorMsg);
                }
            } else {
                String errorMsg = new String("dataflow[" + dfId + "] was not found");
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW, errorMsg);
            }
        }
        catch (Exception e) {
            LOGGER.error("removeDataset(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }

    @RequestMapping(value = "/{dfId}/remove_datasets", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody ResponseEntity<?> removeDatasets (
            @PathVariable("dfId") String dfId,
            @RequestBody PrepParamDatasetIdList dsIds
    ) throws PrepException {
        PrDataflow dataflow = dataflowRepository.findById(dfId).get();
        try {
            if( dataflow!=null ) {
                List<PrDataset> removeList = new ArrayList<PrDataset>();
                List<PrDataset> datasets = dataflow.getDatasets();
                if(datasets!=null) {
                    for (PrDataset dataset : datasets) {
                        if ( true == dsIds.getDsIds().contains(dataset.getDsId()) ) {
                            removeList.add(dataset);
                        }
                    }
                    for(PrDataset removeDataset : removeList) {
                        dataflow.deleteDataset(removeDataset);
                        removeDataset.deleteDataflow(dataflow);
                    }
                    dataflowRepository.saveAndFlush(dataflow);
                }
            } else {
                String errorMsg = new String("dataflow[" + dfId + "] was not found");
                throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, PrepMessageKey.MSG_DP_ALERT_NO_DATAFLOW);
            }
        }
        catch (Exception e) {
            LOGGER.error("removeDatasets(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }

    @RequestMapping(value = "/{dfId}/swap_upstream", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ResponseEntity<?> swapUpstream (
            @PathVariable("dfId") String dfId,
            @RequestBody PrepSwapRequest swapRequest
    ) throws PrepException {
        PrDataflow dataflow = dataflowRepository.findById(dfId).get();

        try {
            List<String> affectedDsIds = transformService.swap_upstream(dataflow, swapRequest);
            transformService.after_swap(affectedDsIds);
        } catch (Exception e) {
            LOGGER.error("swap_upstream(): caught an exception: ", e);
            throw PrepException.create(PrepErrorCodes.PREP_DATAFLOW_ERROR_CODE, e);
        }

        return ResponseEntity.status(HttpStatus.SC_OK).body(dataflow);
    }
}
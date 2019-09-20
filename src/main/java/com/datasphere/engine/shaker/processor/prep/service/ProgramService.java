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

package com.datasphere.engine.shaker.processor.prep.service;

import com.datasphere.core.common.BaseService;
import com.datasphere.engine.shaker.processor.prep.dao.*;
import com.datasphere.engine.shaker.processor.prep.data.ColumnData;
import com.datasphere.engine.shaker.processor.prep.data.OperateData;
import com.datasphere.engine.shaker.processor.prep.data.ProgramFlowData;
import com.datasphere.engine.shaker.processor.prep.data.ProgramOutputData;
import com.datasphere.engine.shaker.processor.prep.model.Column;
import com.datasphere.engine.shaker.processor.prep.model.OperateCode;
import com.datasphere.engine.shaker.processor.prep.model.Program;
import com.datasphere.engine.shaker.processor.prep.model.ProgramColumn;
import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSession;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public class ProgramService extends BaseService {
    private static final String INPUT_COLUMN_FLAG = "0";
    private static final String OUTPUT_CLOUMN_FLAG = "1";
    private static final String ORIGIN_COLUMN = "1";
    private static final String GENERATED_COLUMN = "0";

//    @Autowired
//    private DataProcessDao dataProcessDao;
//    @Inject
//    private ProgramDao programDao;
//    @Inject
//    private ProgramColumnDao programColumnDao;
//    @Inject
//    private ProgramOperateDao programOperateDao;
//    @Inject
//    private OperateDao operateDao;
//    @Inject
//    private ColumnDao columnDao;
//    @Inject
//    private OperateCodeDao operateCodeDao;
//    @Autowired
//    private OperateService operateService;
//    @Inject
//    DataAccessor dataAccessor;

    //方案默认状态
    private final static String isDefault = "0";
    //方案名称分隔符
    private final static String separator = "_";
    //方案默认版本号
    private final static Integer version = 1;

    /**
     * 构建流数据
     * @param processId
     * @return
     */
    public ProgramFlowData buildFlowData(String processId) {
        ProgramOutputData programOutputData = getDefaultProgram(processId);
        if (null == programOutputData) return null;
        return new ProgramFlowData(programOutputData);
    }

//    @Override
//    public ProgramData info(Integer program_id) {
//        ProgramData programData = programDao.selectById(program_id);
//        if (null != programData && programData.getId() > 0) {
//
//        }
//        return programData;
//    }
//
//    @Override
//    public int delete(Integer programId) {
//
//        int res = programDao.deleteByProgramId(programId);
//        if (res > 0) {
//            res += programColumnDao.deleteByProgramId(programId);
//            res += programOperateDao.deleteByProgramId(programId);
//        }
//        return res;
//    }
//
//    @Override
//    public int rename(Integer program_id, String new_name) {
//        return programDao.rename(program_id, new_name);
//    }
//
//    @Override
//    public ProgramData copy(Integer programId) {
//        Program pd = programDao.selectByPrimaryKey(programId);
//        int res = result;
//        if (null != pd) {
//            String name = getProgramName(pd);
//            //Integer integer = (null != pd.getVersion() ? pd.getVersion() : 1) + 1;
//            //res += programDao.updateVersion(programId, integer);
//            Date date = new Date();
//            Program program =
//                    new Program(name, isDefault, version, pd.getProcessId(), pd.getTableId(), date,
//                            date);
//            res += programDao.insertUseGeneratedKeys(program);
//            Integer newId = program.getId();
//            // 数据列
//            List<ProgramColumn> lists = programColumnDao.selectByOrder(programId);
//            List<ProgramColumn> pcs =
//                    Lists.transform(lists, new Function<ProgramColumn, ProgramColumn>() {
//                        public ProgramColumn apply(ProgramColumn column) {
//                            return new ProgramColumn(newId, column.getColumnId(),
//                                    column.getColumnOrder(), date, date);
//                        }
//                    });
//            res += programColumnDao.insertList(pcs);
//            // 数据操作
//            // programOperateDao.selectByProgramId(programId);
//            return programDao.selectById(newId);
//        }
//        return null;
//    }
//
//    private String getProgramName(Program pd) {
//        return getName(pd.getName()) + getMaxNum(programDao.selectByProcessId(pd.getProcessId()));
//    }
//
//    private static String getName(String name) {
//        return name.lastIndexOf("_") > 0 ? name.substring(0, name.lastIndexOf("_") + 1) : name;
//    }
//
//    private static Integer getNum(String name) {
//        return name.lastIndexOf("_") > 0 ?
//                Integer.valueOf(name.substring(name.lastIndexOf("_") + 1)) :
//                0;
//    }
//
//    private Integer getMaxNum(List<ProgramData> programDatas) {
//        Integer max = programDatas.size();
//        for (ProgramData programData : programDatas) {
//            Integer temp = getNum(programData.getName());
//            max = max > temp ? max : temp;
//        }
//        return ++max;
//    }
//
//    @Override
//    public int setDefault(Integer program_id, String processId) {
//        int res = programDao.unsetDefault(processId);
//        return res += programDao.setDefault(program_id);
//    }
//
//    @Override
//    public Integer removeColumn(@NotNull Integer programId, @NotNull Integer columnId) {
//        ProgramColumn programColumn =
//                new ProgramColumn().setColumnId(columnId).setProgramId(programId);
//        return programColumnDao.delete(programColumn);
//    }
//
//    @Override
//    public Integer removeColumn(@NotNull ProgramColumn programColumn) {
//        assert programColumn.getColumnId() != null && programColumn.getId() != null;
//        return programColumnDao.delete(programColumn);
//    }

    public ProgramOutputData getDefaultProgram(String processId) {
        List<OperateData> operateDatas = null;
        List<ColumnData> columnDatas = null;
        Program program = null;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            OperateDao operateDao = sqlSession.getMapper(OperateDao.class);
            operateDatas = operateDao.selectByProcessId(processId);
        }
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            ColumnDao columnDao = sqlSession.getMapper(ColumnDao.class);
            columnDatas = columnDao.selectByProcessId(processId);
        }
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            ProgramDao programDao = sqlSession.getMapper(ProgramDao.class);
            program = programDao.getDefault(processId);
        }
        if (null != program && program.getId() > 0) {
            return buildOutputData(program.getId(), operateDatas, columnDatas);
        }
        return null;
    }

//    @Override
//    public List<OperateData> getProgramAllOperateData(@NotNull String processId) {
//        List<OperateData> operateDatas = operateDao.selectByProcessId(processId);
//        if (null == operateDatas) return null;
//        Arrays.sort(operateDatas.toArray());
//        return operateDatas;
//    }

//    @Override public List<ProgramOutputData> getAllProgramOutputDataByProcessId(String processId) {
//        List<OperateData> operateDatas = operateDao.selectByProcessId(processId);
//        List<ColumnData> columnDatas = columnDao.selectByProcessId(processId);
//        List<ProgramOutputData> result = new ArrayList<>();
//        Program program = new Program();
//        program.setProcessId(processId);
//        List<Program> programs = programDao.select(program);
//        if (null == programs || programs.size() == 0)
//            return result;
//
//        for (Program program1 : programs) {
//            ProgramOutputData programOutputData =
//                    buildOutputData(program1.getId(), operateDatas, columnDatas);
//            if (null != programOutputData) {
//                result.add(programOutputData);
//            }
//        }
//        return result;
//    }


//    @Override
//    public ProgramFlowData buildFlowData(@NotNull String processId) {
//        ProgramOutputData programOutputData = getDefaultProgram(processId);
//        if (null == programOutputData) return null;
//        return new ProgramFlowData(programOutputData);
//
//    }


    private List<Column> buildColumns(List<ProgramColumn> programColumns) {
        List<Integer> columnIds = new ArrayList<>();
        for (ProgramColumn programColumn : programColumns) {
            columnIds.add(programColumn.getColumnId());
        }
        List<Column> columnList = null;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            ColumnDao dao = sqlSession.getMapper(ColumnDao.class);
            columnList = dao.selectByIdS(columnIds);
        }
        return columnIds.isEmpty() ? null : columnList;
    }


//    @Override
//    public List<Program> list(String processId) {
//        return programDao.list(processId);
//    }

    /**
     * @param programId
     * @param operateDataList
     * @param columnDataList
     * @return guo ji tian xu yao chong xie gai fang fa
     */
    private ProgramOutputData buildOutputData(Integer programId, List<OperateData> operateDataList, List<ColumnData> columnDataList) {
        Program program = null;
        List<ProgramColumn> programColumns = null;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            ProgramDao programDao = sqlSession.getMapper(ProgramDao.class);
            program = programDao.findById(programId);//selectByPrimaryKey
        }
        assert null != program;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            ProgramColumnDao programColumnDao = sqlSession.getMapper(ProgramColumnDao.class);
            programColumns = programColumnDao.selectByOrder(programId);
        }
        List<Column> columns = buildColumns(programColumns);
        Map<Integer, OperateData> operateMap = new HashMap<>();
        for (ProgramColumn column : programColumns) {
            List<OperateData> operates = getAllOperateDataByColumnId(column.getColumnId(), operateDataList, columnDataList);
            for (OperateData operate : operates) {
                operateMap.put(operate.getId(), operate);
            }
        }

        List<OperateData> oriColumnOperates = getSourceColumnOperateData(operateDataList);
        for (OperateData oriColumnOperate : oriColumnOperates) {
            operateMap.put(oriColumnOperate.getId(), oriColumnOperate);
        }
        List<OperateCode> operateCodes = null;
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()) {
            OperateCodeDao operateCodeDao = sqlSession.getMapper(OperateCodeDao.class);
            operateCodes = operateCodeDao.selectAll();
        }
        assert null != operateCodes;
        Map<String, String> operateCodeMap = new HashMap<>();
        for (OperateCode operateCode : operateCodes) {
            operateCodeMap.put(operateCode.getOperateCode(), operateCode.getName());
        }
        for (OperateData operateData : operateMap.values()) {
            List<ColumnData> inColumns = operateData.getSources();
            if (null != inColumns) {
                String operateName = "";
                for (ColumnData inColumn : inColumns) {
                    operateName += inColumn.getName() + ",";
                }
                Integer len = operateName.length();
                len = len <= 0 ? 0 : len - 1;
                operateName = operateName.substring(0, len);
                operateName += (" " + operateCodeMap.get(operateData.getOperateCode()));
                operateData.setOperateName(operateName);
            }
        }
        Arrays.sort(operateMap.values().toArray());

        String tableId = program.getTableId();
        long rowCount = 0;
        Integer columnCount = columns == null ? 0 : columns.size();
        try {
            rowCount = 0;//jeq dataAccessor.rowCount(tableId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return new ProgramOutputData(program.getProcessId(), programId, columns, Lists.newArrayList(operateMap.values()), columnCount, rowCount);
    }

    private List<OperateData> getSourceColumnOperateData(List<OperateData> operateDatas) {

        List<OperateData> result = new ArrayList<>();
        for (OperateData operateData : operateDatas) {
            if (operateData.getOperateCode().equals("ChangeType")) {
                result.add(operateData);
            }
        }
        return result;
    }

    private List<OperateData> getAllOperateDataByColumnId(Integer columnId, List<OperateData> operateDatas, List<ColumnData> columnDatas) {
        List<OperateData> result = new ArrayList<>();
        Stack<ColumnData> columnStack = new Stack<>();

        ColumnData columnData = findById(columnId, columnDatas);
        if (null == columnData) {
            throw new RuntimeException(
                    "ERROR: Data corruption " + "program column ID = " + columnId
                            + " not in the process columns!");
        }
        if (ORIGIN_COLUMN.equals(columnData.getSourceFlag())) {
            return result;
        }

        columnStack.push(columnData);
        while (!columnStack.isEmpty()) {
            //查找输出该列的操作
            ColumnData column1 = columnStack.pop();
            OperateData operateData = findGenThisColumnOperate(column1.getId(), operateDatas);
            assert operateData != null;
            result.add(operateData);

            //查找该操作的输入列
            List<ColumnData> operateInputColumns = findOperateInputColumns(operateData.getId(), operateDatas);
            for (ColumnData operateInputColumn : operateInputColumns) {
                if (GENERATED_COLUMN.equals(operateInputColumn.getSourceFlag())) {
                    columnStack.push(operateInputColumn);
                }
            }
        }
        return result;
    }

    private OperateData findGenThisColumnOperate(Integer columnId, List<OperateData> operateDatas) {
        for (OperateData operateData : operateDatas) {
            for (ColumnData columnData : operateData.getTargets()) {
                // 生成列，并且是某个操作的输出列
                if (columnData.getId().equals(columnId)) {
                    return operateData;
                }
            }
        }
        return null;
    }

    private List<ColumnData> findOperateInputColumns(Integer operateId, List<OperateData> operateDatas) {
        for (OperateData operateData : operateDatas) {
            if (operateData.getId().equals(operateId))
                return operateData.getSources();
        }
        return null;
    }

    private ColumnData findById(Integer columnId, List<ColumnData> columnDatas) {
        for (ColumnData columnData : columnDatas) {
            if (columnData.getId().equals(columnId)) {
                return columnData;
            }
        }
        return null;
    }

//    @Override
//    public List<Program> get(String programids) {
//        return programDao.get(programids);
//    }
}

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

import com.datasphere.common.data.Column;
import com.datasphere.common.data.Dataset;
import com.datasphere.core.common.BaseService;
import com.datasphere.engine.datasource.connections.jdbc.service.DataAccessor;
import com.datasphere.engine.shaker.processor.prep.dao.DataProcessDao;
import com.datasphere.engine.shaker.processor.prep.data.JoinInputColumn;
import com.datasphere.engine.shaker.processor.prep.data.JoinInputData;
import com.datasphere.engine.shaker.processor.prep.data.JoinOutputData;

import org.apache.ibatis.session.SqlSession;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class DataProcessService extends BaseService {

    public int check(String processId) {
        try(SqlSession sqlSession = sqlSessionFactoryService.getSqlSession()){
            DataProcessDao processDao = sqlSession.getMapper(DataProcessDao.class);
            return null != processDao.selectById(processId) ? 1 : 0;
        }
    }

    /**1
     * 并表
     * @param inputdata
     * @return
     */
    public JoinOutputData join(JoinInputData inputdata) {
        DataAccessor dataAccessor = new DataAccessor();
        JoinOutputData output = new JoinOutputData();
        // TODO Auto-generated method stub
        try {
            Dataset ds1 = dataAccessor.getDataset(inputdata.getTable1id());// 获取第一个数据集
            Dataset ds2 = dataAccessor.getDataset(inputdata.getTable2id());// 获取第二个数据集

            if (ds1 != null && ds2 != null) {
                String[][] data1 = ds1.getData();// 获取表数据
                String[][] data2 = ds2.getData();
                int maxRows = data1.length * data2.length;

                List<String[]> dataList = new ArrayList<String[]>();
                Column[] ColumnsMeta1List = ds1.getColumnsMeta();// 获取字段的元数据
                Column[] ColumnsMeta2List = ds2.getColumnsMeta();

                int[] ColumnsMeta1Indexs = new int[inputdata.getTable1ColumnList().size()];
                int[] ColumnsMeta2Indexs = new int[inputdata.getTable2ColumnList().size()];
                int columnLength = ColumnsMeta1Indexs.length + ColumnsMeta2Indexs.length;

                Map<String, String> mapList = new HashMap<String, String>();
                Column[] columns = new Column[columnLength];
                int columnIndex = 0;
                for (int i = 0; i < ColumnsMeta1List.length; i++) {
                    String columnName = ColumnsMeta1List[i].getName();
                    for (String cl : inputdata.getTable1ColumnList()) {
                        if (columnName.equals(cl)) {
                            ColumnsMeta1Indexs[columnIndex++] = i;
                            if (!mapList.containsKey(columnName)) {
                                mapList.put(columnName, "#1#" + columnName);
                            }
                        }
                    }
                    for (JoinInputColumn jic : inputdata.getJoinColumnList()) {
                        if (jic.getTable1ColumnName().equals(columnName)) {
                            jic.setTable1ColumnIndex(i);
                        }
                    }
                }
                columnIndex = 0;
                for (int i = 0; i < ColumnsMeta2List.length; i++) {
                    String columnName = ColumnsMeta2List[i].getName();
                    for (String cl : inputdata.getTable2ColumnList()) {
                        if (columnName.equals(cl)) {
                            ColumnsMeta2Indexs[columnIndex++] = i;
                            if (!mapList.containsKey(columnName)) {
                                mapList.put(columnName, "#2#" + columnName);
                            }
                        }
                    }
                    for (JoinInputColumn jic : inputdata.getJoinColumnList()) {
                        if (jic.getTable2ColumnName().equals(columnName)) {
                            jic.setTable2ColumnIndex(i);
                        }
                    }
                }

                for (int i = 0; i < ColumnsMeta1Indexs.length; i++) {
                    String columnName = ColumnsMeta1List[ColumnsMeta1Indexs[i]].getName();
                    columnName = getNewColumnName(mapList, columnName, columnLength, 1);
                    Column column = new Column();
                    column.setName(columnName);
                    column.setType(ColumnsMeta1List[i].getType());
                    columns[i] = column;
                }
                for (int i = 0; i < ColumnsMeta2Indexs.length; i++) {
                    String columnName = ColumnsMeta2List[ColumnsMeta2Indexs[i]].getName();
                    columnName = getNewColumnName(mapList, columnName, columnLength, 2);
                    Column column = new Column();
                    column.setName(columnName);
                    column.setType(ColumnsMeta2List[i].getType());
                    columns[ColumnsMeta1Indexs.length + i] = column;
                }

                for (int i = 0; i < data1.length; i++) {
                    for (int j = 0; j < data2.length; j++) {
                        String[] row = new String[columnLength];
                        if (isDataEqual(data1[i], data2[j], inputdata)) {
                            for (int ii = 0; ii < ColumnsMeta1Indexs.length; ii++) {
                                row[ii] = data1[i][ColumnsMeta1Indexs[ii]];
                            }
                            for (int jj = 0; jj < ColumnsMeta2Indexs.length; jj++) {
                                row[ColumnsMeta1Indexs.length + jj] = data2[j][ColumnsMeta2Indexs[jj]];
                            }
                            dataList.add(row);
                        } else if (inputdata.getJoinType().equals("2")) {
                            //left join
                            for (int ii = 0; ii < ColumnsMeta1Indexs.length; ii++) {
                                row[ii] = data1[i][ColumnsMeta1Indexs[ii]];
                            }
                            dataList.add(row);
                        } else if (inputdata.getJoinType().equals("3")) {
                            //right join
                            for (int jj = 0; jj < ColumnsMeta2Indexs.length; jj++) {
                                row[ColumnsMeta1Indexs.length + jj] = data2[j][ColumnsMeta2Indexs[jj]];
                            }
                            dataList.add(row);
                        }
                    }
                }
                String[][] data = new String[dataList.size()][columnLength];
                for (int index = 0; index < dataList.size(); index++) {
                    data[index] = dataList.get(index);
                }
                output.setTotalrows(dataList.size());

                Dataset dataSet = new Dataset();
                dataSet.setData(data);
                dataSet.setColumnsMeta(columns);
                String table_id = dataAccessor.setDataset(dataSet);
                output.setTable1(table_id);
                output.setTotalrows(data.length);
                return output;
            } else {
                return null;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String getNewColumnName(Map<String, String> map, String columnName, int length, int flag) {
        for (int i = 0; i < length; i++) {
            String key = columnName;
            if (i > 0) {
                key = columnName + (i);
            }
            if (map.containsKey(key)) {
                if (map.get(key).equals("#" + flag + "#" + columnName)) {
                    return columnName;
                }
            } else {
                map.put(key, "#" + flag + "#" + columnName);
                return key;
            }
        }
        return columnName + "_100";
    }

    public boolean isDataEqual(String[] data1, String[] data2, JoinInputData inputdata) {
        for (JoinInputColumn jc : inputdata.getJoinColumnList()) {
            if (!data1[jc.getTable1ColumnIndex()].equals(data2[jc.getTable2ColumnIndex()])) {
                return false;
            }
        }
        return true;
    }

}

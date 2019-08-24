package com.datasphere.server.connections.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.datasphere.resource.manager.module.dal.dao.DatasetDAO;
import com.datasphere.resource.manager.module.dal.service.DataAccessor;
import com.datasphere.common.data.Column;
import com.datasphere.common.data.Dataset;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.ConnectionFactory;
import com.datasphere.resource.manager.module.dal.buscommon.dbutils.JDBCUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DatasetDAOImpl implements DatasetDAO {
	ConnectionFactory connectionFactory = new ConnectionFactoryImpl(DataAccessor.PG_URL,false);

	@Override
	public void insert(Dataset dataset) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		try(PreparedStatement pst = conn.prepareStatement("insert into PUBLIC.DATASET_INSTANCE(ID, COLUMNS_JSON, MESSAGE) values(?,?,?)")) {
			JDBCUtils.set(pst, 1, dataset.getDataKey());
			JDBCUtils.set(pst, 2, dataset.getColumnsMeta() == null ? null : new Gson().toJson(dataset.getColumnsMeta()));
			JDBCUtils.set(pst, 3, dataset.getMessage());
			pst.executeUpdate();
		}
	}

	@Override
	public Boolean exists(String id) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		try(PreparedStatement pst = conn.prepareStatement("select count(1) from PUBLIC.DATASET_INSTANCE where id = ?")) {
			
			JDBCUtils.set(pst, 1, id);
			ResultSet set = pst.executeQuery();
			set.next();
			return set.getBoolean(1);
		}
	}

	@Override
	public int update(Dataset dataset) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();  
		try(PreparedStatement pst = conn.prepareStatement("update PUBLIC.DATASET_INSTANCE set COLUMNS_JSON = ?, MESSAGE = ? where id = ?")) {
			JDBCUtils.set(pst, 1, dataset.getColumnsMeta() == null ? null : new Gson().toJson(dataset.getColumnsMeta()));
			JDBCUtils.set(pst, 2, dataset.getMessage());
			JDBCUtils.set(pst, 3, dataset.getDataKey());
			return pst.executeUpdate();
		}
	}

	@Override
	public int delete(String id) throws SQLException {
		Connection conn = connectionFactory.getThreadConnection();
		try(PreparedStatement pst = conn.prepareStatement("delete from PUBLIC.DATASET_INSTANCE where id = ?")) {
			JDBCUtils.set(pst, 1, id);
			return pst.executeUpdate();
		}
	}


	//@Override
	public Dataset get(String id) throws SQLException {
		Connection conn = connectionFactory.getConnection();
		try(PreparedStatement pst = conn.prepareStatement("select DESCRIPTION,COLUMNS_JSON from PUBLIC.DATASET_INSTANCE where id = ?")) {
			JDBCUtils.set(pst, 1, id);
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				Dataset res = new Dataset();
				res.setDataKey(id);
				res.setMessage(rs.getString(1)); //JDBCUtils.getString(rs, 1)
				String columnMetaData = rs.getString(2); //JDBCUtils.getString(rs, 2)
				if(columnMetaData != null) {
//					res.setColumnsMeta(toArray(new Gson().fromJson(columnMetaData,new TypeToken<List<Column>>(){}.getType())));
					res.setColumnsMeta(toArray(JSONObject.parseArray(columnMetaData, Column.class)));
//					[{"name":"name","sourceType":2,"type":"Decimal","reserve":"1"}]
				}
				return res;
			} else {
				return null;
			}
		}
	}
	
	protected Column[] toArray(Collection<Column> list) {
		if(list == null) return null;
		return list.toArray(new Column[list.size()]);
	}
	
	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}

package com.datasphere.engine.datasource.mybatis.page;

import com.datasphere.core.common.utils.ReflectHelper;
import com.datasphere.engine.datasource.mybatis.page.Pager;

import org.apache.ibatis.plugin.*;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.property.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.*;
import java.util.*;
import org.apache.ibatis.session.*;
import org.apache.ibatis.reflection.*;
import org.apache.ibatis.type.*;
import org.apache.ibatis.executor.statement.*;
import java.sql.*;

@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PagePlugin implements Interceptor
{
    private String dialect;
    private String pageSqlId;
    
    public PagePlugin() {
        this.dialect = "";
        this.pageSqlId = "";
    }
    
    public Object intercept(final Invocation invocation) throws Throwable {
        throw new Error("Unresolved compilation problem: \n\tThe method intercept(Invocation) of type PagePlugin must override a superclass method\n");
    }
    
    public Object plugin(final Object o) {
        throw new Error("Unresolved compilation problem: \n\tThe method plugin(Object) of type PagePlugin must override a superclass method\n");
    }
    
    public void setProperties(final Properties properties) {
        throw new Error("Unresolved compilation problem: \n\tThe method setProperties(Properties) of type PagePlugin must override a superclass method\n");
    }
    
    private String generatePageSql(final String sql, final Pager page) {
        final StringBuffer sqlBuffer = new StringBuffer(sql);
        if ("mysql".equalsIgnoreCase(this.dialect)) {
            return this.getMysqlPageSql(page, sqlBuffer);
        }
        if ("oracle".equalsIgnoreCase(this.dialect)) {
            return this.getOraclePageSql(page, sqlBuffer);
        }
        return sqlBuffer.toString();
    }
    
    private String getMysqlPageSql(final Pager page, final StringBuffer sqlBuffer) {
        final int offset = (page.getPageNumber() - 1) * page.getPageSize();
        sqlBuffer.append(" limit ").append(offset).append(",").append(page.getPageSize());
        return sqlBuffer.toString();
    }
    
    private String getOraclePageSql(final Pager page, final StringBuffer sqlBuffer) {
        final int offset = (page.getPageNumber() - 1) * page.getPageSize() + 1;
        sqlBuffer.insert(0, "select u.*, rownum r from (").append(") u where rownum < ").append(offset + page.getPageSize());
        sqlBuffer.insert(0, "select * from (").append(") where r >= ").append(offset);
        return sqlBuffer.toString();
    }
    
    private String getSnappyDataPageSql(final Pager page, final StringBuffer sqlBuffer) {
        final int offset = (page.getPageNumber() - 1) * page.getPageSize();
        sqlBuffer.append(" offset ").append(offset).append(" rows fetch next ").append(page.getPageSize()).append(" rows only");
        return sqlBuffer.toString();
    }
    
    private String fileterString(String sqlvalue) {
        if (StringUtils.isEmpty(sqlvalue)) {
            return sqlvalue;
        }
        sqlvalue = sqlvalue.trim().toLowerCase();
        sqlvalue = sqlvalue.replace("=", "");
        sqlvalue = sqlvalue.replace("'", "");
        sqlvalue = sqlvalue.replace(";", "");
        sqlvalue = sqlvalue.replace(" or ", "");
        sqlvalue = sqlvalue.replace("select", "");
        sqlvalue = sqlvalue.replace("update", "");
        sqlvalue = sqlvalue.replace("insert", "");
        sqlvalue = sqlvalue.replace("delete", "");
        sqlvalue = sqlvalue.replace("declare", "");
        sqlvalue = sqlvalue.replace("exec", "");
        sqlvalue = sqlvalue.replace("drop", "");
        sqlvalue = sqlvalue.replace("create", "");
        sqlvalue = sqlvalue.replace("%", "");
        sqlvalue = sqlvalue.replace("--", "");
        return sqlvalue;
    }
    
    private void setParameters(final PreparedStatement ps, final MappedStatement mappedStatement, final BoundSql boundSql, final Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        final List<ParameterMapping> parameterMappings = (List<ParameterMapping>)boundSql.getParameterMappings();
        if (parameterMappings != null) {
            final Configuration configuration = mappedStatement.getConfiguration();
            final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            final MetaObject metaObject = (parameterObject == null) ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); ++i) {
                final ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    final String propertyName = parameterMapping.getProperty();
                    final PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    Object value;
                    if (parameterObject == null) {
                        value = null;
                    }
                    else if (typeHandlerRegistry.hasTypeHandler((Class)parameterObject.getClass())) {
                        value = parameterObject;
                    }
                    else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    }
                    else if (propertyName.startsWith("__frch_") && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    }
                    else {
                        value = ((metaObject == null) ? null : metaObject.getValue(propertyName));
                    }
                    final TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }
    
    int count(final Invocation invocation) throws SQLException {
        final RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
        final BaseStatementHandler delegate = ReflectHelper.getValue(statementHandler, "delegate");
        final MappedStatement mappedStatement = ReflectHelper.getValue(delegate, "mappedStatement");
        final BoundSql boundSql = delegate.getBoundSql();
        final Object parameterObject = boundSql.getParameterObject();
        final Connection connection = (Connection)invocation.getArgs()[0];
        final String sql = boundSql.getSql();
        final String countSql = "select count(0) from (" + sql + ") as s1 ";
        final PreparedStatement countStmt = connection.prepareStatement(countSql);
        final BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
        this.setParameters(countStmt, mappedStatement, countBS, parameterObject);
        final ResultSet rs = countStmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        rs.close();
        countStmt.close();
        return count;
    }
    
    boolean isPagerObject(final Object obj) {
        return obj != null && obj instanceof Pager;
    }
}

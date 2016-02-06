package mine.allen.util.sql;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import mine.allen.util.lang.LogL;

/**
 * 需要Jdbc操作类继承,Jdbc操作类只需要实现设置数据池<br>
 * 这个类里面有jdbc的具体操作实现
 * @author Allen
 * @version 1.0 For All Projects 
 */
abstract class JdbcL {
	/**是否自动提交*/
	private static final boolean AUTO_COMMIT = true;
	/**数据池*/
    protected DataSource dataSource;
	/**连接*/
    private Connection conn ;
    
    public abstract void setDataSource(DataSource dataSource);
    
    /**
     * 取得数据库连接,默认自动提交
     * @return Connection
     * @throws SQLException 
     */
    public void getConnection() throws SQLException {  
        getConnection(AUTO_COMMIT);  
    }
    
	/**
     * 取得数据库连接
     * @return Connection
	 * @throws SQLException 
     */
	public void getConnection(boolean autoCommit) throws SQLException{
		release(conn);
		conn = dataSource.getConnection();
	    if (!autoCommit)  
	        conn.setAutoCommit(autoCommit);  
	}
	
	/** 
     * 释放数据库结果集
	 * @throws SQLException 
     */  
    public void commit() throws SQLException{  
        conn.commit();
    }
	
	/** 
     * 释放数据库结果集
	 * @throws SQLException 
     */  
    public void release(ResultSet resultSet) throws SQLException{  
        if(resultSet != null){  
            resultSet.close();
        }
    } 
    
    /** 
     * 释放数据库执行SQL
     * @throws SQLException 
     */  
    public void release(PreparedStatement pstmt) throws SQLException{  
        if(pstmt  != null){  
            pstmt.close();  
        }
    }
    
    /** 
     * 释放数据库连接 
     * @throws SQLException 
     */  
    public void release(Connection connection) throws SQLException{  
        if(connection != null){  
            connection.close();   
        }
    }
    
    /** 
     * 释放数据库连接 
     * @throws SQLException 
     */  
    public void releaseConnection() throws SQLException{  
        if(conn != null){  
        	conn.close();
        }
    }
    
    /** 
     * 释放数据库连接 
     * @throws SQLException 
     */  
    public void release(ResultSet resultSet,PreparedStatement pstmt,Connection connection) throws SQLException{  
        if(resultSet != null){  
            resultSet.close();
        }
        if(pstmt != null){  
            pstmt.close();   
        }
        if(connection != null){  
            connection.close();  
        }
    }
    
    /**
     * 返回ResultSet
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException {  
        return querySql(conn, sql, null);
    }
    
    /**
     * 返回ResultSet
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql, Object[] params) throws SQLException {  
        return querySql(conn, sql, params);
    }
    
    /**
     * 返回ResultSet
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql, List<Object> params) throws SQLException {  
        return querySql(conn, sql, listToObject(params));
    }
    
    /** 
     * 查询单条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public Map<String, Object> getSingleResult(String sql) throws SQLException{
    	ResultSet rs = querySql(conn, sql, null);
    	Map<String, Object> map = getMapFromResultSet(rs);
        release(rs);
        return map;  
    }
    
    /** 
     * 查询单条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public Map<String, Object> getSingleResult(String sql, Object[] params) throws SQLException{
    	ResultSet rs = querySql(conn, sql, params);
    	Map<String, Object> map = getMapFromResultSet(rs);
        release(rs);
        return map;  
    }
    
    /** 
     * 查询单条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public Map<String, Object> getSingleResult(String sql, List<Object> params) throws SQLException{
    	return getSingleResult(sql, listToObject(params));
    }
    
    /**查询多条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> getResultList(String sql) throws SQLException{
    	ResultSet rs = querySql(conn, sql, null);
    	List<Map<String, Object>> list = getMapListFromResultSet(rs);
    	release(rs);
        return list;  
    }
    
    /**查询多条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> getResultList(String sql, Object[] params) throws SQLException{
    	ResultSet rs = querySql(conn, sql, params);
    	List<Map<String, Object>> list = getMapListFromResultSet(rs);
    	release(rs);
        return list;  
    }
    
    /**查询多条记录 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> getResultList(String sql, List<Object> params) throws SQLException{
    	return getResultList(sql, params); 
    }
    
    /**通过反射机制查询单条记录 
     * @param sql 
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> T getSingleRefResult(String sql, Object[] params, Class<T> cls) throws SQLException{  
        T resultObject = null;
        ResultSet resultSet = querySql(conn, sql, params);
        ResultSetMetaData metaData  = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();
        try{
        	while(resultSet.next()){  
                //通过反射机制创建一个实例  
                resultObject = cls.newInstance();  
                for(int i = 0; i<cols_len; i++){
                    String cols_name = metaData.getColumnName(i+1);  
                    Object cols_value = resultSet.getObject(cols_name);  
                    if(cols_value == null){  
                        cols_value = "";  
                    }  
                    Field field = cls.getDeclaredField(cols_name);  
                    field.setAccessible(true); //打开javabean的访问权限  
                    field.set(resultObject, cols_value);  
                }  
            }
        }catch(IllegalAccessException | InstantiationException | NoSuchFieldException | SecurityException e){
        	LogL.getInstance().getLog().error("通过反射机制查询单条记录出错", e);
        	throw new SQLException("通过反射机制查询单条记录 出错",e);
        }
        release(resultSet);
        return resultObject;
    }
    
    /**通过反射机制查询单条记录 
     * @param sql 
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> T getSingleRefResult(String sql, List<Object> params, Class<T> cls) throws SQLException{  
        return getSingleRefResult(sql, listToObject(params), cls);
    }
    
    /**通过反射机制查询多条记录 
     * @param sql  
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> List<T> getRefResultList(String sql, Object[] params, Class<T> cls) throws SQLException {  
        List<T> list = new ArrayList<T>();
        ResultSet resultSet = querySql(conn, sql, params);
        ResultSetMetaData metaData  = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();
        try{
	        while(resultSet.next()){  
	            //通过反射机制创建一个实例  
	            T resultObject = cls.newInstance();  
	            for(int i = 0; i<cols_len; i++){  
	                String cols_name = metaData.getColumnName(i+1);  
	                Object cols_value = resultSet.getObject(cols_name);  
	                if(cols_value == null){  
	                    cols_value = "";  
	                }  
	                Field field = cls.getDeclaredField(cols_name);  
	                field.setAccessible(true); //打开javabean的访问权限  
	                field.set(resultObject, cols_value);  
	            }  
	            list.add(resultObject);  
	        }
        }catch(IllegalAccessException | InstantiationException | NoSuchFieldException | SecurityException e){
        	LogL.getInstance().getLog().error("通过反射机制查询多条记录 出错", e);
        	throw new SQLException("通过反射机制查询多条记录 出错",e);
        }
        release(resultSet);
        return list;  
    }
    
    /**通过反射机制查询多条记录 
     * @param sql  
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> List<T> getRefResultList(String sql, List<Object> params, Class<T> cls )throws Exception {
    	return getRefResultList(sql, listToObject(params), cls);
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean
     * @param <T>
     * @param sql
     * @param params
     * @param mapper
     * @return
     * @throws SQLException
     */
    public <T> T getSingleBean(String sql, Object[] params, RowMapper<T> mapper) throws SQLException {
    	T resultObject = null;
        ResultSet rs = querySql(conn, sql, params);
        if (rs.next()) {  
        	resultObject = mapper.mapRow(rs);  
        }  
        release(rs);
        return resultObject;  
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean
     * @param <T>
     * @param sql
     * @param params
     * @param mapper
     * @return
     * @throws SQLException
     */
    public <T> T getSingleBean(String sql, List<Object> params, RowMapper<T> mapper) throws SQLException {
    	T resultObject = null;
        ResultSet rs = querySql(conn, sql, listToObject(params));
        if (rs.next()) {  
        	resultObject = mapper.mapRow(rs);  
        }  
        release(rs);
        return resultObject;  
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean
     * @param sql
     * @param mapper
     * @return
     * @throws SQLException
     */
    public <T> T getSingleBean(String sql, RowMapper<T> mapper) throws SQLException {
    	T resultObject = null;
        ResultSet rs = querySql(conn, sql, null);
        if (rs.next()) {  
        	resultObject = mapper.mapRow(rs);  
        }
        release(rs);
        return resultObject;  
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean,放到List中
     * @param sql
     * @param params
     * @param mapper
     * @return
     * @throws SQLException
     */
    public List<?> getBeanList(String sql, Object[] params, RowMapper<?> mapper) throws SQLException {  
        ResultSet rs = querySql(conn, sql, params);
        List<Object> list = new ArrayList<Object>();  
        while (rs.next()) {  
            list.add(mapper.mapRow(rs));  
        }  
        release(rs);
        return list;  
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean,放到List中
     * @param sql
     * @param params
     * @param mapper
     * @return
     * @throws SQLException
     */
    public List<?> getBeanList(String sql, List<Object> params, RowMapper<?> mapper) throws SQLException {  
        ResultSet rs = querySql(conn, sql, listToObject(params));
        List<Object> list = new ArrayList<Object>();  
        while (rs.next()) {  
            list.add(mapper.mapRow(rs));  
        }  
        release(rs);
        return list;  
    }
    
    /**
     * RowMapper.mapRow(ResultSet)操作结果集返回bean,放到List中
     * @param sql
     * @param mapper
     * @return
     * @throws SQLException
     */
    public List<?> getBeanList(String sql, RowMapper<?> mapper) throws SQLException {  
        ResultSet rs = querySql(conn, sql, null);
        List<Object> list = new ArrayList<Object>();  
        while (rs.next()) {  
            list.add(mapper.mapRow(rs));  
        }  
        release(rs);
        return list;  
    }
    
    /** 
     * 增加、删除、改 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public boolean excute(String sql, Object[] params)throws SQLException{  
        boolean flag = false;  
        int result = updateSql(conn, sql, params);
        flag = result > 0 ? true : false;
        return flag;
    }
    
    /** 
     * 增加、删除、改 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public boolean excute(String sql, List<Object> params)throws SQLException{  
        boolean flag = false;  
        int result = updateSql(conn, sql, listToObject(params));
        flag = result > 0 ? true : false;
        return flag;
    }
    
    /** 
     * 增加、删除、改 
     * @param sql 
     * @return 
     * @throws SQLException 
     */  
    public boolean excute(String sql)throws SQLException{  
        boolean flag = false;  
        int result = updateSql(conn, sql, null);
        flag = result > 0 ? true : false;
        return flag;
    }
    
    /**
     * 批量执行excute,100句执行一次
     * @param sql
     * @param params
     * @return result 执行的sql数量
     * @throws SQLException
     */
    public int executeBatch(String sql, List<Object[]> params) throws SQLException {  
        int result = 0;PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                Object[] param = params.get(i);
                for (int j = 0; j < param.length; j++)
                    stmt.setObject(j + 1, param[j]);
                stmt.addBatch();
                if (i % 100 == 0) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }
            }
            stmt.executeBatch();
            conn.commit();
            result = params.size();
        } catch (Exception e) {
            conn.rollback();
            LogL.getInstance().getLog().error("批量执行sql出错", e);
            throw new SQLException("批量执行sql出错",e);
        } finally {  
        	release(stmt);
        }  
        return result;  
    }
    
    private Map<String, Object> getMapFromResultSet(ResultSet rs) throws SQLException{
    	ResultSetMetaData metaData = rs.getMetaData();  
        int col_len = metaData.getColumnCount();  
        Map<String, Object> map = new HashMap<String, Object>(col_len);
    	if(rs.next()){  
            for(int i=0; i<col_len; i++ ){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
        }
    	return map;
    }
    
    private List<Map<String, Object>> getMapListFromResultSet(ResultSet rs) throws SQLException{
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	ResultSetMetaData metaData = rs.getMetaData();  
        int cols_len = metaData.getColumnCount();  
        while(rs.next()){  
            Map<String, Object> map = new HashMap<String, Object>(cols_len);  
            for(int i=0; i<cols_len; i++){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = rs.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
            list.add(map);  
        }
    	return list;
    }
    
    
    
    /**
     * 执行QuerySql,返回ResultSet,打印sql和执行时间
     * @throws SQLException 
     */
    private ResultSet querySql(Connection conn, String sql, Object[] params) throws SQLException{
    	PreparedStatement stmt = createPreparedStatement(conn, sql, params);
    	long start = System.currentTimeMillis();
    	ResultSet rs = executeQuery(stmt);
    	long sqlTime = System.currentTimeMillis() - start ;
    	LogL.getInstance().getLog("JdbcL").debug(" [SQL] [QUERY] [ TIME = " + sqlTime + " ]{ [ " + sql + " ] , [ " + fillSql(sql,params) + " ] }");
    	release(stmt);
    	return rs ;
    }
    
    /**
     * 执行UpdateSql,返回int,打印sql和执行时间
     * @return 
     * @throws SQLException 
     */
    private int updateSql(Connection conn, String sql, Object[] params) throws SQLException{
    	PreparedStatement stmt = createPreparedStatement(conn, sql, params);
    	long start = System.currentTimeMillis();
    	int updateColumns = executeUpdate(stmt);
    	long sqlTime = System.currentTimeMillis() - start ;
    	LogL.getInstance().getLog("JdbcL").debug(" [SQL] [UPDATE] [ TIME = " + sqlTime + " ]{ [ " + sql + " ] , [ " + fillSql(sql,params)+" ] }");
    	release(stmt);
    	return updateColumns;
    }
    
    private ResultSet executeQuery(PreparedStatement stmt) throws SQLException{
    	ResultSet rs = stmt.executeQuery();
    	return rs;
    }
    
    private int executeUpdate(PreparedStatement stmt) throws SQLException{
    	int updateColumns = stmt.executeUpdate();
    	return updateColumns;
    }
    
    /** 
     * 初始化预定义PreparedStatement,使用Object[] params
     */ 
    private PreparedStatement createPreparedStatement(Connection conn, String sql, Object[] params) throws SQLException {  
        PreparedStatement stmt = conn.prepareStatement(sql);  
        if(params != null){
        	for (int i = 0; i < params.length; i++)
                stmt.setObject(i + 1, params[i]);  
        }
        return stmt;  
    }

    private String fillSql(String sql, Object[] params){
    	StringBuilder sSql = new StringBuilder(sql);
    	if(params != null){
    		for(int i = 0 ; i < params.length ; i++){
    			int begin = sSql.indexOf("?");
    			if (params[i] == null) {
    				sSql.replace(begin, begin+1, "''");
    			} else if ((params[i] instanceof Byte)) {
    				sSql.replace(begin, begin+1, "'"+((Byte)params[i]).intValue()+"'");
    			} else if ((params[i] instanceof String)) {
    				sSql.replace(begin, begin+1, "'"+(String)params[i]+"'");
    			} else if ((params[i] instanceof BigDecimal)) {
    				sSql.replace(begin, begin+1, ""+(BigDecimal)params[i]);
    			} else if ((params[i] instanceof Short)) {
    				sSql.replace(begin, begin+1, ""+((Short)params[i]).shortValue());
    			} else if ((params[i] instanceof Integer)) {
    				sSql.replace(begin, begin+1, ""+((Integer)params[i]).intValue());
    			} else if ((params[i] instanceof Long)) {
    				sSql.replace(begin, begin+1, ""+((Long)params[i]).longValue());
    			} else if ((params[i] instanceof Float)) {
    				sSql.replace(begin, begin+1, ""+((Float)params[i]).floatValue());
    			} else if ((params[i] instanceof Double)) {
    				sSql.replace(begin, begin+1, ""+((Double)params[i]).doubleValue());
    			} else if ((params[i] instanceof byte[])) {
    				sSql.replace(begin, begin+1, "'"+(byte[])params[i]+"'");
    			} else if ((params[i] instanceof java.sql.Date)) {
    				sSql.replace(begin, begin+1, "'"+(java.sql.Date)params[i]+"'");
    			} else if ((params[i] instanceof Time)) {
    				sSql.replace(begin, begin+1, "'"+(Time)params[i]+"'");
    			} else if ((params[i] instanceof Timestamp)) {
    				sSql.replace(begin, begin+1, "'"+(Timestamp)params[i]+"'");
    			} else if ((params[i] instanceof Boolean)) {
    				sSql.replace(begin, begin+1, ""+((Boolean)params[i]).booleanValue());
    			} else if ((params[i] instanceof InputStream)) {
    				sSql.replace(begin, begin+1, "'"+(InputStream)params[i]+"'");
    			} else if ((params[i] instanceof Blob)) {
    				sSql.replace(begin, begin+1, "'"+(Blob)params[i]+"'");
    			} else if ((params[i] instanceof Clob)) {
    				sSql.replace(begin, begin+1, "'"+(Clob)params[i]+"'");
    			} else if ((params[i] instanceof java.util.Date)){
    				sSql.replace(begin, begin+1, "'"+new Timestamp(((java.util.Date)params[i]).getTime())+"'");
    			} else if ((params[i] instanceof BigInteger)) {
    				sSql.replace(begin, begin+1, params[i].toString());
    			} else {
    				sSql.replace(begin, begin+1, "'"+params[i].toString()+"'");
    			}
    		}
    	}
		return sSql.toString();	
    }
    
    private Object[] listToObject(List<Object> list){
    	if(list != null && !list.isEmpty()){
    		Object[] oo = new Object[list.size()];
    		for (int i = 0; i < list.size(); i++)  
    			oo[i] = list.get(i);
    		return oo;
    	}
		return null;
    }

    public interface RowMapper<T> {
    	public abstract T mapRow(ResultSet rs) throws SQLException; 
    }
    
}

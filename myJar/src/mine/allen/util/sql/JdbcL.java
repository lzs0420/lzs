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
 * ��ҪJdbc������̳�,Jdbc������ֻ��Ҫʵ���������ݳ�<br>
 * �����������jdbc�ľ������ʵ��
 * @author Allen
 * @version 1.0 For All Projects 
 */
abstract class JdbcL {
	/**�Ƿ��Զ��ύ*/
	private static final boolean AUTO_COMMIT = true;
	/**���ݳ�*/
    protected DataSource dataSource;
	/**����*/
    private Connection conn ;
    
    public abstract void setDataSource(DataSource dataSource);
    
    /**
     * ȡ�����ݿ�����,Ĭ���Զ��ύ
     * @return Connection
     * @throws SQLException 
     */
    public void getConnection() throws SQLException {  
        getConnection(AUTO_COMMIT);  
    }
    
	/**
     * ȡ�����ݿ�����
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
     * �ͷ����ݿ�����
	 * @throws SQLException 
     */  
    public void commit() throws SQLException{  
        conn.commit();
    }
	
	/** 
     * �ͷ����ݿ�����
	 * @throws SQLException 
     */  
    public void release(ResultSet resultSet) throws SQLException{  
        if(resultSet != null){  
            resultSet.close();
        }
    } 
    
    /** 
     * �ͷ����ݿ�ִ��SQL
     * @throws SQLException 
     */  
    public void release(PreparedStatement pstmt) throws SQLException{  
        if(pstmt  != null){  
            pstmt.close();  
        }
    }
    
    /** 
     * �ͷ����ݿ����� 
     * @throws SQLException 
     */  
    public void release(Connection connection) throws SQLException{  
        if(connection != null){  
            connection.close();   
        }
    }
    
    /** 
     * �ͷ����ݿ����� 
     * @throws SQLException 
     */  
    public void releaseConnection() throws SQLException{  
        if(conn != null){  
        	conn.close();
        }
    }
    
    /** 
     * �ͷ����ݿ����� 
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
     * ����ResultSet
     * @param sql
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql) throws SQLException {  
        return querySql(conn, sql, null);
    }
    
    /**
     * ����ResultSet
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql, Object[] params) throws SQLException {  
        return querySql(conn, sql, params);
    }
    
    /**
     * ����ResultSet
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public ResultSet getResultSet(String sql, List<Object> params) throws SQLException {  
        return querySql(conn, sql, listToObject(params));
    }
    
    /** 
     * ��ѯ������¼ 
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
     * ��ѯ������¼ 
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
     * ��ѯ������¼ 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public Map<String, Object> getSingleResult(String sql, List<Object> params) throws SQLException{
    	return getSingleResult(sql, listToObject(params));
    }
    
    /**��ѯ������¼ 
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
    
    /**��ѯ������¼ 
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
    
    /**��ѯ������¼ 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> getResultList(String sql, List<Object> params) throws SQLException{
    	return getResultList(sql, params); 
    }
    
    /**ͨ��������Ʋ�ѯ������¼ 
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
                //ͨ��������ƴ���һ��ʵ��  
                resultObject = cls.newInstance();  
                for(int i = 0; i<cols_len; i++){
                    String cols_name = metaData.getColumnName(i+1);  
                    Object cols_value = resultSet.getObject(cols_name);  
                    if(cols_value == null){  
                        cols_value = "";  
                    }  
                    Field field = cls.getDeclaredField(cols_name);  
                    field.setAccessible(true); //��javabean�ķ���Ȩ��  
                    field.set(resultObject, cols_value);  
                }  
            }
        }catch(IllegalAccessException | InstantiationException | NoSuchFieldException | SecurityException e){
        	LogL.getInstance().getLog().error("ͨ��������Ʋ�ѯ������¼����", e);
        	throw new SQLException("ͨ��������Ʋ�ѯ������¼ ����",e);
        }
        release(resultSet);
        return resultObject;
    }
    
    /**ͨ��������Ʋ�ѯ������¼ 
     * @param sql 
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> T getSingleRefResult(String sql, List<Object> params, Class<T> cls) throws SQLException{  
        return getSingleRefResult(sql, listToObject(params), cls);
    }
    
    /**ͨ��������Ʋ�ѯ������¼ 
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
	            //ͨ��������ƴ���һ��ʵ��  
	            T resultObject = cls.newInstance();  
	            for(int i = 0; i<cols_len; i++){  
	                String cols_name = metaData.getColumnName(i+1);  
	                Object cols_value = resultSet.getObject(cols_name);  
	                if(cols_value == null){  
	                    cols_value = "";  
	                }  
	                Field field = cls.getDeclaredField(cols_name);  
	                field.setAccessible(true); //��javabean�ķ���Ȩ��  
	                field.set(resultObject, cols_value);  
	            }  
	            list.add(resultObject);  
	        }
        }catch(IllegalAccessException | InstantiationException | NoSuchFieldException | SecurityException e){
        	LogL.getInstance().getLog().error("ͨ��������Ʋ�ѯ������¼ ����", e);
        	throw new SQLException("ͨ��������Ʋ�ѯ������¼ ����",e);
        }
        release(resultSet);
        return list;  
    }
    
    /**ͨ��������Ʋ�ѯ������¼ 
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
     * RowMapper.mapRow(ResultSet)�������������bean
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
     * RowMapper.mapRow(ResultSet)�������������bean
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
     * RowMapper.mapRow(ResultSet)�������������bean
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
     * RowMapper.mapRow(ResultSet)�������������bean,�ŵ�List��
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
     * RowMapper.mapRow(ResultSet)�������������bean,�ŵ�List��
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
     * RowMapper.mapRow(ResultSet)�������������bean,�ŵ�List��
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
     * ���ӡ�ɾ������ 
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
     * ���ӡ�ɾ������ 
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
     * ���ӡ�ɾ������ 
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
     * ����ִ��excute,100��ִ��һ��
     * @param sql
     * @param params
     * @return result ִ�е�sql����
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
            LogL.getInstance().getLog().error("����ִ��sql����", e);
            throw new SQLException("����ִ��sql����",e);
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
     * ִ��QuerySql,����ResultSet,��ӡsql��ִ��ʱ��
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
     * ִ��UpdateSql,����int,��ӡsql��ִ��ʱ��
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
     * ��ʼ��Ԥ����PreparedStatement,ʹ��Object[] params
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

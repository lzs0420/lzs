package mine.allen.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class contextDB {
	/**
	 * ���ݿ����Ӷ���
	 */
	private Connection connection;  
	/**
	 * ���ݿ�ִ��SQL����
	 */
    private PreparedStatement pstmt;
    /**
	 * ���ݿ���������
	 */
    private ResultSet resultSet;  
	
    /**
     * ȡ�����ݿ�����
     * ͨ��context.xml������
     * @return Connection
     */
	public Connection getConnection(){
		try {      
	        //��ʼ�����������ռ�
	        Context ctx = new InitialContext();  
	        //����java:/comp/envΪ�̶�·��   
	        Context envContext = (Context)ctx.lookup("java:/comp/env"); 
	        //����jdbc/mysqldsΪ����Դ��JNDI�󶨵�����
	        DataSource ds = (DataSource)envContext.lookup("jdbc/mysql"); 
	        
	        connection = ds.getConnection();   
    
	    } catch (NamingException | SQLException e) {     
	        e.printStackTrace();     
	    }  
		return connection;
	}
	
	/** 
     * ���ӡ�ɾ������ 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public boolean updateByPreparedStatement(String sql, List<Object>params)throws SQLException{  
        boolean flag = false;  
        int result = -1;  
        pstmt = connection.prepareStatement(sql);  
        int index = 1;  
        if(params != null && !params.isEmpty()){  
            for(int i=0; i<params.size(); i++){  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        result = pstmt.executeUpdate();  
        flag = result > 0 ? true : false;  
        return flag;  
    }  
  
    /** 
     * ��ѯ������¼ 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public Map<String, Object> findSimpleResult(String sql, List<Object> params) throws SQLException{  
        Map<String, Object> map = new HashMap<String, Object>();  
        int index  = 1;  
        pstmt = connection.prepareStatement(sql);  
        if(params != null && !params.isEmpty()){  
            for(int i=0; i<params.size(); i++){  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        resultSet = pstmt.executeQuery();//���ز�ѯ���  
        ResultSetMetaData metaData = resultSet.getMetaData();  
        int col_len = metaData.getColumnCount();  
        if(resultSet.next()){  
            for(int i=0; i<col_len; i++ ){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = resultSet.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
        }  
        return map;  
    }  
  
    /**��ѯ������¼ 
     * @param sql 
     * @param params 
     * @return 
     * @throws SQLException 
     */  
    public List<Map<String, Object>> findModeResult(String sql, List<Object> params) throws SQLException{  
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
        int index = 1;  
        pstmt = connection.prepareStatement(sql);  
        if(params != null && !params.isEmpty()){  
            for(int i = 0; i<params.size(); i++){  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        resultSet = pstmt.executeQuery();  
        ResultSetMetaData metaData = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();  
        while(resultSet.next()){  
            Map<String, Object> map = new HashMap<String, Object>();  
            for(int i=0; i<cols_len; i++){  
                String cols_name = metaData.getColumnName(i+1);  
                Object cols_value = resultSet.getObject(cols_name);  
                if(cols_value == null){  
                    cols_value = "";  
                }  
                map.put(cols_name, cols_value);  
            }  
            list.add(map);  
        }  
  
        return list;  
    }  
  
    /**ͨ��������Ʋ�ѯ������¼ 
     * @param sql 
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> T findSimpleRefResult(String sql, List<Object> params,  
            Class<T> cls )throws Exception{  
        T resultObject = null;  
        int index = 1;  
        pstmt = connection.prepareStatement(sql);  
        if(params != null && !params.isEmpty()){  
            for(int i = 0; i<params.size(); i++){  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        resultSet = pstmt.executeQuery();  
        ResultSetMetaData metaData  = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();  
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
        return resultObject;  
  
    }  
  
    /**ͨ��������Ʋ�ѯ������¼ 
     * @param sql  
     * @param params 
     * @param cls 
     * @return 
     * @throws Exception 
     */  
    public <T> List<T> findMoreRefResult(String sql, List<Object> params,  
            Class<T> cls )throws Exception {  
        List<T> list = new ArrayList<T>();  
        int index = 1;  
        pstmt = connection.prepareStatement(sql);  
        if(params != null && !params.isEmpty()){  
            for(int i = 0; i<params.size(); i++){  
                pstmt.setObject(index++, params.get(i));  
            }  
        }  
        resultSet = pstmt.executeQuery();  
        ResultSetMetaData metaData  = resultSet.getMetaData();  
        int cols_len = metaData.getColumnCount();  
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
        return list;  
    }  
  
    /** 
     * �ͷ����ݿ�����
     */  
    public void releaseResultSet(){  
        if(resultSet != null){  
            try{  
                resultSet.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }
    } 
    
    /** 
     * �ͷ����ݿ�ִ��SQL
     */  
    public void releasePstmt(){  
        if(pstmt != null){  
            try{  
            	pstmt.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }
    }
    
    /** 
     * �ͷ����ݿ����� 
     */  
    public void releaseConn(){  
        if(resultSet != null){  
            try{  
                resultSet.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }
        if(pstmt != null){  
            try{  
            	pstmt.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }
        if(connection != null){  
            try{  
            	connection.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }
    }  
}

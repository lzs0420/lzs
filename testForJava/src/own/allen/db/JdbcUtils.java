package own.allen.db;
import java.io.InputStream;
import java.lang.reflect.Field;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.ResultSetMetaData;  
import java.sql.SQLException;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  

import java.util.Properties;

import own.allen.bean.UserInfo;  
  
  
public class JdbcUtils {  
    //���ݿ��û���  
    private static String USERNAME ;  
    //���ݿ�����  
    private static String PASSWORD ;  
    //������Ϣ   
    private static String DRIVER ;  
    //���ݿ��ַ  
    private static String URL ;  
    private Connection connection;  
    private PreparedStatement pstmt;  
    private ResultSet resultSet;  
    
    static{
    	loanConfig();
    }
    
   /** 
    * ��ȡjdbc����
    * @return 
    */  
    private static void loanConfig() {
    	try {
    		InputStream inStream = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
    		Properties prop = new Properties();
    		prop.load(inStream);
    		USERNAME = prop.getProperty("jdbc.mysql.username");
    		PASSWORD = prop.getProperty("jdbc.mysql.password");
    		DRIVER = prop.getProperty("jdbc.mysql.driver");
    		URL = prop.getProperty("jdbc.mysql.url");
    		System.out.println("===================��ȡ�����ļ��ɹ���======================");  
		} catch (Exception e) {
			throw new RuntimeException("��ȡ�����ļ��쳣",e);
		}
	}
    
    /** 
     * ��ȡjdbc����
     * @return 
     */  
     private static void loanConfig(String driver) {
     	try {
     		InputStream inStream = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
     		Properties prop = new Properties();
     		prop.load(inStream);
     		USERNAME = prop.getProperty("jdbc."+driver+".username");
     		PASSWORD = prop.getProperty("jdbc."+driver+".password");
     		DRIVER = prop.getProperty("jdbc."+driver+".driver");
     		URL = prop.getProperty("jdbc."+driver+".url");
     		System.out.println("===================��ȡ�����ļ��ɹ���======================");  
 		} catch (Exception e) {
 			throw new RuntimeException("��ȡ�����ļ��쳣",e);
 		}
 	}

    /** 
     * �������ʵ�� 
     * @return 
     */  
    public JdbcUtils() {  
        try{  
            Class.forName(DRIVER);  
            System.out.println("===================�������ӳɹ���======================");  
        }catch(Exception e){  
        	e.printStackTrace();  
        }  
    }  

	/** 
     * �������ʵ�� 
     * @return 
     */  
    public JdbcUtils(String driver) {  
        try{  
        	loanConfig(driver);
            Class.forName(DRIVER);  
            System.out.println("===================�������ӳɹ���======================");  
        }catch(Exception e){  
        	e.printStackTrace();  
        }  
    } 
    
    /** 
     * ������ݿ������ 
     * @return 
     */  
    public Connection getConnection(){  
        try {  
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  
            System.out.println("===================���ݿ����ӳɹ���======================");  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return connection;  
    }  
  
    /** 
     * ������ݿ������ 
     * @return 
     */  
    public Connection getConnection(String URL, String USERNAME, String PASSWORD){  
        try {  
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  
            System.out.println("===================���ݿ����ӳɹ���======================");  
        } catch (SQLException e) {  
        	try {
				connection = DriverManager.getConnection(JdbcUtils.URL, JdbcUtils.USERNAME, JdbcUtils.PASSWORD);
				System.out.println("===============ʹ��Ĭ���ʺ������������ݿ����ӳɹ���======================"); 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}  
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
    public void releaseConn(){  
        if(resultSet != null){  
            try{  
                resultSet.close();  
            }catch(SQLException e){  
                e.printStackTrace();  
            }  
        }  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) throws SQLException {  
        JdbcUtils jdbcUtils = new JdbcUtils();  
        jdbcUtils.getConnection();  
        String sql = null;
        List<Object> params = null;
  
        /*******************��*********************/  
        /*sql = "insert into userinfo (username, pswd) values (?, ?), (?, ?), (?, ?)"; 
        params = new ArrayList<Object>(); 
        params.add("С��"); 
        params.add("123xiaoming"); 
        params.add("����"); 
        params.add("zhangsan"); 
        params.add("����"); 
        params.add("lisi000"); 
        try { 
            boolean flag = jdbcUtils.updateByPreparedStatement(sql, params); 
            System.out.println(flag); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }  */
  
  
        /*******************ɾ*********************/  
        //ɾ������Ϊ�����ļ�¼  
        /*      String sql = "delete from userinfo where username = ?"; 
        List<Object> params = new ArrayList<Object>(); 
        params.add("С��"); 
        boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);*/  
  
        /*******************��*********************/  
        //������Ϊ���ĵ��������  
        /*      String sql = "update userinfo set pswd = ? where username = ? "; 
        List<Object> params = new ArrayList<Object>(); 
        params.add("lisi88888"); 
        params.add("����"); 
        boolean flag = jdbcUtils.updateByPreparedStatement(sql, params); 
        System.out.println(flag);*/  
  
        /*******************��*********************/  
        //�����÷����ѯ�����¼  
        /*      String sql2 = "select * from userinfo "; 
        List<Map<String, Object>> list = jdbcUtils.findModeResult(sql2, null); 
        System.out.println(list);*/  
  
        String sql2 = "select * from userinfo "; 
        Map<String, Object> list = jdbcUtils.findSimpleResult(sql2, null); 
        System.out.println(list);
        
        //���÷����ѯ ������¼  
        sql = "select * from userinfo where username = ? ";  
        params = new ArrayList<Object>();  
        params.add("����");  
        UserInfo userInfo;  
        try {  
            userInfo = jdbcUtils.findSimpleRefResult(sql, params, UserInfo.class);  
            System.out.print(userInfo);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
  
    }  
  
}  

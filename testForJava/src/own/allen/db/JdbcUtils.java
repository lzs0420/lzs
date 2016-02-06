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
    //数据库用户名  
    private static String USERNAME ;  
    //数据库密码  
    private static String PASSWORD ;  
    //驱动信息   
    private static String DRIVER ;  
    //数据库地址  
    private static String URL ;  
    private Connection connection;  
    private PreparedStatement pstmt;  
    private ResultSet resultSet;  
    
    static{
    	loanConfig();
    }
    
   /** 
    * 获取jdbc配置
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
    		System.out.println("===================读取配置文件成功！======================");  
		} catch (Exception e) {
			throw new RuntimeException("读取配置文件异常",e);
		}
	}
    
    /** 
     * 获取jdbc配置
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
     		System.out.println("===================读取配置文件成功！======================");  
 		} catch (Exception e) {
 			throw new RuntimeException("读取配置文件异常",e);
 		}
 	}

    /** 
     * 获得驱动实例 
     * @return 
     */  
    public JdbcUtils() {  
        try{  
            Class.forName(DRIVER);  
            System.out.println("===================驱动连接成功！======================");  
        }catch(Exception e){  
        	e.printStackTrace();  
        }  
    }  

	/** 
     * 获得驱动实例 
     * @return 
     */  
    public JdbcUtils(String driver) {  
        try{  
        	loanConfig(driver);
            Class.forName(DRIVER);  
            System.out.println("===================驱动连接成功！======================");  
        }catch(Exception e){  
        	e.printStackTrace();  
        }  
    } 
    
    /** 
     * 获得数据库的连接 
     * @return 
     */  
    public Connection getConnection(){  
        try {  
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  
            System.out.println("===================数据库连接成功！======================");  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return connection;  
    }  
  
    /** 
     * 获得数据库的连接 
     * @return 
     */  
    public Connection getConnection(String URL, String USERNAME, String PASSWORD){  
        try {  
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  
            System.out.println("===================数据库连接成功！======================");  
        } catch (SQLException e) {  
        	try {
				connection = DriverManager.getConnection(JdbcUtils.URL, JdbcUtils.USERNAME, JdbcUtils.PASSWORD);
				System.out.println("===============使用默认帐号密码链接数据库连接成功！======================"); 
			} catch (SQLException e1) {
				e1.printStackTrace();
			}  
        }  
        return connection;  
    }
      
    /** 
     * 增加、删除、改 
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
     * 查询单条记录 
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
        resultSet = pstmt.executeQuery();//返回查询结果  
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
  
    /**查询多条记录 
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
  
    /**通过反射机制查询单条记录 
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
        return resultObject;  
  
    }  
  
    /**通过反射机制查询多条记录 
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
        return list;  
    }  
  
    /** 
     * 释放数据库连接 
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
  
        /*******************增*********************/  
        /*sql = "insert into userinfo (username, pswd) values (?, ?), (?, ?), (?, ?)"; 
        params = new ArrayList<Object>(); 
        params.add("小明"); 
        params.add("123xiaoming"); 
        params.add("张三"); 
        params.add("zhangsan"); 
        params.add("李四"); 
        params.add("lisi000"); 
        try { 
            boolean flag = jdbcUtils.updateByPreparedStatement(sql, params); 
            System.out.println(flag); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }  */
  
  
        /*******************删*********************/  
        //删除名字为张三的记录  
        /*      String sql = "delete from userinfo where username = ?"; 
        List<Object> params = new ArrayList<Object>(); 
        params.add("小明"); 
        boolean flag = jdbcUtils.updateByPreparedStatement(sql, params);*/  
  
        /*******************改*********************/  
        //将名字为李四的密码改了  
        /*      String sql = "update userinfo set pswd = ? where username = ? "; 
        List<Object> params = new ArrayList<Object>(); 
        params.add("lisi88888"); 
        params.add("李四"); 
        boolean flag = jdbcUtils.updateByPreparedStatement(sql, params); 
        System.out.println(flag);*/  
  
        /*******************查*********************/  
        //不利用反射查询多个记录  
        /*      String sql2 = "select * from userinfo "; 
        List<Map<String, Object>> list = jdbcUtils.findModeResult(sql2, null); 
        System.out.println(list);*/  
  
        String sql2 = "select * from userinfo "; 
        Map<String, Object> list = jdbcUtils.findSimpleResult(sql2, null); 
        System.out.println(list);
        
        //利用反射查询 单条记录  
        sql = "select * from userinfo where username = ? ";  
        params = new ArrayList<Object>();  
        params.add("李四");  
        UserInfo userInfo;  
        try {  
            userInfo = jdbcUtils.findSimpleRefResult(sql, params, UserInfo.class);  
            System.out.print(userInfo);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
  
    }  
  
}  

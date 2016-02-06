package own.allen.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import own.allen.bean.User;

public class JdbcUtil implements JdbcOperation {

	private static final boolean AUTO_COMMIT = true;  
	  
    private DataSource dataSource;  
  
    public JdbcUtil() {  
  
    }  
  
    public JdbcUtil(DataSource dataSource) {  
        this.dataSource = dataSource;  
    }  
  
    public Connection getConnection() {  
        return getConnection(AUTO_COMMIT);  
    }  
  
    public Connection getConnection(boolean autoCommit) {  
        try {  
            Connection conn = dataSource.getConnection();  
            if (!autoCommit)  
                conn.setAutoCommit(autoCommit);  
            return conn;  
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    @Override  
    public int execute(String sql, Object[] params) throws SQLException {  
        Connection conn = getConnection(false);  
        PreparedStatement stmt = null;  
        int result = -1;  
        try {  
            stmt = createPreparedStatement(conn, sql, params);  
            result = stmt.executeUpdate();  
            conn.commit();  
        } catch (Exception e) {  
            conn.rollback();  
            e.printStackTrace();  
        } finally {  
            free(stmt);  
            free(conn);  
        }  
        return result;  
    }  
  
    @Override  
    public int execute(String sql) throws SQLException {  
        return execute(sql, new Object[] {});  
    }  
  
    @Override  
    public ResultSet queryForResultSet(String sql, Object[] params) throws SQLException {  
        Connection conn = getConnection();  
        PreparedStatement stmt = null;  
        try {  
            stmt = createPreparedStatement(conn, sql, params);  
            return stmt.executeQuery();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            free(stmt);  
            free(conn);  
        }  
        return null;  
    }  
  
    @Override  
    public ResultSet queryForResultSet(String sql) throws SQLException {  
        return queryForResultSet(sql, new Object[] {});  
    }  
  
    @Override  
    public int queryForInt(String sql, Object[] params) throws SQLException {  
        Connection conn = getConnection();  
        PreparedStatement stmt = null;  
        ResultSet rs = null;  
        try {  
            stmt = createPreparedStatement(conn, sql, params);  
            rs = createResultSet(stmt);  
            while (rs.next()) {  
                return rs.getInt(1);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            free(rs);  
            free(stmt);  
            free(conn);  
        }  
        return 0;  
    }  
  
    @Override  
    public int queryForInt(String sql) throws SQLException {  
        return queryForInt(sql, new Object[] {});  
    }  
  
    @Override  
    public List<?> queryForBean(String sql, Object[] params, RowMapper<?> mapper) throws SQLException {  
        Connection conn = getConnection();  
        PreparedStatement stmt = null;  
        ResultSet rs = null;  
        List<Object> list = null;  
        try {  
            stmt = createPreparedStatement(conn, sql, params);  
            rs = createResultSet(stmt);  
            list = new ArrayList<Object>();  
            while (rs.next()) {  
                list.add(mapper.mapRow(rs));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            free(rs);  
            free(stmt);  
            free(conn);  
        }  
        return list;  
    }  
  
    @Override  
    public List<?> queryForBean(String sql, RowMapper<?> mapper) throws SQLException {  
        return queryForBean(sql, new Object[] {}, mapper);  
    }  
  
    @Override  
    public List<Map<String, Object>> queryForMap(String sql, Object[] params) throws SQLException {  
        Connection conn = getConnection();  
        PreparedStatement stmt = null;  
        ResultSet rs = null;  
        try {  
            stmt = createPreparedStatement(conn, sql, params);  
            rs = createResultSet(stmt);  
  
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();  
            Map<String, Object> map = null;  
            ResultSetMetaData rsd = rs.getMetaData();  
            int columnCount = rsd.getColumnCount();  
  
            while (rs.next()) {  
                map = new HashMap<String, Object>(columnCount);  
                for (int i = 1; i < columnCount; i++) {  
                    map.put(rsd.getColumnName(i), rs.getObject(i));  
                }  
                list.add(map);  
            }  
  
            return list;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            free(rs);  
            free(stmt);  
            free(conn);  
        }  
        return null;  
    }  
  
    @Override  
    public List<Map<String, Object>> queryForMap(String sql) throws SQLException {  
        return queryForMap(sql, new Object[] {});  
    }  
  
    @Override  
    public int executeBatch(String sql, List<Object[]> params) throws SQLException {  
        int result = 0;  
        Connection conn = getConnection(false);  
        PreparedStatement stmt = null;  
        try {  
            stmt = conn.prepareStatement(sql);  
            for (int i = 0; i < params.size(); i++) {  
                Object[] param = params.get(i);  
                for (int j = 0; j < param.length; j++)  
                    stmt.setObject(j + 1, param[j]);  
                stmt.addBatch();  
                if (i % 1000 == 0) {  
                    stmt.executeBatch();  
                    stmt.clearBatch();  
                }  
            }  
            stmt.executeBatch();  
            conn.commit();  
            result = params.size();  
        } catch (Exception e) {  
            conn.rollback();  
            e.printStackTrace();  
        } finally {  
            free(stmt);  
            free(conn);  
        }  
        return result;  
    }  
  
    @Override  
    public int executeBatch(String sql) throws SQLException {  
        return executeBatch(sql, new ArrayList<Object[]>());  
    }  
  
    public DataSource getDataSource() {  
        return dataSource;  
    }  
  
    public void setDataSource(DataSource dataSource) {  
        this.dataSource = dataSource;  
    }  
  
    @Override  
    public void free(Connection x) {  
        if (x != null)  
            try {  
                x.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
    }  
  
    @Override  
    public void free(Statement x) {  
        if (x != null)  
            try {  
                x.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
    }  
  
    @Override  
    public void free(PreparedStatement x) {  
        if (x != null)  
            try {  
                x.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
    }  
  
    @Override  
    public void free(ResultSet x) {  
        if (x != null)  
            try {  
                x.close();  
            } catch (SQLException e) {  
                e.printStackTrace();  
            }  
    }  
  
    public PreparedStatement createPreparedStatement(Connection conn, String sql, Object[] params) throws SQLException {  
        PreparedStatement stmt = conn.prepareStatement(sql);  
        for (int i = 0; i < params.length; i++)  
            stmt.setObject(i + 1, params[i]);  
        return stmt;  
    }  
  
    public ResultSet createResultSet(PreparedStatement stmt) throws SQLException {  
        return stmt.executeQuery();  
    }  

    @SuppressWarnings("unchecked")
	public static void main(String[] args) throws SQLException {  
        SimpleDataSource dataSource = new SimpleDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test","root","123456");  
        JdbcUtil jdbc = new JdbcUtil(dataSource);  
        List<User> list = (List<User>) jdbc.queryForBean("select * from userinfo", new RowMapper<User>() {  
            User user = null;  
  
            @Override  
            public User mapRow(ResultSet rs) throws SQLException {  
                user = new User();  
                user.setId(rs.getInt("id"));  
                user.setUsername(rs.getString("username"));  
                return user;  
            }  
        });  
        for (User user : list) {  
            System.out.println(user.getId() + "---" + user.getUsername());  
        }  
    }  
    
}

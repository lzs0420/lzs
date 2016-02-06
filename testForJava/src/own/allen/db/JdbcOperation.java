package own.allen.db;

import java.sql.Connection;  
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;  
import java.sql.Statement;  
import java.util.List;  
import java.util.Map;  
import javax.sql.DataSource;  

public interface JdbcOperation {
	/** 
     * update或delete功能 
     *  
     * @param sql 
     * @param params 
     * @return 变更记录数 
     * @throws SQLException 
     */  
    public abstract int execute(String sql, Object[] params) throws SQLException;  
  
    /** 
     * update或delete功能 
     *  
     * @param sql 
     * @return 变更记录数 
     * @throws SQLException 
     */  
    public abstract int execute(String sql) throws SQLException;  
  
    /** 
     * 批处理update或delete功能 
     *  
     * @param sql 
     * @param params 
     * @return 变更记录数 
     * @throws SQLException 
     */  
    public abstract int executeBatch(String sql, List<Object[]> params) throws SQLException;  
  
    /** 
     * 批处理update或delete功能 
     *  
     * @param sql 
     * @param params 
     * @return 变更记录数 
     * @throws SQLException 
     */  
    public abstract int executeBatch(String sql) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @param params 
     * @return 原生ResultSet数据集合 
     * @throws SQLException 
     */  
    public abstract ResultSet queryForResultSet(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @return 原生ResultSet数据集合 
     * @throws SQLException 
     */  
    public abstract ResultSet queryForResultSet(String sql) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @param params 
     * @return List<?>数据集合 
     * @throws SQLException 
     */  
    public abstract List<?> queryForBean(String sql, Object[] params, RowMapper<?> mapper) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @param params 
     * @return List<?>数据集合 
     * @throws SQLException 
     */  
    public abstract List<?> queryForBean(String sql, RowMapper<?> mapper) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @param params 
     * @return List<Map<String, Object>>数据集合 
     * @throws SQLException 
     */  
    public abstract List<Map<String, Object>> queryForMap(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @param params 
     * @return List<Map<String, Object>>数据集合 
     * @throws SQLException 
     */  
    public abstract List<Map<String, Object>> queryForMap(String sql) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @return 统计单列记录数 
     * @throws SQLException 
     */  
    public abstract int queryForInt(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select功能 
     *  
     * @param sql 
     * @return 统计单列记录数 
     * @throws SQLException 
     */  
    public abstract int queryForInt(String sql) throws SQLException;  
  
    /** 
     * 释放Connection资源 
     *  
     * @param x 
     */  
    public abstract void free(Connection x);  
  
    /** 
     * 释放Statement资源 
     *  
     * @param x 
     */  
    public abstract void free(Statement x);  
  
    /** 
     * 释放PreparedStatement资源 
     *  
     * @param x 
     */  
    public abstract void free(PreparedStatement x);  
  
    /** 
     * 释放ResultSet资源 
     *  
     * @param x 
     */  
    public abstract void free(ResultSet x);  
  
    /** 
     * 设置数据源 
     *  
     * @param dataSource 
     */  
    public abstract void setDataSource(DataSource dataSource);  
  
    /** 
     * 获取数据库链接 
     *  
     * @return Connection 
     */  
    public abstract Connection getConnection();  
  
    /** 
     * 获取数据库链接 
     *  
     * @param autoCommit 
     * @return Connection 
     */  
    public Connection getConnection(boolean autoCommit);  
}

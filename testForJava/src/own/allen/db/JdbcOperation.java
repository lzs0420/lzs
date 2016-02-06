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
     * update��delete���� 
     *  
     * @param sql 
     * @param params 
     * @return �����¼�� 
     * @throws SQLException 
     */  
    public abstract int execute(String sql, Object[] params) throws SQLException;  
  
    /** 
     * update��delete���� 
     *  
     * @param sql 
     * @return �����¼�� 
     * @throws SQLException 
     */  
    public abstract int execute(String sql) throws SQLException;  
  
    /** 
     * ������update��delete���� 
     *  
     * @param sql 
     * @param params 
     * @return �����¼�� 
     * @throws SQLException 
     */  
    public abstract int executeBatch(String sql, List<Object[]> params) throws SQLException;  
  
    /** 
     * ������update��delete���� 
     *  
     * @param sql 
     * @param params 
     * @return �����¼�� 
     * @throws SQLException 
     */  
    public abstract int executeBatch(String sql) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @param params 
     * @return ԭ��ResultSet���ݼ��� 
     * @throws SQLException 
     */  
    public abstract ResultSet queryForResultSet(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @return ԭ��ResultSet���ݼ��� 
     * @throws SQLException 
     */  
    public abstract ResultSet queryForResultSet(String sql) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @param params 
     * @return List<?>���ݼ��� 
     * @throws SQLException 
     */  
    public abstract List<?> queryForBean(String sql, Object[] params, RowMapper<?> mapper) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @param params 
     * @return List<?>���ݼ��� 
     * @throws SQLException 
     */  
    public abstract List<?> queryForBean(String sql, RowMapper<?> mapper) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @param params 
     * @return List<Map<String, Object>>���ݼ��� 
     * @throws SQLException 
     */  
    public abstract List<Map<String, Object>> queryForMap(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @param params 
     * @return List<Map<String, Object>>���ݼ��� 
     * @throws SQLException 
     */  
    public abstract List<Map<String, Object>> queryForMap(String sql) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @return ͳ�Ƶ��м�¼�� 
     * @throws SQLException 
     */  
    public abstract int queryForInt(String sql, Object[] params) throws SQLException;  
  
    /** 
     * select���� 
     *  
     * @param sql 
     * @return ͳ�Ƶ��м�¼�� 
     * @throws SQLException 
     */  
    public abstract int queryForInt(String sql) throws SQLException;  
  
    /** 
     * �ͷ�Connection��Դ 
     *  
     * @param x 
     */  
    public abstract void free(Connection x);  
  
    /** 
     * �ͷ�Statement��Դ 
     *  
     * @param x 
     */  
    public abstract void free(Statement x);  
  
    /** 
     * �ͷ�PreparedStatement��Դ 
     *  
     * @param x 
     */  
    public abstract void free(PreparedStatement x);  
  
    /** 
     * �ͷ�ResultSet��Դ 
     *  
     * @param x 
     */  
    public abstract void free(ResultSet x);  
  
    /** 
     * ��������Դ 
     *  
     * @param dataSource 
     */  
    public abstract void setDataSource(DataSource dataSource);  
  
    /** 
     * ��ȡ���ݿ����� 
     *  
     * @return Connection 
     */  
    public abstract Connection getConnection();  
  
    /** 
     * ��ȡ���ݿ����� 
     *  
     * @param autoCommit 
     * @return Connection 
     */  
    public Connection getConnection(boolean autoCommit);  
}

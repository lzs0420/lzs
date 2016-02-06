package own.allen.db;

import java.io.PrintWriter;  
import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
import java.lang.reflect.Proxy;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;  
  
import java.util.logging.Logger;

import javax.sql.DataSource; 

/** 
 * �������ӳ�ʵ���� 
 *  
 *  
 */ 
public class SimpleDataSource implements DataSource {

	private int poolSize = 5;  
	  
    private LinkedList<Connection> pool = new LinkedList<Connection>();  
  
    public SimpleDataSource(String driver, String url, String name, String pwd) {  
        this(driver, url, name, pwd, 5);  
    }  
  
    public SimpleDataSource(String driver, String url) {  
        this(driver, url, "", "", 5);  
    }  
  
    public SimpleDataSource(String driver, String url, String name, String pwd, int poolSize) {  
        try {  
            Class.forName(driver);  
            this.poolSize = poolSize;  
            if (poolSize <= 0) {  
                throw new RuntimeException("��ʼ���ش�Сʧ��: " + poolSize);  
            }  
  
            for (int i = 0; i < poolSize; i++) {  
                Connection con = DriverManager.getConnection(url, name, pwd);  
                con = ConnectionProxy.getProxy(con, pool);// ��ȡ������Ķ���  
                pool.add(con);// ��ӱ�����Ķ���  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e.getMessage(), e);  
        }  
  
    }  
  
    /** ��ȡ�ش�С */  
    public int getPoolSize() {  
        return poolSize;  
  
    }  
  
    /** ��֧����־���� */  
    public PrintWriter getLogWriter() throws SQLException {  
        throw new RuntimeException("Unsupport Operation.");  
    }  
  
    public void setLogWriter(PrintWriter out) throws SQLException {  
        throw new RuntimeException("Unsupport operation.");  
    }  
  
    /** ��֧�ֳ�ʱ���� */  
    public void setLoginTimeout(int seconds) throws SQLException {  
        throw new RuntimeException("Unsupport operation.");  
    }  
  
    public int getLoginTimeout() throws SQLException {  
        return 0;  
    }  
  
    @SuppressWarnings("unchecked")  
    public <T> T unwrap(Class<T> iface) throws SQLException {  
        return (T) this;  
    }  
  
    public boolean isWrapperFor(Class<?> iface) throws SQLException {  
        return DataSource.class.equals(iface);  
    }  
  
    /** �ӳ���ȡһ�����Ӷ���,ʹ����ͬ�����̵߳��� */  
    public Connection getConnection() throws SQLException {  
        synchronized (pool) {  
            if (pool.size() == 0) {  
                try {  
                    pool.wait();  
                } catch (InterruptedException e) {  
                    throw new RuntimeException(e.getMessage(), e);  
                }  
                return getConnection();  
            } else {  
                return pool.removeFirst();  
            }  
        }  
    }  
  
    public Connection getConnection(String username, String password) throws SQLException {  
        throw new RuntimeException("��֧�ֽ����û���������Ĳ���");  
    }  
  
    /** ʵ�ֶ�Connection�Ķ�̬���� */  
    static class ConnectionProxy implements InvocationHandler {  
  
        private Object obj;  
        private LinkedList<Connection> pool;  
  
        private ConnectionProxy(Object obj, LinkedList<Connection> pool) {  
            this.obj = obj;  
            this.pool = pool;  
        }  
  
        public static Connection getProxy(Object o, LinkedList<Connection> pool) {  
            Object proxed = Proxy.newProxyInstance(o.getClass().getClassLoader(), new Class[] { Connection.class },  
                    new ConnectionProxy(o, pool));  
            return (Connection) proxed;  
        }  
  
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
            if (method.getName().equals("close")) {  
                synchronized (pool) {  
                    pool.add((Connection) proxy);  
                    pool.notify();  
                }  
                return null;  
            } else {  
                return method.invoke(obj, args);  
            }  
        }  
  
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}  

}

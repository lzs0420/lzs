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
 * 简易连接池实现类 
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
                throw new RuntimeException("初始化池大小失败: " + poolSize);  
            }  
  
            for (int i = 0; i < poolSize; i++) {  
                Connection con = DriverManager.getConnection(url, name, pwd);  
                con = ConnectionProxy.getProxy(con, pool);// 获取被代理的对象  
                pool.add(con);// 添加被代理的对象  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e.getMessage(), e);  
        }  
  
    }  
  
    /** 获取池大小 */  
    public int getPoolSize() {  
        return poolSize;  
  
    }  
  
    /** 不支持日志操作 */  
    public PrintWriter getLogWriter() throws SQLException {  
        throw new RuntimeException("Unsupport Operation.");  
    }  
  
    public void setLogWriter(PrintWriter out) throws SQLException {  
        throw new RuntimeException("Unsupport operation.");  
    }  
  
    /** 不支持超时操作 */  
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
  
    /** 从池中取一个连接对象,使用了同步和线程调度 */  
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
        throw new RuntimeException("不支持接收用户名和密码的操作");  
    }  
  
    /** 实现对Connection的动态代理 */  
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

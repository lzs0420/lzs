package mine.allen.util.sql;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;  
import java.lang.reflect.InvocationHandler;  
import java.lang.reflect.Method;  
import java.lang.reflect.Proxy;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.SQLException;  
import java.sql.SQLFeatureNotSupportedException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sql.DataSource;

import mine.allen.util.lang.LogL;
import mine.allen.util.lang.StringL;

/** 
 * 简易连接池实现类 
 * @author Allen
 * @version 1.0 For Projects 
 */ 
class SimpleDataSource implements DataSource {
	/** 连接池配置属性*/
	private DBbean dbBean;
	/**连接池活动状态*/
	private boolean isActive = false;
	
	/**记录活动连接开始的时间和状态*/
	private List<ConnectionRecord> Connections = new Vector<ConnectionRecord>();
	/** 将线程和连接绑定，保证事务能统一执行*/
	private static ThreadLocal<ConnectionRecord> threadLocal = new ThreadLocal<ConnectionRecord>();
	/**实例方法*/
    public SimpleDataSource(String config) throws SQLException{
    	this.dbBean = new DBbean(config);
		init();
		cheackPool();
    }

    /**初始化*/
 	private void init() throws SQLException {
 		try {
 			Class.forName(dbBean.getDriver());
 			for (int i = 0; i < dbBean.getInitConnections(); i++) {
 				Connection conn = newConnection();
 				// 获取被代理的对象  ,使用动态代理加工connection.close函数
 				conn = new ConnectionProxy(conn, dbBean,Connections, threadLocal)
 								.getProxy(conn, dbBean,Connections, threadLocal);
 				// 初始化最小连接数
 				if (conn != null) {
 					Connections.add(newConnectionRecord(conn));
 				}
 			}
 			isActive = true;
 		} catch (ClassNotFoundException |SQLException e) {
 			LogL.getInstance().getLog().error("初始化连接池失败", e);
        	throw new SQLException("初始化连接池失败",e);
 		} 
 	}
 	
 	/** 获得新连接*/
 	private synchronized Connection newConnection()
 			throws ClassNotFoundException, SQLException {
 		Connection conn = null;
 		if (dbBean != null) {
 			conn = DriverManager.getConnection(dbBean.getUrl(),
 					dbBean.getUserName(), dbBean.getPassword());
 		}
 		return conn;
 	}
 	
 	/** 从池中取一个连接对象,使用了同步和线程调度 
 	 * @throws SQLException */
 	public synchronized Connection getConnection() throws SQLException {
 		Connection conn = null;
 		try {
 			// 判断是否超过最大连接数限制
 			if(Connections.size() < this.dbBean.getMaxActiveConnections()){
 				//是否取得当前连接
 				if(this.dbBean.isCurrentConnection()){
 					conn = getCurrentConnecton();
 				}else{
 					if (getFreeConnectionRecordNum(Connections) > 0) {
 						ConnectionRecord cr = getConnectionRecord(Connections);
 	 					conn = cr.getConn();
 	 					setNewTime(cr);
 	 					if (conn != null) {
 	 						threadLocal.set(cr);
 	 					}
 	 				} else {
 	 					for (int i = 0; i < this.dbBean.getIncrementConnections(); i++) {
 	 						conn = newConnection();
 	 						if (isValid(conn)) {
 	 							Connections.add(newConnectionRecord(conn));
 	 			 			}
						}
 	 				}
 				}
 			}else{
 				// 继续获得连接,直到从新获得连接
 				wait(this.dbBean.getConnTimeOut());
 				conn = getConnection();
 			}
 		} catch (SQLException|ClassNotFoundException|InterruptedException e) {
 			LogL.getInstance().getLog().error("从池中取一个连接对象失败", e);
        	throw new SQLException("从池中取一个连接对象失败",e);
 		}
 		return conn;
 	}
 	
 	/** 获得当前连接*/
 	public Connection getCurrentConnecton() throws SQLException{
 		// 默认线程里面取
 		Connection conn = threadLocal.get().getConn();
 		if(!isValid(conn)){
 			dbBean.setCurrentConnection(false);
 	 		conn = getConnection();
 	 		dbBean.setCurrentConnection(true);
 	 	}
 		setNewTime(threadLocal.get());
 		return conn;
 	}
 	
 	/** 判断连接是否可用*/
 	private boolean isValid(Connection conn) throws SQLException {
 		try {
 			if (conn == null || conn.isClosed()) {
 				return false;
 			}
 		} catch (SQLException e) {
 			LogL.getInstance().getLog().error("判断连接是否可用失败", e);
        	throw new SQLException("判断连接是否可用失败",e);
 		}
 		return true;
 	}

 	/** 销毁连接池*/
 	public synchronized void destroy() throws SQLException {
 		isActive = false;
 		try {
 			for (ConnectionRecord cr : Connections) {
 				if (isValid(cr.getConn())) {
 					cr.getConn().close();
 				}
 			}
 			Connections.removeAll(Connections);
 		}catch (SQLException e) {
 			LogL.getInstance().getLog().error("销毁连接池失败", e);
 	        throw new SQLException("销毁连接池失败",e);
 		}
 	}

 	/** 连接池状态*/
 	public boolean isActive() {
 		return isActive;
 	}
 	
 	/** 定时检查连接池情况*/
 	public void cheackPool() {
 		if(dbBean.isCheakPool()){
 			new Timer().schedule(new TimerTask() {
	 			@Override
	 			public void run() {
	 				try{
	 					// 1.对线程里面的连接状态
			 			// 2.连接池最小 最大连接数
			 			// 3.其他状态进行检查，因为这里还需要写几个线程管理的类，暂时就不添加了
			 			LogL.getInstance().getLog("JdbcL").debug("空线池连接数："+getFreeConnectionRecordNum(Connections)
			 					+" ## 活动连接数："+getActiveConnectionRecordNum(Connections)
			 					+" ## 总的连接数："+Connections.size());
			 			if(isActive()){
			 				for (ConnectionRecord  cr: Connections) {
				 				if(cr.getConnectTimeEnd().compareTo(StringL.getTodayNow()) < 0){
				 					cr.getConn().close();
				 					LogL.getInstance().getLog("JdbcL").debug("连接["+cr.getConn().toString()+"]开始时间:"+cr.getConnectTimeStart()
				 							+" 上次连接时间:"+cr.getLastConnectTime()
				 							+" 连接结束时间:"+cr.getConnectTimeEnd()
				 							+" 连接关闭时间:"+StringL.getTodayNow());
				 				}
					 		}
			 				
			 				if(getFreeConnectionRecordNum(Connections) < dbBean.getMinConnections()){
			 					for (int i = getFreeConnectionRecordNum(Connections); i < dbBean.getMinConnections(); i++) {
			 						Connection conn = newConnection();
		 	 						if (isValid(conn)) {
		 	 							Connections.add(newConnectionRecord(conn));
		 	 			 			}
								}
			 					LogL.getInstance().getLog("JdbcL").debug("空闲连接数["+getFreeConnectionRecordNum(Connections)
			 							+"]小于最小设定值["+dbBean.getMinConnections()+"],共增加["+
			 							(dbBean.getMinConnections()-getFreeConnectionRecordNum(Connections))+"]个空闲连接");
			 				}
			 			}else{
			 				LogL.getInstance().getLog("JdbcL").debug("连接池已清空,提高系统回收进程优先级");
			 				System.gc();
			 			}
			 			
	 				}catch(SQLException |ClassNotFoundException e){
	 					LogL.getInstance().getLog().error("定时检查连接池出错", e);
	 				}
		 			
 				}
 			},dbBean.getLazyCheck(),dbBean.getPeriodCheck());
 		}
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

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
  
    @Override
    public Connection getConnection(String username, String password) throws SQLException {  
        throw new RuntimeException("不支持接收用户名和密码的操作");  
    }
    /**创建新的连接记录类*/
    private ConnectionRecord newConnectionRecord(Connection conn){
    	ConnectionRecord cr = null;
    	String todaynow = StringL.getTodayNow();
    	String todayend = StringL.addDateFormat(todaynow, 0, (int)(this.dbBean.getConnectionTime()/1000));
    	cr = new ConnectionRecord(conn,todaynow,todayend,todaynow);
    	return cr;
    }
    /**使用已存在的连接时设置新的使用和结束时间*/
    private void setNewTime(ConnectionRecord cr){
		String todaynow = StringL.getTodayNow();
		cr.setLastConnectTime(todaynow);
		cr.setConnectTimeEnd(StringL.addDateFormat(todaynow, 0, (int)(this.dbBean.getConnectionTime()/1000)));
	}
    /**随机获取一个已存在的空闲连接,同时设为活动连接*/
    private ConnectionRecord getConnectionRecord(List<ConnectionRecord> vConnectionRecord){
    	for(;;){
    		int i = new Random().nextInt(vConnectionRecord.size());
    		ConnectionRecord cr = vConnectionRecord.get(i);
    		if(!cr.isActive()){
    			cr.setActive(true);
    			return cr;
    		}
    	}
    }
    /**随机把一个活动连接设为空闲连接*/
    private void addFreeConnectionRecord(List<ConnectionRecord> vConnectionRecord){
    	for(;;){
    		int i = new Random().nextInt(vConnectionRecord.size());
    		ConnectionRecord cr = vConnectionRecord.get(i);
    		if(cr.isActive()){
    			cr.setActive(false);
    			return ;
    		}
    	}
    }
    /**获取空闲连接数*/
    private int getFreeConnectionRecordNum(List<ConnectionRecord> vConnectionRecord){
    	int num = 0;
    	for(int i=0;i<vConnectionRecord.size();i++){
    		ConnectionRecord cr = vConnectionRecord.get(i);
    		if(!cr.isActive()){
    			num ++;
    		}
    	}
    	return num;
    }
    /**获取活动连接数*/
    private int getActiveConnectionRecordNum(List<ConnectionRecord> vConnectionRecord){
    	int num = 0;
    	for(int i=0;i<vConnectionRecord.size();i++){
    		ConnectionRecord cr = vConnectionRecord.get(i);
    		if(cr.isActive()){
    			num ++;
    		}
    	}
    	return num;
    }
  
    /**
     * 记录连接的各种状态
     */
    class ConnectionRecord{
    	private Connection conn;
    	private String connectTimeStart;
    	private String connectTimeEnd;
    	private String lastConnectTime;
    	private Boolean active;
    	
		public ConnectionRecord() {
			super();
		}
		public ConnectionRecord(Connection conn, String connectTimeStart,
				String connectTimeEnd, String lastConnectTime) {
			super();
			this.conn = conn;
			this.connectTimeStart = connectTimeStart;
			this.connectTimeEnd = connectTimeEnd;
			this.lastConnectTime = lastConnectTime;
			this.active = false;
		}
		public ConnectionRecord(Connection conn, String connectTimeStart,
				String connectTimeEnd, String lastConnectTime,boolean isActive) {
			super();
			this.conn = conn;
			this.connectTimeStart = connectTimeStart;
			this.connectTimeEnd = connectTimeEnd;
			this.lastConnectTime = lastConnectTime;
			this.active = isActive;
		}public Connection getConn() {
			return conn;
		}
		public void setConn(Connection conn) {
			this.conn = conn;
		}
		public String getConnectTimeStart() {
			return connectTimeStart;
		}
		public void setConnectTimeStart(String connectTimeStart) {
			this.connectTimeStart = connectTimeStart;
		}
		public String getConnectTimeEnd() {
			return connectTimeEnd;
		}
		public void setConnectTimeEnd(String connectTimeEnd) {
			this.connectTimeEnd = connectTimeEnd;
		}
		public String getLastConnectTime() {
			return lastConnectTime;
		}
		public void setLastConnectTime(String lastConnectTime) {
			this.lastConnectTime = lastConnectTime;
		}
		public Boolean isActive() {
			return active;
		}
		public void setActive(Boolean Active) {
			this.active = Active;
		}
    }
    
    /** 实现对Connection的动态代理 */  
    class ConnectionProxy implements InvocationHandler {  
  
        private Object obj;
        private DBbean dbBean;
        private List<ConnectionRecord> Connections ;
    	private ThreadLocal<ConnectionRecord> threadLocal ;
  
        private ConnectionProxy(Object obj, DBbean dbBean,List<ConnectionRecord> Connections, ThreadLocal<ConnectionRecord> threadLocal) {  
            this.obj = obj;
            this.dbBean = dbBean;
            this.Connections = Connections;
            this.threadLocal = threadLocal;
        }  
  
        public Connection getProxy(Object o, DBbean dbBean, List<ConnectionRecord> Connections ,ThreadLocal<ConnectionRecord> threadLocal) {  
            Object proxed = Proxy.newProxyInstance(o.getClass().getClassLoader(), new Class[] { Connection.class },  
                    new ConnectionProxy(o, dbBean, Connections ,threadLocal));  
            return (Connection) proxed;  
        }  
  
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
            if (method.getName().equals("close")&& getFreeConnectionRecordNum(Connections) < dbBean.getMaxConnections()
            		&&isActive()) {  
                synchronized (this) {
                	if (isValid((Connection) proxy)) {
                    		addFreeConnectionRecord(Connections);
                			threadLocal.remove();
                			// 唤醒所有正待等待的线程，去抢连接
                			notifyAll();
                    }
                    return null;
                }
            } else {  
                return method.invoke(obj, args);
            }  
        }  
  
    }
	
	/**
	 * 这是外部可以配置的连接池属性
	 * 可以允许外部配置，拥有默认值
	 * @author allen
	 */
	class DBbean {
		// 连接池属性
		private String driver;
		private String url;
		private String userName;
		private String password;
		// 连接池名字
		private String poolName;
		private int minConnections = 1; // 空闲池，最小连接数
		private int maxConnections = 30; // 空闲池，最大连接数
		
		private int incrementConnections = 10; //每次增加的连接数
		
		private int initConnections = 5;// 初始化连接数
		
		private long connTimeOut = 1000;// 重复获得连接的频率
		
		private int maxActiveConnections = 100;// 最大允许的连接数，和数据库对应
		
		private long connectionTime = 1000*60*20;// 连接超时时间，默认20分钟
		
		private boolean isCurrentConnection = false; // 是否获得当前连接，默认不取
		
		private boolean isCheakPool = true; // 是否定时检查连接池
		private long lazyCheck = 1000*60*60;// 延迟多少时间后开始 检查
		private long periodCheck = 1000*60*60;// 检查频率
		
		public DBbean(String config){
			try {
	    		InputStream inStream = new FileInputStream(config);
	    		Properties prop = new Properties();
	    		prop.load(inStream);
	    		if(!prop.isEmpty()){
	    			Set<Entry<Object, Object>> es = prop.entrySet();
	    			Iterator<Entry<Object, Object>> ir = es.iterator();
	    			while(ir.hasNext()){
	    				Entry<Object, Object> entry = ir.next();
	    				Method[] methods = DBbean.class.getMethods();
	    				String key = StringL.toString(entry.getKey());
	    				for (Method method : methods) {
							if(("set"+key.substring(5)).equalsIgnoreCase(method.getName())){
								String value = StringL.toString(entry.getValue());
								if(key.substring(5).equalsIgnoreCase("isCurrentConnection")||
										key.substring(5).equalsIgnoreCase("isCheakPool")){
									method.invoke(this, Boolean.valueOf(value));
								}else if("minConnections,maxConnections,incrementConnections,initConnections,maxActiveConnections"
										.contains(key.substring(5))){
									method.invoke(this, Integer.parseInt(value));
								}else if("connTimeOut,connectionTime,lazyCheck,periodCheck"
										.contains(key.substring(5))){
									method.invoke(this, Long.parseLong(value));
								}else{
									method.invoke(this, value);
								}
								break;
							}
						}
	    			}
	    		}
	    		LogL.getInstance().getLog().debug("===================读取配置文件成功！======================");
			} catch (Exception e) {
				LogL.getInstance().getLog().error("DBbean读取配置文件异常",e);
				throw new RuntimeException("读取配置文件异常",e);
			}
		}
		public DBbean(String driver, String url, String userName,
				String password, String poolName) {
			super();
			this.driver = driver;
			this.url = url;
			this.userName = userName;
			this.password = password;
			this.poolName = poolName;
		}
		public DBbean() {
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getUserName() {
			return userName;
		}
		public void setUserName(String userName) {
			this.userName = userName;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPoolName() {
			return poolName;
		}
		public void setPoolName(String poolName) {
			this.poolName = poolName;
		}
		public int getMinConnections() {
			return minConnections;
		}
		public void setMinConnections(int minConnections) {
			this.minConnections = minConnections;
		}
		public int getMaxConnections() {
			return maxConnections;
		}
		public void setMaxConnections(int maxConnections) {
			this.maxConnections = maxConnections;
		}
		public int getInitConnections() {
			return initConnections;
		}
		public void setInitConnections(int initConnections) {
			this.initConnections = initConnections;
		}

		public int getMaxActiveConnections() {
			return maxActiveConnections;
		}
		public void setMaxActiveConnections(int maxActiveConnections) {
			this.maxActiveConnections = maxActiveConnections;
		}
		public long getConnTimeOut() {
			return connTimeOut;
		}
		public void setConnTimeOut(long connTimeOut) {
			this.connTimeOut = connTimeOut;
		}
		public long getConnectionTime() {
			return connectionTime;
		}
		public void setConnectionTime(long connectionTime) {
			this.connectionTime = connectionTime;
		}
		public boolean isCurrentConnection() {
			return isCurrentConnection;
		}
		public void setCurrentConnection(boolean isCurrentConnection) {
			this.isCurrentConnection = isCurrentConnection;
		}
		public long getLazyCheck() {
			return lazyCheck;
		}
		public void setLazyCheck(long lazyCheck) {
			this.lazyCheck = lazyCheck;
		}
		public long getPeriodCheck() {
			return periodCheck;
		}
		public void setPeriodCheck(long periodCheck) {
			this.periodCheck = periodCheck;
		}
		public boolean isCheakPool() {
			return isCheakPool;
		}
		public void setCheakPool(boolean isCheakPool) {
			this.isCheakPool = isCheakPool;
		}
		public int getIncrementConnections() {
			return incrementConnections;
		}
		public void setIncrementConnections(int incrementConnections) {
			this.incrementConnections = incrementConnections;
		}
	}
}

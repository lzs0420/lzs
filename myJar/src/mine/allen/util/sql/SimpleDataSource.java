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
 * �������ӳ�ʵ���� 
 * @author Allen
 * @version 1.0 For Projects 
 */ 
class SimpleDataSource implements DataSource {
	/** ���ӳ���������*/
	private DBbean dbBean;
	/**���ӳػ״̬*/
	private boolean isActive = false;
	
	/**��¼����ӿ�ʼ��ʱ���״̬*/
	private List<ConnectionRecord> Connections = new Vector<ConnectionRecord>();
	/** ���̺߳����Ӱ󶨣���֤������ͳһִ��*/
	private static ThreadLocal<ConnectionRecord> threadLocal = new ThreadLocal<ConnectionRecord>();
	/**ʵ������*/
    public SimpleDataSource(String config) throws SQLException{
    	this.dbBean = new DBbean(config);
		init();
		cheackPool();
    }

    /**��ʼ��*/
 	private void init() throws SQLException {
 		try {
 			Class.forName(dbBean.getDriver());
 			for (int i = 0; i < dbBean.getInitConnections(); i++) {
 				Connection conn = newConnection();
 				// ��ȡ������Ķ���  ,ʹ�ö�̬����ӹ�connection.close����
 				conn = new ConnectionProxy(conn, dbBean,Connections, threadLocal)
 								.getProxy(conn, dbBean,Connections, threadLocal);
 				// ��ʼ����С������
 				if (conn != null) {
 					Connections.add(newConnectionRecord(conn));
 				}
 			}
 			isActive = true;
 		} catch (ClassNotFoundException |SQLException e) {
 			LogL.getInstance().getLog().error("��ʼ�����ӳ�ʧ��", e);
        	throw new SQLException("��ʼ�����ӳ�ʧ��",e);
 		} 
 	}
 	
 	/** ���������*/
 	private synchronized Connection newConnection()
 			throws ClassNotFoundException, SQLException {
 		Connection conn = null;
 		if (dbBean != null) {
 			conn = DriverManager.getConnection(dbBean.getUrl(),
 					dbBean.getUserName(), dbBean.getPassword());
 		}
 		return conn;
 	}
 	
 	/** �ӳ���ȡһ�����Ӷ���,ʹ����ͬ�����̵߳��� 
 	 * @throws SQLException */
 	public synchronized Connection getConnection() throws SQLException {
 		Connection conn = null;
 		try {
 			// �ж��Ƿ񳬹��������������
 			if(Connections.size() < this.dbBean.getMaxActiveConnections()){
 				//�Ƿ�ȡ�õ�ǰ����
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
 				// �����������,ֱ�����»������
 				wait(this.dbBean.getConnTimeOut());
 				conn = getConnection();
 			}
 		} catch (SQLException|ClassNotFoundException|InterruptedException e) {
 			LogL.getInstance().getLog().error("�ӳ���ȡһ�����Ӷ���ʧ��", e);
        	throw new SQLException("�ӳ���ȡһ�����Ӷ���ʧ��",e);
 		}
 		return conn;
 	}
 	
 	/** ��õ�ǰ����*/
 	public Connection getCurrentConnecton() throws SQLException{
 		// Ĭ���߳�����ȡ
 		Connection conn = threadLocal.get().getConn();
 		if(!isValid(conn)){
 			dbBean.setCurrentConnection(false);
 	 		conn = getConnection();
 	 		dbBean.setCurrentConnection(true);
 	 	}
 		setNewTime(threadLocal.get());
 		return conn;
 	}
 	
 	/** �ж������Ƿ����*/
 	private boolean isValid(Connection conn) throws SQLException {
 		try {
 			if (conn == null || conn.isClosed()) {
 				return false;
 			}
 		} catch (SQLException e) {
 			LogL.getInstance().getLog().error("�ж������Ƿ����ʧ��", e);
        	throw new SQLException("�ж������Ƿ����ʧ��",e);
 		}
 		return true;
 	}

 	/** �������ӳ�*/
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
 			LogL.getInstance().getLog().error("�������ӳ�ʧ��", e);
 	        throw new SQLException("�������ӳ�ʧ��",e);
 		}
 	}

 	/** ���ӳ�״̬*/
 	public boolean isActive() {
 		return isActive;
 	}
 	
 	/** ��ʱ������ӳ����*/
 	public void cheackPool() {
 		if(dbBean.isCheakPool()){
 			new Timer().schedule(new TimerTask() {
	 			@Override
	 			public void run() {
	 				try{
	 					// 1.���߳����������״̬
			 			// 2.���ӳ���С ���������
			 			// 3.����״̬���м�飬��Ϊ���ﻹ��Ҫд�����̹߳�����࣬��ʱ�Ͳ������
			 			LogL.getInstance().getLog("JdbcL").debug("���߳���������"+getFreeConnectionRecordNum(Connections)
			 					+" ## ���������"+getActiveConnectionRecordNum(Connections)
			 					+" ## �ܵ���������"+Connections.size());
			 			if(isActive()){
			 				for (ConnectionRecord  cr: Connections) {
				 				if(cr.getConnectTimeEnd().compareTo(StringL.getTodayNow()) < 0){
				 					cr.getConn().close();
				 					LogL.getInstance().getLog("JdbcL").debug("����["+cr.getConn().toString()+"]��ʼʱ��:"+cr.getConnectTimeStart()
				 							+" �ϴ�����ʱ��:"+cr.getLastConnectTime()
				 							+" ���ӽ���ʱ��:"+cr.getConnectTimeEnd()
				 							+" ���ӹر�ʱ��:"+StringL.getTodayNow());
				 				}
					 		}
			 				
			 				if(getFreeConnectionRecordNum(Connections) < dbBean.getMinConnections()){
			 					for (int i = getFreeConnectionRecordNum(Connections); i < dbBean.getMinConnections(); i++) {
			 						Connection conn = newConnection();
		 	 						if (isValid(conn)) {
		 	 							Connections.add(newConnectionRecord(conn));
		 	 			 			}
								}
			 					LogL.getInstance().getLog("JdbcL").debug("����������["+getFreeConnectionRecordNum(Connections)
			 							+"]С����С�趨ֵ["+dbBean.getMinConnections()+"],������["+
			 							(dbBean.getMinConnections()-getFreeConnectionRecordNum(Connections))+"]����������");
			 				}
			 			}else{
			 				LogL.getInstance().getLog("JdbcL").debug("���ӳ������,���ϵͳ���ս������ȼ�");
			 				System.gc();
			 			}
			 			
	 				}catch(SQLException |ClassNotFoundException e){
	 					LogL.getInstance().getLog().error("��ʱ������ӳس���", e);
	 				}
		 			
 				}
 			},dbBean.getLazyCheck(),dbBean.getPeriodCheck());
 		}
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

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}
  
    @Override
    public Connection getConnection(String username, String password) throws SQLException {  
        throw new RuntimeException("��֧�ֽ����û���������Ĳ���");  
    }
    /**�����µ����Ӽ�¼��*/
    private ConnectionRecord newConnectionRecord(Connection conn){
    	ConnectionRecord cr = null;
    	String todaynow = StringL.getTodayNow();
    	String todayend = StringL.addDateFormat(todaynow, 0, (int)(this.dbBean.getConnectionTime()/1000));
    	cr = new ConnectionRecord(conn,todaynow,todayend,todaynow);
    	return cr;
    }
    /**ʹ���Ѵ��ڵ�����ʱ�����µ�ʹ�úͽ���ʱ��*/
    private void setNewTime(ConnectionRecord cr){
		String todaynow = StringL.getTodayNow();
		cr.setLastConnectTime(todaynow);
		cr.setConnectTimeEnd(StringL.addDateFormat(todaynow, 0, (int)(this.dbBean.getConnectionTime()/1000)));
	}
    /**�����ȡһ���Ѵ��ڵĿ�������,ͬʱ��Ϊ�����*/
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
    /**�����һ���������Ϊ��������*/
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
    /**��ȡ����������*/
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
    /**��ȡ�������*/
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
     * ��¼���ӵĸ���״̬
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
    
    /** ʵ�ֶ�Connection�Ķ�̬���� */  
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
                			// �������������ȴ����̣߳�ȥ������
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
	 * �����ⲿ�������õ����ӳ�����
	 * ���������ⲿ���ã�ӵ��Ĭ��ֵ
	 * @author allen
	 */
	class DBbean {
		// ���ӳ�����
		private String driver;
		private String url;
		private String userName;
		private String password;
		// ���ӳ�����
		private String poolName;
		private int minConnections = 1; // ���гأ���С������
		private int maxConnections = 30; // ���гأ����������
		
		private int incrementConnections = 10; //ÿ�����ӵ�������
		
		private int initConnections = 5;// ��ʼ��������
		
		private long connTimeOut = 1000;// �ظ�������ӵ�Ƶ��
		
		private int maxActiveConnections = 100;// ���������������������ݿ��Ӧ
		
		private long connectionTime = 1000*60*20;// ���ӳ�ʱʱ�䣬Ĭ��20����
		
		private boolean isCurrentConnection = false; // �Ƿ��õ�ǰ���ӣ�Ĭ�ϲ�ȡ
		
		private boolean isCheakPool = true; // �Ƿ�ʱ������ӳ�
		private long lazyCheck = 1000*60*60;// �ӳٶ���ʱ���ʼ ���
		private long periodCheck = 1000*60*60;// ���Ƶ��
		
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
	    		LogL.getInstance().getLog().debug("===================��ȡ�����ļ��ɹ���======================");
			} catch (Exception e) {
				LogL.getInstance().getLog().error("DBbean��ȡ�����ļ��쳣",e);
				throw new RuntimeException("��ȡ�����ļ��쳣",e);
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

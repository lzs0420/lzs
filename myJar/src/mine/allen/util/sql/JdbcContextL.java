package mine.allen.util.sql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import mine.allen.util.lang.LogL;

/**
 * 需要在/META-INF/context.xml中配置数据源(名字：<b>"jdbc/allen"</b>)
 * @author Allen
 * @version 1.0 For Web Projects 
 */
public class JdbcContextL extends JdbcL{
	
    /**单例模式，双重锁，实例*/
	private static JdbcContextL jdbcL=null;
	
    private JdbcContextL() {
    	try {      
	        //初始化查找命名空间
	        Context ctx = new InitialContext();  
	        //参数java:/comp/env为固定路径   
	        Context envContext = (Context)ctx.lookup("java:/comp/env"); 
	        //参数jdbc/allen为数据源和JNDI绑定的名字
	        DataSource ds = (DataSource)envContext.lookup("jdbc/allen");
	        setDataSource(ds);
	    } catch (NamingException e) {     
	    	LogL.getInstance().getLog().error(null,e);
	    }
	}
    
    /**单例模式，双重锁，获取实例方法*/
    public static JdbcContextL getInstance(){
        if(jdbcL==null){
            synchronized(JdbcContextL.class){
                if(null==jdbcL){
                	jdbcL=new JdbcContextL();
                }
            }
        }
        return jdbcL;
    }

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource ;
	}

}

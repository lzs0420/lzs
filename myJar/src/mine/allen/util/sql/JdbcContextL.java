package mine.allen.util.sql;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import mine.allen.util.lang.LogL;

/**
 * ��Ҫ��/META-INF/context.xml����������Դ(���֣�<b>"jdbc/allen"</b>)
 * @author Allen
 * @version 1.0 For Web Projects 
 */
public class JdbcContextL extends JdbcL{
	
    /**����ģʽ��˫������ʵ��*/
	private static JdbcContextL jdbcL=null;
	
    private JdbcContextL() {
    	try {      
	        //��ʼ�����������ռ�
	        Context ctx = new InitialContext();  
	        //����java:/comp/envΪ�̶�·��   
	        Context envContext = (Context)ctx.lookup("java:/comp/env"); 
	        //����jdbc/allenΪ����Դ��JNDI�󶨵�����
	        DataSource ds = (DataSource)envContext.lookup("jdbc/allen");
	        setDataSource(ds);
	    } catch (NamingException e) {     
	    	LogL.getInstance().getLog().error(null,e);
	    }
	}
    
    /**����ģʽ��˫��������ȡʵ������*/
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

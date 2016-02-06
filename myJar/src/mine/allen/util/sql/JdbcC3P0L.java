package mine.allen.util.sql;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ��Ҫ��classpath������c3p0-config.xml����c3p0.properties
 * @author Allen
 * @version 1.0 For Projects 
 */
public class JdbcC3P0L extends JdbcL{

    /**����ģʽ��˫������ʵ��*/
	private static JdbcC3P0L jdbcL=null;
	
    private JdbcC3P0L() {
    	System.setProperty("com.mchange.v2.c3p0.cfg.xml","etc/c3p0-config.xml");
    	ComboPooledDataSource ds = new ComboPooledDataSource();
		setDataSource(ds);
	}
    
    /**����ģʽ��˫��������ȡʵ������*/
    public static JdbcC3P0L getInstance(){
        if(jdbcL==null){
            synchronized(JdbcC3P0L.class){
                if(null==jdbcL){
                	jdbcL=new JdbcC3P0L();
                }
            }
        }
        return jdbcL;
    }

	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource ;
	}
	public static void main(String[] args) {
    	try {
    		JdbcC3P0L.getInstance().getConnection();
    		JdbcC3P0L.getInstance().excute("update userinfo set pswd='111' where id =10");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

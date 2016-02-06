package mine.allen.util.sql;

import java.sql.SQLException;
import javax.sql.DataSource;
import mine.allen.util.lang.LogL;

/**
 * ʹ��ʵ��DataSource�ӿڵ��Զ������ݳ�
 * @author Allen
 * @version 1.0 For Projects 
 */
public class JdbcDataSourceL extends JdbcL{

    /**����ģʽ��˫������ʵ��*/
	private static JdbcDataSourceL jdbcL = null;
	
	private SimpleDataSource simpleDataSource = null;
	
    private JdbcDataSourceL() {
    	try {
			simpleDataSource = new SimpleDataSource("etc/jdbcDataSourceL.properties");
			setDataSource(simpleDataSource);
		} catch (SQLException e) {
			LogL.getInstance().getLog().error("��������Դʧ��", e);
		}
	}
    
    /**����ģʽ��˫��������ȡʵ������*/
    public static JdbcDataSourceL getInstance(){
        if(jdbcL==null){
            synchronized(JdbcDataSourceL.class){
                if(null==jdbcL){
                	jdbcL=new JdbcDataSourceL();
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
			JdbcDataSourceL.getInstance().getConnection();
			JdbcDataSourceL.getInstance().excute("update userinfo set pswd='111' where id =10");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

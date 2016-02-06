package own.allen.db.pool;

import java.util.ArrayList;
import java.util.List;
/**
 * ��ʼ����ģ��������е������ļ�
 * @author Ran
 *
 */
public class DBInitInfo {
	public  static List<DBbean>  beans = null;
	static{
		beans = new ArrayList<DBbean>();
		// �������� ���Դ�xml �������ļ����л�ȡ
		// Ϊ�˲��ԣ�������ֱ��д��
		DBbean beanOracle = new DBbean();
		beanOracle.setDriverName("com.mysql.jdbc.Driver");
		beanOracle.setUrl("jdbc:mysql://localhost:3306/test");
		beanOracle.setUserName("root");
		beanOracle.setPassword("123456");
		
		beanOracle.setMinConnections(5);
		beanOracle.setMaxConnections(100);
		
		beanOracle.setPoolName("testPool");
		beans.add(beanOracle);
	}
}


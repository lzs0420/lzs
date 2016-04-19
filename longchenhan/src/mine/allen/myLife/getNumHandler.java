package mine.allen.myLife;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mine.allen.util.contextDB;

public class getNumHandler {
	/**
	 * 根据data获得data_show的num
	 * @param data
	 * @param dataAddType 1:加1
	 * @return num
	 * @throws SQLException 
	 */
	public static String getData_showNum(String data,int dataAddType) throws SQLException{
		List<Object> params = new ArrayList<Object>();
		contextDB contextDb = new contextDB();
		contextDb.getConnection();
		
		if(dataAddType == 1){
			addData_showNum(contextDb,data);
		}
		
		String sSql = "select amount from data_show where data=?";
		params.add(data);
		Map<String, Object> NUMmap = contextDb.findSimpleResult(sSql, params);
		String num = NUMmap.get("amount")==null?"":(String)NUMmap.get("amount");
		contextDb.releaseConn();
		return num;
	}
	
	/**
	 * 增加数目
	 * @param contextDb
	 * @param data
	 * @throws SQLException
	 */
	private static void addData_showNum(contextDB contextDb,String data) throws SQLException{
		List<Object> params = new ArrayList<Object>();
		String sSql = "update data_show set amount=amount+1 where data=?";
		params.add(data);
		contextDb.updateByPreparedStatement(sSql, params);
	}
}

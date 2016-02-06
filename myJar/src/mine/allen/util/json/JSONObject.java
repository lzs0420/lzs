package mine.allen.util.json;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JSONObject extends HashMap implements JSONAware, JSONStreamAware{
	
	private static final long serialVersionUID = -503443796854799292L;
  
	  public static void writeJSONString(Map paramMap, Writer paramWriter)
	    throws IOException
	  {
	    if (paramMap == null)
	    {
	      paramWriter.write("null");
	      return;
	    }
	    int i = 1;
	    Iterator localIterator = paramMap.entrySet().iterator();
	    paramWriter.write(123);
	    while (localIterator.hasNext())
	    {
	      if (i != 0) {
	        i = 0;
	      } else {
	        paramWriter.write(44);
	      }
	      Map.Entry localEntry = (Map.Entry)localIterator.next();
	      paramWriter.write(34);
	      paramWriter.write(escape(String.valueOf(localEntry.getKey())));
	      paramWriter.write(34);
	      paramWriter.write(58);
	      JSONValue.writeJSONString(localEntry.getValue(), paramWriter);
	    }
	    paramWriter.write(125);
	  }
	  
	  public void writeJSONString(Writer paramWriter)
	    throws IOException
	  {
	    writeJSONString(this, paramWriter);
	  }
	  
	  public static String toJSONString(Map paramMap)
	  {
	    if (paramMap == null) {
	      return "null";
	    }
	    StringBuffer localStringBuffer = new StringBuffer();
	    int i = 1;
	    Iterator localIterator = paramMap.entrySet().iterator();
	    localStringBuffer.append('{');
	    while (localIterator.hasNext())
	    {
	      if (i != 0) {
	        i = 0;
	      } else {
	        localStringBuffer.append(',');
	      }
	      Map.Entry localEntry = (Map.Entry)localIterator.next();
	      toJSONString(String.valueOf(localEntry.getKey()), localEntry.getValue(), localStringBuffer);
	    }
	    localStringBuffer.append('}');
	    return localStringBuffer.toString();
	  }
	  
	  public String toJSONString()
	  {
	    return toJSONString(this);
	  }
	  
	  private static String toJSONString(String paramString, Object paramObject, StringBuffer paramStringBuffer)
	  {
	    paramStringBuffer.append('"');
	    if (paramString == null) {
	      paramStringBuffer.append("null");
	    } else {
	      JSONValue.escape(paramString, paramStringBuffer);
	    }
	    paramStringBuffer.append('"').append(':');
	    paramStringBuffer.append(JSONValue.toJSONString(paramObject));
	    return paramStringBuffer.toString();
	  }
	  
	  public String toString()
	  {
	    return toJSONString();
	  }
	  
	  public static String toString(String paramString, Object paramObject)
	  {
	    StringBuffer localStringBuffer = new StringBuffer();
	    toJSONString(paramString, paramObject, localStringBuffer);
	    return localStringBuffer.toString();
	  }
	  
	  public static String escape(String paramString)
	  {
	    return JSONValue.escape(paramString);
	  }
	  
	  public static void main(String[] paramArrayOfString)
	    throws Exception
	  {
	    String str = "{\"returnCode\":\"SUCCESS\",\"ResponseParams\":{\"manager\":\"jbo.trade.WEBSERVICE_RESPONSE\",\"objects\":"
	    		+ "[{\"FRPOSITION_INFO\":\"\",\"FRINV_INFO\":[{\"REGNO\":\"130100000168452\",\"REGCAP\":\"100.000000\",\"ENTNAME\":\""
	    		+ "石家庄林格科技有限公司\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"有限责任公司(自然人投资或控股)\",\"REGORG\":\"河北省石家庄市长安区工商行政管"
	    		+ "理局"
	    		+ "\",\"ENTSTATUS\":\"注销\",\"CURRENCY\":\"\"},{\"REGNO\":\"320282000246071\",\"REGCAP\":\"2000.000000\",\"ENTNAME\":\"江苏艾特"
	    		+ "克阻燃材料有限公司\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"有限责任公司(自然人投资或控股)\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ENTSTA"
	    		+ "TUS\":\""
	    		+ "在营（开业）\",\"CURRENCY\":\"\"},{\"REGNO\":\"320282000260188\",\"REGCAP\":\"28622.000000\",\"ENTNAME\":\"宜兴市中科官林创业投资有"
	    		+ "限公"
	    		+ "司\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"有限责任公司(自然人投资或控股)\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ENTSTATUS\":\"在营（"
	    		+ "开业）\",\""
	    		+ "CURRENCY\":\"\"}],\"BASIC_INFO\":[{\"REGNO\":\"320282000083048\",\"OPSCOANDFORM\":\"\",\"REGCAPCUR\":\"\",\"ENTNAME\":\"艾特克"
	    		+ "控股集团有限公司\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ESDATE\":\"2006-09-06\",\"OPSCOPE\":\"无。\",\"CANDATE\":\"\",\"ENTTYPE"
	    		+ "\":\"有限责"
	    		+ "任公司(自然人投资或控股)\",\"ABUITEM\":\"无。\",\"ENTSTATUS\":\"在营（开业）\",\"ANCHEYEAR\":\"2011\",\"REGCAP\":\"7669.000000\",\"CB"
	    		+ "UITEM\":\"利"
	    		+ "用自有资产对外投资（国家法律、法规禁止或限制的领域除外）；环保水处理设备、玻璃钢制品、塑料制品、实验设备、低压电器电控柜的制造、销售；保温"
	    		+ "及耐火材料、给排水设备、仪器仪表、五金电器、化工产品及原料（不含危险化学品）的销售；环保设备及环境工程、新型节能绝热保温材料、新型结构加固"
	    		+ "材料技术的研究、开发、设计、推广；自营和代理各类商品及技术的进出口业务（国家限定企业经营或禁止进出口的商品和技术除外）。\n**（前述范围涉及"
	    		+ "专项审批的经批准后方可经营）**\",\"REVDATE\":\"\",\"ANCHEDATE\":\"2012-06-07\",\"FRNAME\":\"徐畅\",\"DOM\":\"宜兴市高塍镇外商投资工业"
	    		+ "园宜高路6"
	    		+ "8号\",\"OPTO\":\"2036-12-31\",\"OPFROM\":\"2006-09-06\"}],\"ENTINV_INFO\":[{\"REGNO\":\"320282000074596\",\"REGCAP\":\"580.00"
	    		+ "0000\",\"ENTNAME\":\"江苏艾特克化学工业有限公司\",\"CONGROCUR\":\"人民币\",\"SUBCONAM\":\"580.000000\",\"ENTTYPE\":\"有限责"
	    		+ "任公司(法人独资)\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ENTSTATUS\":\"在营（开业）\"},{\"REGNO\":\"320282000189917\",\"REGC"
	    		+ "AP\":\"1000.000000\",\"ENTNAME\":\"江苏艾特克环境工程设计研究院有限公司\",\"CONGROCUR\":\"人民币\",\"SUBCONAM\":\"1000.000000\",\"E"
	    		+ "NTTYPE\":\"有限责任公司(法人独资)\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ENTSTATUS\":\"在营（开业）\"},{\"REGNO\":\"32028200021"
	    		+ "0337\",\"REGCAP\":\"5000.000000\",\"ENTNAME\":\"江苏艾特克环境工程有限公司\",\"CONGROCUR\":\"人民币\",\"SUBCONAM\":\"5000.000000\","
	    		+ "\"ENTTYPE\":\"有限责任公司(法人独资)\",\"REGORG\":\"无锡市宜兴工商行政管理局\",\"ENTSTATUS\":\"在营（开业）\"},{\"REGNO\":\"32048300"
	    		+ "0328097\",\"REGCAP\":\"380.000000\",\"ENTNAME\":\"常州市艾特克污泥处理有限公司\",\"CONGROCUR\":\"\",\"SUBCONAM\":\"193.800000\",\"E"
	    		+ "NTTYPE\":\"有限责任公司(自然人投资或控股)\",\"REGORG\":\"江苏省常州市武进区工商行政管理局\",\"ENTSTATUS\":\"在营（开业）\"},{\"REGNO"
	    		+ "\":\"370127200021587\",\"REGCAP\":\"500.000000\",\"ENTNAME\":\"山东艾特克环境工程有限公司\",\"CONGROCUR\":\"\",\"SUBCONAM\":\"25"
	    		+ "5.000000\",\"ENTTYPE\":\"有限责任公司(自然人投资或控股)\",\"REGORG\":\"济南市工商行政管理局高新技术产业开发区分局\",\"ENTSTATUS\":\""
	    		+ "在营（开业）\"}],\"PERSON_INFO\":[{\"POSITION\":\"董事\",\"PERNAME\":\"刘秀茹\"},{\"POSITION\":\"监事\",\"PERNAME\":\"吕优棋\"},{\""
	    		+ "POSITION\":\"监事\",\"PERNAME\":\"彭丽燕\"},{\"POSITION\":\"其他人员\",\"PERNAME\":\"沈希光\"},{\"POSITION\":\"董事长\",\"PERNAME\":"
	    		+ "\"徐畅\"},{\"POSITION\":\"副董事长\",\"PERNAME\":\"周亚强\"}],\"SHAREHOLDER_INFO\":[{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"江苏迈"
	    		+ "新创业投资有限公司\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM\":\"669.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"蒋红\",\"CONDA"
	    		+ "TE\":\"2012-12-27\",\"SUBCONAM\":\"5.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"刘秀茹\",\"CONDATE\":\"2011-09-27\",\"SUBCO"
	    		+ "NAM\":\"20.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"吕优棋\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"50.000000\"},{\"REG"
	    		+ "CAPCUR\":\"人民币\",\"SHANAME\":\"彭丽燕\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"15.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANA"
	    		+ "ME\":\"沈希光\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"20.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"吴爱君\",\"CONDATE\":"
	    		+ "\"2011-09-27\",\"SUBCONAM\":\"1200.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"吴建国\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM"
	    		+ "\":\"10.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"吴智仁\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"195.000000\"},{\"REGC"
	    		+ "APCUR\":\"人民币\",\"SHANAME\":\"徐畅\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM\":\"4785.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANA"
	    		+ "ME\":\"赵东\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"150.000000\"},{\"REGCAPCUR\":\"人民币\",\"SHANAME\":\"周亚强\",\"CONDATE\":"
	    		+ "\"2011-09-27\",\"SUBCONAM\":\"550.000000\"}]}]}}";
	    Object localObject = JSONValue.parse(str);
	    JSONObject localJSONObject = (JSONObject)localObject;
	    System.out.println(localJSONObject.get("ResponseParams"));
	  }
}
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
	    		+ "ʯ��ׯ�ָ�Ƽ����޹�˾\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"�������ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"REGORG\":\"�ӱ�ʡʯ��ׯ�г���������������"
	    		+ "���"
	    		+ "\",\"ENTSTATUS\":\"ע��\",\"CURRENCY\":\"\"},{\"REGNO\":\"320282000246071\",\"REGCAP\":\"2000.000000\",\"ENTNAME\":\"���հ���"
	    		+ "����ȼ�������޹�˾\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"�������ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"REGORG\":\"���������˹������������\",\"ENTSTA"
	    		+ "TUS\":\""
	    		+ "��Ӫ����ҵ��\",\"CURRENCY\":\"\"},{\"REGNO\":\"320282000260188\",\"REGCAP\":\"28622.000000\",\"ENTNAME\":\"�������пƹ��ִ�ҵͶ����"
	    		+ "�޹�"
	    		+ "˾\",\"SUBCONAM\":\"\",\"ENTTYPE\":\"�������ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"REGORG\":\"���������˹������������\",\"ENTSTATUS\":\"��Ӫ��"
	    		+ "��ҵ��\",\""
	    		+ "CURRENCY\":\"\"}],\"BASIC_INFO\":[{\"REGNO\":\"320282000083048\",\"OPSCOANDFORM\":\"\",\"REGCAPCUR\":\"\",\"ENTNAME\":\"���ؿ�"
	    		+ "�عɼ������޹�˾\",\"REGORG\":\"���������˹������������\",\"ESDATE\":\"2006-09-06\",\"OPSCOPE\":\"�ޡ�\",\"CANDATE\":\"\",\"ENTTYPE"
	    		+ "\":\"������"
	    		+ "�ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"ABUITEM\":\"�ޡ�\",\"ENTSTATUS\":\"��Ӫ����ҵ��\",\"ANCHEYEAR\":\"2011\",\"REGCAP\":\"7669.000000\",\"CB"
	    		+ "UITEM\":\"��"
	    		+ "�������ʲ�����Ͷ�ʣ����ҷ��ɡ������ֹ�����Ƶ�������⣩������ˮ�����豸����������Ʒ��������Ʒ��ʵ���豸����ѹ������ع�����졢���ۣ�����"
	    		+ "���ͻ���ϡ�����ˮ�豸�������Ǳ���������������Ʒ��ԭ�ϣ�����Σ�ջ�ѧƷ�������ۣ������豸���������̡����ͽ��ܾ��ȱ��²��ϡ����ͽṹ�ӹ�"
	    		+ "���ϼ������о�����������ơ��ƹ㣻��Ӫ�ʹ��������Ʒ�������Ľ�����ҵ�񣨹����޶���ҵ��Ӫ���ֹ�����ڵ���Ʒ�ͼ������⣩��\n**��ǰ����Χ�漰"
	    		+ "ר�������ľ���׼�󷽿ɾ�Ӫ��**\",\"REVDATE\":\"\",\"ANCHEDATE\":\"2012-06-07\",\"FRNAME\":\"�쳩\",\"DOM\":\"�����и���������Ͷ�ʹ�ҵ"
	    		+ "԰�˸�·6"
	    		+ "8��\",\"OPTO\":\"2036-12-31\",\"OPFROM\":\"2006-09-06\"}],\"ENTINV_INFO\":[{\"REGNO\":\"320282000074596\",\"REGCAP\":\"580.00"
	    		+ "0000\",\"ENTNAME\":\"���հ��ؿ˻�ѧ��ҵ���޹�˾\",\"CONGROCUR\":\"�����\",\"SUBCONAM\":\"580.000000\",\"ENTTYPE\":\"������"
	    		+ "�ι�˾(���˶���)\",\"REGORG\":\"���������˹������������\",\"ENTSTATUS\":\"��Ӫ����ҵ��\"},{\"REGNO\":\"320282000189917\",\"REGC"
	    		+ "AP\":\"1000.000000\",\"ENTNAME\":\"���հ��ؿ˻�����������о�Ժ���޹�˾\",\"CONGROCUR\":\"�����\",\"SUBCONAM\":\"1000.000000\",\"E"
	    		+ "NTTYPE\":\"�������ι�˾(���˶���)\",\"REGORG\":\"���������˹������������\",\"ENTSTATUS\":\"��Ӫ����ҵ��\"},{\"REGNO\":\"32028200021"
	    		+ "0337\",\"REGCAP\":\"5000.000000\",\"ENTNAME\":\"���հ��ؿ˻����������޹�˾\",\"CONGROCUR\":\"�����\",\"SUBCONAM\":\"5000.000000\","
	    		+ "\"ENTTYPE\":\"�������ι�˾(���˶���)\",\"REGORG\":\"���������˹������������\",\"ENTSTATUS\":\"��Ӫ����ҵ��\"},{\"REGNO\":\"32048300"
	    		+ "0328097\",\"REGCAP\":\"380.000000\",\"ENTNAME\":\"�����а��ؿ����ദ�����޹�˾\",\"CONGROCUR\":\"\",\"SUBCONAM\":\"193.800000\",\"E"
	    		+ "NTTYPE\":\"�������ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"REGORG\":\"����ʡ������������������������\",\"ENTSTATUS\":\"��Ӫ����ҵ��\"},{\"REGNO"
	    		+ "\":\"370127200021587\",\"REGCAP\":\"500.000000\",\"ENTNAME\":\"ɽ�����ؿ˻����������޹�˾\",\"CONGROCUR\":\"\",\"SUBCONAM\":\"25"
	    		+ "5.000000\",\"ENTTYPE\":\"�������ι�˾(��Ȼ��Ͷ�ʻ�ع�)\",\"REGORG\":\"�����й�����������ָ��¼�����ҵ�������־�\",\"ENTSTATUS\":\""
	    		+ "��Ӫ����ҵ��\"}],\"PERSON_INFO\":[{\"POSITION\":\"����\",\"PERNAME\":\"������\"},{\"POSITION\":\"����\",\"PERNAME\":\"������\"},{\""
	    		+ "POSITION\":\"����\",\"PERNAME\":\"������\"},{\"POSITION\":\"������Ա\",\"PERNAME\":\"��ϣ��\"},{\"POSITION\":\"���³�\",\"PERNAME\":"
	    		+ "\"�쳩\"},{\"POSITION\":\"�����³�\",\"PERNAME\":\"����ǿ\"}],\"SHAREHOLDER_INFO\":[{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"������"
	    		+ "�´�ҵͶ�����޹�˾\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM\":\"669.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"����\",\"CONDA"
	    		+ "TE\":\"2012-12-27\",\"SUBCONAM\":\"5.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"������\",\"CONDATE\":\"2011-09-27\",\"SUBCO"
	    		+ "NAM\":\"20.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"������\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"50.000000\"},{\"REG"
	    		+ "CAPCUR\":\"�����\",\"SHANAME\":\"������\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"15.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANA"
	    		+ "ME\":\"��ϣ��\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"20.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"�Ⱞ��\",\"CONDATE\":"
	    		+ "\"2011-09-27\",\"SUBCONAM\":\"1200.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"�⽨��\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM"
	    		+ "\":\"10.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"������\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"195.000000\"},{\"REGC"
	    		+ "APCUR\":\"�����\",\"SHANAME\":\"�쳩\",\"CONDATE\":\"2012-12-27\",\"SUBCONAM\":\"4785.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANA"
	    		+ "ME\":\"�Զ�\",\"CONDATE\":\"2011-09-27\",\"SUBCONAM\":\"150.000000\"},{\"REGCAPCUR\":\"�����\",\"SHANAME\":\"����ǿ\",\"CONDATE\":"
	    		+ "\"2011-09-27\",\"SUBCONAM\":\"550.000000\"}]}]}}";
	    Object localObject = JSONValue.parse(str);
	    JSONObject localJSONObject = (JSONObject)localObject;
	    System.out.println(localJSONObject.get("ResponseParams"));
	  }
}
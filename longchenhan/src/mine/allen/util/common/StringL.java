package mine.allen.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 关于String的一些方法
 * @author Allen
 * @version 1.0
 * @since 2016/01/20 15:36
 */
public class StringL {
	
	/**
	 * 获取日期
	 * @return yyyy/MM/dd
	 */
	public static String getToday(){
	     return new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
	}
	
	/**
	 * 根据指定格式获取日期
	 * @param sFormat
	 * @return
	 */
	public static String getToday(String sFormat){
	     java.util.Date dToday = new java.util.Date();
	     String sToday = new java.sql.Date(dToday.getTime()).toString();
	     sToday = replace(sToday, "-", sFormat);
	     return sToday;
	}
	
	/**
	 * 获取当前时间，没有日期 
	 * @return HH:mm:ss
	 */
	public static String getNow() {
	     Calendar rightNow = Calendar.getInstance();
	     return String.valueOf(String.valueOf(rightNow.get(11)) + ":" + String.valueOf(rightNow.get(12)) + ":" + String.valueOf(rightNow.get(13)));
	}
	
	/**
	 * 获取日期时间
	 * @return yyyy/MM/dd HH:mm:ss
	 */
	public static String getTodayNow(){
	     return getToday("/") + " " + getNow();
	}
	
	/***
	 * 替换字符串
	 * @param s
	 * @param sOld
	 * @param sNew
	 * @return
	 */
	public static String replace(String s, String sOld, String sNew)
	{
	  if ((s != null) && (s.length() < 256))
	  {
	    int iPos = s.indexOf(sOld, 0);
	    while (iPos != -1)
	    {
	      s = s.substring(0, iPos) + sNew + s.substring(iPos + sOld.length());
	      iPos = s.indexOf(sOld, iPos + sNew.length());
	    }
	    return s; }
	  if (s == null) {
	    return null;
	  }
	  
	  Pattern p = null;
	  Matcher mm = null;
	  
	  p = Pattern.compile(convert2Reg(sOld));
	  mm = p.matcher(s);
	  return mm.replaceAll(sNew);
	}
	
	/**
	 * 为字符串添加转义字符"\"
	 * @param src
	 * @return
	 */
	public static String convert2Reg(String src)
	{
	  Hashtable<Character, Character> hs = new Hashtable<Character, Character>();
	  hs.put(new Character('\n'), new Character('n'));
	  hs.put(new Character('\r'), new Character('r'));
	  hs.put(new Character('\\'), new Character('\\'));
	  hs.put(new Character('{'), new Character('{'));
	  hs.put(new Character('['), new Character('['));
	  hs.put(new Character('$'), new Character('$'));
	  hs.put(new Character('^'), new Character('^'));
	  hs.put(new Character('|'), new Character('|'));
	  hs.put(new Character('('), new Character('('));
	  hs.put(new Character(')'), new Character(')'));
	  hs.put(new Character('*'), new Character('*'));
	  hs.put(new Character('+'), new Character('+'));
	  StringBuffer sb = new StringBuffer();
	  char[] arr = src.toCharArray();
	  
	  for (int i = 0; i < arr.length; i++) {
	    Character ch = new Character(arr[i]);
	    if (hs.containsKey(ch))
	    {
	      sb.append('\\');
	      sb.append(((Character)hs.get(ch)).charValue());
	    }
	    else
	    {
	      sb.append(arr[i]);
	    }
	  }
	  return sb.toString();
	}
	
	/**
	 * 移除所有空格
	 * @param s
	 * @return
	 */
	public static String removeSpaces(String s){
	     StringTokenizer st = new StringTokenizer(s, " ", false);
	     String t = "";
	     while (st.hasMoreElements()) t = t + st.nextElement();
	     return t;
	}
	
	/**
	 * 根据全路径获取文件名
	 * @param filePathName
	 * @return
	 */
	public static String getFileName(String filePathName){
		  filePathName = filePathName==null?"":filePathName;
		  int pos = 0;
		  pos = filePathName.lastIndexOf('/');
		  if (pos != -1)
		    return filePathName.substring(pos + 1, filePathName.length());
		  pos = filePathName.lastIndexOf('\\');
		  if (pos != -1) {
		    return filePathName.substring(pos + 1, filePathName.length());
		  }
		  return filePathName;
	}
	
	/**
	 * 返回iBeginPos之后最后一个sTarget的位置
	 * @param sSource
	 * @param sTarget
	 * @param sDelim1
	 * @param sDelim2
	 * @param iBeginPos
	 * @return
	 */
	public static int indexOf(String sSource, String sTarget, String sDelim1, String sDelim2, int iBeginPos){
	  int iPos = sSource.indexOf(sTarget, iBeginPos);
	  while ((iPos >= 0) && (getOccurTimes(sSource, sDelim1, sDelim2, iBeginPos, iPos) != 0))
	    iPos = sSource.indexOf(sTarget, iPos + sTarget.length());
	  return iPos;
	}

	/**
	 * 返回iBeginPos, iEndPos之间的sSource中sTarget出现的位置
	 * @param sSource
	 * @param sTarget
	 * @param iBeginPos
	 * @param iEndPos
	 * @return
	 */
	public static int indexOf(String sSource, String sTarget, int iBeginPos, int iEndPos){
	  return sSource.substring(iBeginPos, iEndPos).indexOf(sTarget);
	}

	/**
	 * 返回iBeginPos, iEndPos之间的sSource中sDelim1出现的次数-sDelim2出现的次数<br>
	 * 如果sDelim1=sDelim2，返回sDelim1出现的次数%2（0或1）
	 * @param sSource
	 * @param sDelim1
	 * @param sDelim2
	 * @param iBeginPos
	 * @param iEndPos
	 * @return
	 */
	public static int getOccurTimes(String sSource, String sDelim1, String sDelim2, int iBeginPos, int iEndPos){
	  sSource = sSource.substring(iBeginPos, iEndPos);
	  if (sDelim1.equals(sDelim2)) {
	    return getOccurTimes(sSource, sDelim1) % 2;
	  }
	  return getOccurTimes(sSource, sDelim1) - getOccurTimes(sSource, sDelim2);
	}

	/**
	 * 获取sDelim出现的次数
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getOccurTimes(String sSource, String sDelim){
	  int iPos = 0;int iCount = 0;
	  int iDelLen = sDelim.length();
	  while ((iPos = sSource.indexOf(sDelim, iPos) + iDelLen) > iDelLen - 1) iCount++;
	  return iCount;
	}

	/**
	 * 获取sDelim出现的次数（忽略大小写）
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getOccurTimesIgnoreCase(String sSource, String sDelim){
	  int iPos = 0;int iCount = 0;
	  int iDelLen = sDelim.length();
	  while ((iPos = sSource.toLowerCase().indexOf(sDelim.toLowerCase(), iPos) + iDelLen) > iDelLen - 1) iCount++;
	  return iCount;
	}

	/**
	 * 截取第iOrder-1个sDelim和第iOrder个sDelim之间的字符串
	 * @param sSource
	 * @param sDelim
	 * @param iOrder
	 * @return
	 */
	public static String getSeparate(String sSource, String sDelim, int iOrder){
	  int iLastPos = 0;int iCount = 0;
	  int iDelLen = sDelim.length();
	  sSource = sSource + sDelim;
	  int iPos;
	  while ((iPos = sSource.indexOf(sDelim, iLastPos)) >= 0)
	  {
	    iCount++;
	    if (iCount == iOrder) return sSource.substring(iLastPos, iPos);
	    iLastPos = iPos + iDelLen;
	  }
	  return "";
	}
	
	/**
	  * String转换成Array，以sDelim为间隔符
	  * @param sSource
	  * @param sDelim
	  * @return
	  */
	String[] toStringArray(String sSource, String sDelim){
	   String[] sList = null;
	   int iCount = getSeparateSum(sSource, sDelim);
	   if (iCount > 0)
	   {
	     sList = new String[iCount];
	     for (int i = 1; i <= iCount; i++)
	       sList[(i - 1)] = getSeparate(sSource, sDelim, i);
	   }
	   return sList;
	 }

	 /**
	  * Array转换成String，以sDelim为间隔符
	  * @param sSource
	  * @param sDelim
	  * @return
	  */
	 public static String toArrayString(String[] sSource, String sDelim){
	   String sList = "";
	   int iCount = sSource.length;
	   
	   for (int i = 1; i <= iCount; i++) {
	     sList = sList + sDelim + sSource[(i - 1)];
	   }
	   if (sList.length() > 0) sList = sList.substring(sDelim.length());
	   return sList;
	 }
	
	/**
	 * string(年月日)转换为date
	 * @param str
	 * @return date
	 */
	public static Date parseDate(String str){
	  if (str == null) return null;
	  Date d = null;
	  String format = null;
	  char[] split = { '/', '-', '.' };
	  
	  if (str.length() < 8) return d;
	  if (str.length() > 10) str = str.substring(0, 10);
	  for (int i = 0; i < split.length; i++) {
	    int k = str.indexOf(split[i]);
	    if (k >= 0)
	    {
	      if (k == 4) {
	        format = "yyyy" + split[i] + 'M' + split[i] + 'd';
	        break; }
	      if (k < 4) {
	        format = 'M' + split[i] + 100 + split[i] + "yyyy";
	        break;
	      }
	      return null;
	    }
	  }
	  
	  if (format == null) {
	    if (str.substring(0, 2).compareTo("12") > 0) {
	      format = "yyyyMMdd";
	    } else {
	      format = "MMddyyyy";
	    }
	  }
	  
	  SimpleDateFormat df = new SimpleDateFormat(format);
	  try {
	    d = df.parse(str);
	  } catch (ParseException e) {
	    d = null;
	  }
	  return d;
	}
	
	/**
	 * 分割字符串<br>
	 * {1}2{1}2{1}2{1}分割为字符数组[1][1][1][1]
	 * @param str
	 * @return String[]
	 */
	public static String[] parseArray(String str)          
	{                                                      
	  if (str == null) return null;                        
	  ArrayList<String> l = new ArrayList<String>();                       
	  int sc = 0;int ec = 0;int p = 0;int sp = -1;         
	  while (p < str.length()) {                           
	    if (str.charAt(p) == '{') {                        
	      sc++;                                            
	      if (sc == 1) sp = p + 1;                         
	    } else if (str.charAt(p) == '}') {                 
	      ec++;                                            
	      if (ec == sc) {                                  
	        l.add(str.substring(sp, p));                   
	                                                       
	        sp = -1;                                       
	        sc = 0;                                        
	        ec = 0;                                        
	      }                                                
	    }                                                  
	    p++;                                               
	  }                                                    
	  return (String[])l.toArray(new String[0]);           
	}
	
	/**
	 * [1=2][2=1][0=0]转换为<br>
	 * 1=2<br>
	 * 2=1<br>
	 * 0=0<br>
	 * 这种Properties
	 * @param str
	 * @return Properties
	 */
	public static Properties parseProperties(String str){
	     Properties p = new Properties();
	     String[] s = parseArray(str);
	     if ((s != null) && (s.length > 0))
	        for (int i = 0; i < s.length; i++) {
	        	int p0 = s[i].indexOf('=');
	        	if ((p0 >= 1) && (p0 != s[i].length())) {
	        		String n = trimAll(s[i].substring(0, p0));
	        		if (n != "") {
	        			String v = s[i].substring(p0 + 1);
	        			p.setProperty(n, v);
	        		}
	        	} 
	         }
	     return p;
	}
	
	/**
	 * 讲字节数组转化为16进制字符串,toUpperCase为true大写，toUpperCase为false小写
	 * @param bytes
	 * @param toUpperCase
	 * @return String
	 */
	public static String bytesToHexString(byte[] bytes, boolean toUpperCase){
	  if (bytes == null) return null;
	  StringBuffer sb = new StringBuffer();
	  for (int i = 0; i < bytes.length; i++) {
	    int t = bytes[i];
	    sb.append(Integer.toString(t >> 4 & 0xF, 16)).append(Integer.toString(t & 0xF, 16));
	  }
	  
	  String ret = sb.toString();
	  return toUpperCase ? ret.toUpperCase() : ret.toLowerCase();
	}

	/**
	 * 除去字符串开头的分隔符
	 * @param str
	 * @return String
	 */
	public static String trimStart(String str){
	  String s = null;
	  int p = 0;
	  if (str != null) {
	    for (p = 0; p < str.length(); p++) {
	      if (!Character.isSpaceChar(str.charAt(p))) {
	        break;
	      }
	    }
	    s = p < str.length() ? str.substring(p) : "";
	  }
	  return s;
	}

	/**
	 * 除去字符串结尾的分隔符
	 * @param str
	 * @return String
	 */
	public static String trimEnd(String str){
	  String s = null;
	  int p = 0;
	  if (str != null) {
	    for (p = str.length() - 1; p >= 0; p--) {
	      if (!Character.isSpaceChar(str.charAt(p))) {
	        break;
	      }
	    }
	    s = p >= 0 ? str.substring(0, p + 1) : "";
	  }
	  return s;
	}

	/**
	 * 除去字符串开头结尾的分隔符
	 * @param str
	 * @return String
	 */
	public static String trimAll(String str){
	  String s = null;
	  if (str != null) {
	    s = trimEnd(trimStart(str));
	  }
	  return s;
	}

	/**
	 * 检测是否为null或"",是返回true
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str){
	  return (str == null) || (str.equals(""));
	}

	/**
	 * 检测是否为null或""或开头为分隔符,是返回true
	 * @param str
	 * @return boolean
	 */
	public static boolean isSpace(String str){
	  if (str == null) return true;
	  boolean r = true;
	  for (int i = 0; i < str.length(); i++) {
	    if (!Character.isSpaceChar(str.charAt(i))) {
	      r = false;
	      break;
	    }
	  }
	  return r;
	}

	/**
	 * 检测是否为null或"",是返回true
	 * @param o Object 
	 * @return boolean
	 */
	public static boolean isEmpty(Object o){
	  return (o == null) || (isEmpty((String)o));
	}

	/**
	 * 检测是否为null或""或开头为分隔符,是返回true
	 * @param str
	 * @return boolean
	 */
	public static boolean isSpace(Object o){
	  return (isEmpty(o)) || (isSpace((String)o));
	}
	
	/**
	 * 截取字符串<br>后面加...
	 */
	public static String screenStr(String str,int len){

		if(str==null)
			return "";
		else{
			if(str.length()<len)
				return str;
			else
				return str.substring(0, len)+"...";
		}
	}
	
	/***
	 * @param 
	 * day  日期字符串
	 * ft   格式(按照分钟、小时、天、月、年)
	 * x    加几
	 * @return  日期字符串
	 *  
	 * */
	public static String addDateFormat(String day, int ft,int x,String fmt){
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 24小时制  
        SimpleDateFormat format1 = new SimpleDateFormat(fmt);// 24小时制  
        int caft=0;
        if(ft==0){//秒
        	caft = Calendar.SECOND;
        }
        else if(ft==1){//分钟
        	caft = Calendar.MINUTE;
        }else if(ft==2){//小时
        	caft = Calendar.HOUR;
        }else if(ft==3){//天
        	caft = Calendar.DATE;
        }else if(ft==4){//月
        	caft = Calendar.MONTH;
        }else {//年
        	caft = Calendar.YEAR;
        }
        day = day.replaceAll("-", "/");
        Date date = null;    
        try {    
            date = format.parse(day);    
        } catch (Exception ex) {    
            ex.printStackTrace(); 
        }    
        if (date == null)    
            return "";    
        //System.out.println("front:" + format.format(date)); //显示输入的日期   
        Calendar cal = Calendar.getInstance();    
        cal.setTime(date);    
        cal.add(caft, x);// 24小时制    
        date = cal.getTime();    
        //System.out.println("after:" + format.format(date));  //显示更新后的日期  
        cal = null;    
        return format1.format(date);    	   
	}
	
	/***
	 * @param 
	 * day  日期字符串
	 * ft   格式(按照分钟、小时、天、月、年)
	 * x    加几
	 * @return  日期字符串
	 *  
	 * */
	public static String addDateFormat(String day, int ft,int x){		        
        return addDateFormat(day, ft, x, "yyyy年MM月dd日 HH时mm分ss秒");    	   
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss str格式
	 * @param str
	 * @return 时间间隔
	 */
	public static String getBetweenTime(String str){
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
		String sReturn = "";
		str = str.replaceAll("/", "-");
		try {
			java.util.Date begin = new Date();
			java.util.Date end = dfs.parse(str);   
			long between = (end.getTime()-begin.getTime())/1000;//除以1000是为了转换成秒   
			long day = between/(24*3600);   
			long hour = (between - day * 86400) / 3600;   
			long minute =  (between - day * 86400 - hour * 3600) / 60;   
			long second = between - day * 86400 - hour * 3600 - minute * 60;   

			if(day<0)
				sReturn = "已结束";
			else{
				if(day>0)
					sReturn = day+"天"+hour+"小时"+minute+"分"+second+"秒";
				if(day==0)
					sReturn = hour+"小时"+minute+"分"+second+"秒";
				if(hour==0)
					sReturn = minute+"分"+second+"秒";
				if(minute==0)
					sReturn = second+"秒";
				if(second<0)
					sReturn = "已开始";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}  
		return sReturn;
	}
	
	/**
	 * 获取随机字符串<br>length表示生成字符串的长度<br>type:1 只有数字
	 * <br>type:2 字母数字
	 * @param length
	 * @param type
	 * @return
	 */
	public static String getRandomString(int length,int type) { 
		String base = null;
		String base1 = "0123456789";
		String base2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		switch (type){
		case 1 : base = base1;break;
		case 2 : base = base2;break;
		default : base = base1;break;
		}
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 字节流到文本转换
	 * @param inStream
	 * @param charsetName 编码方式
	 * @return String
	 */
	public static String inputStream2String(InputStream inStream,String charsetName){
		try {
			ByteArrayOutputStream ois = new ByteArrayOutputStream();
	    	byte[] buffer = new byte[10240];
	    	int b = inStream.read(buffer);
	    	while(b>-1){
	    		ois.write(buffer,0,b);
	    		b = inStream.read(buffer);
	    	}
			String sResult =new String(ois.toByteArray(),charsetName); 
			return sResult;
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}	
	
	/**
	 * 文本到流的转换
	 * @param content
	 * @param charsetName
	 */
	public static InputStream string2InputStream(String content,String charsetName){
		try{
			byte[] bytes = content.getBytes(charsetName);
			ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
			return ins;
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 获得sDelim在sSource中的数量
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getSeparateSum(String sSource, String sDelim)
	{
	  if (sSource == null) return 0;
	  int iPos = 0;int iCount = 0;
	  int iDelLen = sDelim.length();
	  while ((iPos = sSource.indexOf(sDelim, iPos) + iDelLen) > iDelLen - 1) iCount++;
	  return iCount + 1;
	}
	
	/**
	 * "{{\"1\",\"2\"},{\"3\",\"4\"}}"<br>
	 * 转换为字符数组
	 * @param s
	 * @return
	 */
	public static String[][] genStringArray(String s)
	{
	  int beginDelim = getSeparateSum(s, "{") - 1;
	  int endDelim = getSeparateSum(s, "}") - 1;
	  int totalColDelim = getSeparateSum(s, "\"") - 1;
	  int totalElement = totalColDelim / 2;
	  int rowCount = beginDelim - 1;
	  int colCount = 0;
	  int rowPoint = 0;
	  int colPoint = 0;
	  colCount = totalElement / rowCount;
	  String sTemp = "";
	  if ((beginDelim < 2) || (beginDelim != endDelim) || (totalColDelim % 2 != 0)) {
	    return (String[][])null;
	  }
	  

	  String sCur = s.trim();
	  sCur = sCur.substring(sCur.indexOf("{") + 1, sCur.lastIndexOf("}")).trim();
	  
	  String[][] strArray2 = new String[rowCount][colCount];
	  for (int irow = 0; irow < rowCount; irow++) {
	    beginDelim = sCur.indexOf("{", rowPoint) + 1;
	    endDelim = sCur.indexOf("}", rowPoint);
	    rowPoint = endDelim + 1;
	    if (beginDelim > endDelim) { return (String[][])null;
	    }
	    sTemp = sCur.substring(beginDelim, endDelim).trim();
	    System.out.println(sTemp);
	    for (int icol = 0; icol < colCount; icol++) {
	      beginDelim = sTemp.indexOf("\"", colPoint) + 1;
	      endDelim = sTemp.indexOf("\"", beginDelim);
	      strArray2[irow][icol] = sTemp.substring(beginDelim, endDelim);
	      System.out.println("strArray2[" + irow + "][" + icol + "]=" + strArray2[irow][icol]);
	      colPoint = endDelim + 1;
	    }
	    colPoint = 0;
	    if (rowPoint >= sCur.length()) break;
	  }
	  return strArray2;
	}
	
	public static void main(String[] args) {
//		String sss= "{1}fef{2}{122}fef{23}{13}fef{22}";
//		System.err.println(sss);
//		String[]  s = parseArray(sss);
//		System.err.println(s.length);
//		for (String string : s) {
//			System.err.println(string);
//		}
		
	}
}

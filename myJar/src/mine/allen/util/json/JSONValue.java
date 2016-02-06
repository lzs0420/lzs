package mine.allen.util.json;

import mine.allen.util.json.parser.JSONParser;
import mine.allen.util.json.parser.ParseException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class JSONValue{
	  public static Object parse(Reader paramReader)
	  {
	    try
	    {
	      JSONParser localJSONParser = new JSONParser();
	      return localJSONParser.parse(paramReader);
	    }
	    catch (Exception localException) {}
	    return null;
	  }
	  
	  public static Object parse(String paramString)
	  {
	    StringReader localStringReader = new StringReader(paramString);
	    return parse(localStringReader);
	  }
	  
	  public static Object parseWithException(Reader paramReader)
	    throws IOException, ParseException
	  {
	    JSONParser localJSONParser = new JSONParser();
	    return localJSONParser.parse(paramReader);
	  }
	  
	  public static Object parseWithException(String paramString)
	    throws ParseException
	  {
	    JSONParser localJSONParser = new JSONParser();
	    return localJSONParser.parse(paramString);
	  }
	  
	  public static void writeJSONString(Object paramObject, Writer paramWriter)
	    throws IOException
	  {
	    if (paramObject == null)
	    {
	      paramWriter.write("null");
	      return;
	    }
	    if ((paramObject instanceof String))
	    {
	      paramWriter.write(34);
	      paramWriter.write(escape((String)paramObject));
	      paramWriter.write(34);
	      return;
	    }
	    if ((paramObject instanceof Double))
	    {
	      if ((((Double)paramObject).isInfinite()) || (((Double)paramObject).isNaN())) {
	        paramWriter.write("null");
	      } else {
	        paramWriter.write(paramObject.toString());
	      }
	      return;
	    }
	    if ((paramObject instanceof Float))
	    {
	      if ((((Float)paramObject).isInfinite()) || (((Float)paramObject).isNaN())) {
	        paramWriter.write("null");
	      } else {
	        paramWriter.write(paramObject.toString());
	      }
	      return;
	    }
	    if ((paramObject instanceof Number))
	    {
	      paramWriter.write(paramObject.toString());
	      return;
	    }
	    if ((paramObject instanceof Boolean))
	    {
	      paramWriter.write(paramObject.toString());
	      return;
	    }
	    if ((paramObject instanceof JSONStreamAware))
	    {
	      ((JSONStreamAware)paramObject).writeJSONString(paramWriter);
	      return;
	    }
	    if ((paramObject instanceof JSONAware))
	    {
	      paramWriter.write(((JSONAware)paramObject).toJSONString());
	      return;
	    }
	    if ((paramObject instanceof Map))
	    {
	      JSONObject.writeJSONString((Map)paramObject, paramWriter);
	      return;
	    }
	    if ((paramObject instanceof List))
	    {
	      JSONArray.writeJSONString((List)paramObject, paramWriter);
	      return;
	    }
	    paramWriter.write(paramObject.toString());
	  }
	  
	  public static String toJSONString(Object paramObject)
	  {
	    if (paramObject == null) {
	      return "null";
	    }
	    if ((paramObject instanceof String)) {
	      return "\"" + escape((String)paramObject) + "\"";
	    }
	    if ((paramObject instanceof Double))
	    {
	      if ((((Double)paramObject).isInfinite()) || (((Double)paramObject).isNaN())) {
	        return "null";
	      }
	      return paramObject.toString();
	    }
	    if ((paramObject instanceof Float))
	    {
	      if ((((Float)paramObject).isInfinite()) || (((Float)paramObject).isNaN())) {
	        return "null";
	      }
	      return paramObject.toString();
	    }
	    if ((paramObject instanceof Number)) {
	      return paramObject.toString();
	    }
	    if ((paramObject instanceof Boolean)) {
	      return paramObject.toString();
	    }
	    if ((paramObject instanceof JSONAware)) {
	      return ((JSONAware)paramObject).toJSONString();
	    }
	    if ((paramObject instanceof Map)) {
	      return JSONObject.toJSONString((Map)paramObject);
	    }
	    if ((paramObject instanceof List)) {
	      return JSONArray.toJSONString((List)paramObject);
	    }
	    return paramObject.toString();
	  }
	  
	  public static String escape(String paramString)
	  {
	    if (paramString == null) {
	      return null;
	    }
	    StringBuffer localStringBuffer = new StringBuffer();
	    escape(paramString, localStringBuffer);
	    return localStringBuffer.toString();
	  }
	  
	  static void escape(String paramString, StringBuffer paramStringBuffer)
	  {
	    for (int i = 0; i < paramString.length(); i++)
	    {
	      char c = paramString.charAt(i);
	      switch (c)
	      {
	      case '"': 
	        paramStringBuffer.append("\\\"");
	        break;
	      case '\\': 
	        paramStringBuffer.append("\\\\");
	        break;
	      case '\b': 
	        paramStringBuffer.append("\\b");
	        break;
	      case '\f': 
	        paramStringBuffer.append("\\f");
	        break;
	      case '\n': 
	        paramStringBuffer.append("\\n");
	        break;
	      case '\r': 
	        paramStringBuffer.append("\\r");
	        break;
	      case '\t': 
	        paramStringBuffer.append("\\t");
	        break;
	      case '/': 
	        paramStringBuffer.append("\\/");
	        break;
	      default: 
	        if (((c >= 0) && (c <= '\037')) || ((c >= '') && (c <= '?')) || ((c >= '?') && (c <= '?')))
	        {
	          String str = Integer.toHexString(c);
	          paramStringBuffer.append("\\u");
	          for (int j = 0; j < 4 - str.length(); j++) {
	            paramStringBuffer.append('0');
	          }
	          paramStringBuffer.append(str.toUpperCase());
	        }
	        else
	        {
	          paramStringBuffer.append(c);
	        }
	        break;
	      }
	    }
	  }
}
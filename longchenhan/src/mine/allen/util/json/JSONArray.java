package mine.allen.util.json;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JSONArray
  extends ArrayList
  implements List, JSONAware, JSONStreamAware
{
  private static final long serialVersionUID = 3957988303675231981L;
  
  public static void writeJSONString(List paramList, Writer paramWriter)
    throws IOException
  {
    if (paramList == null)
    {
      paramWriter.write("null");
      return;
    }
    int i = 1;
    Iterator localIterator = paramList.iterator();
    paramWriter.write(91);
    while (localIterator.hasNext())
    {
      if (i != 0) {
        i = 0;
      } else {
        paramWriter.write(44);
      }
      Object localObject = localIterator.next();
      if (localObject == null) {
        paramWriter.write("null");
      } else {
        JSONValue.writeJSONString(localObject, paramWriter);
      }
    }
    paramWriter.write(93);
  }
  
  public void writeJSONString(Writer paramWriter)
    throws IOException
  {
    writeJSONString(this, paramWriter);
  }
  
  public static String toJSONString(List paramList)
  {
    if (paramList == null) {
      return "null";
    }
    int i = 1;
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = paramList.iterator();
    localStringBuffer.append('[');
    while (localIterator.hasNext())
    {
      if (i != 0) {
        i = 0;
      } else {
        localStringBuffer.append(',');
      }
      Object localObject = localIterator.next();
      if (localObject == null) {
        localStringBuffer.append("null");
      } else {
        localStringBuffer.append(JSONValue.toJSONString(localObject));
      }
    }
    localStringBuffer.append(']');
    return localStringBuffer.toString();
  }
  
  public String toJSONString()
  {
    return toJSONString(this);
  }
  
  public String toString()
  {
    return toJSONString();
  }
}


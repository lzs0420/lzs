package mine.allen.util.json;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ItemList
{
  private String sp = ",";
  List items = new ArrayList();
  
  public ItemList() {}
  
  public ItemList(String paramString)
  {
    split(paramString, this.sp, this.items);
  }
  
  public ItemList(String paramString1, String paramString2)
  {
    this.sp = paramString1;
    split(paramString1, paramString2, this.items);
  }
  
  public ItemList(String paramString1, String paramString2, boolean paramBoolean)
  {
    split(paramString1, paramString2, this.items, paramBoolean);
  }
  
  public List getItems()
  {
    return this.items;
  }
  
  public String[] getArray()
  {
    return (String[])this.items.toArray(new String[0]);
  }
  
  public void split(String paramString1, String paramString2, List paramList, boolean paramBoolean)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return;
    }
    if (paramBoolean)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(paramString1, paramString2);
      while (localStringTokenizer.hasMoreTokens()) {
        paramList.add(localStringTokenizer.nextToken().trim());
      }
    }
    else
    {
      split(paramString1, paramString2, paramList);
    }
  }
  
  public void split(String paramString1, String paramString2, List paramList)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return;
    }
    int i = 0;
    int j = 0;
    do
    {
      j = i;
      i = paramString1.indexOf(paramString2, i);
      if (i == -1) {
        break;
      }
      paramList.add(paramString1.substring(j, i).trim());
      i += paramString2.length();
    } while (i != -1);
    paramList.add(paramString1.substring(j).trim());
  }
  
  public void setSP(String paramString)
  {
    this.sp = paramString;
  }
  
  public void add(int paramInt, String paramString)
  {
    if (paramString == null) {
      return;
    }
    this.items.add(paramInt, paramString.trim());
  }
  
  public void add(String paramString)
  {
    if (paramString == null) {
      return;
    }
    this.items.add(paramString.trim());
  }
  
  public void addAll(ItemList paramItemList)
  {
    this.items.addAll(paramItemList.items);
  }
  
  public void addAll(String paramString)
  {
    split(paramString, this.sp, this.items);
  }
  
  public void addAll(String paramString1, String paramString2)
  {
    split(paramString1, paramString2, this.items);
  }
  
  public void addAll(String paramString1, String paramString2, boolean paramBoolean)
  {
    split(paramString1, paramString2, this.items, paramBoolean);
  }
  
  public String get(int paramInt)
  {
    return (String)this.items.get(paramInt);
  }
  
  public int size()
  {
    return this.items.size();
  }
  
  public String toString()
  {
    return toString(this.sp);
  }
  
  public String toString(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < this.items.size(); i++) {
      if (i == 0)
      {
        localStringBuffer.append(this.items.get(i));
      }
      else
      {
        localStringBuffer.append(paramString);
        localStringBuffer.append(this.items.get(i));
      }
    }
    return localStringBuffer.toString();
  }
  
  public void clear()
  {
    this.items.clear();
  }
  
  public void reset()
  {
    this.sp = ",";
    this.items.clear();
  }
}


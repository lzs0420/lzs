package mine.allen.util.json.parser;

import mine.allen.util.json.JSONArray;
import mine.allen.util.json.JSONObject;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONParser
{
  public static final int S_INIT = 0;
  public static final int S_IN_FINISHED_VALUE = 1;
  public static final int S_IN_OBJECT = 2;
  public static final int S_IN_ARRAY = 3;
  public static final int S_PASSED_PAIR_KEY = 4;
  public static final int S_IN_PAIR_VALUE = 5;
  public static final int S_END = 6;
  public static final int S_IN_ERROR = -1;
  private LinkedList handlerStatusStack;
  private Yylex lexer = new Yylex((Reader)null);
  private Yytoken token = null;
  private int status = 0;
  
  private int peekStatus(LinkedList paramLinkedList)
  {
    if (paramLinkedList.size() == 0) {
      return -1;
    }
    Integer localInteger = (Integer)paramLinkedList.getFirst();
    return localInteger.intValue();
  }
  
  public void reset()
  {
    token = null;
    status = 0;
    handlerStatusStack = null;
  }
  
  public void reset(Reader paramReader)
  {
    lexer.yyreset(paramReader);
    reset();
  }
  
  public int getPosition()
  {
    return lexer.getPosition();
  }
  
  public Object parse(String paramString)
    throws ParseException
  {
    return parse(paramString, (ContainerFactory)null);
  }
  
  public Object parse(String paramString, ContainerFactory paramContainerFactory)
    throws ParseException
  {
    StringReader localStringReader = new StringReader(paramString);
    try
    {
      return parse(localStringReader, paramContainerFactory);
    }
    catch (IOException localIOException)
    {
      throw new ParseException(-1, 2, localIOException);
    }
  }
  
  public Object parse(Reader paramReader)
    throws IOException, ParseException
  {
    return parse(paramReader, (ContainerFactory)null);
  }
  
  public Object parse(Reader paramReader, ContainerFactory paramContainerFactory)
    throws IOException, ParseException
  {
    reset(paramReader);
    LinkedList localLinkedList1 = new LinkedList();
    LinkedList localLinkedList2 = new LinkedList();
    try
    {
      do
      {
        nextToken();
        Object localObject;
        Map localMap1;
        List localList;
        switch (status)
        {
        case 0: 
          switch (token.type)
          {
          case 0: 
            status = 1;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(token.value);
            break;
          case 1: 
            status = 2;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(createObjectContainer(paramContainerFactory));
            break;
          case 3: 
            status = 3;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(createArrayContainer(paramContainerFactory));
            break;
          case 2: 
          default: 
            status = -1;
          }
          break;
        case 1: 
          if (token.type == -1) {
            return localLinkedList2.removeFirst();
          }
          throw new ParseException(getPosition(), 1, token);
        case 2: 
          switch (token.type)
          {
          case 5: 
            break;
          case 0: 
            if ((token.value instanceof String))
            {
              localObject = (String)token.value;
              localLinkedList2.addFirst(localObject);
              status = 4;
              localLinkedList1.addFirst(Integer.valueOf(status));
            }
            else
            {
              status = -1;
            }
            break;
          case 2: 
            if (localLinkedList2.size() > 1)
            {
              localLinkedList1.removeFirst();
              localLinkedList2.removeFirst();
              status = peekStatus(localLinkedList1);
            }
            else
            {
              status = 1;
            }
            break;
          default: 
            status = -1;
          }
          break;
        case 4: 
          switch (token.type)
          {
          case 6: 
            break;
          case 0: 
            localLinkedList1.removeFirst();
            localObject = (String)localLinkedList2.removeFirst();
            localMap1 = (Map)localLinkedList2.getFirst();
            localMap1.put(localObject, token.value);
            status = peekStatus(localLinkedList1);
            break;
          case 3: 
            localLinkedList1.removeFirst();
            localObject = (String)localLinkedList2.removeFirst();
            localMap1 = (Map)localLinkedList2.getFirst();
            localList = createArrayContainer(paramContainerFactory);
            localMap1.put(localObject, localList);
            status = 3;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(localList);
            break;
          case 1: 
            localLinkedList1.removeFirst();
            localObject = (String)localLinkedList2.removeFirst();
            localMap1 = (Map)localLinkedList2.getFirst();
            Map localMap2 = createObjectContainer(paramContainerFactory);
            localMap1.put(localObject, localMap2);
            status = 2;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(localMap2);
            break;
          case 2: 
          case 4: 
          case 5: 
          default: 
            status = -1;
          }
          break;
        case 3: 
          switch (token.type)
          {
          case 5: 
            break;
          case 0: 
            localObject = (List)localLinkedList2.getFirst();
            ((List)localObject).add(token.value);
            break;
          case 4: 
            if (localLinkedList2.size() > 1)
            {
              localLinkedList1.removeFirst();
              localLinkedList2.removeFirst();
              status = peekStatus(localLinkedList1);
            }
            else
            {
              status = 1;
            }
            break;
          case 1: 
            localObject = (List)localLinkedList2.getFirst();
            localMap1 = createObjectContainer(paramContainerFactory);
            ((List)localObject).add(localMap1);
            status = 2;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(localMap1);
            break;
          case 3: 
            localObject = (List)localLinkedList2.getFirst();
            localList = createArrayContainer(paramContainerFactory);
            ((List)localObject).add(localList);
            status = 3;
            localLinkedList1.addFirst(Integer.valueOf(status));
            localLinkedList2.addFirst(localList);
            break;
          case 2: 
          default: 
            status = -1;
          }
          break;
        case -1: 
          throw new ParseException(getPosition(), 1, token);
        }
        if (status == -1) {
          throw new ParseException(getPosition(), 1, token);
        }
      } while (token.type != -1);
    }
    catch (IOException localIOException)
    {
      throw localIOException;
    }
    throw new ParseException(getPosition(), 1, token);
  }
  
  private void nextToken()
    throws ParseException, IOException
  {
    token = lexer.yylex();
    if (token == null) {
      token = new Yytoken(-1, null);
    }
  }
  
  private Map createObjectContainer(ContainerFactory paramContainerFactory)
  {
    if (paramContainerFactory == null) {
      return new JSONObject();
    }
    Map localMap = paramContainerFactory.createObjectContainer();
    if (localMap == null) {
      return new JSONObject();
    }
    return localMap;
  }
  
  private List createArrayContainer(ContainerFactory paramContainerFactory)
  {
    if (paramContainerFactory == null) {
      return new JSONArray();
    }
    List localList = paramContainerFactory.creatArrayContainer();
    if (localList == null) {
      return new JSONArray();
    }
    return localList;
  }
  
  public void parse(String paramString, ContentHandler paramContentHandler)
    throws ParseException
  {
    parse(paramString, paramContentHandler, false);
  }
  
  public void parse(String paramString, ContentHandler paramContentHandler, boolean paramBoolean)
    throws ParseException
  {
    StringReader localStringReader = new StringReader(paramString);
    try
    {
      parse(localStringReader, paramContentHandler, paramBoolean);
    }
    catch (IOException localIOException)
    {
      throw new ParseException(-1, 2, localIOException);
    }
  }
  
  public void parse(Reader paramReader, ContentHandler paramContentHandler)
    throws IOException, ParseException
  {
    parse(paramReader, paramContentHandler, false);
  }
  
  public void parse(Reader paramReader, ContentHandler paramContentHandler, boolean paramBoolean)
    throws IOException, ParseException
  {
    if (!paramBoolean)
    {
      reset(paramReader);
      handlerStatusStack = new LinkedList();
    }
    else if (handlerStatusStack == null)
    {
      paramBoolean = false;
      reset(paramReader);
      handlerStatusStack = new LinkedList();
    }
    LinkedList localLinkedList = handlerStatusStack;
    try
    {
      do
      {
        switch (status)
        {
        case 0: 
          paramContentHandler.startJSON();
          nextToken();
          switch (token.type)
          {
          case 0: 
            status = 1;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.primitive(token.value)) {
              return;
            }
            break;
          case 1: 
            status = 2;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startObject()) {
              return;
            }
            break;
          case 3: 
            status = 3;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startArray()) {
              return;
            }
            break;
          case 2: 
          default: 
            status = -1;
          }
          break;
        case 1: 
          nextToken();
          if (token.type == -1)
          {
            paramContentHandler.endJSON();
            status = 6;
            return;
          }
          status = -1;
          throw new ParseException(getPosition(), 1, token);
        case 2: 
          nextToken();
          switch (token.type)
          {
          case 5: 
            break;
          case 0: 
            if ((token.value instanceof String))
            {
              String str = (String)token.value;
              status = 4;
              localLinkedList.addFirst(Integer.valueOf(status));
              if (!paramContentHandler.startObjectEntry(str)) {
                return;
              }
            }
            else
            {
              status = -1;
            }
            break;
          case 2: 
            if (localLinkedList.size() > 1)
            {
              localLinkedList.removeFirst();
              status = peekStatus(localLinkedList);
            }
            else
            {
              status = 1;
            }
            if (!paramContentHandler.endObject()) {
              return;
            }
            break;
          default: 
            status = -1;
          }
          break;
        case 4: 
          nextToken();
          switch (token.type)
          {
          case 6: 
            break;
          case 0: 
            localLinkedList.removeFirst();
            status = peekStatus(localLinkedList);
            if (!paramContentHandler.primitive(token.value)) {
              return;
            }
            if (!paramContentHandler.endObjectEntry()) {
              return;
            }
            break;
          case 3: 
            localLinkedList.removeFirst();
            localLinkedList.addFirst(Integer.valueOf(5));
            status = 3;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startArray()) {
              return;
            }
            break;
          case 1: 
            localLinkedList.removeFirst();
            localLinkedList.addFirst(Integer.valueOf(5));
            status = 2;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startObject()) {
              return;
            }
            break;
          case 2: 
          case 4: 
          case 5: 
          default: 
            status = -1;
          }
          break;
        case 5: 
          localLinkedList.removeFirst();
          status = peekStatus(localLinkedList);
          if (!paramContentHandler.endObjectEntry()) {
            return;
          }
          break;
        case 3: 
          nextToken();
          switch (token.type)
          {
          case 5: 
            break;
          case 0: 
            if (!paramContentHandler.primitive(token.value)) {
              return;
            }
            break;
          case 4: 
            if (localLinkedList.size() > 1)
            {
              localLinkedList.removeFirst();
              status = peekStatus(localLinkedList);
            }
            else
            {
              status = 1;
            }
            if (!paramContentHandler.endArray()) {
              return;
            }
            break;
          case 1: 
            status = 2;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startObject()) {
              return;
            }
            break;
          case 3: 
            status = 3;
            localLinkedList.addFirst(Integer.valueOf(status));
            if (!paramContentHandler.startArray()) {
              return;
            }
            break;
          case 2: 
          default: 
            status = -1;
          }
          break;
        case 6: 
          return;
        case -1: 
          throw new ParseException(getPosition(), 1, token);
        }
        if (status == -1) {
          throw new ParseException(getPosition(), 1, token);
        }
      } while (token.type != -1);
    }
    catch (IOException localIOException)
    {
      status = -1;
      throw localIOException;
    }
    catch (ParseException localParseException)
    {
      status = -1;
      throw localParseException;
    }
    catch (RuntimeException localRuntimeException)
    {
      status = -1;
      throw localRuntimeException;
    }
    catch (Error localError)
    {
      status = -1;
      throw localError;
    }
    status = -1;
    throw new ParseException(getPosition(), 1, token);
  }
}

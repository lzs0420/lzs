package mine.allen.util.json.parser;

public class ParseException
  extends Exception
{
  private static final long serialVersionUID = -7880698968187728548L;
  public static final int ERROR_UNEXPECTED_CHAR = 0;
  public static final int ERROR_UNEXPECTED_TOKEN = 1;
  public static final int ERROR_UNEXPECTED_EXCEPTION = 2;
  private int errorType;
  private Object unexpectedObject;
  private int position;
  
  public ParseException(int paramInt)
  {
    this(-1, paramInt, null);
  }
  
  public ParseException(int paramInt, Object paramObject)
  {
    this(-1, paramInt, paramObject);
  }
  
  public ParseException(int paramInt1, int paramInt2, Object paramObject)
  {
    this.position = paramInt1;
    this.errorType = paramInt2;
    this.unexpectedObject = paramObject;
  }
  
  public int getErrorType()
  {
    return this.errorType;
  }
  
  public void setErrorType(int paramInt)
  {
    this.errorType = paramInt;
  }
  
  public int getPosition()
  {
    return this.position;
  }
  
  public void setPosition(int paramInt)
  {
    this.position = paramInt;
  }
  
  public Object getUnexpectedObject()
  {
    return this.unexpectedObject;
  }
  
  public void setUnexpectedObject(Object paramObject)
  {
    this.unexpectedObject = paramObject;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    switch (this.errorType)
    {
    case 0: 
      localStringBuffer.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
      break;
    case 1: 
      localStringBuffer.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
      break;
    case 2: 
      localStringBuffer.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
      break;
    default: 
      localStringBuffer.append("Unkown error at position ").append(this.position).append(".");
    }
    return localStringBuffer.toString();
  }
}


package mine.allen.util.json.parser;

public class Yytoken
{
  public static final int TYPE_VALUE = 0;
  public static final int TYPE_LEFT_BRACE = 1;
  public static final int TYPE_RIGHT_BRACE = 2;
  public static final int TYPE_LEFT_SQUARE = 3;
  public static final int TYPE_RIGHT_SQUARE = 4;
  public static final int TYPE_COMMA = 5;
  public static final int TYPE_COLON = 6;
  public static final int TYPE_EOF = -1;
  public int type = 0;
  public Object value = null;
  
  public Yytoken(int paramInt, Object paramObject)
  {
    this.type = paramInt;
    this.value = paramObject;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    switch (this.type)
    {
    case 0: 
      localStringBuffer.append("VALUE(").append(this.value).append(")");
      break;
    case 1: 
      localStringBuffer.append("LEFT BRACE({)");
      break;
    case 2: 
      localStringBuffer.append("RIGHT BRACE(})");
      break;
    case 3: 
      localStringBuffer.append("LEFT SQUARE([)");
      break;
    case 4: 
      localStringBuffer.append("RIGHT SQUARE(])");
      break;
    case 5: 
      localStringBuffer.append("COMMA(,)");
      break;
    case 6: 
      localStringBuffer.append("COLON(:)");
      break;
    case -1: 
      localStringBuffer.append("END OF FILE");
    }
    return localStringBuffer.toString();
  }
}


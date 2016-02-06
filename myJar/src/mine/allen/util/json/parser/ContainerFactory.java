package mine.allen.util.json.parser;

import java.util.List;
import java.util.Map;

public abstract interface ContainerFactory
{
  public abstract Map createObjectContainer();
  
  public abstract List creatArrayContainer();
}

package own.test.test;

import java.lang.reflect.Method;  
import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.Hashtable;  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  
import own.test.bean.CacheData;
  
/** 
 * @author zsy 
 */  
public class CacheUtils {  
    private static final Log log = LogFactory.getLog(CacheUtils.class);  
    private static CacheUtils singleton = null;  
      
    private Hashtable<String, CacheData> cacheMap;//存放缓存数据  
      
    private ArrayList<String> threadKeys;//处于线程更新中的key值列表  
      
    public static CacheUtils getInstance() {  
        if (singleton == null) {  
            singleton = new CacheUtils();  
        }  
        return singleton;  
    }  
      
    private CacheUtils() {  
        cacheMap = new Hashtable<String, CacheData>();  
        threadKeys = new ArrayList<String>();  
    }  
      
    /** 
     * 添加数据缓存 
     * 与方法getCacheData(String key, long intervalTime, int maxVisitCount)配合使用 
     * @param key 
     * @param data 
     */  
    public void addCacheData(String key, Object data) {  
        addCacheData(key, data, true);  
    }  
      
    private void addCacheData(String key, Object data, boolean check) {  
        if (Runtime.getRuntime().freeMemory() < 5L*1024L*1024L) {//虚拟机内存小于10兆，则清除缓存  
            log.warn("WEB缓存：内存不足，开始清空缓存！");  
            removeAllCacheData();  
            return;  
        } else if(check && cacheMap.containsKey(key)) {  
            log.warn("WEB缓存：key值= " + key + " 在缓存中重复, 本次不缓存！");  
            return;  
        }  
        cacheMap.put(key, new CacheData(data));  
    }  
      
    /** 
     * 取得缓存中的数据 
     * 与方法addCacheData(String key, Object data)配合使用 
     * @param key  
     * @param intervalTime 缓存的时间周期，小于等于0时不限制 
     * @param maxVisitCount 访问累积次数，小于等于0时不限制 
     * @return 
     */  
    public Object getCacheData(String key, long intervalTime, int maxVisitCount) {  
        CacheData cacheData = (CacheData)cacheMap.get(key);  
        if (cacheData == null) {  
            return null;  
        }  
        if (intervalTime > 0 && (System.currentTimeMillis() - cacheData.getTime()) > intervalTime) {  
            removeCacheData(key);  
            return null;  
        }  
        if (maxVisitCount > 0 && (maxVisitCount - cacheData.getCount()) <= 0) {  
            removeCacheData(key);  
            return null;  
        } else {  
            cacheData.addCount();  
        }  
        return cacheData.getData();  
    }  
      
    /** 
     * 当缓存中数据失效时，用不给定的方法线程更新数据 
     * @param o 取得数据的对像(该方法是静态方法是不用实例，则传Class实列) 
     * @param methodName 该对像中的方法 
     * @param parameters 该方法的参数列表(参数列表中对像都要实现toString方法,若列表中某一参数为空则传它所属类的Class) 
     * @param intervalTime 缓存的时间周期，小于等于0时不限制 
     * @param maxVisitCount 访问累积次数，小于等于0时不限制 
     * @return 
     */  
    public Object getCacheData(Object o, String methodName,Object[] parameters,   
            long intervalTime, int maxVisitCount) {  
        Class<? extends Object> oc = o instanceof Class ? (Class<?>)o : o.getClass();  
        StringBuffer key = new StringBuffer(oc.getName());//生成缓存key值  
        key.append("-").append(methodName);  
        if (parameters != null) {  
            for (int i = 0; i < parameters.length; i++) {  
                if (parameters[i] instanceof Object[]) {  
                    key.append("-").append(Arrays.toString((Object[])parameters[i]));  
                } else {  
                    key.append("-").append(parameters[i]);  
                }  
            }  
        }  
          
        CacheData cacheData = (CacheData)cacheMap.get(key.toString());  
        if (cacheData == null) {//等待加载并返回  
            Object returnValue = invoke(o, methodName, parameters, key.toString());  
            return returnValue instanceof Class ? null : returnValue;  
        }  
        if (intervalTime > 0 && (System.currentTimeMillis() - cacheData.getTime()) > intervalTime) {  
            daemonInvoke(o, methodName, parameters, key.toString());//缓存时间超时,启动线程更新数据  
        } else if (maxVisitCount > 0 && (maxVisitCount - cacheData.getCount()) <= 0) {//访问次数超出,启动线程更新数据  
            daemonInvoke(o, methodName, parameters, key.toString());  
        } else {  
            cacheData.addCount();  
        }  
        return cacheData.getData();  
    }  
      
    /** 
     * 递归调用给定方法更新缓存中数据据 
     * @param o 
     * @param methodName 
     * @param parameters 
     * @param key 
     * @return 若反射调用方法返回值为空则返回该值的类型 
     */  
    private Object invoke(Object o, String methodName,Object[] parameters, String key) {  
        Object returnValue = null;  
        try {  
            Class<?>[] pcs = null;  
            if (parameters != null) {  
                pcs = new Class[parameters.length];  
                for (int i = 0; i < parameters.length; i++) {  
                    if (parameters[i] instanceof MethodInfo) {//参数类型是MethodInfo则调用该方法的返回值做这参数  
                        MethodInfo pmi = (MethodInfo)parameters[i];  
                        Object pre = invoke(pmi.getO(), pmi.getMethodName(), pmi.getParameters(), null);  
                        parameters[i] = pre;  
                    }  
                    if (parameters[i] instanceof Class) {  
                        pcs[i] = (Class<?>)parameters[i];  
                        parameters[i] = null;  
                    } else {  
                        pcs[i] = parameters[i].getClass();  
                    }  
                }  
            }  
            Class<?> oc = o instanceof Class ? (Class<?>)o : o.getClass();  
        //    Method m = oc.getDeclaredMethod(methodName, pcs);  
            Method m = matchMethod(oc, methodName, pcs);  
            returnValue = m.invoke(o, parameters);  
            if (key != null && returnValue != null) {  
                addCacheData(key, returnValue, false);  
            }  
            if (returnValue == null) {  
                returnValue = m.getReturnType();  
            }  
        } catch(Exception e) {  
            log.error("调用方法失败,methodName=" + methodName);  
            if (key != null) {  
                removeCacheData(key);  
                log.error("更新缓存失败，缓存key=" + key);  
            }  
            e.printStackTrace();  
        }  
        return returnValue;  
    }  
      
    /** 
     * 找不到完全匹配的方法时,对参数进行向父类匹配 
     * 因为方法aa(java.util.List) 与 aa(java.util.ArrayList)不能自动匹配到 
     *  
     * @param oc 
     * @param methodName 
     * @param pcs 
     * @return 
     * @throws NoSuchMethodException  
     * @throws NoSuchMethodException 
     */  
    private Method matchMethod(Class<?> oc, String methodName, Class<?>[] pcs  
            ) throws NoSuchMethodException, SecurityException {  
        try {  
            Method method = oc.getDeclaredMethod(methodName, pcs);  
            return method;  
        } catch (NoSuchMethodException e) {  
            Method[] ms = oc.getDeclaredMethods();  
            aa:for (int i = 0; i < ms.length; i++) {  
                if (ms[i].getName().equals(methodName)) {  
                    Class<?>[] pts = ms[i].getParameterTypes();  
                    if (pts.length == pcs.length) {  
                        for (int j = 0; j < pts.length; j++) {  
                            if (!pts[j].isAssignableFrom(pcs[j])) {  
                                break aa;  
                            }  
                        }  
                        return ms[i];  
                    }  
                }  
            }  
            throw new NoSuchMethodException();  
        }  
    }  
      
    /** 
     * 新启线程后台调用给定方法更新缓存中数据据 
     * @param o 
     * @param methodName 
     * @param parameters 
     * @param key 
     */  
    private void daemonInvoke(Object o, String methodName,Object[] parameters, String key) {  
        if (!threadKeys.contains(key)) {  
            InvokeThread t = new InvokeThread(o, methodName, parameters, key);  
            t.start();  
        }  
    }  
      
    /** 
     * 些类存放方法的主调对像,名称及参数数组 
     * @author zsy 
     * 
     */  
    public class MethodInfo {  
        private Object o;  
        private String methodName;  
        private Object[] parameters;  
        public MethodInfo(Object o, String methodName,Object[] parameters) {  
            this.o = o;  
            this.methodName = methodName;  
            this.parameters = parameters;  
        }  
        public String getMethodName() {  
            return methodName;  
        }  
        public void setMethodName(String methodName) {  
            this.methodName = methodName;  
        }  
        public Object getO() {  
            return o;  
        }  
        public void setO(Object o) {  
            this.o = o;  
        }  
        public Object[] getParameters() {  
            return parameters;  
        }  
        public void setParameters(Object[] parameters) {  
            this.parameters = parameters;  
        }  
          
        public String toString() {  
            StringBuffer str = new StringBuffer(methodName);  
            if (parameters != null) {  
                str.append("(");  
                for (int i = 0; i < parameters.length; i++) {  
                    if (parameters[i] instanceof Object[]) {  
                        str.append(Arrays.toString((Object[])parameters[i])).append(",");  
                    } else {  
                        str.append(parameters[i]).append(",");  
                    }  
                }  
                str.append(")");  
            }  
            return str.toString();  
        }  
    }  
      
    /** 
     * 线程调用方法 
     * @author zsy 
     * 
     */  
    private class InvokeThread extends Thread {  
        private Object o;  
        private String methodName;  
        private Object[] parameters;  
        private String key;  
        public InvokeThread(Object o, String methodName,Object[] parameters, String key) {  
            this.o = o;  
            this.methodName = methodName;  
            this.parameters = parameters;  
            this.key = key;  
        }  
          
        public void run() {  
            threadKeys.add(key);  
            invoke(o, methodName, parameters, key);  
            threadKeys.remove(key);  
        }  
    }  
      
    /** 
     * 移除缓存中的数据 
     * @param key 
     */  
    public void removeCacheData(String key) {  
        cacheMap.remove(key);  
    }  
      
    /** 
     * 移除所有缓存中的数据 
     * 
     */  
    public void removeAllCacheData() {  
        cacheMap.clear();  
    }  
      
    public String toString() {  
        StringBuffer sb = new StringBuffer("************************ ");  
        sb.append("正在更新的缓存数据： ");  
        for (int i = 0; i < threadKeys.size(); i++) {  
            sb.append(threadKeys.get(i)).append(" ");  
        }  
        sb.append("当前缓存大小：").append(cacheMap.size()).append(" ");  
        sb.append("************************");  
        return sb.toString();  
    }  
    /**
用法：
例1：代码片段如下：
public class Test {
  String rulStr=....;
  String encoding=....;
  public void getData() {
    DataCreator c = new DataCreator();
    String result = c.initUrlData(urlStr,encoding);
    System.out.println(result);
  }
}
每次执行上面代码时都要通过调用 initUrlData方法取得数据，假设此方法很耗资源而耗时间，但对数据时实性要求不高，就是可以用以下方式进行缓存处理，
保证很快地取得数据，并根据设置的参数自动更新缓存中数据
注意：initUrlData方法参数值一样时才属于同一个缓存，否则会生成一个新的缓存，也就是说从缓存中取数据与initUrlData方法参数值有关
  ......
public void getData() {
    DataCreator data = new DataCreator();
    CacheOperation co = CacheOperation.getInstance();
    String str = (String)co.getCacheData(data, "initUrlData",new Object[]{urlStr, encoding},  120000, 100);
    System.out.println(result);
  }
......
getCacheData方法返回值与initUrlData方法返回类型一样，参数说明：
data：调用initUrlData方法的实列，如果该方法是静态的，则传类的类型，如（DataCreator .class）；
"initUrlData"：方法名称；
new Object[]{urlStr, encoding}：initUrlData方法的参数数组，如果某一参数为空则传该参数的类型，若encoding 为空，则为new Object[]{urlStr, String.class}
或new Object[]{urlStr, ""}；
120000：缓存时间，单位：豪秒，即过两分钟更新一次缓存；值为0时为不限，即不更新缓存；
100：访问次数，当缓存中数据被访问100次时更新一次缓存；值为0时为不限，即不更新缓存；
例2：代码片段如下：
......
String province = request.getParameter("province");
String city= request.getParameter("city");
String county= request.getParameter("county");
Document doc = XMLBuilder.buildLatelyKeyword(kwm.latelyKeyword(province, city, county));
out.write(doc);
......
做缓存并两分钟更新一次,如下：
......
String province = request.getParameter("province");
String city= request.getParameter("city");
String county= request.getParameter("county");
 
CacheOperation co = CacheOperation.getInstance();
MethodInfo mi = co.new MethodInfo(kwm, "latelyKeyword", new Object[]{province, city, county});
  
Document doc = (Document )co.getCacheData(XMLBuilder.class,"buildLatelyKeyword",new Object[]{mi}, 120000, 0);
 
out.write(doc);
......
以上方法是嵌套调用， 要先定义内部方法说明即MethodInfo，此类是CacheOperation 的一个内部类。
     */
    public static void main(String[] args) {
		
	}
}  

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
      
    private Hashtable<String, CacheData> cacheMap;//��Ż�������  
      
    private ArrayList<String> threadKeys;//�����̸߳����е�keyֵ�б�  
      
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
     * ������ݻ��� 
     * �뷽��getCacheData(String key, long intervalTime, int maxVisitCount)���ʹ�� 
     * @param key 
     * @param data 
     */  
    public void addCacheData(String key, Object data) {  
        addCacheData(key, data, true);  
    }  
      
    private void addCacheData(String key, Object data, boolean check) {  
        if (Runtime.getRuntime().freeMemory() < 5L*1024L*1024L) {//������ڴ�С��10�ף����������  
            log.warn("WEB���棺�ڴ治�㣬��ʼ��ջ��棡");  
            removeAllCacheData();  
            return;  
        } else if(check && cacheMap.containsKey(key)) {  
            log.warn("WEB���棺keyֵ= " + key + " �ڻ������ظ�, ���β����棡");  
            return;  
        }  
        cacheMap.put(key, new CacheData(data));  
    }  
      
    /** 
     * ȡ�û����е����� 
     * �뷽��addCacheData(String key, Object data)���ʹ�� 
     * @param key  
     * @param intervalTime �����ʱ�����ڣ�С�ڵ���0ʱ������ 
     * @param maxVisitCount �����ۻ�������С�ڵ���0ʱ������ 
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
     * ������������ʧЧʱ���ò������ķ����̸߳������� 
     * @param o ȡ�����ݵĶ���(�÷����Ǿ�̬�����ǲ���ʵ������Classʵ��) 
     * @param methodName �ö����еķ��� 
     * @param parameters �÷����Ĳ����б�(�����б��ж���Ҫʵ��toString����,���б���ĳһ����Ϊ�������������Class) 
     * @param intervalTime �����ʱ�����ڣ�С�ڵ���0ʱ������ 
     * @param maxVisitCount �����ۻ�������С�ڵ���0ʱ������ 
     * @return 
     */  
    public Object getCacheData(Object o, String methodName,Object[] parameters,   
            long intervalTime, int maxVisitCount) {  
        Class<? extends Object> oc = o instanceof Class ? (Class<?>)o : o.getClass();  
        StringBuffer key = new StringBuffer(oc.getName());//���ɻ���keyֵ  
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
        if (cacheData == null) {//�ȴ����ز�����  
            Object returnValue = invoke(o, methodName, parameters, key.toString());  
            return returnValue instanceof Class ? null : returnValue;  
        }  
        if (intervalTime > 0 && (System.currentTimeMillis() - cacheData.getTime()) > intervalTime) {  
            daemonInvoke(o, methodName, parameters, key.toString());//����ʱ�䳬ʱ,�����̸߳�������  
        } else if (maxVisitCount > 0 && (maxVisitCount - cacheData.getCount()) <= 0) {//���ʴ�������,�����̸߳�������  
            daemonInvoke(o, methodName, parameters, key.toString());  
        } else {  
            cacheData.addCount();  
        }  
        return cacheData.getData();  
    }  
      
    /** 
     * �ݹ���ø����������»��������ݾ� 
     * @param o 
     * @param methodName 
     * @param parameters 
     * @param key 
     * @return ��������÷�������ֵΪ���򷵻ظ�ֵ������ 
     */  
    private Object invoke(Object o, String methodName,Object[] parameters, String key) {  
        Object returnValue = null;  
        try {  
            Class<?>[] pcs = null;  
            if (parameters != null) {  
                pcs = new Class[parameters.length];  
                for (int i = 0; i < parameters.length; i++) {  
                    if (parameters[i] instanceof MethodInfo) {//����������MethodInfo����ø÷����ķ���ֵ�������  
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
            log.error("���÷���ʧ��,methodName=" + methodName);  
            if (key != null) {  
                removeCacheData(key);  
                log.error("���»���ʧ�ܣ�����key=" + key);  
            }  
            e.printStackTrace();  
        }  
        return returnValue;  
    }  
      
    /** 
     * �Ҳ�����ȫƥ��ķ���ʱ,�Բ�����������ƥ�� 
     * ��Ϊ����aa(java.util.List) �� aa(java.util.ArrayList)�����Զ�ƥ�䵽 
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
     * �����̺߳�̨���ø����������»��������ݾ� 
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
     * Щ���ŷ�������������,���Ƽ��������� 
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
     * �̵߳��÷��� 
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
     * �Ƴ������е����� 
     * @param key 
     */  
    public void removeCacheData(String key) {  
        cacheMap.remove(key);  
    }  
      
    /** 
     * �Ƴ����л����е����� 
     * 
     */  
    public void removeAllCacheData() {  
        cacheMap.clear();  
    }  
      
    public String toString() {  
        StringBuffer sb = new StringBuffer("************************ ");  
        sb.append("���ڸ��µĻ������ݣ� ");  
        for (int i = 0; i < threadKeys.size(); i++) {  
            sb.append(threadKeys.get(i)).append(" ");  
        }  
        sb.append("��ǰ�����С��").append(cacheMap.size()).append(" ");  
        sb.append("************************");  
        return sb.toString();  
    }  
    /**
�÷���
��1������Ƭ�����£�
public class Test {
  String rulStr=....;
  String encoding=....;
  public void getData() {
    DataCreator c = new DataCreator();
    String result = c.initUrlData(urlStr,encoding);
    System.out.println(result);
  }
}
ÿ��ִ���������ʱ��Ҫͨ������ initUrlData����ȡ�����ݣ�����˷����ܺ���Դ����ʱ�䣬��������ʱʵ��Ҫ�󲻸ߣ����ǿ��������·�ʽ���л��洦��
��֤�ܿ��ȡ�����ݣ����������õĲ����Զ����»���������
ע�⣺initUrlData��������ֵһ��ʱ������ͬһ�����棬���������һ���µĻ��棬Ҳ����˵�ӻ�����ȡ������initUrlData��������ֵ�й�
  ......
public void getData() {
    DataCreator data = new DataCreator();
    CacheOperation co = CacheOperation.getInstance();
    String str = (String)co.getCacheData(data, "initUrlData",new Object[]{urlStr, encoding},  120000, 100);
    System.out.println(result);
  }
......
getCacheData��������ֵ��initUrlData������������һ��������˵����
data������initUrlData������ʵ�У�����÷����Ǿ�̬�ģ���������ͣ��磨DataCreator .class����
"initUrlData"���������ƣ�
new Object[]{urlStr, encoding}��initUrlData�����Ĳ������飬���ĳһ����Ϊ���򴫸ò��������ͣ���encoding Ϊ�գ���Ϊnew Object[]{urlStr, String.class}
��new Object[]{urlStr, ""}��
120000������ʱ�䣬��λ�����룬���������Ӹ���һ�λ��棻ֵΪ0ʱΪ���ޣ��������»��棻
100�����ʴ����������������ݱ�����100��ʱ����һ�λ��棻ֵΪ0ʱΪ���ޣ��������»��棻
��2������Ƭ�����£�
......
String province = request.getParameter("province");
String city= request.getParameter("city");
String county= request.getParameter("county");
Document doc = XMLBuilder.buildLatelyKeyword(kwm.latelyKeyword(province, city, county));
out.write(doc);
......
�����沢�����Ӹ���һ��,���£�
......
String province = request.getParameter("province");
String city= request.getParameter("city");
String county= request.getParameter("county");
 
CacheOperation co = CacheOperation.getInstance();
MethodInfo mi = co.new MethodInfo(kwm, "latelyKeyword", new Object[]{province, city, county});
  
Document doc = (Document )co.getCacheData(XMLBuilder.class,"buildLatelyKeyword",new Object[]{mi}, 120000, 0);
 
out.write(doc);
......
���Ϸ�����Ƕ�׵��ã� Ҫ�ȶ����ڲ�����˵����MethodInfo��������CacheOperation ��һ���ڲ��ࡣ
     */
    public static void main(String[] args) {
		
	}
}  

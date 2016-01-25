package mine.allen.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class FileUtils {

	/**
	 * �鿴���ļ�
	 * @param filePath
	 * @return byte[]
	 */
	public static byte[] toByteArray(String filePath){
		byte[] result = null;
		filePath = filePath.replace("\\\\/", "/");
		LogUtils.debugPrint("filePath[BytesViewHandler]="+filePath);
		BufferedInputStream bis = null;
		java.io.ByteArrayOutputStream bos = null;
		try{
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new ByteArrayOutputStream();
			byte[] buff = new byte[2048];
			int bytesread;
			while (-1 != (bytesread = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesread);
			}
			result = bos.toByteArray();  
			LogUtils.debugPrint("read file:" + filePath + "(size="+result.length+")");
		}
		catch(Exception e){
			LogUtils.errorPrint(null, e);
		}
		finally{
			try{
				if(bis!=null)bis.close();
				if(bos!=null)bis.close();
			}
			catch(Exception e){
				LogUtils.errorPrint(null, e);
			}
		}
		
		return result;
	}
	
	/**
	 * ����ļ���
	 * @param FilePath
	 * @param sFileName
	 * @return
	 * @throws IOException
	 */
	public static String getFullName(String FilePath, String sFileName){
		File dFile = null;
		String sFullPath = FilePath;
		if (sFullPath != null) {
			dFile = new File(sFullPath);
			if (!dFile.exists()) {
				LogUtils.errorPrint("at FileUtils.getFullName: �ļ�[" + dFile + "]�����ڣ�����");
			}
			if(sFileName.startsWith("/")&&!sFullPath.endsWith("/")||!sFileName.startsWith("/")&&sFullPath.endsWith("/")){
				sFullPath = sFullPath + sFileName;
			}else if(sFileName.startsWith("/")&&sFullPath.endsWith("/")){
				sFullPath = sFullPath + sFileName.substring(1);
			}else{
				sFullPath = sFullPath + "/" + sFileName;
			}
			dFile = new File(sFullPath);
			if (!dFile.exists()) {
				LogUtils.errorPrint("at FileUtils.getFullName: �ļ�[" + sFullPath + "]�����ڣ�����");
			}
		} else {
			LogUtils.errorPrint("at FileUtils.getFullName: �ļ� [" + FilePath + "]�����ڣ�����");
		}
		return StringL.convert2Reg(sFullPath);
	}
	
	/**�滻�ַ���*/
	public static String replaceStr(String strOriginal, String strOld, String strNew) {
		int i = 0;
		StringBuffer strBuffer = new StringBuffer(strOriginal);
		while ((i = strOriginal.indexOf(strOld, i)) >= 0) {
			strBuffer.delete(i, i + strOld.length());
			strBuffer.insert(i, strNew);
			i = i + strNew.length();
			strOriginal = strBuffer.toString();
		}
		return strOriginal;
	}

	
  	/**
  	 * �滻�����ַ�
  	 * <br>. �ͣ�
  	 */
  	public static String replaceStr(String str) {
  		String sSource = str;
  		sSource = replaceStr(sSource,"��","&#8226;");
  		sSource = replaceStr(sSource,"?","&copy;");
  		return sSource;
  	}

  	
  	/**
  	 * ץȡҳ������
  	 * @param  sUrl  ҳ���ַ
  	 * @param  scode �����ʽ
  	 * @return �����ַ���
  	 * **/
	public static String url2String(String sUrl,String scode) throws IOException {
		URL url = new URL(sUrl);
		InputStreamReader isr = new InputStreamReader(url.openStream(), scode);
		StringBuffer sb = new StringBuffer();
		int n = 0;
		while ((n = isr.read()) != -1) {
			sb.append((char) n);
		}
		isr.close();
		return sb.toString();
	}

	
  	/**
  	 * Ĭ�ϵ�GBK�����ʽץȡҳ������
  	 * @param  sUrl  ҳ���ַ
  	 * @return �����ַ���
  	 * **/
	public static String url2String(String sUrl) throws IOException {
		return url2String(sUrl,"GBK");
	}

	
	/**
	 * ��ָ��������д��ָ�����ļ�
	 * @param fileName  �ļ� 
	 * @param source   ��д�������
	 * **/
  	private static void writeFile(String fileName, String source) throws IOException {
  		FileWriter fos = new FileWriter(fileName);
  		for(int i=0; i<source.length();i++){
  			fos.write(source.charAt(i));
  		}
  		fos.close();
  		return;
  	}

  	
  	/**
  	 * @describe ����sUrl�������ļ�sFile
  	 * @param sFile ���ɵ��ļ�
  	 * @param sUrl  ��Դҳ��
  	 * */
  	public static void writeFileFromUrl(String sFile, String sUrl) throws IOException {
  		String sSource = url2String(sUrl);
  		sSource = replaceStr(sSource);
  		writeFile(sFile,sSource);
  	}
  	

  	/**
  	 * @describe ��ȡ�ļ�,�����ַ���
  	 * @param sFileName �ļ�
  	 * @param aReplaceArr  ���滻���ַ�����
  	 * @return �ַ���
  	 * */
  	public static String file2String(String sFileName,String[][] aReplaceArr) throws Exception {
		//����ģ���ļ�������
		String sReturn = "";
		FileReader fr = new FileReader(sFileName);
		BufferedReader br = new BufferedReader(fr);//����BufferedReader���󣬲�ʵ����Ϊbr
		String Line=br.readLine();//���ļ���ȡһ���ַ���
		//�ж϶�ȡ�����ַ����Ƿ�Ϊ��
		while(Line!=null){
			sReturn += Line;
			Line=br.readLine();//���ļ��м�����ȡһ������
		}
		br.close();//�ر�BufferedReader����
		fr.close();//�ر��ļ�
		
		//�滻�����ж�����ַ�
		if (aReplaceArr!=null){
			for(int i=0;i<aReplaceArr.length;i++){
				if(aReplaceArr[i][0]!=null)
					sReturn = replaceStr(sReturn,aReplaceArr[i][0],aReplaceArr[i][1]);
			}
		}		
		return sReturn;
	}


  	/**
  	 * @describe ��ȡ�ļ�,�����ַ���
  	 * @param sModulePath �ļ�
  	 * @return �ַ���
  	 * */
  	public static String file2String(String sFileName) throws Exception {
  		return file2String(sFileName,null);
	}
  	
  	
	/**
	 * ɾ���ļ�
	 * @param sFileName �ļ�
	 * */
	public static boolean deleteFile(String sFileName) {   
		File myFile;   
		boolean success=false;   
		try{   
			myFile = new File(sFileName);   
			if(myFile.exists()){   
				myFile.delete();   
				success = true;   
			}   
		} catch(Exception e) { 
		}   
		return success;   
	}
	
	/**
	 * �½��ļ�
	 * @param sFileName �ļ�
	 * */
    public static boolean createFile(String filename) throws IOException {
    	File file = new File(filename);
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    } 

	/**
	 * �½��ļ�
	 * @param sFileName �ļ�
	 * */
    public static boolean createFile(File file) throws IOException {  
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    }  
      
    /** �����ļ���
     */  
    public static void makeDir(File dir) {  
        if(! dir.getParentFile().exists()) {  
            makeDir(dir.getParentFile());  
        }  
        dir.mkdir();  
    } 

    /**
     * �޸��ļ���������ʱ�䡣
     * ����ļ��������򴴽����ļ���
     * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ�
 ���С�</b>
     * @param file ��Ҫ�޸�������ʱ����ļ���
     * @since   0.1
     */
    public static void touch(File file) {
      long currentTime = System.currentTimeMillis();
      if (!file.exists()) {
    	LogUtils.errorPrint("file not found:" + file.getName());
        LogUtils.infoPrint("Create a new file:" + file.getName());
        try {
          if (file.createNewFile()) {
        	  LogUtils.debugPrint("success!");
          }
          else {
        	  LogUtils.errorPrint("Create file failed!");
          }
        }
        catch (IOException e) {
        	LogUtils.errorPrint("Create file failed!", e);
        }
      }
      boolean result = file.setLastModified(currentTime);
      if (!result) {
    	  LogUtils.errorPrint("touch failed: " + file.getName());
      }
    }
    /**
     * �޸��ļ���������ʱ�䡣
     * ����ļ��������򴴽����ļ���
     * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ�
 ���С�</b>
     * @param fileName ��Ҫ�޸�������ʱ����ļ����ļ�����
     * @since   0.1
     */
    public static void touch(String fileName) {
      File file = new File(fileName);
      touch(file);
    }
    /**
     * �޸��ļ���������ʱ�䡣
     * ����ļ��������򴴽����ļ���
     * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ�
 ���С�</b>
     * @param files ��Ҫ�޸�������ʱ����ļ����顣
     * @since   0.1
     */
    public static void touch(File[] files) {
      for (int i = 0; i < files.length; i++) {
        touch(files);
      }
    }
    /**
     * �޸��ļ���������ʱ�䡣
     * ����ļ��������򴴽����ļ���
     * <b>Ŀǰ�����������Ϊ��ʽ�����ȶ�����Ҫ�Ƿ�����Щ��Ϣ�������Щ��Ϣ����Ƿ������ڿ�
 ���С�</b>
     * @param fileNames ��Ҫ�޸�������ʱ����ļ������顣
     * @since   0.1
     */
    public static void touch(String[] fileNames) {
      File[] files = new File[fileNames.length];
      for (int i = 0; i < fileNames.length; i++) {
        files[i] = new File(fileNames[i]);
      }
      touch(files);
    }
    /**
     * �ж�ָ�����ļ��Ƿ���ڡ�
     * @param fileName Ҫ�жϵ��ļ����ļ���
     * @return ����ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean isFileExist(String fileName) {
      return new File(fileName).isFile();
    }
    /**
     * �ж�ָ�����ļ��Ƿ���ڡ�
     * @param file Ҫ�жϵ��ļ�
     * @return ����ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean isFileExist(File file) {
      return file.isFile();
    }
    /**
     * ����ָ����Ŀ¼��
     * ���ָ����Ŀ¼�ĸ�Ŀ¼�������򴴽���Ŀ¼����������Ҫ�ĸ�Ŀ¼��
     * <b>ע�⣺���ܻ��ڷ���false��ʱ�򴴽����ָ�Ŀ¼��</b>
     * @param file Ҫ������Ŀ¼
     * @return ��ȫ�����ɹ�ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean makeDirectory(File file) {
      File parent = file.getParentFile();
      if (parent != null) {
        return parent.mkdirs();
      }
      return false;
    }
    /**
     * ����ָ����Ŀ¼��
     * ���ָ����Ŀ¼�ĸ�Ŀ¼�������򴴽���Ŀ¼����������Ҫ�ĸ�Ŀ¼��
     * <b>ע�⣺���ܻ��ڷ���false��ʱ�򴴽����ָ�Ŀ¼��</b>
     * @param fileName Ҫ������Ŀ¼��Ŀ¼��
     * @return ��ȫ�����ɹ�ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean makeDirectory(String fileName) {
      File file = new File(fileName);
      return makeDirectory(file);
    }
    /**
     * ���ָ��Ŀ¼�е��ļ���
     * ���������������ɾ�����е��ļ�������ֻҪ��һ���ļ�û�б�ɾ�����᷵��false��
     * ������������������ɾ����������ɾ����Ŀ¼�������ݡ�
     * @param directory Ҫ��յ�Ŀ¼
     * @return Ŀ¼�µ������ļ������ɹ�ɾ��ʱ����true�����򷵻�false.
     * @since   0.1
     */
    public static boolean emptyDirectory(File directory) {
      boolean result = false;
      File[] entries = directory.listFiles();
      for (int i = 0; i < entries.length; i++) {
        if (!entries[i].delete()) {
          result = false;
        }
      }
      return result;
    }
    /**
     * ���ָ��Ŀ¼�е��ļ���
     * ���������������ɾ�����е��ļ�������ֻҪ��һ���ļ�û�б�ɾ�����᷵��false��
     * ������������������ɾ����������ɾ����Ŀ¼�������ݡ�
     * @param directoryName Ҫ��յ�Ŀ¼��Ŀ¼��
     * @return Ŀ¼�µ������ļ������ɹ�ɾ��ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean emptyDirectory(String directoryName) {
      File dir = new File(directoryName);
      return emptyDirectory(dir);
    }
    /**
     * ɾ��ָ��Ŀ¼�����е��������ݡ�
     * @param dirName Ҫɾ����Ŀ¼��Ŀ¼��
     * @return ɾ���ɹ�ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean deleteDirectory(String dirName) {
      return deleteDirectory(new File(dirName));
    }
    /**
     * ɾ��ָ��Ŀ¼�����е��������ݡ�
     * @param dir Ҫɾ����Ŀ¼
     * @return ɾ���ɹ�ʱ����true�����򷵻�false��
     * @since   0.1
     */
    public static boolean deleteDirectory(File dir) {
      if ( (dir == null) || !dir.isDirectory()) {
        throw new IllegalArgumentException("Argument " + dir +
                                           " is not a directory. ");
      }
      File[] entries = dir.listFiles();
      int sz = entries.length;
      for (int i = 0; i < sz; i++) {
        if (entries[i].isDirectory()) {
          if (!deleteDirectory(entries[i])) {
            return false;
          }
        }
        else {
          if (!entries[i].delete()) {
            return false;
          }
        }
      }
      if (!dir.delete()) {
        return false;
      }
      return true;
    }

    /**
     * �����ļ���URL��ַ��
     * @param file �ļ�
     * @return �ļ���Ӧ�ĵ�URL��ַ
     * @throws MalformedURLException
     * @since   0.4
     * @deprecated ��ʵ�ֵ�ʱ��û��ע�⵽File�౾���һ��toURL�������ļ�·��ת��ΪURL��
     *              ��ʹ��File.toURL������
     */
    public static URL getURL(File file) throws MalformedURLException {
      String fileURL = "file:/" + file.getAbsolutePath();
      URL url = new URL(fileURL);
      return url;
    }
    /**
     * ���ļ�·���õ��ļ�����
     * @param filePath �ļ���·�������������·��Ҳ�����Ǿ���·��
     * @return ��Ӧ���ļ���
     * @since   0.4
     */
    public static String getFileName(String filePath) {
      File file = new File(filePath);
      return file.getName();
    }
    /**
     * ���ļ����õ��ļ�����·����
     * @param fileName �ļ���
     * @return ��Ӧ���ļ�·��
     * @since   0.4
     */
    public static String getFilePath(String fileName) {
      File file = new File(fileName);
      return file.getAbsolutePath();
    }
    /**
     * ��DOS/Windows��ʽ��·��ת��ΪUNIX/Linux��ʽ��·����
     * ��ʵ���ǽ�·���е�"\"ȫ����Ϊ"/"����Ϊ��ĳЩ���������ת��Ϊ���ַ�ʽ�ȽϷ��㣬
     * ĳ�г̶���˵"/"��"\"���ʺ���Ϊ·���ָ���������DOS/WindowsҲ��������·���ָ�����
     * @param filePath ת��ǰ��·��
     * @return ת�����·��
     * @since   0.4
     */
    public static String toUNIXpath(String filePath) {
      return filePath.replace('\\', '/');
    }
    /**
     * ���ļ����õ�UNIX�����ļ�����·����
     * @param fileName �ļ���
     * @return ��Ӧ��UNIX�����ļ�·��
     * @since   0.4
     * @see #toUNIXpath(String filePath) toUNIXpath
     */
    public static String getUNIXfilePath(String fileName) {
      File file = new File(fileName);
      return toUNIXpath(file.getAbsolutePath());
    }
    /**
     * �õ��ļ������͡�
     * ʵ���Ͼ��ǵõ��ļ��������һ����.������Ĳ��֡�
     * @param fileName �ļ���
     * @return �ļ����е����Ͳ���
     * @since   0.5
     */
    public static String getTypePart(String fileName) {
      int point = fileName.lastIndexOf('.');
      int length = fileName.length();
      if (point == -1 || point == length - 1) {
        return "";
      }
      else {
        return fileName.substring(point + 1, length);
      }
    }
    /**
     * �õ��ļ������͡�
     * ʵ���Ͼ��ǵõ��ļ��������һ����.������Ĳ��֡�
     * @param file �ļ�
     * @return �ļ����е����Ͳ���
     * @since   0.5
     */
    public static String getFileType(File file) {
      return getTypePart(file.getName());
    }
    /**
     * �õ��ļ������ֲ��֡�
     * ʵ���Ͼ���·���е����һ��·���ָ�����Ĳ��֡�
     * @param fileName �ļ���
     * @return �ļ����е����ֲ���
     * @since   0.5
     */
    public static String getNamePart(String fileName) {
      int point = getPathLsatIndex(fileName);
      int length = fileName.length();
      if (point == -1) {
        return fileName;
      }
      else if (point == length - 1) {
        int secondPoint = getPathLsatIndex(fileName, point - 1);
        if (secondPoint == -1) {
          if (length == 1) {
            return fileName;
          }
          else {
            return fileName.substring(0, point);
          }
        }
        else {
          return fileName.substring(secondPoint + 1, point);
        }
      }
      else {
        return fileName.substring(point + 1);
      }
    }
    /**
     * �õ��ļ����еĸ�·�����֡�
     * ������·���ָ�������Ч��
     * ������ʱ����""��
     * ����ļ�������·���ָ�����β���򲻿��Ǹ÷ָ���������"/path/"����""��
     * @param fileName �ļ���
     * @return ��·���������ڻ����Ѿ��Ǹ�Ŀ¼ʱ����""
     * @since   0.5
     */
    public static String getPathPart(String fileName) {
      int point = getPathLsatIndex(fileName);
      int length = fileName.length();
      if (point == -1) {
        return "";
      }
      else if (point == length - 1) {
        int secondPoint = getPathLsatIndex(fileName, point - 1);
        if (secondPoint == -1) {
          return "";
        }
        else {
          return fileName.substring(0, secondPoint);
        }
      }
      else {
        return fileName.substring(0, point);
      }
    }
    /**
     * �õ�·���ָ������ļ�·�����״γ��ֵ�λ�á�
     * ����DOS����UNIX���ķָ��������ԡ�
     * @param fileName �ļ�·��
     * @return ·���ָ�����·�����״γ��ֵ�λ�ã�û�г���ʱ����-1��
     * @since   0.5
     */
    public static int getPathIndex(String fileName) {
      int point = fileName.indexOf('/');
      if (point == -1) {
        point = fileName.indexOf('\\');
      }
      return point;
    }
    /**
     * �õ�·���ָ������ļ�·����ָ��λ�ú��״γ��ֵ�λ�á�
     * ����DOS����UNIX���ķָ��������ԡ�
     * @param fileName �ļ�·��
     * @param fromIndex ��ʼ���ҵ�λ��
     * @return ·���ָ�����·����ָ��λ�ú��״γ��ֵ�λ�ã�û�г���ʱ����-1��
     * @since   0.5
     */
    public static int getPathIndex(String fileName, int fromIndex) {
      int point = fileName.indexOf('/', fromIndex);
      if (point == -1) {
        point = fileName.indexOf('\\', fromIndex);
      }
      return point;
    }
    /**
     * �õ�·���ָ������ļ�·���������ֵ�λ�á�
     * ����DOS����UNIX���ķָ��������ԡ�
     * @param fileName �ļ�·��
     * @return ·���ָ�����·���������ֵ�λ�ã�û�г���ʱ����-1��
     * @since   0.5
     */
    public static int getPathLsatIndex(String fileName) {
      int point = fileName.lastIndexOf('/');
      if (point == -1) {
        point = fileName.lastIndexOf('\\');
      }
      return point;
    }
    /**
     * �õ�·���ָ������ļ�·����ָ��λ��ǰ�����ֵ�λ�á�
     * ����DOS����UNIX���ķָ��������ԡ�
     * @param fileName �ļ�·��
     * @param fromIndex ��ʼ���ҵ�λ��
     * @return ·���ָ�����·����ָ��λ��ǰ�����ֵ�λ�ã�û�г���ʱ����-1��
     * @since   0.5
     */
    public static int getPathLsatIndex(String fileName, int fromIndex) {
      int point = fileName.lastIndexOf('/', fromIndex);
      if (point == -1) {
        point = fileName.lastIndexOf('\\', fromIndex);
      }
      return point;
    }
    /**
     * ���ļ����е����Ͳ���ȥ����
     * @param filename �ļ���
     * @return ȥ�����Ͳ��ֵĽ��
     * @since   0.5
     */
    public static String trimType(String filename) {
      int index = filename.lastIndexOf(".");
      if (index != -1) {
        return filename.substring(0, index);
      }
      else {
        return filename;
      }
    }
    /**
     * �õ����·����
     * �ļ�������Ŀ¼�����ӽڵ�ʱ�����ļ�����
     * @param pathName Ŀ¼��
     * @param fileName �ļ���
     * @return �õ��ļ��������Ŀ¼�������·����Ŀ¼�²����ڸ��ļ�ʱ�����ļ���
     * @since   0.5
     */
    public static String getSubpath(String pathName,String fileName) {
      int index = fileName.indexOf(pathName);
      if (index != -1) {
        return fileName.substring(index + pathName.length() + 1);
      }
      else {
        return fileName;
      }
    }
    /**
     * ������·���õ�����·���ľ���·����
     * @param pathName Ŀ¼��
     * @param fileName �ļ���
     * @return �õ��ļ��������Ŀ¼�������·����Ŀ¼�²����ڸ��ļ�ʱ�����ļ���
     * @since   0.5
     */
    public static String getConfigAbsolutepath() {
    	String fileUrlPath = FileUtils.getPathPart(FileUtils.class.getResource("/").getPath());
    	String os = System.getProperty("os.name");
    	if(os.toLowerCase().startsWith("win")){  
    		fileUrlPath = fileUrlPath.substring(1,fileUrlPath.length());
    	}
		return fileUrlPath;
    }
	
	public static void main(String[] args) {
		try {
//			String str = FileUtils.class.getClass().getResource("/").getPath();
//			str.substring(str.indexOf("/WEB-INF"));
//			str = str.substring(1,str.indexOf("/WEB-INF"))+"/help/html/";
//			FileUtils.writeFileFromUrl(str+"noticelist.jsp", "http://localhost:8080/qsh_p2p/help/noticelist.jsp");
			File file = new File("1");
			System.out.println(file.getAbsolutePath());
			System.out.println(file.getCanonicalPath());
			System.out.println(file.getParent());
			System.out.println(file.getPath());
			System.out.println(file.getTotalSpace());
			System.out.println(file.getAbsoluteFile());
			
		} catch (Exception e) {
//			System.out.println(e.getCause());
//			System.out.println(e.toString());
//			System.out.println(e.getClass());
//			System.out.println(e.getStackTrace());
//			System.out.println(e.getLocalizedMessage());
//			System.out.println(e.getMessage());
//			StackTraceElement[] ste = e.getStackTrace();
//			for (StackTraceElement stackTraceElement : ste) {
//				System.out.println(stackTraceElement);
//			}
		}
	}
}

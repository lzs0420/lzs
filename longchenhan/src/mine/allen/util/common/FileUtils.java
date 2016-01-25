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
	 * 查看流文件
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
	 * 组合文件名
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
				LogUtils.errorPrint("at FileUtils.getFullName: 文件[" + dFile + "]不存在！！！");
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
				LogUtils.errorPrint("at FileUtils.getFullName: 文件[" + sFullPath + "]不存在！！！");
			}
		} else {
			LogUtils.errorPrint("at FileUtils.getFullName: 文件 [" + FilePath + "]不存在！！！");
		}
		return StringL.convert2Reg(sFullPath);
	}
	
	/**替换字符串*/
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
  	 * 替换特殊字符
  	 * <br>. 和？
  	 */
  	public static String replaceStr(String str) {
  		String sSource = str;
  		sSource = replaceStr(sSource,"·","&#8226;");
  		sSource = replaceStr(sSource,"?","&copy;");
  		return sSource;
  	}

  	
  	/**
  	 * 抓取页面内容
  	 * @param  sUrl  页面地址
  	 * @param  scode 编码格式
  	 * @return 返回字符串
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
  	 * 默认的GBK编码格式抓取页面内容
  	 * @param  sUrl  页面地址
  	 * @return 返回字符串
  	 * **/
	public static String url2String(String sUrl) throws IOException {
		return url2String(sUrl,"GBK");
	}

	
	/**
	 * 将指定的内容写入指定的文件
	 * @param fileName  文件 
	 * @param source   待写入的内容
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
  	 * @describe 根据sUrl，生成文件sFile
  	 * @param sFile 生成的文件
  	 * @param sUrl  来源页面
  	 * */
  	public static void writeFileFromUrl(String sFile, String sUrl) throws IOException {
  		String sSource = url2String(sUrl);
  		sSource = replaceStr(sSource);
  		writeFile(sFile,sSource);
  	}
  	

  	/**
  	 * @describe 读取文件,返回字符串
  	 * @param sFileName 文件
  	 * @param aReplaceArr  需替换的字符数组
  	 * @return 字符串
  	 * */
  	public static String file2String(String sFileName,String[][] aReplaceArr) throws Exception {
		//根据模版文件绑定数据
		String sReturn = "";
		FileReader fr = new FileReader(sFileName);
		BufferedReader br = new BufferedReader(fr);//建立BufferedReader对象，并实例化为br
		String Line=br.readLine();//从文件读取一行字符串
		//判断读取到的字符串是否不为空
		while(Line!=null){
			sReturn += Line;
			Line=br.readLine();//从文件中继续读取一行数据
		}
		br.close();//关闭BufferedReader对象
		fr.close();//关闭文件
		
		//替换数组中定义的字符
		if (aReplaceArr!=null){
			for(int i=0;i<aReplaceArr.length;i++){
				if(aReplaceArr[i][0]!=null)
					sReturn = replaceStr(sReturn,aReplaceArr[i][0],aReplaceArr[i][1]);
			}
		}		
		return sReturn;
	}


  	/**
  	 * @describe 读取文件,返回字符串
  	 * @param sModulePath 文件
  	 * @return 字符串
  	 * */
  	public static String file2String(String sFileName) throws Exception {
  		return file2String(sFileName,null);
	}
  	
  	
	/**
	 * 删除文件
	 * @param sFileName 文件
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
	 * 新建文件
	 * @param sFileName 文件
	 * */
    public static boolean createFile(String filename) throws IOException {
    	File file = new File(filename);
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    } 

	/**
	 * 新建文件
	 * @param sFileName 文件
	 * */
    public static boolean createFile(File file) throws IOException {  
        if(! file.exists()) {  
            makeDir(file.getParentFile());  
        }  
        return file.createNewFile();  
    }  
      
    /** 创建文件夹
     */  
    public static void makeDir(File dir) {  
        if(! dir.getParentFile().exists()) {  
            makeDir(dir.getParentFile());  
        }  
        dir.mkdir();  
    } 

    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考
 虑中。</b>
     * @param file 需要修改最后访问时间的文件。
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
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考
 虑中。</b>
     * @param fileName 需要修改最后访问时间的文件的文件名。
     * @since   0.1
     */
    public static void touch(String fileName) {
      File file = new File(fileName);
      touch(file);
    }
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考
 虑中。</b>
     * @param files 需要修改最后访问时间的文件数组。
     * @since   0.1
     */
    public static void touch(File[] files) {
      for (int i = 0; i < files.length; i++) {
        touch(files);
      }
    }
    /**
     * 修改文件的最后访问时间。
     * 如果文件不存在则创建该文件。
     * <b>目前这个方法的行为方式还不稳定，主要是方法有些信息输出，这些信息输出是否保留还在考
 虑中。</b>
     * @param fileNames 需要修改最后访问时间的文件名数组。
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
     * 判断指定的文件是否存在。
     * @param fileName 要判断的文件的文件名
     * @return 存在时返回true，否则返回false。
     * @since   0.1
     */
    public static boolean isFileExist(String fileName) {
      return new File(fileName).isFile();
    }
    /**
     * 判断指定的文件是否存在。
     * @param file 要判断的文件
     * @return 存在时返回true，否则返回false。
     * @since   0.1
     */
    public static boolean isFileExist(File file) {
      return file.isFile();
    }
    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param file 要创建的目录
     * @return 完全创建成功时返回true，否则返回false。
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
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param fileName 要创建的目录的目录名
     * @return 完全创建成功时返回true，否则返回false。
     * @since   0.1
     */
    public static boolean makeDirectory(String fileName) {
      File file = new File(fileName);
      return makeDirectory(file);
    }
    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directory 要清空的目录
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
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
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directoryName 要清空的目录的目录名
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
     * @since   0.1
     */
    public static boolean emptyDirectory(String directoryName) {
      File dir = new File(directoryName);
      return emptyDirectory(dir);
    }
    /**
     * 删除指定目录及其中的所有内容。
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     * @since   0.1
     */
    public static boolean deleteDirectory(String dirName) {
      return deleteDirectory(new File(dirName));
    }
    /**
     * 删除指定目录及其中的所有内容。
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
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
     * 返回文件的URL地址。
     * @param file 文件
     * @return 文件对应的的URL地址
     * @throws MalformedURLException
     * @since   0.4
     * @deprecated 在实现的时候没有注意到File类本身带一个toURL方法将文件路径转换为URL。
     *              请使用File.toURL方法。
     */
    public static URL getURL(File file) throws MalformedURLException {
      String fileURL = "file:/" + file.getAbsolutePath();
      URL url = new URL(fileURL);
      return url;
    }
    /**
     * 从文件路径得到文件名。
     * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since   0.4
     */
    public static String getFileName(String filePath) {
      File file = new File(filePath);
      return file.getName();
    }
    /**
     * 从文件名得到文件绝对路径。
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since   0.4
     */
    public static String getFilePath(String fileName) {
      File file = new File(fileName);
      return file.getAbsolutePath();
    }
    /**
     * 将DOS/Windows格式的路径转换为UNIX/Linux格式的路径。
     * 其实就是将路径中的"\"全部换为"/"，因为在某些情况下我们转换为这种方式比较方便，
     * 某中程度上说"/"比"\"更适合作为路径分隔符，而且DOS/Windows也将它当作路径分隔符。
     * @param filePath 转换前的路径
     * @return 转换后的路径
     * @since   0.4
     */
    public static String toUNIXpath(String filePath) {
      return filePath.replace('\\', '/');
    }
    /**
     * 从文件名得到UNIX风格的文件绝对路径。
     * @param fileName 文件名
     * @return 对应的UNIX风格的文件路径
     * @since   0.4
     * @see #toUNIXpath(String filePath) toUNIXpath
     */
    public static String getUNIXfilePath(String fileName) {
      File file = new File(fileName);
      return toUNIXpath(file.getAbsolutePath());
    }
    /**
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     * @param fileName 文件名
     * @return 文件名中的类型部分
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
     * 得到文件的类型。
     * 实际上就是得到文件名中最后一个“.”后面的部分。
     * @param file 文件
     * @return 文件名中的类型部分
     * @since   0.5
     */
    public static String getFileType(File file) {
      return getTypePart(file.getName());
    }
    /**
     * 得到文件的名字部分。
     * 实际上就是路径中的最后一个路径分隔符后的部分。
     * @param fileName 文件名
     * @return 文件名中的名字部分
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
     * 得到文件名中的父路径部分。
     * 对两种路径分隔符都有效。
     * 不存在时返回""。
     * 如果文件名是以路径分隔符结尾的则不考虑该分隔符，例如"/path/"返回""。
     * @param fileName 文件名
     * @return 父路径，不存在或者已经是父目录时返回""
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
     * 得到路径分隔符在文件路径中首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中首次出现的位置，没有出现时返回-1。
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
     * 得到路径分隔符在文件路径中指定位置后首次出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置后首次出现的位置，没有出现时返回-1。
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
     * 得到路径分隔符在文件路径中最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @return 路径分隔符在路径中最后出现的位置，没有出现时返回-1。
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
     * 得到路径分隔符在文件路径中指定位置前最后出现的位置。
     * 对于DOS或者UNIX风格的分隔符都可以。
     * @param fileName 文件路径
     * @param fromIndex 开始查找的位置
     * @return 路径分隔符在路径中指定位置前最后出现的位置，没有出现时返回-1。
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
     * 将文件名中的类型部分去掉。
     * @param filename 文件名
     * @return 去掉类型部分的结果
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
     * 得到相对路径。
     * 文件名不是目录名的子节点时返回文件名。
     * @param pathName 目录名
     * @param fileName 文件名
     * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
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
     * 根据类路径得到配置路径的绝对路径。
     * @param pathName 目录名
     * @param fileName 文件名
     * @return 得到文件名相对于目录名的相对路径，目录下不存在该文件时返回文件名
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

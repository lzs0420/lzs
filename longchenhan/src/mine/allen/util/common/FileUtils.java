package mine.allen.util.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

public class FileUtils {

	/**
	 * 查看流文件
	 * @param filePath
	 * @return byte[]
	 */
	public static byte[] toByteArray(String filePath){
		byte[] result = null;
		filePath = filePath.replace("\\\\/", "/");
		System.out.println("filePath[BytesViewHandler]="+filePath);
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
			System.out.println("read file:" + filePath + "(size="+result.length+")");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				if(bis!=null)bis.close();
				if(bos!=null)bis.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return result;
	}
}

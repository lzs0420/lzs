package mine.allen.util.common;

import java.security.NoSuchAlgorithmException;

/**
 * 关于加密字符串
 * @author Allen
 *
 */
public class MessageDigest{
	
	/**
	 * 指定加密模式加密字节数组
	 * @param algorithm 为null时默认MD5
	 * @param srcData byte[]
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getDigest(String algorithm, byte[] srcData)throws NoSuchAlgorithmException{
	    byte[] dgt = null;
	    if (srcData == null) return dgt;
	    if (algorithm == null) algorithm = "MD5";
	    java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm.toUpperCase());
	    md.reset();
	    md.update(srcData);
	    dgt = md.digest();
	    return dgt;
	}
	
	/**
	 * 指定加密模式加密字符串
	 * @param algorithm 为null时默认MD5
	 * @param srcData String
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getDigest(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return getDigest(algorithm, srcData.getBytes());
	}
	
	/**
	 * 将字符串加密转化成16进制小写字符串
	 * @param algorithm 为null时默认MD5
	 * @param srcData
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsLowerHexString(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return StringL.bytesToHexString(getDigest(algorithm, srcData.getBytes()), false);
	}
	
	/**
	 * 将字符串加密转化成16进制大写字符串
	 * @param algorithm 为null时默认MD5
	 * @param srcData
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsUpperHexString(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return StringL.bytesToHexString(getDigest(algorithm, srcData.getBytes()), true);
	}
  
	/**
	 * 将字节数组加密转化成16进制小写字符串
	 * @param algorithm 为null时默认MD5
	 * @param srcData byte[]
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsLowerHexString(String algorithm, byte[] srcData)throws NoSuchAlgorithmException{
		return StringL.bytesToHexString(getDigest(algorithm, srcData), false);
	}
  
	/**
	 * 将字节数组加密转化成16进制大写字符串
	 * @param algorithm 为null时默认MD5
	 * @param srcData byte[]
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsUpperHexString(String algorithm, byte[] srcData)throws NoSuchAlgorithmException{
		return StringL.bytesToHexString(getDigest(algorithm, srcData), true);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException {
		String[] arrays = {"long","chen","han","hou","fan","zhu"};
		for (String string : arrays) {
			System.err.println(MessageDigest.getDigestAsLowerHexString(null, string));
		}
	}
}

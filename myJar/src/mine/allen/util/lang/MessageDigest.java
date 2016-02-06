package mine.allen.util.lang;

import java.security.NoSuchAlgorithmException;

/**
 * ���ڼ����ַ���
 * @author Allen
 * @version 1.0 For All Projects 
 */
public class MessageDigest{
	
	/**
	 * ָ������ģʽ�����ֽ�����
	 * @param algorithm ΪnullʱĬ��MD5
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
	 * ָ������ģʽ�����ַ���
	 * @param algorithm ΪnullʱĬ��MD5
	 * @param srcData String
	 * @return byte[]
	 * @throws NoSuchAlgorithmException
	 */
	public static byte[] getDigest(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return getDigest(algorithm, srcData.getBytes());
	}
	
	/**
	 * ���ַ�������ת����16����Сд�ַ���
	 * @param algorithm ΪnullʱĬ��MD5
	 * @param srcData
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsLowerHexString(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return StringL.bytesToHexString(getDigest(algorithm, srcData.getBytes()), false);
	}
	
	/**
	 * ���ַ�������ת����16���ƴ�д�ַ���
	 * @param algorithm ΪnullʱĬ��MD5
	 * @param srcData
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsUpperHexString(String algorithm, String srcData)throws NoSuchAlgorithmException{
	    if (srcData == null) return null;
	    return StringL.bytesToHexString(getDigest(algorithm, srcData.getBytes()), true);
	}
  
	/**
	 * ���ֽ��������ת����16����Сд�ַ���
	 * @param algorithm ΪnullʱĬ��MD5
	 * @param srcData byte[]
	 * @return String
	 * @throws NoSuchAlgorithmException
	 */
	public static String getDigestAsLowerHexString(String algorithm, byte[] srcData)throws NoSuchAlgorithmException{
		return StringL.bytesToHexString(getDigest(algorithm, srcData), false);
	}
  
	/**
	 * ���ֽ��������ת����16���ƴ�д�ַ���
	 * @param algorithm ΪnullʱĬ��MD5
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

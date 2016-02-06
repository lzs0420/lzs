package mine.allen.util.lang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.BreakIterator;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;

/**
 * ����String��һЩ����
 * 
 * @author Allen
 * @version 1.0 For All Projects 
 * @since 2016/01/20 15:36
 */
public class StringL {

	/**
	 * ����(")
	 */
	public static final String HTML_QUOTE = "&quot;";
	/**
	 * &
	 */
	public static final String HTML_AMP = "&amp;";
	/**
	 * ������(<)
	 */
	public static final String HTML_LT = "&lt;";
	/**
	 * ������(>)
	 */
	public static final String HTML_GT = "&gt;";
	/**
	 * �ո�( )
	 */
	public static final String SPACE = "&nbsp;";

	// Constants used by escapeHTMLTags
	private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	private static final char[] AMP_ENCODE = "&amp;".toCharArray();
	private static final char[] LT_ENCODE = "&lt;".toCharArray();
	private static final char[] GT_ENCODE = "&gt;".toCharArray();

	/**
	 * ��ȡ����
	 * 
	 * @return yyyy/MM/dd
	 */
	public static String getToday() {
		return new SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date());
	}

	/**
	 * ����ָ����ʽ��ȡ����
	 * 
	 * @param sFormat
	 * @return
	 */
	public static String getToday(String sFormat) {
		java.util.Date dToday = new java.util.Date();
		String sToday = new java.sql.Date(dToday.getTime()).toString();
		sToday = replace(sToday, "-", sFormat);
		return sToday;
	}

	/**
	 * ��ȡ��ǰʱ�䣬û������
	 * 
	 * @return HH:mm:ss
	 */
	public static String getNow() {
		Calendar rightNow = Calendar.getInstance();
		return String.valueOf(String.valueOf(rightNow.get(11)) + ":"
				+ String.valueOf(rightNow.get(12)) + ":"
				+ String.valueOf(rightNow.get(13)));
	}

	/**
	 * ��ȡ��ǰʱ�䣬û������
	 * 
	 * @return HH:mm:ss,SSS
	 */
	private static String getLogNow() {
		Calendar rightNow = Calendar.getInstance();
		String hh = rightNow.get(11) < 10 ? "0"
				+ String.valueOf(rightNow.get(11)) : String.valueOf(rightNow.get(11));
		String mm = rightNow.get(12) < 10 ? "0"
				+ String.valueOf(rightNow.get(12)) : String.valueOf(rightNow.get(12));
		String ss = rightNow.get(13) < 10 ? "0"
				+ String.valueOf(rightNow.get(13)) : String.valueOf(rightNow.get(13));
		String sss = rightNow.get(14) < 100 ? (rightNow.get(14) < 10 ? "00"+ String.valueOf(rightNow.get(14)) : "0"
				+ String.valueOf(rightNow.get(14))) : String.valueOf(rightNow.get(14));
		return String.valueOf(hh + ":" + mm + ":" + ss + "," + sss);
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return yyyy/MM/dd HH:mm:ss
	 */
	public static String getTodayNow() {
		return getToday("/") + " " + getNow();
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return yyyy/MM/dd HH:mm:ss,SSS
	 */
	public static String getLogTodayNow() {
		return getToday("-") + " " + getLogNow();
	}

	/***
	 * �滻�ַ���
	 * 
	 * @param s
	 * @param sOld
	 * @param sNew
	 * @return
	 */
	public static String replace(String s, String sOld, String sNew) {
		if ((s != null) && (s.length() < 256)) {
			int iPos = s.indexOf(sOld, 0);
			while (iPos != -1) {
				s = s.substring(0, iPos) + sNew
						+ s.substring(iPos + sOld.length());
				iPos = s.indexOf(sOld, iPos + sNew.length());
			}
			return s;
		}
		if (s == null) {
			return null;
		}

		Pattern p = null;
		Matcher mm = null;

		p = Pattern.compile(convert2Reg(sOld));
		mm = p.matcher(s);
		return mm.replaceAll(sNew);
	}

	/**
	 * Ϊ�ַ������ת���ַ�"\"
	 * 
	 * @param src
	 * @return
	 */
	public static String convert2Reg(String src) {
		Hashtable<Character, Character> hs = new Hashtable<Character, Character>();
		hs.put(new Character('\n'), new Character('n'));
		hs.put(new Character('\r'), new Character('r'));
		hs.put(new Character('\\'), new Character('\\'));
		hs.put(new Character('{'), new Character('{'));
		hs.put(new Character('['), new Character('['));
		hs.put(new Character('$'), new Character('$'));
		hs.put(new Character('^'), new Character('^'));
		hs.put(new Character('|'), new Character('|'));
		hs.put(new Character('('), new Character('('));
		hs.put(new Character(')'), new Character(')'));
		hs.put(new Character('*'), new Character('*'));
		hs.put(new Character('+'), new Character('+'));
		StringBuffer sb = new StringBuffer();
		char[] arr = src.toCharArray();

		for (int i = 0; i < arr.length; i++) {
			Character ch = new Character(arr[i]);
			if (hs.containsKey(ch)) {
				sb.append('\\');
				sb.append(((Character) hs.get(ch)).charValue());
			} else {
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * �Ƴ����пո�
	 * 
	 * @param s
	 * @return
	 */
	public static String removeSpaces(String s) {
		StringTokenizer st = new StringTokenizer(s, " ", false);
		String t = "";
		while (st.hasMoreElements())
			t = t + st.nextElement();
		return t;
	}

	/**
	 * toString
	 * 
	 * @param s
	 * @return
	 */
	public static String toString(Object s) {
		String t = null;
		if (s == null)
			t = "";
		else
			t = s.toString();
		return t;
	}

	/**
	 * ����ȫ·����ȡ�ļ���
	 * 
	 * @param filePathName
	 * @return
	 */
	public static String getFileName(String filePathName) {
		filePathName = filePathName == null ? "" : filePathName;
		int pos = 0;
		pos = filePathName.lastIndexOf('/');
		if (pos != -1)
			return filePathName.substring(pos + 1, filePathName.length());
		pos = filePathName.lastIndexOf('\\');
		if (pos != -1) {
			return filePathName.substring(pos + 1, filePathName.length());
		}
		return filePathName;
	}

	/**
	 * ����iBeginPos֮�����һ��sTarget��λ��
	 * 
	 * @param sSource
	 * @param sTarget
	 * @param sDelim1
	 * @param sDelim2
	 * @param iBeginPos
	 * @return
	 */
	public static int indexOf(String sSource, String sTarget, String sDelim1,
			String sDelim2, int iBeginPos) {
		int iPos = sSource.indexOf(sTarget, iBeginPos);
		while ((iPos >= 0)
				&& (getOccurTimes(sSource, sDelim1, sDelim2, iBeginPos, iPos) != 0))
			iPos = sSource.indexOf(sTarget, iPos + sTarget.length());
		return iPos;
	}

	/**
	 * ����iBeginPos, iEndPos֮���sSource��sTarget���ֵ�λ��
	 * 
	 * @param sSource
	 * @param sTarget
	 * @param iBeginPos
	 * @param iEndPos
	 * @return
	 */
	public static int indexOf(String sSource, String sTarget, int iBeginPos,
			int iEndPos) {
		return sSource.substring(iBeginPos, iEndPos).indexOf(sTarget);
	}

	/**
	 * ����iBeginPos, iEndPos֮���sSource��sDelim1���ֵĴ���-sDelim2���ֵĴ���<br>
	 * ���sDelim1=sDelim2������sDelim1���ֵĴ���%2��0��1��
	 * 
	 * @param sSource
	 * @param sDelim1
	 * @param sDelim2
	 * @param iBeginPos
	 * @param iEndPos
	 * @return
	 */
	public static int getOccurTimes(String sSource, String sDelim1,
			String sDelim2, int iBeginPos, int iEndPos) {
		sSource = sSource.substring(iBeginPos, iEndPos);
		if (sDelim1.equals(sDelim2)) {
			return getOccurTimes(sSource, sDelim1) % 2;
		}
		return getOccurTimes(sSource, sDelim1)
				- getOccurTimes(sSource, sDelim2);
	}

	/**
	 * ��ȡsDelim���ֵĴ���
	 * 
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getOccurTimes(String sSource, String sDelim) {
		int iPos = 0;
		int iCount = 0;
		int iDelLen = sDelim.length();
		while ((iPos = sSource.indexOf(sDelim, iPos) + iDelLen) > iDelLen - 1)
			iCount++;
		return iCount;
	}

	/**
	 * ��ȡsDelim���ֵĴ��������Դ�Сд��
	 * 
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getOccurTimesIgnoreCase(String sSource, String sDelim) {
		int iPos = 0;
		int iCount = 0;
		int iDelLen = sDelim.length();
		while ((iPos = sSource.toLowerCase()
				.indexOf(sDelim.toLowerCase(), iPos) + iDelLen) > iDelLen - 1)
			iCount++;
		return iCount;
	}

	/**
	 * ��ȡ��iOrder-1��sDelim�͵�iOrder��sDelim֮����ַ���
	 * 
	 * @param sSource
	 * @param sDelim
	 * @param iOrder
	 * @return
	 */
	public static String getSeparate(String sSource, String sDelim, int iOrder) {
		int iLastPos = 0;
		int iCount = 0;
		int iDelLen = sDelim.length();
		sSource = sSource + sDelim;
		int iPos;
		while ((iPos = sSource.indexOf(sDelim, iLastPos)) >= 0) {
			iCount++;
			if (iCount == iOrder)
				return sSource.substring(iLastPos, iPos);
			iLastPos = iPos + iDelLen;
		}
		return "";
	}

	/**
	 * Stringת����Array����sDelimΪ�����
	 * 
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	String[] toStringArray(String sSource, String sDelim) {
		String[] sList = null;
		int iCount = getSeparateSum(sSource, sDelim);
		if (iCount > 0) {
			sList = new String[iCount];
			for (int i = 1; i <= iCount; i++)
				sList[(i - 1)] = getSeparate(sSource, sDelim, i);
		}
		return sList;
	}

	/**
	 * Arrayת����String����sDelimΪ�����
	 * 
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static String toArrayString(String[] sSource, String sDelim) {
		String sList = "";
		int iCount = sSource.length;

		for (int i = 1; i <= iCount; i++) {
			sList = sList + sDelim + sSource[(i - 1)];
		}
		if (sList.length() > 0)
			sList = sList.substring(sDelim.length());
		return sList;
	}

	/**
	 * dateת����string�������sFormat <br>
	 * yyyy<b>sFormat</b>MM<b>sFormat</b>dd
	 * 
	 * @deprecated
	 * @param dateDate
	 * @param sFormat
	 * @return String
	 */
	public static String date2String(java.util.Date dateDate, String sFormat) {
		String sToday = new java.sql.Date(dateDate.getTime()).toString();
		sToday = replace(sToday, "-", sFormat);
		return sToday;
	}

	/**
	 * ��code����data�ַ���
	 * 
	 * @param data
	 * @param code
	 * @return String[]
	 */
	public static String[] encodingStrings(String[] data, String code) {
		int length = data.length;
		String[] result = new String[length];
		try {
			for (int i = 0; i < length; i++) {
				result[i] = new String(data[i].getBytes(code));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	/**
	 * key=value*****delim<br>
	 * ����value
	 * 
	 * @param data
	 * @param key
	 * @param delim
	 * @return String
	 */
	public static String getProfileString(String data, String key, String delim) {
		if (data == null) {
			return "";
		}
		int curIndex = data.indexOf(key + "=");
		if (curIndex < 0) {
			return "";
		}
		int endIndex = (data + delim).indexOf(delim, curIndex);
		if (endIndex < 0) {
			return "";
		}
		int beginIndex = data.indexOf("=", curIndex);
		if (beginIndex < 0) {
			return "";
		}
		return data.substring(beginIndex + 1, endIndex);
	}

	/**
	 * key=value*****;<br>
	 * ����value<br>
	 * Ĭ�ϷֺŽ�β
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static String getProfileString(String data, String key) {
		return getProfileString(data, key, ";");
	}

	/**
	 * ����data��key1Ϊ�ָ������ַ��������� ��order-1���͵�order��key2֮����ַ��� ��ɵ�����
	 * toStringArray("12131;14151;" , ";" , "1" , 2)<br>
	 * ����[2][4]
	 * 
	 * @param data
	 * @param key1
	 * @param key2
	 * @param order
	 * @return
	 */
	public static String[] toStringArray(String data, String key1, String key2,
			int order) {
		if ((key1 == null) || (key1.equals("")))
			key1 = " ";
		if ((key2 == null) || (key2.equals(""))) {
			key2 = " ";
		}
		int iCount = getSeparateSum(data, key1);
		String[] sArray = new String[iCount];
		for (int i = 1; i <= iCount; i++) {
			String sTemp = getSeparate(data, key1, i);
			sArray[(i - 1)] = getSeparate(sTemp, key2, order);
		}
		return sArray;
	}

	/**
	 * �Ƿ�����<br>
	 * isLike(Stringxxx,String)����true
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	public static boolean isLike(String source, String destination) {
		int iSourceLength = source.length();
		int iDesLength = destination.length();
		if (replace(destination, "%", "").length() > iSourceLength) {
			return false;
		}
		int j = 0;
		int i = 0;
		boolean isLike = true;
		char chSource = ' ';
		char chDes = ' ';

		while (((i < iSourceLength) || (j < iDesLength)) && (isLike)) {
			if (i < iSourceLength)
				chSource = source.charAt(i);
			if (j < iDesLength) {
				chDes = destination.charAt(j);
			}
			if (chSource == chDes) {
				i++;
				j++;
			} else if (chDes == '_') {
				if ((j < iDesLength) && (i < iSourceLength)) {
					return isLike(source.substring(i + 1),
							destination.substring(j + 1));
				}
				isLike = false;
			} else if (chDes == '%') {
				int underLines = 0;
				j++;
				while (((chDes == '_') || (chDes == '%')) && (j < iDesLength)) {
					if (chDes == '_')
						underLines++;
					chDes = destination.charAt(j);
					j++;
				}
				j--;
				if (chDes != '%') {
					boolean subLike = false;
					i = source.indexOf(chDes, i + underLines);

					while ((i >= 0) && (!subLike)) {
						subLike = isLike(source.substring(i),
								destination.substring(j));
						i = source.indexOf(chDes, i + 1);
					}
					return subLike;
				}
				i++;
				j = iDesLength;
			} else {
				isLike = false;
			}
		}
		return isLike;
	}

	/**
	 * yyyy�ָ���MM��ʽ�ַ�������ָ������������
	 * 
	 * @param sAccountMonth
	 * @param sType
	 *            month��year
	 * @param iStep
	 *            ���ӵ�����������
	 * @return
	 */
	public static String getRelativeAccountMonth(String sAccountMonth,
			String sType, int iStep) {
		String sYear = sAccountMonth.substring(0, 4);
		String sMonth = sAccountMonth.substring(5, 7);

		int iYear = Integer.valueOf(sYear).intValue();
		int iMonth = Integer.valueOf(sMonth).intValue();

		if (sType.equalsIgnoreCase("year")) {
			iYear += iStep;
		} else if (sType.equalsIgnoreCase("month")) {
			iYear += (iMonth + iStep) / 12;
			iMonth = (iMonth + iStep) % 12;
			if (iMonth <= 0) {
				iYear--;
				iMonth += 12;
			}
		}

		sYear = String.valueOf(iYear);
		sMonth = String.valueOf(iMonth);
		if (sMonth.length() == 1) {
			sMonth = "0" + sMonth;
		}

		return sYear + "/" + sMonth;
	}

	/**
	 * double���ͻ��Ҹ�ʽ����ת���ɴ�д�����ַ���,�Զ�ȡ��λС��
	 * 
	 * @param doubleNum
	 * @return
	 */
	public static String numberToChinese(double doubleNum) {
		DecimalFormat df = new DecimalFormat("############0.00");
		String sNum = df.format(doubleNum);

		int pointPos = sNum.indexOf(".");

		if (sNum.substring(pointPos).compareTo(".00") == 0) {
			sNum = sNum.substring(0, pointPos);
		}
		String temp = "";
		String[] sBIT = new String[4];
		String[] sUNIT = new String[4];
		String[] sCents = new String[2];
		String sIntD = "";
		String sDecD = "";
		String NtoC = "";
		int iCount = 0;
		int lStartPos = 0;
		int iLength = 0;

		sBIT[0] = "";
		sBIT[1] = "ʰ";
		sBIT[2] = "��";
		sBIT[3] = "Ǫ";
		sUNIT[0] = "";
		sUNIT[1] = "��";
		sUNIT[2] = "��";
		sUNIT[3] = "��";
		sCents[0] = "��";
		sCents[1] = "��";

		if ((sNum.compareTo("0") == 0) || (sNum.compareTo("0.0") == 0)
				|| (sNum.compareTo("0.00") == 0)) {
			NtoC = "��Ԫ��";
			return NtoC;
		}

		if (sNum.indexOf(".") > 0) {
			temp = sNum.substring(0, sNum.indexOf("."));
		} else
			temp = sNum;
		iCount = temp.length() % 4 != 0 ? temp.length() / 4 + 1
				: temp.length() / 4;

		for (int i = iCount; i >= 1; i--) {
			if ((i == iCount) && (temp.length() % 4 != 0)) {
				iLength = temp.length() % 4;
			} else
				iLength = 4;
			sIntD = temp.substring(lStartPos, lStartPos + iLength);
			for (int j = 0; j < sIntD.length(); j++) {
				if (Integer.parseInt(sIntD.substring(j, j + 1)) != 0) {
					switch (Integer.parseInt(sIntD.substring(j, j + 1))) {
					case 1:
						NtoC = NtoC + "Ҽ" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 2:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 3:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 4:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 5:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 6:
						NtoC = NtoC + "½" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 7:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 8:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];
						break;
					case 9:
						NtoC = NtoC + "��" + sBIT[(sIntD.length() - j - 1)];

					}

				} else if ((j + 1 < sIntD.length())
						&& (sIntD.charAt(j + 1) != '0'))
					NtoC = NtoC + "��";
			}
			lStartPos += iLength;
			if (i < iCount) {
				if ((Integer.parseInt(sIntD.substring(sIntD.length() - 1,
						sIntD.length())) != 0)
						|| (Integer.parseInt(sIntD.substring(
								sIntD.length() - 2, sIntD.length() - 1)) != 0)
						|| (Integer.parseInt(sIntD.substring(
								sIntD.length() - 3, sIntD.length() - 2)) != 0)
						|| (Integer.parseInt(sIntD.substring(
								sIntD.length() - 4, sIntD.length() - 3)) != 0)) {

					NtoC = NtoC + sUNIT[(i - 1)];
				}
			} else
				NtoC = NtoC + sUNIT[(i - 1)];
		}
		if (NtoC.length() > 0) {
			NtoC = NtoC + "Ԫ";
		}

		if (sNum.indexOf(".") > 0) {
			sDecD = sNum.substring(sNum.indexOf(".") + 1);
			for (int i = 0; i < 2; i++) {
				if (Integer.parseInt(sDecD.substring(i, i + 1)) != 0) {
					switch (Integer.parseInt(sDecD.substring(i, i + 1))) {
					case 1:
						NtoC = NtoC + "Ҽ" + sCents[(1 - i)];
						break;
					case 2:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 3:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 4:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 5:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 6:
						NtoC = NtoC + "½" + sCents[(1 - i)];
						break;
					case 7:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 8:
						NtoC = NtoC + "��" + sCents[(1 - i)];
						break;
					case 9:
						NtoC = NtoC + "��" + sCents[(1 - i)];

					}

				} else if (NtoC.length() > 0) {
					NtoC = NtoC + "��";
				}
			}
		} else {
			NtoC = NtoC + "��";
		}

		if (NtoC.substring(NtoC.length() - 1).compareTo("��") == 0)
			NtoC = NtoC.substring(0, NtoC.length() - 1);
		return NtoC;
	}

	/**
	 * ��ȡ�ַ���
	 * 
	 * @param sSource
	 * @param sBeginIdentifier
	 * @param sEndIdentifier
	 * @return
	 */
	public static String subXString(String sSource, String sBeginIdentifier,
			String sEndIdentifier) {
		return sSource.substring(sSource.indexOf(sBeginIdentifier)
				+ sBeginIdentifier.length(), sSource.indexOf(sEndIdentifier));
	}

	/**
	 * string(������)ת��Ϊdate
	 * 
	 * @param str
	 * @return date
	 */
	public static Date parseDate(String str) {
		if (str == null)
			return null;
		Date d = null;
		String format = null;
		char[] split = { '/', '-', '.' };

		if (str.length() < 8)
			return d;
		if (str.length() > 10)
			str = str.substring(0, 10);
		for (int i = 0; i < split.length; i++) {
			int k = str.indexOf(split[i]);
			if (k >= 0) {
				if (k == 4) {
					format = "yyyy" + split[i] + 'M' + split[i] + 'd';
					break;
				}
				if (k < 4) {
					format = 'M' + split[i] + 100 + split[i] + "yyyy";
					break;
				}
				return null;
			}
		}

		if (format == null) {
			if (str.substring(0, 2).compareTo("12") > 0) {
				format = "yyyyMMdd";
			} else {
				format = "MMddyyyy";
			}
		}

		SimpleDateFormat df = new SimpleDateFormat(format);
		try {
			d = df.parse(str);
		} catch (ParseException e) {
			d = null;
		}
		return d;
	}

	/**
	 * �ָ��ַ���<br>
	 * {1}2{1}2{1}2{1}�ָ�Ϊ�ַ�����[1][1][1][1]
	 * 
	 * @param str
	 * @return String[]
	 */
	public static String[] parseArray(String str) {
		if (str == null)
			return null;
		ArrayList<String> l = new ArrayList<String>();
		int sc = 0;
		int ec = 0;
		int p = 0;
		int sp = -1;
		while (p < str.length()) {
			if (str.charAt(p) == '{') {
				sc++;
				if (sc == 1)
					sp = p + 1;
			} else if (str.charAt(p) == '}') {
				ec++;
				if (ec == sc) {
					l.add(str.substring(sp, p));

					sp = -1;
					sc = 0;
					ec = 0;
				}
			}
			p++;
		}
		return (String[]) l.toArray(new String[0]);
	}

	/**
	 * [1=2][2=1][0=0]ת��Ϊ<br>
	 * 1=2<br>
	 * 2=1<br>
	 * 0=0<br>
	 * ����Properties
	 * 
	 * @param str
	 * @return Properties
	 */
	public static Properties parsePropertie(String str) {
		Properties p = new Properties();
		String[] s = parseArray(str);
		if ((s != null) && (s.length > 0))
			for (int i = 0; i < s.length; i++) {
				int p0 = s[i].indexOf('=');
				if ((p0 >= 1) && (p0 != s[i].length())) {
					String n = trimAll(s[i].substring(0, p0));
					if (n != "") {
						String v = s[i].substring(p0 + 1);
						p.setProperty(n, v);
					}
				}
			}
		return p;
	}

	/**
	 * ���ֽ�����ת��Ϊ16�����ַ���,toUpperCaseΪtrue��д��toUpperCaseΪfalseСд
	 * 
	 * @param bytes
	 * @param toUpperCase
	 * @return String
	 */
	public static String bytesToHexString(byte[] bytes, boolean toUpperCase) {
		if (bytes == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			int t = bytes[i];
			sb.append(Integer.toString(t >> 4 & 0xF, 16)).append(
					Integer.toString(t & 0xF, 16));
		}

		String ret = sb.toString();
		return toUpperCase ? ret.toUpperCase() : ret.toLowerCase();
	}

	/**
	 * ��ȥ�ַ�����ͷ�ķָ���
	 * 
	 * @param str
	 * @return String
	 */
	public static String trimStart(String str) {
		String s = null;
		int p = 0;
		if (str != null) {
			for (p = 0; p < str.length(); p++) {
				if (!Character.isSpaceChar(str.charAt(p))) {
					break;
				}
			}
			s = p < str.length() ? str.substring(p) : "";
		}
		return s;
	}

	/**
	 * ��ȥ�ַ�����β�ķָ���
	 * 
	 * @param str
	 * @return String
	 */
	public static String trimEnd(String str) {
		String s = null;
		int p = 0;
		if (str != null) {
			for (p = str.length() - 1; p >= 0; p--) {
				if (!Character.isSpaceChar(str.charAt(p))) {
					break;
				}
			}
			s = p >= 0 ? str.substring(0, p + 1) : "";
		}
		return s;
	}

	/**
	 * ��ȥ�ַ�����ͷ��β�ķָ���
	 * 
	 * @param str
	 * @return String
	 */
	public static String trimAll(String str) {
		String s = null;
		if (str != null) {
			s = trimEnd(trimStart(str));
		}
		return s;
	}

	/**
	 * ����Ƿ�Ϊnull��""��ͷΪ�ָ���,�Ƿ���true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isSpace(String str) {
		if (str == null)
			return true;
		boolean r = true;
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isSpaceChar(str.charAt(i))) {
				r = false;
				break;
			}
		}
		return r;
	}

	/**
	 * ����Ƿ�Ϊnull��""��ͷΪ�ָ���,�Ƿ���true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isSpace(Object o) {
		return (isEmpty(o)) || (isSpace((String) o));
	}

	/**
	 * ��ȡ�ַ���<br>
	 * �����...
	 */
	public static String screenStr(String str, int len) {

		if (str == null)
			return "";
		else {
			if (str.length() < len)
				return str;
			else
				return str.substring(0, len) + "...";
		}
	}

	/***
	 * @param day
	 *            �����ַ��� ft ��ʽ(����0�롢1���ӡ�2Сʱ��3�졢4�¡�5��) x �Ӽ�
	 * @return �����ַ���
	 * 
	 * */
	public static String addDateFormat(String day, int ft, int x, String fmt) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 24Сʱ��
		SimpleDateFormat format1 = new SimpleDateFormat(fmt);// 24Сʱ��
		int caft = 0;
		if (ft == 0) {// ��
			caft = Calendar.SECOND;
		} else if (ft == 1) {// ����
			caft = Calendar.MINUTE;
		} else if (ft == 2) {// Сʱ
			caft = Calendar.HOUR;
		} else if (ft == 3) {// ��
			caft = Calendar.DATE;
		} else if (ft == 4) {// ��
			caft = Calendar.MONTH;
		} else {// ��
			caft = Calendar.YEAR;
		}
		day = day.replaceAll("-", "/");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		// System.out.println("front:" + format.format(date)); //��ʾ���������
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(caft, x);// 24Сʱ��
		date = cal.getTime();
		// System.out.println("after:" + format.format(date)); //��ʾ���º������
		cal = null;
		return format1.format(date);
	}

	/***
	 * @param day
	 *            �����ַ��� ft ��ʽ(����0�롢1���ӡ�2Сʱ��3�졢4�¡�5��) x �Ӽ�
	 * @return �����ַ���yyyy��MM��dd�� HHʱmm��ss��
	 * 
	 * */
	public static String addDateFormat(String day, int ft, int x) {
		return addDateFormat(day, ft, x, "yyyy��MM��dd�� HHʱmm��ss��");
	}

	/**
	 * yyyy-MM-dd HH:mm:ss str��ʽ
	 * 
	 * @param str
	 * @return ʱ����
	 */
	public static String getBetweenTime(String str) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sReturn = "";
		str = str.replaceAll("/", "-");
		try {
			java.util.Date begin = new Date();
			java.util.Date end = dfs.parse(str);
			long between = (end.getTime() - begin.getTime()) / 1000;// ����1000��Ϊ��ת������
			long day = between / (24 * 3600);
			long hour = (between - day * 86400) / 3600;
			long minute = (between - day * 86400 - hour * 3600) / 60;
			long second = between - day * 86400 - hour * 3600 - minute * 60;

			if (day < 0)
				sReturn = "�ѽ���";
			else {
				if (day > 0)
					sReturn = day + "��" + hour + "Сʱ" + minute + "��" + second
							+ "��";
				if (day == 0)
					sReturn = hour + "Сʱ" + minute + "��" + second + "��";
				if (hour == 0)
					sReturn = minute + "��" + second + "��";
				if (minute == 0)
					sReturn = second + "��";
				if (second < 0)
					sReturn = "�ѿ�ʼ";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sReturn;
	}

	/**
	 * ��ȡ����ַ���<br>
	 * length��ʾ�����ַ����ĳ���<br>
	 * type:1 ֻ������ <br>
	 * type:2 ��ĸ����
	 * 
	 * @param length
	 * @param type
	 * @return
	 */
	public static String getRandomString(int length, int type) {
		String base = null;
		String base1 = "0123456789";
		String base2 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
		switch (type) {
		case 1:
			base = base1;
			break;
		case 2:
			base = base2;
			break;
		default:
			base = base1;
			break;
		}
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * �ֽ������ı�ת��
	 * 
	 * @param inStream
	 * @param charsetName
	 *            ���뷽ʽ
	 * @return String
	 */
	public static String inputStream2String(InputStream inStream,
			String charsetName) {
		try {
			ByteArrayOutputStream ois = new ByteArrayOutputStream();
			byte[] buffer = new byte[10240];
			int b = inStream.read(buffer);
			while (b > -1) {
				ois.write(buffer, 0, b);
				b = inStream.read(buffer);
			}
			String sResult = new String(ois.toByteArray(), charsetName);
			return sResult;

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * �ı�������ת��
	 * 
	 * @param content
	 * @param charsetName
	 */
	public static InputStream string2InputStream(String content,
			String charsetName) {
		try {
			byte[] bytes = content.getBytes(charsetName);
			ByteArrayInputStream ins = new ByteArrayInputStream(bytes);
			return ins;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ���sDelim��sSource�е�����
	 * 
	 * @param sSource
	 * @param sDelim
	 * @return
	 */
	public static int getSeparateSum(String sSource, String sDelim) {
		if (sSource == null)
			return 0;
		int iPos = 0;
		int iCount = 0;
		int iDelLen = sDelim.length();
		while ((iPos = sSource.indexOf(sDelim, iPos) + iDelLen) > iDelLen - 1)
			iCount++;
		return iCount + 1;
	}

	/**
	 * "{{\"1\",\"2\"},{\"3\",\"4\"}}"<br>
	 * ת��Ϊ�ַ�������
	 * 
	 * @param s
	 * @return
	 */
	public static String[][] getStringArray(String s) {
		int beginDelim = getSeparateSum(s, "{") - 1;
		int endDelim = getSeparateSum(s, "}") - 1;
		int totalColDelim = getSeparateSum(s, "\"") - 1;
		int totalElement = totalColDelim / 2;
		int rowCount = beginDelim - 1;
		int colCount = 0;
		int rowPoint = 0;
		int colPoint = 0;
		colCount = totalElement / rowCount;
		String sTemp = "";
		if ((beginDelim < 2) || (beginDelim != endDelim)
				|| (totalColDelim % 2 != 0)) {
			return (String[][]) null;
		}

		String sCur = s.trim();
		sCur = sCur.substring(sCur.indexOf("{") + 1, sCur.lastIndexOf("}"))
				.trim();

		String[][] strArray2 = new String[rowCount][colCount];
		for (int irow = 0; irow < rowCount; irow++) {
			beginDelim = sCur.indexOf("{", rowPoint) + 1;
			endDelim = sCur.indexOf("}", rowPoint);
			rowPoint = endDelim + 1;
			if (beginDelim > endDelim) {
				return (String[][]) null;
			}
			sTemp = sCur.substring(beginDelim, endDelim).trim();
			System.out.println(sTemp);
			for (int icol = 0; icol < colCount; icol++) {
				beginDelim = sTemp.indexOf("\"", colPoint) + 1;
				endDelim = sTemp.indexOf("\"", beginDelim);
				strArray2[irow][icol] = sTemp.substring(beginDelim, endDelim);
				System.out.println("strArray2[" + irow + "][" + icol + "]="
						+ strArray2[irow][icol]);
				colPoint = endDelim + 1;
			}
			colPoint = 0;
			if (rowPoint >= sCur.length())
				break;
		}
		return strArray2;
	}
	
	/**
	   * �滻���з���Ҫ����ַ���.
	   * 
	   * @param line
	   *          ԭ�ַ���
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   */
		public static final String replaceAll(String line, String oldString,
				String newString) {
			return replace(line, oldString, newString, 0, false);
		}

		/**
	   * �滻���з���Ҫ����ַ���(StringBuffer).
	   * 
	   * @param line
	   *          ԭ�ַ���(StringBuffer)
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final StringBuffer replaceAll(StringBuffer line,
				String oldString, String newString) {
			if (line == null || oldString == null)
				return line;
			else {
				int pos = line.indexOf(oldString);
				return line.replace(pos, pos + oldString.length(), newString);
			}
		}

		/**
	   * �滻��һ������Ҫ����ַ���.
	   * 
	   * @param line
	   *          ԭ�ַ���(StringBuffer)
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final String replaceFirst(String line, String oldString,
				String newString) {
			// ���� replace, ֻ�滻һ��
			return replace(line, oldString, newString, 0, false);
		}

		/**
	   * �滻�����ַ��������Դ�Сд
	   * 
	   * @param line
	   *          ԭ�ַ���(StringBuffer)
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final String replaceIgnoreCase(String line, String oldString,
				String newString) {
			return replace(line, oldString, newString, 0, true);
		}

		/**
	   * �滻�����ַ��������Դ�Сд
	   * 
	   * @param line
	   *          ԭ�ַ���(StringBuffer)
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final String replaceIgnoreCase(String line, String oldString,
				String newString, int count) {
			return replace(line, oldString, newString, count, true);
		}

		/**
	   * �滻��1���ַ��������Դ�Сд
	   * 
	   * @param line
	   *          ԭ�ַ���(StringBuffer)
	   * @param oldString
	   *          ���滻�ַ���, ��֧��������ʽ.
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final String replaceFirstIgnoreCase(String line,
				String oldString, String newString) {
			return replace(line, oldString, newString, 1, true);
		}

		/**
	   * �滻�ַ���. ���ַ��� line �����е� oldString �滻�� newString. ���� count �Ǳ�ʾ�滻����.
	   * 
	   * @param line
	   *          �ַ���
	   * @param oldString
	   *          ���滻���ַ���, ��֧��������ʽ.
	   * @param newString
	   *          �����滻 oldString ���ַ���
	   * @param count
	   *          �滻����, <= 0 ʱ����ȫ���滻
	   * 
	   * @return �����滻����ַ���
	   */
		public static final String replace(String line, String oldString,
				String newString, int count) {
			return replace(line, oldString, newString, count, false);
		}

		/**
	   * �滻ָ������ָ����ʼλ�õ�����λ�õ��ַ���.
	   * 
	   * <pre>
	   *      ����:
	   *        replace(&quot;12345678&quot;, 1, 2, &quot;AB&quot;)
	   *        ���� &quot;1AB345678&quot;
	   *     
	   *        replace(&quot;12345678&quot;, 1, 1, &quot;A&quot;)
	   *        ���� &quot;1A2345678&quot;
	   * </pre>
	   * 
	   * @param line
	   *          ���滻���ַ���
	   * @param start
	   *          ��ʼλ��
	   * @param end
	   *          ����λ��
	   * @param newString
	   *          ���ַ���
	   * @return �滻��Ľ���ַ���
	   */
		public static final String replace(String line, int start, int end,
				String newString) {
			if (line == null || newString == null) {
				return line;
			} else {
				String ls_Prefix;
				String ls_Sufix;
				if (start > line.length())
					ls_Prefix = "";
				else
					ls_Prefix = line.substring(0, start);
				if (end > line.length())
					ls_Sufix = "";
				else
					ls_Sufix = line.substring(end + 1);
				return ls_Prefix + newString + ls_Sufix;
			}
		}

		/**
	   * �滻����.
	   * 
	   * <pre>
	   *      ���ݵ��ʽ����滻���������һ�������ڲ����򲻽����滻.
	   *      ����:
	   *        replaceAllWord(&quot;i am a student&quot;, &quot;a&quot;, &quot;A&quot;)
	   *        ����: i am A student
	   *        
	   *      ��Ƚϵ�:
	   *        &quot;i am a student&quot;.replaceAll(&quot;a&quot;, &quot;A&quot;)
	   *        ����: &quot;i Am A student&quot;
	   * </pre>
	   * 
	   * @param baseString
	   *          ���滻��
	   * @param oldString
	   *          ���滻�ĵ���
	   * @param newString
	   *          �µĵ���
	   * @return String ���
	   */
		public static final String replaceAllWord(String baseString,
				String oldString, String newString) {
			return replaceAllWord(baseString, oldString, newString, null);
		}

		/**
	   * �滻����.
	   * 
	   * <pre>
	   *      ���ݵ��ʽ����滻���������һ�������ڲ����򲻽����滻.
	   *      ����:
	   *        replaceAllWord(&quot;i am a student&quot;, &quot;a&quot;, &quot;A&quot;)
	   *        ����: i am A student
	   *        
	   *      ��Ƚϵ�:
	   *        &quot;i am a student&quot;.replaceAll(&quot;a&quot;, &quot;A&quot;)
	   *        ����: &quot;i Am A student&quot;
	   * </pre>
	   * 
	   * @param baseString
	   *          ���滻��
	   * @param oldString
	   *          ���滻�ĵ���
	   * @param newString
	   *          �µĵ���
	   * @param identString
	   *          ��ʶ����ĸ
	   * @return String ���
	   */
		public static final String replaceAllWord(String baseString,
				String oldString, String newString, String identString) {
			if (baseString == null || oldString == null || newString == null) {
				return baseString;
			} else {
				int li_Pos = 0;
				String ls_Return = baseString;
				if (oldString.length() > 0) {
					while (li_Pos >= 0) {
						boolean lb_Replace = true;
						li_Pos = ls_Return.indexOf(oldString, li_Pos);
						if (li_Pos >= 0) {
							if (li_Pos > 0) {
								if (isExpression(ls_Return.substring(li_Pos - 1, li_Pos),
										identString)) {
									lb_Replace = false;
								}
							}
							if (li_Pos + oldString.length() < baseString.length()) {
								if (isExpression(ls_Return.substring(li_Pos + oldString.length(),
										li_Pos + oldString.length() + 1), identString)) {
									lb_Replace = false;
								}
							}
							if (lb_Replace) {
								ls_Return = replace(ls_Return, li_Pos, li_Pos
										+ oldString.length() - 1, newString);
								li_Pos += newString.length();
							}
							li_Pos++;
						}
					}
				}
				return ls_Return;
			}
		}

		/**
	   * ���������滻�ַ���.
	   * 
	   * <pre>
	   *      �������ݻ���������(baseMask)�а����滻������(replaceMask)�����, ���滻������(baseString).
	   *      ����:
	   *        replaceByMask(&quot;1234&quot;, &quot;A&quot;, &quot;X??X&quot;, &quot;?&quot;) 
	   *        ���� &quot;1AA34&quot;
	   *     
	   *        replaceByMask(&quot;12345678&quot;, &quot;ABCD&quot;, &quot;X?X?X?X?&quot;, &quot;?&quot;)
	   *        ���� &quot;1A3B5C7D&quot;
	   * </pre>
	   * 
	   * @param baseString
	   *          ������
	   * @param replaceString
	   *          �滻��
	   * @param baseMask
	   *          ������������
	   * @param replaceMask
	   *          �滻��������
	   * @return �滻��Ľ����
	   */
		public static final String replaceByMask(String baseString,
				String replaceString, String baseMask, String replaceMask) {
			if (baseString == null || replaceString == null || baseMask == null
					|| replaceMask == null) {
				return baseString;
			} else {
				String ls_Result = baseString;

				int li_Pos = 0;
				int li_PosOfReplace = 0;
				while (li_Pos >= 0) {
					li_Pos = baseMask.indexOf(replaceMask, li_Pos);
					if (li_Pos >= 0) {
						String ls_ReplaceString = replaceString.substring(li_PosOfReplace);
						// ����õ������ݳ����滻�����룬���ȡ
						if (ls_ReplaceString.length() > replaceMask.length()) {
							ls_ReplaceString = ls_ReplaceString.substring(0, replaceMask
									.length());
						} else if (ls_ReplaceString.length() < replaceMask.length()) {
							ls_ReplaceString += replaceString.substring(0, replaceMask.length()
									- ls_ReplaceString.length());
						}

						li_PosOfReplace += ls_ReplaceString.length();
						if (li_PosOfReplace >= replaceString.length())
							li_PosOfReplace = 0;

						ls_Result = replace(ls_Result, li_Pos, li_Pos + replaceMask.length()
								- 1, ls_ReplaceString);
						li_Pos++;
					}
				}
				return ls_Result;
			}
		}

		/**
	   * ��ȡ�ַ�����һ��������. �ӿ�ʼλ��(start)һֱ������λ��(end). ��ʼλ��(start) �ͽ���λ��(end) �� 0 ��ʼ����.
	   * ���ַ���Ϊ��(null)��start < 0��end < 0��start > end���򷵻ؿ�(null).
	   * 
	   * @param start
	   *          ��ʼλ��
	   * @param end
	   *          ����λ��
	   */
		public static final String substring(String baseString, int start, int end) {
			if (baseString == null)
				return null;
			else if (start >= baseString.length() || start < 0 || end < 0
					|| start > end)
				return null;
			else if (end >= baseString.length())
				return baseString.substring(start);
			else {
				return baseString.substring(start, end);
			}
		}

		/**
	   * ��ȡ�ַ�����һ��������. �ӿ�ʼλ��(start)һֱ���ַ���δ. ��ʼλ��(start) �� 0 ��ʼ����. ���ַ���Ϊ��(null)��start <
	   * 0 �򷵻ؿ�(null).
	   * 
	   * @param start
	   *          ��ʼλ��
	   */
		public static final String substring(String baseString, int start) {
			if (baseString == null)
				return null;
			else
				return substring(baseString, start, baseString.length());
		}

		/**
	   * �����������Ӵ�
	   * 
	   * <pre>
	   *      �����Ӵ�����(subMask)��Դ������(baseMask)�е�������Դ��(baseString)���Ӵ�.
	   *      ����:
	   *        subString(&quot;12345678&quot;, &quot;XX??XX??&quot;, &quot;?&quot;)
	   *        ���� &quot;3478&quot;
	   * </pre>
	   * 
	   * @param baseString
	   *          Դ��
	   * @param baseMask
	   *          Դ������
	   * @param subMask
	   *          �Ӵ�����
	   * @return �����Ӵ�
	   */
		public static final String substringByMask(String baseString,
				String baseMask, String subMask) {
			if ((baseString == null) || (baseMask == null) || (subMask == null)) {
				return null;
			} else if (isEmpty(baseString))
				return baseString;
			else {
				String ls_Return = "";
				int li_Pos = 0;
				while (li_Pos >= 0) {
					li_Pos = baseMask.indexOf(subMask, li_Pos);
					if (li_Pos >= 0) {
						ls_Return += baseString.substring(li_Pos, li_Pos + subMask.length());
						li_Pos += subMask.length();
					}
				}
				return ls_Return;
			}
		}

		/**
	   * ���ݷָ����Ż���Ӵ�.
	   * <p>
	   * ���ؿ�ʼ�ָ����źͽ����ָ�����֮����ַ���.
	   * <pre>
	   *      ����:
	   *        substringBySperator(&quot;China is a big [country].&quot;, &quot;[&quot;, &quot;]&quot;)
	   *        ���� &quot;country&quot;
	   *      ���������У�������ԭ�ַ����С�[���͡�]��֮������ݡ�
	   * </pre>
	   * @param baseString
	   *          Դ��
	   * @param startSperator
	   *          ��ʼ�ָ�����
	   * @param finishSperator
	   *          �����ָ�����
	   * @return �����Ӵ�
	   */
		public static final String substringBySperator(String baseString,
				String startSperator, String finishSperator) {
			if ((baseString == null)) {
				return null;
			} else {
				String ls_Return = null;
				int li_PosOfStart = -1;
				int li_PosOfFinish = -1;
				int li_LenOfStartSperator = 0;

				// �õ��ĵ� 1 ���ָ�����
				if (startSperator == null) {
					li_LenOfStartSperator = 0;
					li_PosOfStart = 0;
				} else {
					li_LenOfStartSperator = startSperator.length();
					li_PosOfStart = indexOf(baseString, startSperator, 0, 1);
				}
				if (li_PosOfStart >= 0) {
					// �õ��ĵ� 2 ���ָ�����
					if (finishSperator == null)
						li_PosOfFinish = baseString.length();
					else
						li_PosOfFinish = indexOf(baseString, finishSperator, li_PosOfStart
								+ li_LenOfStartSperator, 1);
					if (li_PosOfFinish >= li_PosOfStart)
						ls_Return = substring(baseString, li_PosOfStart
								+ li_LenOfStartSperator, li_PosOfFinish);
				}
				return ls_Return;
			}
		}

		/**
	   * This method takes a string which may contain HTML tags (ie, &lt;b&gt;,
	   * &lt;table&gt;, etc) and converts the '&lt'' and '&gt;' characters to their
	   * HTML escape sequences.
	   * 
	   * @param in
	   *          the text to be converted.
	   * @return the input string with the characters '&lt;' and '&gt;' replaced
	   *         with their HTML escape sequences.
	   */
		public static final String escapeHTMLTags(String in) {
			if (in == null) {
				return null;
			}
			char ch;
			int i = 0;
			int last = 0;
			char[] input = in.toCharArray();
			int len = input.length;
			StringBuffer out = new StringBuffer((int) (len * 1.3));
			for (; i < len; i++) {
				ch = input[i];
				if (ch > '>') {
					continue;
				} else if (ch == '<') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '>') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(GT_ENCODE);
				}
			}
			if (last == 0) {
				return in;
			}
			if (i > last) {
				out.append(input, last, i - last);
			}
			return out.toString();
		}

		/**
	   * Used by the hash method.
	   */
		private static MessageDigest digest = null;

		/**
	   * Hashes a String using the Md5 algorithm and returns the result as a String
	   * of hexadecimal numbers. This method is synchronized to avoid excessive
	   * MessageDigest object creation. If calling this method becomes a bottleneck
	   * in your code, you may wish to maintain a pool of MessageDigest objects
	   * instead of using this method.
	   * <p>
	   * A hash is a one-way function -- that is, given an input, an output is
	   * easily computed. However, given the output, the input is almost impossible
	   * to compute. This is useful for passwords since we can store the hash and a
	   * hacker will then have a very hard time determining the original password.
	   * <p>
	   * In Jive, every time a user logs in, we simply take their plain text
	   * password, compute the hash, and compare the generated hash to the stored
	   * hash. Since it is almost impossible that two passwords will generate the
	   * same hash, we know if the user gave us the correct password or not. The
	   * only negative to this system is that password recovery is basically
	   * impossible. Therefore, a reset password method is used instead.
	   * 
	   * @param data
	   *          the String to compute the hash of.
	   * @return a hashed version of the passed-in String
	   */
		public synchronized static final String hash(String data) {
			if (digest == null) {
				try {
					digest = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException nsae) {
					System.err.println("Failed to load the MD5 MessageDigest. "
							+ "Jive will be unable to function normally.");
					nsae.printStackTrace();
				}
			}
			// Now, compute hash.
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		}

		/**
	   * Turns an array of bytes into a String representing each byte as an unsigned
	   * hex number.
	   * <p>
	   * Method by Santeri Paavolainen, Helsinki Finland 1996<br>
	   * (c) Santeri Paavolainen, Helsinki Finland 1996<br>
	   * Distributed under LGPL.
	   * 
	   * @param bytes
	   *          an array of bytes to convert to a hex-string
	   * @return generated hex string
	   */
		public static final String encodeHex(byte[] bytes) {
			StringBuffer buf = new StringBuffer(bytes.length * 2);
			int i;

			for (i = 0; i < bytes.length; i++) {
				if (((int) bytes[i] & 0xff) < 0x10) {
					buf.append("0");
				}
				buf.append(Long.toString((int) bytes[i] & 0xff, 16));
			}
			return buf.toString();
		}

		/**
	   * ���ַ������Ƴ�16���Ʊ������.<p>
	   * ���й����������룬������d6d0b9fa��.
	   * <p>
	   * @param str �����Ƶ��ַ���
	   * @return ������16���Ʒ��ŵı���
	   */
		public static final String encodeHexString(String str) {
			byte[] lb_Bytes = str.getBytes();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < lb_Bytes.length; i++) {
				buf.append(byteToHex(lb_Bytes[i]));
			}
			return buf.toString();
		}
		
		/**
		 * ��һ�� byte ת���� 16 ���Ʊ���.<p>
		 * @param b
		 * @return
		 */
		public static String byteToHex(byte b) {
	    // Returns hex String representation of byte b
	    char hexDigit[] = {
	       '0', '1', '2', '3', '4', '5', '6', '7',
	       '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	    };
	    char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
	    return new String(array);
	 }	

		/**
	   * ��16���Ʊ������ת�����ַ���.<p>
		 * ��d6d0b9fa������ת�����������й���.
	   * <p>
	   * @param str 16���Ʒ��ŵı���
	   * @return ת������ַ���
	   */
		public static final String decodeHexString(String str) {
			if(str == null)
				return null;
			else
				return new String(decodeHex(str));
		}
		
		/**
	   * Turns a hex encoded string into a byte array. It is specifically meant to
	   * "reverse" the toHex(byte[]) method.
	   * 
	   * @param hex
	   *          a hex encoded String to transform into a byte array.
	   * @return a byte array representing the hex String[
	   */
		public static final byte[] decodeHex(String hex) {
			if(hex == null)
				return new byte[0];
			char[] chars = hex.toCharArray();
			byte[] bytes = new byte[chars.length / 2];
			try {
				int byteCount = 0;
				for (int i = 0; i < chars.length; i += 2) {
					byte newByte = 0x00;
					newByte |= hexCharToByte(chars[i]);
					newByte <<= 4;
					newByte |= hexCharToByte(chars[i + 1]);
					bytes[byteCount] = newByte;
					byteCount++;
				}
			}
			catch(Exception e) {
				//
			}
			finally {
				//
			}
			return bytes;
		}

		/**
	   * Returns the the byte value of a hexadecmical char (0-f). It's assumed that
	   * the hexidecimal chars are lower case as appropriate.
	   * 
	   * @param ch
	   *          a hexedicmal character (0-f)
	   * @return the byte value of the character (0x00-0x0F)
	   */
		private static final byte hexCharToByte(char ch) {
			switch (ch) {
			case '0':
				return 0x00;
			case '1':
				return 0x01;
			case '2':
				return 0x02;
			case '3':
				return 0x03;
			case '4':
				return 0x04;
			case '5':
				return 0x05;
			case '6':
				return 0x06;
			case '7':
				return 0x07;
			case '8':
				return 0x08;
			case '9':
				return 0x09;
			case 'a':
				return 0x0A;
			case 'b':
				return 0x0B;
			case 'c':
				return 0x0C;
			case 'd':
				return 0x0D;
			case 'e':
				return 0x0E;
			case 'f':
				return 0x0F;
			}
			return 0x00;
		}

		/**
	   * Converts a line of text into an array of lower case words using a
	   * BreakIterator.wordInstance().
	   * <p>
	   * 
	   * This method is under the Jive Open Source Software License and was written
	   * by Mark Imbriaco.
	   * 
	   * @param text
	   *          a String of text to convert into an array of words
	   * @return text broken up into an array of words.
	   */
		public static final String[] toLowerCaseWordArray(String text) {
			if (text == null || text.length() == 0) {
				return new String[0];
			}

			ArrayList<String> wordList = new ArrayList<String>();
			BreakIterator boundary = BreakIterator.getWordInstance();
			boundary.setText(text);
			int start = 0;

			for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary
					.next()) {
				String tmp = text.substring(start, end).trim();
				// Remove characters that are not needed.
				tmp = replace(tmp, "+", "");
				tmp = replace(tmp, "/", "");
				tmp = replace(tmp, "\\", "");
				tmp = replace(tmp, "#", "");
				tmp = replace(tmp, "*", "");
				tmp = replace(tmp, ")", "");
				tmp = replace(tmp, "(", "");
				tmp = replace(tmp, "&", "");
				if (tmp.length() > 0) {
					wordList.add(tmp);
				}
			}
			return (String[]) wordList.toArray(new String[wordList.size()]);
		}

		/**
	   * Pseudo-random number generator object for use with randomString(). The
	   * Random class is not considered to be cryptographically secure, so only use
	   * these random Strings for low to medium security applications.
	   */
		private static Random randGen = new Random();

		/**
	   * Array of numbers and letters of mixed case. Numbers appear in the list
	   * twice so that there is a more equal chance that a number will be picked. We
	   * can use the array to get a random number or letter by picking a random
	   * array index.
	   */
		private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz"
				+ "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

		/**
	   * Returns a random String of numbers and letters (lower and upper case) of
	   * the specified length. The method uses the Random class that is built-in to
	   * Java which is suitable for low to medium grade security uses. This means
	   * that the output is only pseudo random, i.e., each number is mathematically
	   * generated so is not truly random.
	   * <p>
	   * 
	   * The specified length must be at least one. If not, the method will return
	   * null.
	   * 
	   * @param length
	   *          the desired length of the random String to return.
	   * @return a random String of numbers and letters of the specified length.
	   */
		public static final String randomString(int length) {
			if (length < 1) {
				return null;
			}
			// Create a char buffer to put random letters and numbers in.
			char[] randBuffer = new char[length];
			for (int i = 0; i < randBuffer.length; i++) {
				randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
			}
			return new String(randBuffer);
		}

		/**
	   * Intelligently chops a String at a word boundary (whitespace) that occurs at
	   * the specified index in the argument or before. However, if there is a
	   * newline character before <code>length</code>, the String will be chopped
	   * there. If no newline or whitespace is found in <code>string</code> up to
	   * the index <code>length</code>, the String will chopped at
	   * <code>length</code>.
	   * <p>
	   * For example, chopAtWord("This is a nice String", 10) will return "This is
	   * a" which is the first word boundary less than or equal to 10 characters
	   * into the original String.
	   * 
	   * @param string
	   *          the String to chop.
	   * @param length
	   *          the index in <code>string</code> to start looking for a
	   *          whitespace boundary at.
	   * @return a substring of <code>string</code> whose length is less than or
	   *         equal to <code>length</code>, and that is chopped at whitespace.
	   */
		public static final String chopAtWord(String string, int length) {
			if (string == null) {
				return string;
			}

			char[] charArray = string.toCharArray();
			int sLength = string.length();
			if (length < sLength) {
				sLength = length;
			}

			// First check if there is a newline character before length; if so,
			// chop word there.
			for (int i = 0; i < sLength - 1; i++) {
				// Windows
				if (charArray[i] == '\r' && charArray[i + 1] == '\n') {
					return string.substring(0, i + 1);
				}
				// Unix
				else if (charArray[i] == '\n') {
					return string.substring(0, i);
				}
			}
			// Also check boundary case of Unix newline
			if (charArray[sLength - 1] == '\n') {
				return string.substring(0, sLength - 1);
			}

			// Done checking for newline, now see if the total string is less than
			// the specified chop point.
			if (string.length() < length) {
				return string;
			}

			// No newline, so chop at the first whitespace.
			for (int i = length - 1; i > 0; i--) {
				if (charArray[i] == ' ') {
					return string.substring(0, i).trim();
				}
			}

			// Did not find word boundary so return original String chopped at
			// specified length.
			return string.substring(0, length);
		}

		/**
	   * Escapes all necessary characters in the String so that it can be used in an
	   * XML doc.
	   * 
	   * @param string
	   *          the string to escape.
	   * @return the string with appropriate characters escaped.
	   */
		public static final String escapeForXML(String string) {
			if (string == null) {
				return null;
			}
			char ch;
			int i = 0;
			int last = 0;
			char[] input = string.toCharArray();
			int len = input.length;
			StringBuffer out = new StringBuffer((int) (len * 1.3));
			for (; i < len; i++) {
				ch = input[i];
				if (ch > '>') {
					continue;
				} else if (ch == '<') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(LT_ENCODE);
				} else if (ch == '&') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(AMP_ENCODE);
				} else if (ch == '"') {
					if (i > last) {
						out.append(input, last, i - last);
					}
					last = i + 1;
					out.append(QUOTE_ENCODE);
				}
			}
			if (last == 0) {
				return string;
			}
			if (i > last) {
				out.append(input, last, i - last);
			}
			return out.toString();
		}

		/**
	   * Unescapes the String by converting XML escape sequences back into normal
	   * characters.
	   * 
	   * @param string
	   *          the string to unescape.
	   * @return the string with appropriate characters unescaped.
	   */
		public static final String unescapeFromXML(String string) {
			string = replace(string, "&lt;", "<");
			string = replace(string, "&gt;", ">");
			string = replace(string, "&quot;", "\"");
			return replace(string, "&amp;", "&");
		}

		private static final char[] zeroArray = "0000000000000000".toCharArray();

		/**
	   * Pads the supplied String with 0's to the specified length and returns the
	   * result as a new String. For example, if the initial String is "9999" and
	   * the desired length is 8, the result would be "00009999". This type of
	   * padding is useful for creating numerical values that need to be stored and
	   * sorted as character data. Note: the current implementation of this method
	   * allows for a maximum <tt>length</tt> of 16.
	   * 
	   * @param string
	   *          the original String to pad.
	   * @param length
	   *          the desired length of the new padded String.
	   * @return a new String padded with the required number of 0's.
	   */
		public static final String zeroPadString(String string, int length) {
			if (string == null || string.length() > length) {
				return string;
			}
			StringBuffer buf = new StringBuffer(length);
			buf.append(zeroArray, 0, length - string.length()).append(string);
			return buf.toString();
		}

		/**
	   * Formats a Date as a fifteen character long String made up of the Date's
	   * padded millisecond value.
	   * 
	   * @return a Date encoded as a String.
	   */
		public static final String dateToMillis(Date date) {
			return zeroPadString(Long.toString(date.getTime()), 15);
		}

		/**
	   * �Ƿ��Ǳ��ʽ
	   */
		public static boolean isExpression(String word) {
			return isExpression(word, null);
		}

		/**
	   * �Ƿ��Ǳ��ʽ
	   * 
	   * @param word
	   *          �жϵĵ���
	   * @param identString
	   *          ��ʶ����ĸ
	   */
		public static boolean isExpression(String word, String identString) {
			boolean lb_IsExpression = true;
			if (word == null)
				word = "";
			word = word.trim();
			if (word.equals("")) {
				lb_IsExpression = false;
			} else {
				if (((word.charAt(0) >= 'a') & (word.charAt(0) <= 'z'))
						|| ((word.charAt(0) >= 'A') & (word.charAt(0) <= 'Z'))
						|| ((word.charAt(0) >= '0') & (word.charAt(0) <= '9'))
						|| (word.charAt(0) == '_') || (word.charAt(0) == '$')
						|| (indexOf(identString, String.valueOf(word.charAt(0))) >= 0)
						|| (word.charAt(0) > 128)) {
					lb_IsExpression = true;
				}
				if (!(((word.charAt(word.length() - 1) >= 'a') & (word.charAt(word
						.length() - 1) <= 'z'))
						|| ((word.charAt(word.length() - 1) >= 'A') & (word.charAt(word
								.length() - 1) <= 'Z'))
						|| ((word.charAt(word.length() - 1) >= '0') & (word.charAt(word
								.length() - 1) <= '9'))
						|| (word.charAt(word.length() - 1) == '_')
						|| (word.charAt(word.length() - 1) == '$')
						|| (indexOf(identString, String.valueOf(word
								.charAt(word.length() - 1))) >= 0)
						|| (word.charAt(word.length() - 1) > 128) || (word.charAt(word
						.length() - 1) == ')'))) {
					lb_IsExpression = false;
				}
			}
			return lb_IsExpression;
		}

		public static String[] fromVector(Vector<String> v) {
			String ls_Return[] = new String[v.size()];
			for (int i = 0; i < ls_Return.length; i++) {
				if (v.get(i) == null) {
					ls_Return[i] = null;
				} else {
					ls_Return[i] = v.get(i);
				}
			}
			return ls_Return;
		}

		public static String toGBKString(String oldString) {
			if (oldString == null) {
				return null;
			} else {
				try {
					byte[] ls_temp = oldString.getBytes("ISO8859_1");
					return new String(ls_temp);
				} catch (UnsupportedEncodingException e) {
					return oldString;
				}
			}
		}

		/**
	   * ��һ���ַ����л�ȡ�ؼ��ֵ���Ϣ.
	   * 
	   * <pre>
	   *      ����ַ�������һ����Ҫ���, ���ĸ�ʽ������������Ĺ淶:
	   *     
	   *        �ؼ���=ֵ&lt;�ָ���&gt;�ؼ���=ֵ&lt;�ָ���&gt;...
	   *     
	   *      �ָ�������Ϊ:
	   *        &tilde;t: TAB
	   *        \t: TAB
	   *        &amp;: &amp;����
	   *     
	   *      ���ֵ�к��зָ�������ֵ������˫������������
	   *     
	   *      ������һЩ����:
	   *        name=Dong
	   *        name=Dong&tilde;tSex=Male
	   *        name=&quot;Dong&tilde;tWeiyong&quot;&tilde;tSex=Male
	   *     
	   *      ���ڱ�����������:
	   *        getKeyValue(&quot;name=Dong&quot;, &quot;name&quot;) ���� &quot;Dong&quot;
	   *        getKeyValue(&quot;name=Dong&quot;, &quot;sex&quot;) ���� null
	   *        getKeyValue(&quot;name=\&quot;Dong&tilde;tWeiyong\&quot;&tilde;tSex=Male&quot;, &quot;name&quot;) ���� &quot;Dong&tilde;tWeiyong&quot;
	   *     
	   * </pre>
	   * 
	   * @param valueList
	   *          �ַ���
	   * @param key
	   *          �ؼ���
	   * @param defaultValue
	   *          ȱʡֵ
	   * @return �ؼ��ֶ�Ӧ����Ϣ
	   * @see #setKeyValue
	   */
		public static final String getKeyValue(String valueList, String key,
				String defaultValue) {
			String ls_Value = getKeyValue(valueList, key);
			if (ls_Value == null)
				ls_Value = defaultValue;
			return ls_Value;
		}

		/**
	   * ��һ���ַ����л�ȡ�ؼ��ֵ���Ϣ.
	   * 
	   * <pre>
	   *      ����ַ�������һ����Ҫ���, ���ĸ�ʽ������������Ĺ淶:
	   *     
	   *        �ؼ���=ֵ&lt;�ָ���&gt;�ؼ���=ֵ&lt;�ָ���&gt;...
	   *     
	   *      �ָ�������Ϊ:
	   *        &tilde;t: TAB
	   *        \t: TAB
	   *        &amp;: &amp;����
	   *     
	   *      ���ֵ�к��зָ�������ֵ������˫������������
	   *     
	   *      ������һЩ����:
	   *        name=Dong
	   *        name=Dong&tilde;tSex=Male
	   *        name=&quot;Dong&tilde;tWeiyong&quot;&tilde;tSex=Male
	   *     
	   *      ���ڱ�����������:
	   *        getKeyValue(&quot;name=Dong&quot;, &quot;name&quot;) ���� &quot;Dong&quot;
	   *        getKeyValue(&quot;name=Dong&quot;, &quot;sex&quot;) ���� null
	   *        getKeyValue(&quot;name=\&quot;Dong&tilde;tWeiyong\&quot;&tilde;tSex=Male&quot;, &quot;name&quot;) ���� &quot;Dong&tilde;tWeiyong&quot;
	   *     
	   * </pre>
	   * 
	   * @param valueList
	   *          �ַ���
	   * @param key
	   *          �ؼ���
	   * @return �ؼ��ֶ�Ӧ����Ϣ
	   * @see #setKeyValue
	   */
		public static final String getKeyValue(String valueList, String key) {
			String ls_Value = null;
			if (valueList != null && valueList.indexOf("=") >= 0) {
				/*
	       * if((valueList.indexOf("\t") < 0) && (valueList.indexOf("&") < 0) &&
	       * (valueList.indexOf("\\t") < 0) && (valueList.indexOf("~t") < 0)) { //
	       * ֻ��һ���ؼ��� String ls_Key = valueList.substring(0, valueList.indexOf("="));
	       * if(equalsIgnoreCase(ls_Key, key)) ls_Value =
	       * valueList.substring(valueList.indexOf("=") + 1); } else {
	       */
				Properties lp_Values = parseOneProperties(valueList);
				if (lp_Values != null && key != null) {
					// ��λ���� key ��ͬ�Ĺؼ���
					Enumeration<?> le_Keys = lp_Values.propertyNames();
					while (le_Keys.hasMoreElements()) {
						String ls_Key = (String) le_Keys.nextElement();
						if (ls_Key.equalsIgnoreCase(key)) {
							ls_Value = lp_Values.getProperty(ls_Key);
						}
					}
				}
				// }
			}
			return ls_Value;
		}

		/**
	   * �ڲ����� key == null, �򷵻�ȫ��
	   */

		public static Properties parseOneProperties(String valueList) {
			return parseOneProperties(valueList, false);
		}

		public static Properties parseOneProperties(String valueList,
				boolean pbUpperCase) {
			Properties lp_Values = new Properties();
			String ls_Key = null; // �ؼ���
			if (valueList == null || valueList.equals("")) {
				lp_Values = null;
			} else {
				valueList = valueList.replaceAll("&", "\t");
				valueList = valueList.replaceAll("\\t", "\t");
				valueList = valueList.replaceAll("~t", "\t");

				int li_PosOfEqual; // �Ⱥ�λ��
				int li_PosOfNext; // ��һ���ָ�
				String ls_ValueTemp; // ��ʱ����,ֵ

				while (true) {
					li_PosOfEqual = valueList.indexOf("=");
					if (li_PosOfEqual > 0) {
						ls_Key = valueList.substring(0, li_PosOfEqual);
						valueList = valueList.substring(li_PosOfEqual + 1);
						// �����1����ĸ�����ţ�����һ�����ţ��м�Ϊ���ؼ��ֵ�ֵ
						if (valueList.startsWith("\"")) {
							li_PosOfNext = valueList.indexOf("\"", 1);
							if (li_PosOfNext >= 1)
								ls_ValueTemp = valueList.substring(1, li_PosOfNext);
							else
								ls_ValueTemp = valueList;
							// ��ʣ�µ�
							valueList = substring(valueList, li_PosOfNext + 1);
							if (!isEmpty(valueList)) {
								valueList = valueList.substring(1);
							}
						} else {
							// ��������һ���ָ���
							li_PosOfNext = valueList.indexOf("\t", 0);
							if (li_PosOfNext >= 0) {
								ls_ValueTemp = valueList.substring(0, li_PosOfNext);
								// ��ʣ�µ�
								valueList = valueList.substring(li_PosOfNext + 1);
							} else {
								ls_ValueTemp = valueList;
								// ��ʣ�µ�
								valueList = null;
							}
						}
						if (ls_ValueTemp != null) {
							if (pbUpperCase)
								lp_Values.setProperty(toUpperCase(ls_Key),
										toNormalString(ls_ValueTemp));
							else
								lp_Values.setProperty(ls_Key, toNormalString(ls_ValueTemp));
						}
						if (isEmpty(valueList))
							break;
					} else
						break;
				}
			}
			return lp_Values;
		}

		/**
	   * ����һ���ַ���, ���л�ȡ�ؼ��ֵ���Ϣ.
	   * 
	   * <pre>
	   *      ����ַ�������һ����Ҫ���, ���ĸ�ʽ������������Ĺ淶:
	   *     
	   *        �ؼ���=ֵ&lt;�ָ���&gt;�ؼ���=ֵ&lt;�ָ���&gt;...
	   *     
	   *      �ָ�������Ϊ:
	   *        &tilde;t: TAB
	   *        \t: TAB
	   *        &amp;: &amp;����
	   *     
	   *      ���ֵ�к��зָ�������ֵ������˫������������
	   *     
	   *      ������һЩ����:
	   *        name=Dong
	   *        name=Dong&tilde;tSex=Male
	   *        name=&quot;Dong&tilde;tWeiyong&quot;&tilde;tSex=Male
	   *     
	   *      ���ڱ�����������:
	   *        setKeyValue(&quot;&quot;, &quot;name&quot;, &quot;Dong&quot;) ���� &quot;name=Dong&quot;
	   *        setKeyValue(null, &quot;name&quot;, &quot;Dong&quot;) ���� &quot;name=Dong&quot;
	   *        setKeyValue(&quot;name=Dong&quot;, &quot;sex&quot;, &quot;Male&quot;) ���� &quot;name=Dong&tilde;tsex=Male&quot;
	   *     
	   * </pre>
	   * 
	   * @param valueList
	   *          �ַ���
	   * @param key
	   *          �ؼ���
	   * @param value
	   *          �ؼ��ֶ�Ӧ����Ϣ
	   * @return ��ϵĴ�
	   * @see #getKeyValue
	   */
		public static final String setKeyValue(String valueList, String key,
				String value) {
			Properties lp_Values = parseOneProperties(valueList);
			String ls_ValueList = null;
			String ls_Value = value;
			if (ls_Value != null) {
				if (ls_Value.indexOf("&") > 0 || ls_Value.indexOf("~t") > 0
						|| ls_Value.indexOf("\t") > 0 || ls_Value.indexOf("\\t") > 0)
					ls_Value = "\"" + ls_Value + "\"";
			}

			if (lp_Values == null) {
				if (isEmpty(key) || ls_Value == null)
					ls_ValueList = null;
				else
					ls_ValueList = key + "=" + ls_Value;
			} else {
				Enumeration<?> le_Keys = lp_Values.propertyNames();
				boolean lb_Setted = false;
				while (le_Keys.hasMoreElements()) {
					String ls_Key = (String) le_Keys.nextElement();
					String ls_KeyValue;
					if (ls_Key.equalsIgnoreCase(key)) {
						lb_Setted = true;
						ls_KeyValue = ls_Value;
					} else {
						ls_KeyValue = lp_Values.getProperty(ls_Key);
						if (ls_KeyValue != null)
							if (ls_KeyValue.indexOf("&") > 0 || ls_KeyValue.indexOf("~t") > 0
									|| ls_KeyValue.indexOf("\t") > 0
									|| ls_KeyValue.indexOf("\\t") > 0)
								ls_KeyValue = "\"" + ls_KeyValue + "\"";
					}
					if (ls_KeyValue != null) {
						if (ls_ValueList != null)
							ls_ValueList += "\t";
						ls_ValueList = nullToString(ls_ValueList) + ls_Key + "="
								+ ls_KeyValue;
					}
				}
				if (!lb_Setted) {
					if (ls_Value != null) {
						if (ls_ValueList != null)
							ls_ValueList += "\t";
						ls_ValueList = nullToString(ls_ValueList) + key + "=" + ls_Value;
					}
				}
			}
			return ls_ValueList;
		}

		/**
	   * �õ��ڵ�ֵ.
	   * 
	   * <pre>
	   *      ��ԭʼ����ȡ�ڵ�����ݡ�
	   *     
	   *      �ڵ�һ���������ĸ�ʽ:
	   *         &lt;key&gt;...&lt;/key&gt;
	   * </pre>
	   * 
	   * @param valueList
	   *          String ԭʼֵ
	   * @param key
	   *          String �ؼ���
	   */
		public static final String getNode(String valueList, String key) {
			String dsValue;
			if ((valueList == null) || (key == null)) {
				dsValue = null;
			} else {
				int di_Start = valueList.indexOf("<" + key + ">");
				int di_End = valueList.indexOf("</" + key + ">", di_Start);
				if ((0 <= di_Start) && (di_Start <= di_End)) {
					dsValue = valueList.substring(di_Start + key.length() + 2, di_End);
				} else
					dsValue = null;
			}
			return dsValue;
		}

		/**
	   * �ȽϺ���.
	   * <p>
	   * ��������ʹ��� equals ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� false.
	   * 
	   * @param baseString
	   *          ������
	   * @param param1
	   *          ���Ƚϴ�
	   * @return true ������, false ��������
	   * @see #isEmpty
	   * @see #equalsIgnoreCase
	   */
		public static final boolean equals(String baseString, Object param1) {
			if (baseString == null) {
				if (param1 == null)
					return true;
				else
					return false;
			} else
				return baseString.equals(param1);
		}

		/**
	   * ��鴮�Ƿ�Ϊ��, null ���߿մ�
	   * <p>
	   * 
	   * @since soja 1.3.40903
	   * @param baseString
	   *          ������
	   * @return true ����� null ���߿մ�, false �������
	   * @see #equals
	   * @see #equalsIgnoreCase
	   */
		public static final boolean isEmpty(String baseString) {
			return isEmpty((Object) baseString);
		}

		/**
	   * ��� baseString �Ƿ�Ϊ��, null ���߿մ�(String)
	   * <p>
	   * 
	   * @since soja 1.3.60423
	   * @param baseObject
	   *          ����
	   * @return true ����� null ���߿մ�, false �������
	   * @see #equals
	   * @see #equalsIgnoreCase
	   */
		public static final boolean isEmpty(Object baseObject) {
			if (baseObject == null)
				return true;
			else if (baseObject instanceof String)
				return ((String) baseObject).equals("");
			else
				return false;
		}

		/**
	   * ת���� boolean ����.
	   * 
	   * <pre>
	   *      yes��true��on ת���� true
	   *      no��false��off ת���� false
	   * </pre>
	   * 
	   * @param baseString
	   *          ��ת����
	   * @return ����boolean
	   */
		public static final boolean toBoolean(String baseString) {
			return toBoolean(baseString, true);
		}

		/**
	   * ת���� boolean ����.
	   * 
	   * <pre>
	   *      yes��true��on ת���� true
	   *      no��false��off ת���� false
	   * </pre>
	   * 
	   * @param baseString
	   *          ��ת����
	   * @param pbDeault
	   *          ȱʡֵ
	   */
		public static final boolean toBoolean(String baseString, boolean pbDeault) {
			if (baseString == null)
				return pbDeault;
			else if (equalsIgnoreCase(baseString, "yes")
					|| equalsIgnoreCase(baseString, "true")
					|| equalsIgnoreCase(baseString, "on")
					|| equalsIgnoreCase(baseString, "y")
					|| equalsIgnoreCase(baseString, "1"))
				return true;
			else if (equalsIgnoreCase(baseString, "no")
					|| equalsIgnoreCase(baseString, "false")
					|| equalsIgnoreCase(baseString, "off")
					|| equalsIgnoreCase(baseString, "n")
					|| equalsIgnoreCase(baseString, "0"))
				return false;
			else
				return pbDeault;
		}

		/**
	   * ��λ(indexOf)����.
	   * <p>
	   * ��������ʹ��� indexOf ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� -1.
	   * 
	   * @param baseString
	   *          ������
	   * @param indexString
	   *          ���Ƚϴ�
	   * @see #lastIndexOf
	   */
		public static final int indexOf(String baseString, String indexString) {
			return indexOf(baseString, indexString, 0);
		}

		/**
	   * ��λ(indexOf)����.
	   * <p>
	   * ��������ʹ��� indexOf ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� -1.
	   * 
	   * @param baseString
	   *          ������
	   * @param indexString
	   *          ���Ƚϴ�
	   * @see #lastIndexOf
	   */
		public static final int indexOf(String baseString, String indexString,
				int index) {
			if (baseString == null)
				return -1;
			else
				return baseString.indexOf(indexString, index);
		}

		/**
		   * ��λ(indexOf)����.index֮���repeat��
		   * <p>
		   * ��������ʹ��� indexOf ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� -1.
		   * 
		   * @param baseString
		   *          ������
		   * @param indexString
		   *          ���Ƚϴ�
		   * @see #lastIndexOf
		   */
		public static int indexOfRepeat(String baseString, String indexString, int index,
				int repeat) {
			if (baseString == null || indexString == null)
				return -1;
			else {
				int li_PosOfStart = index;
				int li_Repeat = 0;
				int li_Times = 0;
				while (li_PosOfStart >= 0) {
					li_Times++;
					if (li_Times > 1000) {
						System.err.println("StringUtils.indexOf ��ѭ��:"
								+ "baseString=" + baseString + ", indexString=" + indexString
								+ ", index=" + index + ", repeat=" + repeat);
						return -1;
					}
					li_PosOfStart = baseString.indexOf(indexString, li_PosOfStart);
					li_Repeat++;
					if (li_PosOfStart >= 0
							&& startsWith(substring(baseString, li_PosOfStart
									+ indexString.length()), indexString))
						li_PosOfStart += indexString.length();
					else {
						if (li_Repeat == repeat) {
							break;
						} else {
							li_Repeat = 0;
							li_PosOfStart++;
						}
					}
				}
				return li_PosOfStart;
			}
		}

		/**
	   * ��λ(lastIndexOf)����.
	   * <p>
	   * ��������ʹ��� lastIndexOf ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� -1.
	   * 
	   * @param baseString
	   *          ������
	   * @param indexString
	   *          ���Ƚϴ�
	   * @see #indexOf
	   */
		public static final int lastIndexOf(String baseString, String indexString) {
			if (baseString == null)
				return -1;
			else
				return baseString.lastIndexOf(indexString);
		}

		/**
	   * ��λ����(indexOfWord)����.
	   * <p>
	   * ��������ʹ��� indexOf �����������ơ������Ǽ���һ�γ��ָôʵ�λ��
	   * 
	   * @param baseString
	   *          ������
	   * @param indexString
	   *          ���Ƚϴ�
	   * @see #indexOf
	   */
		public static final int indexOfWord(String baseString, String indexString) {
			int li_Pos = -1;
			if (baseString != null && !StringL.isEmpty(indexString))
				while (true) {
					li_Pos = indexOf(baseString, indexString, li_Pos + 1);
					if (li_Pos < 0)
						break;
					String ls_Prior = substring(baseString, li_Pos - 1, li_Pos);
					String ls_Next = substring(baseString, li_Pos + indexString.length(),
							li_Pos + indexString.length() + 1);
					if ((ls_Prior == null || !isExpression(ls_Prior))
							&& (ls_Next == null || !isExpression(ls_Next)))
						break;
				}
			return li_Pos;
		}

		/**
	   * ��λ(indexOf)����.
	   * <p>
	   * ��������ʹ��� indexOf ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� -1.
	   * 
	   * @param baseString
	   *          ������
	   * @param indexString
	   *          ���Ƚϴ�
	   * @see #lastIndexOf
	   */
		public static final int indexOfIgnoreCase(String baseString,
				String indexString) {
			return indexOf(toUpperCase(baseString), toUpperCase(indexString));
		}

		/**
	   * �ж��ַ����Ƿ����.
	   */
		public static boolean contains(String baseString, String matchString) {
			if (indexOf(baseString, matchString) >= 0)
				return true;
			else
				return false;
		}

		/**
	   * �ж��ַ����Ƿ����.
	   */
		public static boolean containsIgnoreCase(String baseString, String matchString) {
			if (indexOfIgnoreCase(baseString, matchString) >= 0)
				return true;
			else
				return false;
		}

		/**
	   * �ж϶����Ƿ����.
	   */
		public static boolean contains(Object baseObject, Object matchObject) {
			if (baseObject == null)
				return false;
			else if (baseObject instanceof String)
				return contains((String) baseObject, "" + matchObject);
			else if (baseObject instanceof Collection) {
				return ((Collection<?>) baseObject).contains(matchObject);
			} else
				return false;
		}

		/**
	   * ����ַ����ĳ���(�� String �����Ⱥ��� length ͬ).
	   * 
	   * @param baseString
	   *          ������
	   */
		public static final int length(String baseString) {
			if (baseString == null)
				return 0;
			else
				return baseString.length();
		}

		/**
	   * ����ַ����ĳ���(Byte)
	   * 
	   * @param baseString
	   *          ������
	   */
		public static final int lengthByte(String baseString) {
			if (baseString == null)
				return 0;
			else {
				return baseString.replaceAll("[^\\x00-\\xff]", "**").length();
			}
		}

		/**
	   * ��ȡ���ݸ���
	   */
		public static int size(Object object) {
			if (object == null)
				return 0;
			else if (object instanceof Collection)
				return ((Collection<?>) object).size();
			else if (object instanceof Map)
				return ((Map<?, ?>) object).size();
			else if (object instanceof Object[])
				return ((Object[]) object).length;
			else if (object instanceof int[])
				return ((int[]) object).length;
			else if (object instanceof byte[])
				return ((byte[]) object).length;
			else if (object instanceof short[])
				return ((short[]) object).length;
			else if (object instanceof boolean[])
				return ((boolean[]) object).length;
			else if (object instanceof double[])
				return ((double[]) object).length;
			else if (object instanceof float[])
				return ((float[]) object).length;
			else
				return 1;
		}

		/**
	   * ��������ĸ�ıȽϺ��� ��������ʹ��� equals ����������ͬ�������ǵ������� �� null ʱ�����ᴥ�����󣬶��Ƿ��� false.
	   * 
	   * @param baseString
	   *          ������
	   * @param param1
	   *          ���Ƚϴ�
	   * @return true ������, false ��������
	   * @see #equals
	   * @see #isEmpty
	   */
		public static final boolean equalsIgnoreCase(String baseString, String param1) {
			if (baseString == null) {
				if (param1 == null)
					return true;
				else
					return false;
			} else
				return baseString.equalsIgnoreCase(param1);
		}

		/**
	   * ͳ���ַ�����ָ�����ĸ���
	   * 
	   * @param baseString
	   *          ��ͳ�ƵĻ�����
	   * @param includeString
	   *          ͳ�Ƶ��ַ���
	   * @return ���ظ���(int)
	   */
		public static final int countString(String baseString, String includeString) {
			if (baseString == null || includeString == null) {
				return 0;
			} else {
				int li_Pos = 0;
				int li_Count = 0;
				int li_Len = baseString.length();
				while (li_Pos <= li_Len) {
					if (baseString.substring(li_Pos).startsWith(includeString)) {
						li_Count++;
						li_Pos += includeString.length();
					} else
						li_Pos++;
				}
				return li_Count;
			}
		}

		/**
	   * �ظ�ĳ���ַ���
	   * 
	   * @param baseString
	   *          ������
	   * @param count
	   *          �ظ�����
	   * @return �ظ��Ľ��
	   */
		public static final String repeat(String baseString, int count) {
			if (baseString == null)
				return null;
			else {
				StringBuffer ls_Result = new StringBuffer("");
				for (int i = 0; i < count; i++) {
					ls_Result.append(baseString);
				}
				return ls_Result.toString();
			}
		}

		/**
	   * ���ַ���ת���� Properties ����
	   */
		public static final Properties[] parseProperties(String baseString) {
			if (isEmpty(baseString)) {
				return null;
			} else {
				String ls_Line[] = baseString.split("]");
				Properties properties[] = new Properties[ls_Line.length];
				for (int i = 0; i < ls_Line.length; i++) {
					if (startsWith(ls_Line[i], "["))
						ls_Line[i] = substring(ls_Line[i], 1);
					properties[i] = parseOneProperties(ls_Line[i]);
				}
				return properties;
			}
		}

		/**
	   * �����ȡһ���ַ�����һ����
	   * 
	   * @param baseString
	   *          �ַ���
	   * @param pos
	   *          ȡ��ĸ����
	   * @return ���ؽ�ȡ�Ľ��
	   */
		public static String left(String baseString, int pos) {
			int diLength;
			if (baseString == null || pos <= 0) {
				return null;
			} else {
				diLength = baseString.length();
				if (diLength < pos) {
					return baseString.substring(0);
				} else {
					return baseString.substring(0, pos);
				}
			}
		}

		/**
	   * ���ұ�ȡһ���ַ�����һ����
	   * 
	   * @param baseString
	   *          �ַ���
	   * @param pos
	   *          ȡ��ĸ����
	   * @return ���ؽ�ȡ�Ľ��
	   */
		public static String right(String baseString, int pos) {
			int diLength;
			int diSubString;
			if (baseString == null) {
				return null;
			} else {
				diLength = baseString.length();
				if (diLength < pos) {
					diSubString = 0;
				} else {
					diSubString = diLength - pos;
				}
				return baseString.substring(diSubString);
			}
		}

		/**
	   * �� null ת���ɴ� ""
	   * 
	   * @param baseString
	   *          �����Ĵ�
	   * @return ת������ַ���
	   */
		public static String nullToString(String baseString) {
			return nullToString(baseString, "");
		}

		/**
	   * �� null ת���ɴ� defaultString
	   * 
	   * @param baseString
	   *          �����Ĵ�
	   * @return ת������ַ���
	   */
		public static String nullToString(String baseString, String defaultString) {
			if (baseString == null)
				return defaultString;
			else
				return baseString;
		}

		/**
	   * �� null ת���ɴ� "NULL"
	   * 
	   * @param baseString
	   *          �����Ĵ�
	   * @return ת������ַ���
	   */
		public static String nullToName(String baseString) {
			if (baseString == null)
				return "NULL";
			else
				return baseString;
		}

		/**
	   * ��һ����ĸת���ɴ�д��ĸ
	   * 
	   * @param c
	   *          ��ת������ĸ
	   * @return ת����Ĵ�д��ĸ
	   */
		public static char toUpperCase(char c) {
			return Character.toUpperCase(c);
		}

		/**
	   * ��һ����ĸת����Сд��ĸ
	   * 
	   * @param c
	   *          ��ת������ĸ
	   * @return ת�����Сд��ĸ
	   */
		public static char toLowerCase(char c) {
			return Character.toLowerCase(c);
		}

		public static String toTitleCase(String s) {
			StringBuffer stringbuffer = new StringBuffer();
			for (int i = 0; i < s.length(); i++)
				stringbuffer.append(toTitleCase(s.charAt(i)));

			return stringbuffer.toString();
		}

		/**
	   * ��һ���ַ���ת���ɴ�д�ַ���
	   * 
	   * @param s
	   *          ��ת���Ĵ�
	   * @return ת����Ĵ�д�ַ���
	   */
		public static String toUpperCase(String s) {
			if (s == null)
				return null;
			else {
				return s.toUpperCase();
				/*
	       * StringBuffer stringbuffer = new StringBuffer(); for(int i = 0; i <
	       * s.length(); i++) stringbuffer.append(toUpperCase(s.charAt(i)));
	       * 
	       * return stringbuffer.toString();
	       */
			}
		}

		/**
	   * ��һ���ַ���ת���ɴ�д�ַ���
	   * 
	   * @param s
	   *          ��ת���Ĵ�
	   * @return ת����Ĵ�д�ַ���
	   */
		public static String toLowerCase(String s) {
			if (s == null)
				return null;
			else {
				return s.toLowerCase();
				/*
	       * StringBuffer stringbuffer = new StringBuffer(); for(int i = 0; i <
	       * s.length(); i++) stringbuffer.append(toLowerCase(s.charAt(i)));
	       * 
	       * return stringbuffer.toString();
	       */
			}
		}

		public static char toTitleCase(char c) {
			return Character.toTitleCase(c);
		}

		/*
	   * public static boolean regionMatches(StringLike stringlike, boolean flag,
	   * int i, StringLike stringlike1, int j, int k) { int l = j + k; if(l >
	   * stringlike1.length() || i + k > stringlike.length()) return false;
	   * if(!flag) { for(int i1 = j; i1 < l; i1++) if(stringlike1.charAt(i1) !=
	   * stringlike.charAt(i++)) return false; } else { for(int j1 = j; j1 < l;
	   * j1++) if(toLowerCase(stringlike1.charAt(j1)) !=
	   * toLowerCase(stringlike.charAt(i++))) return false; } return true; }
	   * 
	   * public static boolean regionMatches(String s, boolean flag, int i,
	   * StringLike stringlike, int j, int k) { int l = j + k; if(l >
	   * stringlike.length() || i + k > s.length()) return false; if(!flag) {
	   * for(int i1 = j; i1 < l; i1++) if(stringlike.charAt(i1) != s.charAt(i++))
	   * return false; } else { for(int j1 = j; j1 < l; j1++)
	   * if(toLowerCase(stringlike.charAt(j1)) != toLowerCase(s.charAt(i++))) return
	   * false; } return true; }
	   * 
	   * public static boolean regionMatches(StringLike stringlike, boolean flag,
	   * int i, String s, int j, int k) { int l = j + k; if(l > s.length() || i + k >
	   * stringlike.length()) return false; if(!flag) { for(int i1 = j; i1 < l;
	   * i1++) if(s.charAt(i1) != stringlike.charAt(i++)) return false; } else {
	   * for(int j1 = j; j1 < l; j1++) if(toLowerCase(s.charAt(j1)) !=
	   * toLowerCase(stringlike.charAt(i++))) return false; } return true; }
	   * 
	   * public static boolean regionMatches(String s, boolean flag, int i, String
	   * s1, int j, int k) { int l = j + k; if(l > s1.length() || i + k >
	   * s.length()) return false; if(!flag) { for(int i1 = j; i1 < l; i1++)
	   * if(s1.charAt(i1) != s.charAt(i++)) return false; } else { for(int j1 = j;
	   * j1 < l; j1++) if(toLowerCase(s1.charAt(j1)) != toLowerCase(s.charAt(i++)))
	   * return false; } return true; }
	   */

		/**
	   * ȥ���� Ascii ��ĸ
	   */
		public static String removeNonAscii(String baseString) {
			if (baseString == null) {
				return null;
			} else {
				baseString = replace(baseString, "\t", " ");
				baseString = replace(baseString, "\r\n", " ");
				return baseString;
			}
		}

		/**
	   * Trim
	   */
		public static String trim(String baseString) {
			if (baseString == null)
				return null;
			else
				return baseString.trim();
		}

		private static String __ENCODE__ = "GBK"; // һ��Ҫ��GBK
		private static String __SERVER_ENCODE__ = "GB2312"; // �������ϵ�ȱʡ����

		/**
	   * compareTo
	   */
		public static int compareTo(String baseString, String compareString) {
			if (baseString == null || compareString == null)
				return -1;
			else {
				String m_s1 = null, m_s2 = null;
				try {
					// �Ƚ����ַ��������GBK
					m_s1 = new String(baseString.getBytes(__SERVER_ENCODE__), __ENCODE__);
					m_s2 = new String(compareString.getBytes(__SERVER_ENCODE__), __ENCODE__);
				} catch (Exception ex) {
					return baseString.compareTo(compareString);
				}
				int res = chineseCompareTo(m_s1, m_s2);
				return res;
			}
		}

		// ��ȡһ������/��ĸ��Charֵ
		public static int getCharCode(String s) {
			if (s == null && "".equals(s))
				return -1; // ��������
			byte[] b = s.getBytes();
			int value = 0;
			// ��֤ȡ��һ���ַ������ֻ���Ӣ�ģ�
			for (int i = 0; i < b.length && i <= 2; i++) {
				value = value * 100 + b[i];
			}
			return value;
		}

		// �Ƚ������ַ���
		public static int chineseCompareTo(String s1, String s2) {
			int len1 = s1.length();
			int len2 = s2.length();
			int n = Math.min(len1, len2);
			for (int i = 0; i < n; i++) {
				int s1_code = getCharCode(s1.charAt(i) + "");
				int s2_code = getCharCode(s2.charAt(i) + "");
				if (s1_code != s2_code)
					return s1_code - s2_code;
			}
			return len1 - len2;
		}

		/**
	   * compareToIgnoreCase
	   */
		public static int compareToIgnoreCase(String baseString, String compareString) {
			return compareTo(toUpperCase(baseString), toUpperCase(compareString));
		}

		/**
	   * startWith
	   */
		public static boolean startsWith(String baseString, String compareString) {
			if (baseString == null || compareString == null)
				return false;
			else
				return baseString.startsWith(compareString);
		}

		/**
	   * startWith
	   */
		public static boolean startsWithIgnoreCase(String baseString,
				String compareString) {
			if (baseString == null || compareString == null)
				return false;
			else
				return baseString.toUpperCase().startsWith(compareString.toUpperCase());
		}

		/**
	   * endWith
	   */
		public static boolean endsWith(String baseString, String compareString) {
			if (baseString == null)
				return false;
			else
				return baseString.endsWith(compareString);
		}

		/**
	   * endsWithIgnoreCase
	   */
		public static boolean endsWithIgnoreCase(String baseString,
				String compareString) {
			if (baseString == null)
				return false;
			else
				return baseString.toUpperCase().endsWith(compareString.toUpperCase());
		}

		/**
	   * matches
	   */
		public static boolean matches(String baseString, String compareString) {
			if (baseString == null)
				return false;
			else
				return baseString.matches(compareString);
		}

		/**
	   * ���ַ��� baseString �����ַ��� splitString ��ֳ��ַ�������.
	   */
		public static String[] split(String baseString, String splitString) {
			if (baseString == null)
				return new String[0];
			else
				return baseString.split(splitString);
		}

		/**
	   * ���ַ��� baseString �����ַ������� splitString ��ֳ��ַ�������. �κ�һ���ַ������ݶ��Ƿָ�����.
	   */
		public static String[] split(String baseString, String[] splitString) {
			if (baseString == null)
				return new String[0];
			else if (splitString == null)
				return new String[] { baseString };
			else {
				// ������ splitString �����ķָ����滻�ɵ�1���ָ���
				for (int i = 1; i < splitString.length; i++) {
					baseString = StringL.replace(baseString, splitString[i],
							splitString[0]);
				}
				return baseString.split(splitString[0]);
			}
		}

		/**
	   * ��һ����ͨ�ַ���ת���� Unicode ��
	   */
		public static String toUnicodeString(String baseString) {
			return toUnicodeString(baseString, "\\u");
		}

		/**
	   * ��һ����ͨ�ַ���ת���� Unicode ��
	   */
		public static String toUnicodeString(String baseString, String prefix) {
			if (baseString == null)
				return null;
			else {
				char[] c = baseString.toCharArray();
				String ls_Return = "";
				for (int i = 0; i < c.length; i++) {
					int j = (int) c[i];
					if (j > 256) {
						// j += Integer.parseInt("8000", 16);
						ls_Return += prefix + right("0000" + Integer.toHexString(j), 4);
					} else {
						ls_Return += prefix + right("00" + Integer.toHexString(j), 2);
						// c[i];
					}
				}
				return ls_Return;
			}
		}

		/**
	   * ��һ�� Unicode �ַ���ת������ͨ�ַ���
	   */
		public static String toNormalString(String baseString) {
			if (baseString == null)
				return null;
			else {
				String ls_Return = "";
				try {
					char[] c = baseString.toCharArray();
					int i = 0;
					while (i < c.length) {
						if (c[i] == '\\') {
							if ((i + 1 < c.length) && (c[i + 1] == 'u')) {
								if (i + 5 < c.length && c[i + 4] != '\\') {
									ls_Return += (char) (Integer.parseInt("" + c[i + 2] + c[i + 3]
											+ c[i + 4] + c[i + 5], 16));
									i += 6;
								} 
								else if (i + 3 < c.length) {
									ls_Return += (char) (Integer.parseInt("" + c[i + 2] + c[i + 3], 16));
									i += 4;
								} 
								else
									i++;
							} else
								i++;
						} else {
							ls_Return += c[i++];
						}
					}
				} catch (Exception e) {
					// �����κδ��󣬲���ת��
					ls_Return = baseString;
				}
				return ls_Return;
			}
		}


		/**
	   * ����������еİ���
	   */
		public static String getPackageName(String className) {
			int li_PosOfComma = lastIndexOf(className, ".");
			String ls_PackageName = null;
			if (li_PosOfComma >= 0) {
				ls_PackageName = left(className, li_PosOfComma);
				if (indexOf(className, "(") >= 0)
					ls_PackageName = left(ls_PackageName, lastIndexOf(ls_PackageName, "."));
			}
			return ls_PackageName;
		}

		/**
	   * ����������еĶ�����
	   */
		public static String getObjectName(String className) {
			int li_StartPos = lastIndexOf(className, ".");
			if (indexOf(className, "(") >= 0)
				li_StartPos = lastIndexOf(left(className, lastIndexOf(className, ".")),
						".");
			String ls_ObjectName = substring(className, li_StartPos + 1);
			if (indexOf(ls_ObjectName, ".") > 0)
				ls_ObjectName = left(ls_ObjectName, indexOf(ls_ObjectName, "."));
			return ls_ObjectName;
		}

		/**
	   * ����������еķ�����
	   */
		public static String getMothedName(String className) {
			int li_StartPos = lastIndexOf(className, ".");
			if (indexOf(className, "(") >= 0)
				li_StartPos = lastIndexOf(left(className, lastIndexOf(className, ".")),
						".");
			else
				return null;
			String ls_ObjectName = substring(className, li_StartPos + 1);
			if (indexOf(ls_ObjectName, ".") > 0)
				ls_ObjectName = substring(ls_ObjectName, indexOf(ls_ObjectName, ".") + 1);
			return ls_ObjectName;
		}

		/**
	   * ��õ�1���ָ�����ǰ��ֵ
	   */
		public static String before(String str, String sperator) {
			if (str == null || isEmpty(sperator))
				return str;
			else {
				int li_Pos = indexOf(str, sperator);
				if (li_Pos >= 0)
					return left(str, li_Pos);
				else
					return str;
			}
		}

		/**
	   * ��õ�1���ָ�����ǰ��ֵ
	   */
		public static String lastBefore(String str, String sperator) {
			if (str == null || isEmpty(sperator))
				return str;
			else {
				int li_Pos = lastIndexOf(str, sperator);
				if (li_Pos >= 0)
					return left(str, li_Pos);
				else
					return str;
			}
		}

		/**
	   * ��õ�1���ָ����ź��ֵ
	   */
		public static String after(String str, String sperator) {
			if (str == null || isEmpty(sperator))
				return null;
			else {
				int li_Pos = indexOf(str, sperator);
				if (li_Pos >= 0)
					return substring(str, li_Pos + length(sperator));
				else
					return null;
			}
		}

		/**
	   * ��õ�1���ָ����ź��ֵ
	   */
		public static String lastAfter(String str, String sperator) {
			if (str == null || isEmpty(sperator))
				return null;
			else {
				int li_Pos = lastIndexOf(str, sperator);
				if (li_Pos >= 0)
					return substring(str, li_Pos + length(sperator));
				else
					return null;
			}
		}

		/*****************************************************************************
	   * �ڲ�ʵ��
	   ****************************************************************************/
		private static String replace(String line, String oldString,
				String newString, int count, boolean isIgnoreCase) {
			if (line == null || isEmpty(oldString) || newString == null) {
				return line;
			}

			String ls_MatchLine = isIgnoreCase ? line.toLowerCase() : line;
			String ls_MatchStr = isIgnoreCase ? oldString.toLowerCase() : oldString;

			if (ls_MatchLine.indexOf(ls_MatchStr) >= 0) {
				int counter = 0;

				char[] line2 = line.toCharArray();
				char[] newString2 = null;
				if (newString != null)
					newString2 = newString.toCharArray();

				int oLength = oldString.length();
				StringBuffer buf = new StringBuffer(line2.length);

				int i = 0;
				int j = 0;

				while ((i = ls_MatchLine.indexOf(ls_MatchStr, i)) >= 0) {
					// �� count > 0, ���Ѿ������滻�����ģ������滻
					if (count > 0 && counter >= count)
						break;

					counter++;
					buf.append(line2, j, i - j);
					if (newString2 != null)
						buf.append(newString2);
					i += oLength;
					j = i;
				}
				buf.append(line2, j, line2.length - j);

				return buf.toString();
			} else
				return line;
		}

	public static void main(String[] args) {
		System.err.println(StringL.getNow());

	}
}

package com.org.palmcampus.oa.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.kobjects.base64.Base64;
/**
 * string������
 * @author denny  
 * @version 2009��08��21�� 17:22:03
 *
 */
public class StringUtil {
	/**
	 * stringתLong
	 * @param tmp
	 * @return -1:tmp��������; 0:tmp==null;
	 */
	public static Long stringToLong(String tmp){
		if(tmp==null)
			return new Long(0);
		else{
			try{
				long l = Long.parseLong(tmp);
				return new Long(l);
			}catch(NumberFormatException e){
				return new Long(-1);
			}
		}
	}
	/**
	 * stringתbyte[]
	 * @param tmp
	 * @return null:tmp==null;
	 */
	public static byte[] StringToByte(String tmp){
		if(tmp==null)
			return null;
		else{
			byte[] b = Base64.decode(tmp);
			return b;
		}
	}
	/**
	 * byte[]תstring
	 * @param tmp
	 * @return null:tmp==null;
	 */	
	public static String byteToString(byte[] bytes){
		if(bytes==null||bytes.length==0)
			return "";
		else{
			String tmp = Base64.encode(bytes);
			//String rs = replaceAll(tmp,"\r\n","");
			return tmp;
		}
	}
	/**
	 * �滻�����ַ�������jdk��replaceAll��
	 * @param tmp
	 * @return null:tmp==null;
	 */	
	public static String replaceAll(String rss, String olds, String news){
		int index = rss.indexOf(olds);
		StringBuffer sb = new StringBuffer();
		String rs;
		while(index>-1){
			sb.append(rss.substring(0, index));
			rs = rss.substring(index+olds.length());
			index = rs.indexOf(olds);
			rss = rs;
		}
		sb.append(rss);
		return sb.toString();
	}
	
	/**
	 * ��ȡ��׺
	 * @param src
	 * @param token
	 * @return
	 */
	public static String getPhinex(String src,String token){
		
		int index = src.indexOf(token);
		if(index>-1){
			return src.substring(index+1);
		}
		return null;
	}
	
	/**
	 * �ַ�����ת����ԭ�ַ�����sourceEncodeתΪtargetEncode�ַ�����
	 * @param str
	 * @param sourceEncode ԭ�ַ�����
	 * @param targetEncode ת������ַ�����
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException ������ַ�����
	 */
	public static String convertEncode(String str,String sourceEncode,String targetEncode) throws UnsupportedEncodingException{
		String result = "";
		try {
			byte[] bytes = str.getBytes(sourceEncode);
			result = new String(bytes,targetEncode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	/**
	 * ��str��ISO-8859-1ת��ΪUTF-8
	 * @param str Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException ������ַ�����
	 */
	public static String ISO2UTF(String str) throws UnsupportedEncodingException{
		String result = "";
		try {
			result = convertEncode(str, "ISO-8859-1", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	/**
	 * ��str��UTF-8ת��ΪISO-8859-1
	 * @param str Ҫת�����ַ���
	 * @return ת������ַ���
	 * @throws UnsupportedEncodingException ������ַ�����
	 */
	public static String UTF2ISO(String str) throws UnsupportedEncodingException{
		String result = "";
		try {
			result = convertEncode(str, "UTF-8", "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
	
	public static void main(String[] args){
		
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try{
			is = new StringUtil().getClass().getResourceAsStream("/test.bmp");
			baos = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			while(is.read(bytes)>-1){
				baos.write(bytes);
			}
			String tmp = StringUtil.byteToString(baos.toByteArray());
			String tmp1 = replaceAll(tmp,"\r\n","");
			System.out.println(tmp1);
		}catch(Exception e){
			
		}finally{
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

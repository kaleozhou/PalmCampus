package com.org.palmcampus.oa.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.kobjects.base64.Base64;
/**
 * string工具类
 * @author denny  
 * @version 2009年08月21日 17:22:03
 *
 */
public class StringUtil {
	/**
	 * string转Long
	 * @param tmp
	 * @return -1:tmp不是数字; 0:tmp==null;
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
	 * string转byte[]
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
	 * byte[]转string
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
	 * 替换所有字符（类似jdk的replaceAll）
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
	 * 获取后缀
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
	 * 字符编码转换从原字符编码sourceEncode转为targetEncode字符编码
	 * @param str
	 * @param sourceEncode 原字符编码
	 * @param targetEncode 转换后的字符编码
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException 错误的字符编码
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
	 * 将str从ISO-8859-1转换为UTF-8
	 * @param str 要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException 错误的字符编码
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
	 * 将str从UTF-8转换为ISO-8859-1
	 * @param str 要转换的字符串
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException 错误的字符编码
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

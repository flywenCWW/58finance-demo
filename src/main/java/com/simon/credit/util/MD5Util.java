package com.simon.credit.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具
 */
public class MD5Util {
    
	private static MessageDigest messagedigest;
	static{
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	// MD5
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static String md5(String text) {
		return md5(new String[]{text});
	}

	public static String md5(String[] text) {
		byte[] bytes = digest(text);
		return new String(encodeHex(bytes));
	}
	/**
	 * 前64位转换为long
	 */
	public static long halfDigest(String... text) {
		long ret = 0;
		byte[] bytes = digest(text);
		for (int i=0; i<8; i++)
			ret = ret << 8 | (bytes[i] & 0xFFL);
		return ret;
	}

	public static byte[] digest(String... text) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			for (String str : text) {
				msgDigest.update(str.getBytes("utf-8"));
			}

		}
		catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		return msgDigest.digest();
	}

	// 16位的MD5就是32位的中间的是内容
	public static String md5_16(String text) {
		return md5(text).substring(8, 24);
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
	
	/**
	 * 对byte类型的数组进行MD5加密
	 * @throws Exception 
	 * 
	 */
	public synchronized static String MD5(byte[] bytes) throws Exception {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}
	
	private static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}
	
	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			char c0 = DIGITS[(bytes[l] & 0xf0) >> 4];
			char c1 = DIGITS[bytes[l] & 0xf];
			stringbuffer.append(c0);
			stringbuffer.append(c1);
		}
		return stringbuffer.toString();
	}
	
}

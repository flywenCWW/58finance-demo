package com.simon.credit.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {

	public static final String MD5 		= "MD5";
	public static final String SHA1 	= "SHA1";
	public static final String HmacMD5 	= "HmacMD5";
	public static final String HmacSHA1 = "HmacSHA1";
	public static final String DES 		= "DES";
	public static final String AES 		= "AES";
	public static final String CHARSET 	= "UTF-8";
	public static final int KEYSIZEDES 	= 0;
	public static final int KEYSIZEAES 	= 128;

	public static final EncryptUtil INSTANCE = new EncryptUtil();

	private EncryptUtil() {}

	public static EncryptUtil getInstance() {
		return INSTANCE;
	}

	private String messageDigest(String str, String algorithm) {
		try {
			MessageDigest e = MessageDigest.getInstance(algorithm);
			byte[] resBytes = str.getBytes("UTF-8");
			return this.base64(e.digest(resBytes));
		} catch (Exception var5) {
			var5.printStackTrace();
			return null;
		}
	}

	private String keyGeneratorMac(String str, String algorithm, String key) {
		try {
			Object e = null;
			if (key == null) {
				KeyGenerator mac = KeyGenerator.getInstance(algorithm);
				e = mac.generateKey();
			} else {
				byte[] mac1 = key.getBytes("UTF-8");
				e = new SecretKeySpec(mac1, algorithm);
			}

			Mac mac2 = Mac.getInstance(algorithm);
			mac2.init((Key) e);
			byte[] result = mac2.doFinal(str.getBytes());
			return this.base64(result);
		} catch (Exception var7) {
			var7.printStackTrace();
			return null;
		}
	}

	private String keyGeneratorES(String str, String algorithm, String key,
			int keysize, boolean isEncode) {
		try {
			SecureRandom e = SecureRandom.getInstance("SHA1PRNG");
			KeyGenerator kg = KeyGenerator.getInstance(algorithm);
			byte[] sk;
			if (keysize == 0) {
				sk = key.getBytes("UTF-8");
				e.setSeed(sk);
				kg.init(e);
			} else if (key == null) {
				kg.init(keysize);
			} else {
				sk = key.getBytes("UTF-8");
				e.setSeed(sk);
				kg.init(keysize, e);
			}

			SecretKey sk1 = kg.generateKey();
			SecretKeySpec sks = new SecretKeySpec(sk1.getEncoded(), algorithm);
			Cipher cipher = Cipher.getInstance(algorithm);
			if (isEncode) {
				cipher.init(1, sks);
				byte[] resBytes = str.getBytes("UTF-8");
				return parseByte2HexStr(cipher.doFinal(resBytes));
			} else {
				cipher.init(2, sks);
				return new String(cipher.doFinal(parseHexStr2Byte(str)));
			}
		} catch (Exception var12) {
			var12.printStackTrace();
			return null;
		}
	}

	private String base64(byte[] bytes) {
		return Base64.encode(bytes);
	}

	public static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < buf.length; ++i) {
			String hex = Integer.toHexString(buf[i] & 255);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}

			sb.append(hex.toUpperCase());
		}

		return sb.toString();
	}

	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		} else {
			byte[] result = new byte[hexStr.length() / 2];

			for (int i = 0; i < hexStr.length() / 2; ++i) {
				int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1),
						16);
				int low = Integer.parseInt(
						hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
				result[i] = (byte) (high * 16 + low);
			}

			return result;
		}
	}

	public String md5Encode(String str) {
		return this.messageDigest(str, "MD5");
	}

	public String md5Encode(String str, String key) {
		return this.keyGeneratorMac(str, "HmacMD5", key);
	}

	public String sha1Encode(String str) {
		return this.messageDigest(str, "SHA1");
	}

	public String sha1Encode(String str, String key) {
		return this.keyGeneratorMac(str, "HmacSHA1", key);
	}

	public String deSencode(String str, String key) {
		return this.keyGeneratorES(str, "DES", key, 0, true);
	}

	public String deSdecode(String str, String key) {
		return this.keyGeneratorES(str, "DES", key, 0, false);
	}

	public String aeSencode(String str, String key) {
		return this.keyGeneratorES(str, "AES", key, 128, true);
	}

	public String aeSdecode(String str, String key) {
		return this.keyGeneratorES(str, "AES", key, 128, false);
	}

	public String xoRencode(String str, String key) {
		byte[] bs = str.getBytes();

		for (int i = 0; i < bs.length; ++i) {
			bs[i] = (byte) (bs[i] ^ key.hashCode());
		}

		return parseByte2HexStr(bs);
	}

	public String xoRdecode(String str, String key) {

		byte[] bs = parseHexStr2Byte(str);
		if (bs == null) {
			return null;
		}

		for (int i = 0; i < bs.length; ++i) {
			bs[i] = (byte) (bs[i] ^ key.hashCode());
		}

		return new String(bs);
	}

	public int xor(int str, String key) {
		return str ^ key.hashCode();
	}

}

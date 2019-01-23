package com.kuaidaili.sdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
	public static void main(String[] args){
		sign("836819141237","2014-12-05 15:30:23","f305524991549d99768f09e345a871bf");
	}
	
	/**
	 * 签名
	 * @param partid
	 * @return
	 */
	public static String sign(String partner_id,String timestamp,String api_key ){
		String result = md5(partner_id+timestamp+api_key);
		return result;
	}
	
	/**
	 * md5加密
	 * @param s
	 * @return
	 */
	private static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();
			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			System.err.println(e.getMessage());
		}
		return "";
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}
}

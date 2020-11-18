package com.yhh.core.utils;


import com.yhh.core.encrypter.Decrypter;
import com.yhh.core.encrypter.Des;
import com.yhh.core.encrypter.DesUtil;



public class MngSvrDecrypter implements Decrypter {
	public static final String DEFAULT_KEY = MngSvrEncrypter.DEFAULT_KEY;

	protected String key = DEFAULT_KEY;

	public MngSvrDecrypter() {
	}

	public MngSvrDecrypter(String key) {
		this.key = key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public String decrypt(String string) {
		StringBuilder decrypted = new StringBuilder();
		Des hisuDes = new Des(getKey());
		String hisuPasswdPlainHex = hisuDes.dec(string);
		int hisuLen = 0;
		if ((hisuPasswdPlainHex == null) || (hisuPasswdPlainHex.length() <= 2))
			return hisuPasswdPlainHex;
		hisuLen = Integer.valueOf(hisuPasswdPlainHex.substring(0, 2)).intValue();
		hisuPasswdPlainHex = hisuPasswdPlainHex.substring(2, 2 + hisuLen * 2);
		byte[] hisuBytes = DesUtil.hisuHexString2Bytes(hisuPasswdPlainHex);
		try {
			decrypted.append(new String(hisuBytes, "utf-8"));
		} catch (Exception e) {
			return "";
		}
		return decrypted.toString();
	}
}

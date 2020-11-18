package com.yhh.core.utils;


import com.yhh.core.encrypter.Des;
import com.yhh.core.encrypter.DesUtil;
import com.yhh.core.encrypter.Encrypter;



public class MngSvrEncrypter implements Encrypter {
	public static String DEFAULT_KEY = "B4011F49527A00964CE3A961D82A9B40";

	protected String key = DEFAULT_KEY;

	public MngSvrEncrypter() {
	}

	public MngSvrEncrypter(String key) {
		this.key = key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public String encrypt(String string) {
		int hisuLen = string.getBytes().length;
		StringBuilder encrypted = new StringBuilder();

		byte[] hisuBytes = new byte[hisuLen];
		try {
			byte[] hisuPasswdPlainBt = string.getBytes("utf-8");
			System.arraycopy(hisuPasswdPlainBt, 0, hisuBytes, 0, hisuLen);
		} catch (Exception e) {
			return "";
		}
		Des hisuDes = new Des(getKey());
		encrypted.append(hisuDes.enc(DesUtil.hisuLeftAddZero(new StringBuilder().append(hisuLen).toString(), 2)
				+ DesUtil.hisuBytes2HexString(hisuBytes)));
		return encrypted.toString();
	}
}

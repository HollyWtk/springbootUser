package com.yhh.core.encrypter;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.crypto.provider.SunJCE;

@SuppressWarnings({ "restriction", "unused" })
public class Des {

    private String Algorithm = "DESede";

    private byte[] inkey = { 49, 50, 51, 52, 53, 54, 55, 56 };

    private KeyGenerator keygen;

    private SecretKey deskey;

    private Cipher c;

    public Des() {
        init();
    }

    public Des(String hexkey) {
        String key = hexkey;
        if (hexkey.length() == 16)
            key = key + hexkey + hexkey;
        else if (hexkey.length() == 32) {
            key = key + hexkey.substring(0, 16);
        }
        this.inkey = DesUtil.hisuHexString2Bytes(key);
        init();
    }

    public void init() {
        Security.addProvider(new SunJCE());
        try {
            this.keygen = KeyGenerator.getInstance(this.Algorithm);
            this.deskey = new SecretKeySpec(this.inkey, this.Algorithm);
            this.c = Cipher.getInstance("DESede/ECB/NOPADDING");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        }
    }

    public byte[] createEncryptor(byte[] tb) {
        byte[] cipherByte = (byte[]) null;
        try {
            this.c.init(1, this.deskey);
            cipherByte = this.c.doFinal(DesUtil.hisuAllRightZreoTo8Multiple(tb));
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cipherByte;
    }

    public byte[] createDecryptor(byte[] tb) {
        byte[] plainByte = (byte[]) null;
        byte[] plainByteTrim = (byte[]) null;
        try {
            this.c.init(2, this.deskey);
            plainByte = this.c.doFinal(tb);
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        }
        plainByteTrim = DesUtil.hisuAllTrimZreoFrom8Multiple(plainByte);
        return plainByteTrim;
    }

    public String dec(String HexStr) {
        return DesUtil.hisuBytes2HexString(createDecryptor(DesUtil.hisuHexString2Bytes(HexStr)));
    }

    public String enc(String HexStr) {
        return DesUtil.hisuBytes2HexString(createEncryptor(DesUtil.hisuHexString2Bytes(HexStr)));
    }

    public String encStr2Hex(String str) {
        String HexStr = "";
        try {
            HexStr = DesUtil.hisuBytes2HexString(str.getBytes("utf-8"));
        } catch (Exception e) {
            return HexStr;
        }
        return enc(HexStr);
    }

    public String decHex2Str(String HexStr) {
        String decHexstr = dec(HexStr);
        String str = "";
        try {
            str = new String(DesUtil.hisuHexString2Bytes(decHexstr), "utf-8");
        } catch (Exception e) {
            return str;
        }
        return str;
    }
}

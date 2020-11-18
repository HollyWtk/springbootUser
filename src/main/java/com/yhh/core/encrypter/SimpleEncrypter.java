package com.yhh.core.encrypter;

//@Service
public class SimpleEncrypter implements Encrypter {

    public static final String DEFAULT_KEY = "32183921j3jkjsajdias";

    protected String key = "32183921j3jkjsajdias";

    public SimpleEncrypter() {
    }

    public SimpleEncrypter(String key) {
        this.key = key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String encrypt(String string) {
        int len = this.key.length();
        int ikey = 0;
        int iPos = 0;
        StringBuilder encrypted = new StringBuilder();

        for (int x = 1; x <= string.length(); x++) {
            iPos = x % len - len * (x % len == 0 ? -1 : 0);
            ikey = this.key.charAt(iPos - 1);
            encrypted.append(toHex(string.charAt(x - 1) ^ ikey));
        }
        return encrypted.toString();
    }

    public static int toDec(char cHex) {
        int iDec = 0;
        if (cHex <= '9')
            iDec = cHex - '0';
        else {
            iDec = Character.toUpperCase(cHex) - '7';
        }
        return iDec;
    }

    public static String toHex(int dec) {
        String hexChars = "0123456789ABCDEF";
        if (dec > 255)
            return null;
        int i = dec % 16;
        int j = (dec - i) / 16;
        String result = "";
        result = result + hexChars.charAt(j);
        result = result + hexChars.charAt(i);
        return result;
    }
}

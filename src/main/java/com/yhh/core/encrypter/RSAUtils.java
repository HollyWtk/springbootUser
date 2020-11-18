package com.yhh.core.encrypter;

import java.io.File;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public abstract class RSAUtils {

    private static final String ALGORITHOM = "RSA";

    private static final String RSA_PAIR_FILENAME = "/__RSA_PAIR.txt";

    private static final int KEY_SIZE = 1024;

    private static final Provider DEFAULT_PROVIDER = new BouncyCastleProvider();

    private static KeyPairGenerator keyPairGen = null;

    private static KeyFactory keyFactory = null;

    private static KeyPair oneKeyPair = null;

    private static File rsaPairFile = new File(getRSAPairFilePath());

    static {
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA", DEFAULT_PROVIDER);
            keyFactory = KeyFactory.getInstance("RSA", DEFAULT_PROVIDER);
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
        }
    }

    private static synchronized KeyPair generateKeyPair() {
        try {
            keyPairGen.initialize(1024, new SecureRandom(DateFormatUtils.format(new Date(), "yyyyMMdd").getBytes()));
            oneKeyPair = keyPairGen.generateKeyPair();
            // saveKeyPair(oneKeyPair);
            return oneKeyPair;
        } catch (InvalidParameterException ex) {
            log.error("KeyPairGenerator does not support a key length of 1024.", ex);
        } catch (NullPointerException ex) {
            log.error("RSAUtils#KEY_PAIR_GEN is null, can not generate KeyPairGenerator instance.", ex);
        }
        return null;
    }

    private static String getRSAPairFilePath() {
        String urlPath = RSAUtils.class.getResource("/").getPath();
        return new File(urlPath.replaceAll("%20", " ")).getParent() + "/__RSA_PAIR.txt";
    }

    private static boolean isCreateKeyPairFile() {
        boolean createNewKeyPair = false;
        if ((!rsaPairFile.exists()) || (rsaPairFile.isDirectory())) {
            createNewKeyPair = true;
        }
        return createNewKeyPair;
    }

    public static KeyPair getKeyPair() {
        if (isCreateKeyPairFile()) {
            return generateKeyPair();
        }
        if (oneKeyPair != null) {
            return oneKeyPair;
        }

        return oneKeyPair;
    }

    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) {
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
        } catch (InvalidKeySpecException ex) {
            log.error("RSAPublicKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            log.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) {
        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus),
                new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
        } catch (InvalidKeySpecException ex) {
            log.error("RSAPrivateKeySpec is unavailable.", ex);
        } catch (NullPointerException ex) {
            log.error("RSAUtils#KEY_FACTORY is null, can not generate KeyFactory instance.", ex);
        }
        return null;
    }

    public static RSAPrivateKey getRSAPrivateKey(String hexModulus, String hexPrivateExponent) {
        if ((StringUtils.isBlank(hexModulus)) || (StringUtils.isBlank(hexPrivateExponent))) {
            if (log.isDebugEnabled()) {
                log.debug("hexModulus and hexPrivateExponent cannot be empty. RSAPrivateKey value is null to return.");
            }
            return null;
        }
        byte[] modulus = (byte[]) null;
        byte[] privateExponent = (byte[]) null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            privateExponent = Hex.decodeHex(hexPrivateExponent.toCharArray());
        } catch (DecoderException ex) {
            log.error("hexModulus or hexPrivateExponent value is invalid. return null(RSAPrivateKey).");
        }
        if ((modulus != null) && (privateExponent != null)) {
            return generateRSAPrivateKey(modulus, privateExponent);
        }
        return null;
    }

    public static RSAPublicKey getRSAPublidKey(String hexModulus, String hexPublicExponent) {
        if ((StringUtils.isBlank(hexModulus)) || (StringUtils.isBlank(hexPublicExponent))) {
            if (log.isDebugEnabled()) {
                log.debug("hexModulus and hexPublicExponent cannot be empty. return null(RSAPublicKey).");
            }
            return null;
        }
        byte[] modulus = (byte[]) null;
        byte[] publicExponent = (byte[]) null;
        try {
            modulus = Hex.decodeHex(hexModulus.toCharArray());
            publicExponent = Hex.decodeHex(hexPublicExponent.toCharArray());
        } catch (DecoderException ex) {
            log.error("hexModulus or hexPublicExponent value is invalid. return null(RSAPublicKey).");
        }
        if ((modulus != null) && (publicExponent != null)) {
            return generateRSAPublicKey(modulus, publicExponent);
        }
        return null;
    }

    public static byte[] encrypt(PublicKey publicKey, byte[] data) {

        try {
            Cipher ci = Cipher.getInstance("RSA", DEFAULT_PROVIDER);
            ci.init(1, publicKey);
            return ci.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(PrivateKey privateKey, byte[] data) {

        try {
            Cipher ci = Cipher.getInstance("RSA/ECB/PKCS1Padding", DEFAULT_PROVIDER);
            ci.init(2, privateKey);
            return ci.doFinal(data);
        } catch (Exception ex) {
            log.error(ex.getCause().getMessage());
        }
        return null;
    }

    public static String encryptString(PublicKey publicKey, String plaintext) {
        if ((publicKey == null) || (plaintext == null)) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        try {
            byte[] en_data = encrypt(publicKey, data);
            return new String(Hex.encodeHex(en_data));
        } catch (Exception ex) {
            log.error(ex.getCause().getMessage());
        }
        return null;
    }

    public static String encryptString(String plaintext) {
        if (plaintext == null) {
            return null;
        }
        byte[] data = plaintext.getBytes();
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = encrypt((RSAPublicKey) keyPair.getPublic(), data);
            return new String(Hex.encodeHex(en_data));
        } catch (NullPointerException ex) {
            log.error("keyPair cannot be null.");
        } catch (Exception ex) {
            log.error(ex.getCause().getMessage());
        }
        return null;
    }

    public static String decryptString(PrivateKey privateKey, String encrypttext) {
        if ((privateKey == null) || (StringUtils.isBlank(encrypttext)))
            return null;
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt(privateKey, en_data);
            return new String(data);
        } catch (Exception ex) {
            log.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[] { encrypttext, ex.getCause().getMessage() }));
        }
        return null;
    }

    public static String decryptString(String encrypttext) {
        if (StringUtils.isBlank(encrypttext)) {
            return null;
        }
        KeyPair keyPair = getKeyPair();
        try {
            byte[] en_data = Hex.decodeHex(encrypttext.toCharArray());
            byte[] data = decrypt((RSAPrivateKey) keyPair.getPrivate(), en_data);
            return new String(data);
        } catch (NullPointerException ex) {
            log.error("keyPair cannot be null.");
        } catch (Exception ex) {
            log.error(String.format("\"%s\" Decryption failed. Cause: %s",
                    new Object[] { encrypttext, ex.getMessage() }));
        }
        return null;
    }

    public static String decryptStringByJs(String encrypttext) {
        String text = decryptString(encrypttext);
        if (text == null) {
            return null;
        }
        return StringUtils.reverse(text);
    }

    public static RSAPublicKey getDefaultPublicKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPublicKey) keyPair.getPublic();
        }
        return null;
    }

    public static RSAPrivateKey getDefaultPrivateKey() {
        KeyPair keyPair = getKeyPair();
        if (keyPair != null) {
            return (RSAPrivateKey) keyPair.getPrivate();
        }
        return null;
    }

    public static RSAPublicKey loadPublicKey(String baseCodePublic) {
        try {
            byte[] buffer = decryptBase64(baseCodePublic);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static RSAPrivateKey loadPrivateKey(String baseCodePrivate) {
        try {
            byte[] buffer = decryptBase64(baseCodePrivate);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHOM, DEFAULT_PROVIDER);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64解密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) {

        return Base64.decodeBase64(key);

    }
}

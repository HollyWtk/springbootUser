package com.yhh.core.encrypter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.apache.commons.codec.binary.Base64;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年11月18日  
 */
public class GenerateKey {

    public static void main(String[] args) throws NoSuchAlgorithmException {
     // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
        // 初始化密钥对生成器，密钥大小为96-1024位  
        keyPairGen.initialize(1024,new SecureRandom());  
        // 生成一个密钥对，保存在keyPair中  
        KeyPair keyPair = keyPairGen.generateKeyPair();  
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));  
        // 得到私钥字符串  
        String privateKeyStr = new String(Base64.encodeBase64((privateKey.getEncoded())));  
        System.out.println(publicKeyStr);
        System.out.println(privateKeyStr);
    }
}

package com.yhh;

import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.yhh.core.config.RsaKeyProperties;
import com.yhh.core.encrypter.RSAEncrypt;

@SpringBootTest
class YhhApplicationTests {

    @Autowired
    private RsaKeyProperties key;
    
	@Test
	void contextLoads() throws Exception {
	   String a =  RSAEncrypt.encrypt("123123",key.getPublicKey());
	   System.out.println(a);
	   String b = RSAEncrypt.decrypt(a,key.getPrivateKey());
	   System.out.println(b);
	}

}

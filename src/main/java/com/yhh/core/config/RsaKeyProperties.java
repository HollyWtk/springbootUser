package com.yhh.core.config;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年11月18日  
 */
@Configuration
@Data
public class RsaKeyProperties {

    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCs4Wq9j7BNltPT83ylhrtg2qTgTQDNbZcIirCKQfI4+rE1nGVw8eyNwze7uMRmPHLV3DQeIixd+/jNKPJosbwtiAH+b/7s4Rev0Oq2pGw0TgXdZCCYJR16JMKn6Wig8Q20Z8bZe3R6D17EFttsss7CcSxqYiBgsCjUhIAMbjyw6QIDAQAB";
    
    private String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKzhar2PsE2W09PzfKWGu2DapOBNAM1tlwiKsIpB8jj6sTWcZXDx7I3DN7u4xGY8ctXcNB4iLF37+M0o8mixvC2IAf5v/uzhF6/Q6rakbDROBd1kIJglHXokwqfpaKDxDbRnxtl7dHoPXsQW22yyzsJxLGpiIGCwKNSEgAxuPLDpAgMBAAECgYAr36kvImAj0LtBdvGTMHYtRk/BfMlPWRiTWl8jN0k5nKWMbXxxBXR7JafnRCTTrVOXYqL3YsTPtTqXFmUunKyclUinGCbZClm3sfMLJkP//WdgEBX+pyX+9MSfJ8XJsQQhNN1Z7rwrbTD2lzaDkXHBtm6hOKJZtfEIZe+N2nStgQJBAPfQbGCdlexFO+rz4PijDwKwnKOXjVIeTFYHpsssQKPstvIk6H57mBJFOtNVvxkMZO3lCKotgJZG0mm+fYeSgzUCQQCyl1Y0G0HCK7HnB7+7j2vz6ohs9K7DKe+0UwRgCZ8efmqK9ROmUEaxbTXl3DM0No6kJqIBxoKi5CCI9rHXO9llAkEA0cVIOHCs3BevJK/UnCPLLQe7UG+VXRHwpFJNymZnQVu7cTX06DxO+ghIR2SqItXvCcF6mDN0ScEhlAl/0ScgkQJAcZMIwU5sPV2oX7Jv3onRL2eBpzjcQ0VSKa155NF5ndzrSL+e5W+75W38jtfAwH3JrN7cvKBPx+OoRTIN20E2eQJBAISPKK5i8MueVjBLlcL1HyiChf+ZTYfesTaI/zdwxn/BVOe7LRFK4gINKHtb2C42Y+9w0M/w+YsCsznKpvhmw9s=";
}

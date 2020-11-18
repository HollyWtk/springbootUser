package com.yhh.core.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.yhh.core.utils.ResMsg;
import com.yhh.model.dto.UserRegisterDto;
import com.yhh.model.dto.VerifyCodeDto;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年11月17日  
 */
public interface IHomePageService {

    void createImage(HttpServletResponse response, String captchaUUID) throws IOException;
    
    ResMsg checkVerifyCode(String verifyCode,String captchaUUID);
    
    void clearVerifyCode(String captchaUUID);
    
    ResMsg sendSmsVerifyCode(VerifyCodeDto dto);
    
    ResMsg registerUser(UserRegisterDto dto);
}

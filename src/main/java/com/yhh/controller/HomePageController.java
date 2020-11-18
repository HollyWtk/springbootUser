package com.yhh.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yhh.core.service.IHomePageService;
import com.yhh.core.utils.ResMsg;
import com.yhh.model.dto.UserRegisterDto;
import com.yhh.model.dto.VerifyCodeDto;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年11月17日  
 */
@RestController
public class HomePageController {
    
    @Autowired
    private IHomePageService homePageService;
    
    @GetMapping(value = "/captcha.jpg/{captchaUUID}")
    public void captcha(HttpServletResponse response,@PathVariable String captchaUUID) throws IOException {
        homePageService.createImage(response, captchaUUID);
    }
    
    @PostMapping("/smsCode")
    public ResMsg smsVerifyCode(@RequestBody @Valid VerifyCodeDto dto) {
        return homePageService.sendSmsVerifyCode(dto);
    }
    
    @PostMapping("/register")
    public ResMsg registerByMobile(@RequestBody @Valid UserRegisterDto dto) {
        return homePageService.registerUser(dto);
    }
    
}

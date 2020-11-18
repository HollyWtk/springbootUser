package com.yhh.core.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yhh.core.service.IHomePageService;
import com.yhh.core.utils.ResMsg;
import com.yhh.core.utils.SpringBeanFactoryUtils;

/**
 * 密码验证
 */
public class PasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;

        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String verifyCode = request.getHeader("verifyCode");
        String captchaUUID = request.getHeader("captchaUUID");
        IHomePageService verifyCodeService = SpringBeanFactoryUtils.getBeanByClass(IHomePageService.class);
        ResMsg res = verifyCodeService.checkVerifyCode(verifyCode,captchaUUID);
        if(!res.isSuccess()) {
            try {
                responseText(response, res);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        responseText(response, ResMsg.ERROR(-1, failed.getMessage()));
    }

    private static void responseText(HttpServletResponse response, Object content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String strContent = mapper.writeValueAsString(content);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = strContent.getBytes(StandardCharsets.UTF_8);
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
    }

}

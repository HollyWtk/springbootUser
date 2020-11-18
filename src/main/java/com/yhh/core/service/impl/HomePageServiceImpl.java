package com.yhh.core.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.code.kaptcha.Producer;
import com.yhh.core.enums.CacheConsts;
import com.yhh.core.service.IHomePageService;
import com.yhh.core.utils.DateTimeUtils;
import com.yhh.core.utils.KeyPrefixConst;
import com.yhh.core.utils.RandomStrUtil;
import com.yhh.core.utils.RedisUtil;
import com.yhh.core.utils.ResMsg;
import com.yhh.model.dto.UserRegisterDto;
import com.yhh.model.dto.VerifyCodeDto;
import com.yhh.model.entity.User;
import com.yhh.service.IUserService;

import lombok.extern.slf4j.Slf4j;

/**  
 * <p>Description: 验证码业务类 </p>  
 * @author yhh  
 * @date 2020年11月17日  
 */
@Service
@Slf4j
public class HomePageServiceImpl implements IHomePageService{

    @Autowired
    private Producer producer;
    
    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private IUserService userService;
    
    @Override
    public void createImage(HttpServletResponse response, String captchaUUID) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        redisUtil.set(KeyPrefixConst.captcha + captchaUUID, text, 5 * 60);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }

    @Override
    public ResMsg checkVerifyCode(String verifyCode, String captchaUUID) {
       String cacheCode = (String) redisUtil.getAndDeleteValue(KeyPrefixConst.captcha + captchaUUID);
       return ResMsg.SUCCESS();
//       return Objects.nonNull(cacheCode) && Objects.equals(cacheCode.toLowerCase(), verifyCode.toLowerCase()) 
//               ? ResMsg.SUCCESS() : ResMsg.ERROR(-1, "验证码验证失败");
    }

    @Override
    public void clearVerifyCode(String captchaUUID) {
        redisUtil.delete(KeyPrefixConst.captcha + captchaUUID);
    }
    
    @Override
    public ResMsg sendSmsVerifyCode(VerifyCodeDto dto) {
        String limitKey = CacheConsts.getPrefixWithMobile(CacheConsts.SMS_VERIFY_LIMIT, dto.getMobile());
        String code; 
        if (redisUtil.hasKey(limitKey)) {
            return ResMsg.ERROR(-1, "发送短信过于频繁");
        }else {
            CacheConsts cache = CacheConsts.getByType(dto.getType());
            if(Objects.nonNull(cache)) {
                code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                //TODO 发送验证码
                log.info("模拟短信验证码:{},验证码类型:{},接收手机号:{}",code,cache.getType(),dto.getMobile());
                redisUtil.set(CacheConsts.getPrefixWithMobile(cache,dto.getMobile()),code,cache.expireTime());
                redisUtil.set(limitKey,code,CacheConsts.SMS_VERIFY_LIMIT.expireTime());
            }else {
                return ResMsg.ERROR(-1, "短信验证码类型错误 ");
            }
        }
        return ResMsg.SUCCESS();
    }

    @Override
    public ResMsg registerUser(UserRegisterDto dto) {
        //TODO 模拟前台ras公钥加密
        String password = "NWWSJvqwB6mQGTHKFWFgUG6I3ebAXo7OnfWoomqSlLY5R5wB92AHTEhTVBkk5kE7Qh4ECcQO8i2jXWe60RHUHHrWECUQSvbw713RU16eELiKI7DTuv0JOEIrPSaQVIlAGsCQ+U9/giRmMEB3vW51ViImu2m2/F/VqSNrq9Fy1jU=";
        dto.setPassword(password);
        long count = userService.count(new LambdaQueryWrapper<User>().eq(User::getMobile, dto.getMobile()));
        if(count > 0) {
            return ResMsg.ERROR(-1, "该手机号已被注册");
        }
        String regSmsCode = (String) redisUtil.getAndDeleteValue(
                                CacheConsts.getPrefixWithMobile(CacheConsts.SMS_VERIFY_CODE_REGISTER, dto.getMobile()));
        if(Objects.nonNull(regSmsCode) && Objects.equals(dto.getSmsVerifyCode(), regSmsCode)) {
            User user = new User();
            user.setAccountName(RandomStrUtil.randomPassword(10));
            user.setPassword(dto.getPassword());
            user.setStatus("ENABLED");
            user.setMobile(dto.getMobile());
            user.setCreateTime(DateTimeUtils.getStringToday());
            userService.save(user);
        }else {
            return ResMsg.ERROR(-1, "验证码校验失败");
        }
        return ResMsg.SUCCESS();
    }
    
}

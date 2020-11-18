package com.yhh.core.enums;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * <p>Description: 验证码缓存前缀 </p>  
 * @author yhh  
 * @date 2020年11月18日
 */
public enum CacheConsts {
    
    /**
     * 登陆短信验证码
     */
    SMS_VERIFY_CODE_LOGIN("login","verify_code:sms_login", 180),
    
    /**
     * 注册短信验证码
     */
    SMS_VERIFY_CODE_REGISTER("register","verify_code:sms_register",300),
    
    /**
     * 重置密码短信验证码
     */
    SMS_VERIFY_CODE_RESET_PASSWORD("resetPwd","verify_code:sms_reset_password",300),
    
    /**
     * 用户修改手机号
     */
    SMS_VERIFY_CODE_CHANGE_PHONE("changePhone","verify_code:sms_change_phone",300),
    
    /**
     * 用户发短信时间间隔
     */
    SMS_VERIFY_LIMIT("limit","verify_code:sms_limit",45),
    
    ;

    private String type;
    
    private String prefixKey;
    
    private int expireTime;
    
    CacheConsts(String type,String prefixKey,int expireTime){
        this.type = type;
        this.prefixKey = prefixKey;
        this.expireTime = expireTime;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getPrefixKey() {
        return this.prefixKey;
    }
    
    public int expireTime() {
        return this.expireTime;
    }
    
    public static String getKeyByType(String type) {
        return Arrays.stream(CacheConsts.values())
                .filter(k -> Objects.equals(k.getType(), type))
                .findFirst().map(CacheConsts::getPrefixKey)
                .orElse(StringUtils.EMPTY);
    }
    
    public static CacheConsts getByType(String type) {
        return Arrays.stream(CacheConsts.values()).filter(k -> Objects.equals(k.getType(), type)).findFirst().get();
    }
    
    public static String jointCacheKey(String... tags){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tags.length ; i++){
            sb.append(tags[i]);
            if(i < tags.length - 1){
                sb.append(":");
            }
        }
        return sb.toString();
    }
    
    public static String getPrefixWithMobile(CacheConsts cache,String mobile) {
        return cache.prefixKey + ":" + mobile;
    }
}

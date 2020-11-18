package com.yhh.core.cache;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yhh.core.config.CacheType;
import com.yhh.core.config.PermissionContext;
import com.yhh.core.encrypter.Md5Util;
import com.yhh.core.utils.DateTimeUtils;
import com.yhh.core.utils.RedisUtil;
import com.yhh.model.entity.User;
import com.yhh.service.IUserService;

import lombok.extern.slf4j.Slf4j;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年8月31日  
 */
@Slf4j
@Component
public class RedisCacheService implements CacheService{

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IUserService userService;

    private static final String LOGIN_WRONG_CACHE = "user_login_fail_";

    private static final Integer LOGIN_WRONG_TIME_EXPIRE = 300;//s

    private static final Integer TOKEN_TIME_EXPIRE = 1800;//s

    private static final Integer LOGIN_WRONG_LIMIT = 5;
    
    private static final Integer USER_CACHE_TIME_EXPIRE = 300;
    
    private static final String CACHE_PREFIX = "yhh_cache_";

    @Override
    public String updateToken(String username) {
        String token = Md5Util.md5(UUID.randomUUID().toString().replace("-", "") 
                + DateTimeUtils.getStringDate() + username, 32);
        updateRedisCache(CacheType.TOKEN,username, token);
        //MngContext mngContext = new MngContext(username, getUserAuthedResources(username));
       // updateRedisCache(CacheType.SESSION,username, mngContext);
        User user = userService.getUserByMobileOrEmail(username);
        updateRedisCache(CacheType.USER,username, user);
        return token;
    }

    @Override
    public String updateToken(String username, String token) {
        updateRedisCache(CacheType.TOKEN,username, token);
        return token;
    }

    @Override
    public String getToken(String username) {
        return (String)redisUtil.hget(buildRedisKey(username), CacheType.TOKEN);
    }

    @Override
    public boolean verifyToken(String username, String token) {
        String exsitToken = getToken(username);
        boolean flag = Objects.equals(exsitToken,token);
        if(flag) {
            //更新token过期时间
            redisUtil.expire(buildRedisKey(username), TOKEN_TIME_EXPIRE);
        }
        return flag;
    }

    @Override
    public boolean loginStatus(String username) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public PermissionContext getSessionContext(String username) {
        return redisUtil.hget(buildRedisKey(username), CacheType.SESSION,PermissionContext.class);
    }

    @Override
    public void setMngContextAttribute(String username, String key, Serializable value) {
        redisUtil.hset(buildRedisKey(username), CacheType.SESSION,value);

    }

    @Override
    public Object getMngContextAttribute(String username, String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clearToken(String username) {
        log.info("清除用户:{},token:{}",username,getToken(username));
        redisUtil.hdel(buildRedisKey(username), CacheType.TOKEN,CacheType.SESSION,CacheType.USER);

    }

    @Override
    public String getUserAuthedResources(String username) {
//        if (StringUtils.isBlank(username)) return null;
//        //Role roleModel = roleMapper.queryRoleByUsername(username);
//        if (Objects.isNull(roleModel)) {
//            log.warn("Not find user: " + username + "'s role information.");
//            return null;
//        } else {
//            return roleModel.getFldRole();
//        }
        return null;
    }

    @Override
    public User updateUserInfo(String username) {
        User user = userService.getUserByMobileOrEmail(username);
        redisUtil.hset(buildRedisKey(username), CacheType.USER,user,USER_CACHE_TIME_EXPIRE);
        return user;
    }

    public User getUserInfo(String username) {
        return redisUtil.hget(buildRedisKey(username), CacheType.USER,User.class);
    }

    /**
     * 更新redis缓存
     * 
     * @param type
     * @param username
     * @param cache
     */
    private void updateRedisCache(CacheType type, String username, Serializable cache) {
        redisUtil.hset(buildRedisKey(username), type, cache,TOKEN_TIME_EXPIRE);
    }

    private static String buildRedisKey(String key) {
        return CACHE_PREFIX + key;
    }

    @Override
    public boolean checkWrongTime(String username) {
        Integer wrongTime = (Integer) redisUtil.get(LOGIN_WRONG_CACHE + username);
        boolean flag = true;
        if(Objects.nonNull(wrongTime) && wrongTime >= LOGIN_WRONG_LIMIT) {
            flag = false;
        }
        return flag;

    }

    @Override
    public void increaseWrongTime(String username) {
        Integer wrongTime = (Integer) redisUtil.get(LOGIN_WRONG_CACHE + username);
        wrongTime = Objects.isNull(wrongTime) ? 1 : ++wrongTime;
        redisUtil.set(LOGIN_WRONG_CACHE + username, wrongTime,LOGIN_WRONG_TIME_EXPIRE);
    }

    @Override
    public void clearWrongTime(String username) {
        redisUtil.delete(LOGIN_WRONG_CACHE + username);
        log.info("清除用户登陆错误次数:{}",username);
    }

}

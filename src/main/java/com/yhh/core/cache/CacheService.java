package com.yhh.core.cache;


import java.io.Serializable;

import com.yhh.core.config.PermissionContext;
import com.yhh.model.entity.User;

public interface CacheService {

    String updateToken(String username);
    
    /**
     * 人为操作致token被清除
     * @param username
     * @param token
     * @return
     */
    String updateToken(String username,String token);
    
    String getToken(String username);

    boolean verifyToken(String username, String token);

    boolean loginStatus(String username);

    PermissionContext getSessionContext(String username);

    void setMngContextAttribute(String username,String key,Serializable value);

    Object getMngContextAttribute(String username,String key);

    void clearToken(String username);

    public String getUserAuthedResources(String username);

    User updateUserInfo(String username);
    
    User getUserInfo(String username);
    
    boolean checkWrongTime(String username);
    
    void increaseWrongTime(String username);
    
    void clearWrongTime(String username);
}

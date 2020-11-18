package com.yhh.core.config;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年9月1日  
 */
public enum CacheType {
    
    TOKEN,
    
    USER,

    SESSION;

    public static CacheType parseType(String value) {
        switch (value) {
            case "session":
                return CacheType.SESSION;
            case "token":
                return CacheType.TOKEN;
            case "user":
                return CacheType.USER;
            default:
                return null;
        }
    }
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}

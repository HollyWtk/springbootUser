package com.yhh.core.utils;


import javax.servlet.http.HttpServletRequest;


/**
 *@sinse：0.1
 *@author：lxy 2018年4月16日 上午11:35:15 
*/
public class MvcUtils {
	
    public static String getIP(HttpServletRequest request){  
        String ip=request.getHeader("x-forwarded-for");  
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
            ip=request.getHeader("Proxy-Client-IP");  
        }  
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
            ip=request.getHeader("WL-Proxy-Client-IP");  
        }  
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
            ip=request.getHeader("X-Real-IP");  
        }  
        if(ip==null || ip.length()==0 || "unknown".equalsIgnoreCase(ip)){  
            ip=request.getRemoteAddr();  
        }  
        return ip;  
    }  

 
}


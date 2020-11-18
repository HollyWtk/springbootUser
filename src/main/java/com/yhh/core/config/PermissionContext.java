package com.yhh.core.config;

import com.yhh.model.entity.User;

import lombok.Data;

/**  
 * <p>Description:用户上下文 </p>  
 * @author yhh  
 * @date 2020年11月17日  
 */
@Data
public class PermissionContext {

    private Integer roleId;
    
    //private Role role;
}

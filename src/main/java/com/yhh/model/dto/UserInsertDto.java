package com.yhh.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**  
 * <p>Description: </p>  
 * @author yhh  
 * @date 2020年11月17日  
 */
@Data
public class UserInsertDto {

    @Pattern(regexp = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$"
            , message = "请输入正确的手机号")
    private String mobile;

    private String accountName;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotNull(message = "角色不能为空")
    private Integer roleId;

    @Pattern(regexp = "^$|^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"
            , message = "请输入正确的邮箱")
    private String email;
    
}

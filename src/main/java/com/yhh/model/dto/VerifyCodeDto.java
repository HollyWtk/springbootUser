package com.yhh.model.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

/**  
 * <p>Description: 获取验证码</p>  
 * @author yhh  
 * @date 2020年11月18日  
 */
@Data
public class VerifyCodeDto {

    /**
     * 验证码类型
     * {@link com.yhh.core.enums.CacheConsts}
     */
    @NotNull(message = "验证码类型不能为空")
    private String type;

    @Pattern(regexp = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$"
            , message = "请输入正确的手机号")
    private String mobile;
}

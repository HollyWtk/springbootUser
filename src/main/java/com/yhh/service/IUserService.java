package com.yhh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhh.model.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhh
 * @since 2020-11-17
 */
public interface IUserService extends IService<User> {

    boolean checkUserPwd(String username,String password) throws Exception;
    
    User getUserByMobileOrEmail(String username);
}

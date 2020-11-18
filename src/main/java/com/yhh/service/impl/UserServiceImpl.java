package com.yhh.service.impl;

import com.yhh.core.config.RsaKeyProperties;
import com.yhh.core.encrypter.RSAEncrypt;
import com.yhh.mapper.UserMapper;
import com.yhh.model.entity.User;
import com.yhh.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yhh
 * @since 2020-11-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RsaKeyProperties properties;
    
    @Override
    public boolean checkUserPwd(String username, String password) throws Exception {
        boolean flag = false;
        String dePassword = RSAEncrypt.decrypt(password, properties.getPrivateKey());
        User user = this.getUserByMobileOrEmail(username);
        if(Objects.equals(RSAEncrypt.decrypt(user.getPassword(), properties.getPrivateKey()), dePassword)) {
           flag = true; 
        }
        return flag;
    }

    @Override
    public User getUserByMobileOrEmail(String username) {
        User user = this.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username).or().eq(User::getMobile, username));
        return user;
    }

}

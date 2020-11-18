package com.yhh.core.security;


import java.io.Serializable;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yhh.core.cache.CacheService;
import com.yhh.core.utils.MngSvrEncrypter;
import com.yhh.core.utils.ResMsg;
import com.yhh.model.entity.User;
import com.yhh.service.IUserService;


/**
 * 从数据库中获取用户数据
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8965556313708080404L;

	@Autowired
    private IUserService userService;
	
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, username).or().eq(User::getMobile, username));
	    if (user == null) {
            throw new DisabledException("账户密码错误");
        }
	    return new User(user.getUsername(), user.getPassword(), new HashSet<>());
	}

}

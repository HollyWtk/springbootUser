package com.yhh.controller;


import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yhh.model.entity.User;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yhh
 * @since 2020-11-17
 */
@RestController
@RequestMapping("/yhh/user")
public class UserController {
    
    @PostMapping("/add")
    public User addUser() {
        return null;
    }
    
}

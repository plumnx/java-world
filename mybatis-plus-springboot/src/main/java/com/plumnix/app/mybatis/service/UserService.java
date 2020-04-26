package com.plumnix.app.mybatis.service;

import com.plumnix.app.mybatis.entity.User;
import com.plumnix.app.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void saveObject(User user) {
        userMapper.saveObject(user);
    }

    @Transactional
    public void saveSplits(User user) {
        userMapper.saveSplits(Integer.valueOf(new Random().nextInt(10_000_000)).longValue(), user.getName(), user.getAge(), user.getEmail(), user.getAddress());
    }

    public List<User> list() {
        return userMapper.list();
    }

    @Transactional
    public void saveNew(User user) {
        userMapper.saveNew(user);
    }
}

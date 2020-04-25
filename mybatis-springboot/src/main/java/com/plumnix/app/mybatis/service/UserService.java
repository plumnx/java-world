package com.plumnix.app.mybatis.service;

import com.plumnix.app.mybatis.mapper.UserMapper;
import com.plumnix.app.mybatis.entity.User;
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
    public void save(User user) {
        userMapper.save(user);
    }

    @Transactional
    public void saveSplits(User user) {
        userMapper.save(Integer.valueOf(new Random().nextInt(10_000_000)).longValue(), user.getName(), user.getAge(), user.getEmail(), user.getAddress());
    }

    public List<User> list() {
        return userMapper.list();
    }

    @Transactional
    public void saveNew(User user) {
        userMapper.saveNew(user);
    }
}

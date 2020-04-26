package com.plumnix.app.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.plumnix.app.mybatis.entity.UserPlus;
import com.plumnix.app.mybatis.mapper.UserPlusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserPlusService {

    @Autowired
    private UserPlusMapper userPlusMapper;

    @Transactional
    public void save(UserPlus userPlus) {
        userPlusMapper.insert(userPlus);
    }

    public List<UserPlus> list() {
        return userPlusMapper.selectList(new QueryWrapper<>());
    }

    @Transactional
    public void saveNew(UserPlus userPlus) {
        userPlusMapper.insert(userPlus);
    }
}

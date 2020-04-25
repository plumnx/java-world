package com.plumnix.app.mybatis.api;

import com.plumnix.app.mybatis.entity.UserPlus;
import com.plumnix.app.mybatis.service.UserPlusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/plus")
public class UserPlusController {

    @Autowired
    private UserPlusService userPlusService;

    @GetMapping("/save")
    public void saveUser() {
        UserPlus user = new UserPlus();
        user.setId(new Random(1_000_000_000).nextLong());
        user.setName("name " + new Random(1000).nextLong());
        user.setAge(new Random(100).nextInt());
        user.setEmail("email " + new Random(1000).nextLong());
        user.setAddress("address " + new Random(1000).nextLong());
        userPlusService.save(user);
    }

    @GetMapping("/saveNew")
    public void saveNew() {
        UserPlus user = new UserPlus();
        user.setName("name " + new Random(1000).nextLong());
        user.setAge(new Random(100).nextInt());
        user.setEmail("email " + new Random(1000).nextLong());
        user.setAddress("address " + new Random(1000).nextLong());
        userPlusService.saveNew(user);
    }

    @GetMapping("/list")
    public List<UserPlus> list() {
        return userPlusService.list();
    }

}

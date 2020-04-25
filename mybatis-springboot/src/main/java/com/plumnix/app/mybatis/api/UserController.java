package com.plumnix.app.mybatis.api;

import com.plumnix.app.mybatis.entity.User;
import com.plumnix.app.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/save")
    public void saveUser() {
        User user = new User();
        user.setId(new Random(1_000_000_000).nextLong());
        user.setName("name " + new Random(1000).nextLong());
        user.setAge(new Random(100).nextInt());
        user.setEmail("email " + new Random(1000).nextLong());
        user.setAddress("address " + new Random(1000).nextLong());
        userService.save(user);
    }

    @GetMapping("/saveSplits")
    public void saveSplits() {
        User user = new User();
        user.setId(new Random(1_000_000_000).nextLong());
        user.setName("name " + new Random(1000).nextLong());
        user.setAge(new Random(100).nextInt());
        user.setEmail("email " + new Random(1000).nextLong());
        user.setAddress("address " + new Random(1000).nextLong());
        userService.saveSplits(user);
    }

    @GetMapping("/saveNew")
    public void saveNew() {
        User user = new User();
        user.setName("name " + new Random(1000).nextLong());
        user.setAge(new Random(100).nextInt());
        user.setEmail("email " + new Random(1000).nextLong());
        user.setAddress("address " + new Random(1000).nextLong());
        userService.saveNew(user);
    }

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

}

package com.plumnix.app.mybatis.mapper;

import com.plumnix.app.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into user2(id, name, age, email, address) values(#{user.id}, #{user.name}, #{user.age}, #{user.email}, #{user.address})")
    boolean saveObject(@Param("user") User user);

    @Insert("insert into user(id, name, age, email, address) values(#{id}, #{name}, #{age}, #{email}, #{address})")
    boolean saveSplits(@Param("id") Long id, @Param("name") String name, @Param("age") int age, @Param("email") String email, @Param("address") String address);

    @SelectKey(statement = "select user_seq.nextval", keyProperty = "user.id", before = true, resultType = long.class)
    @Insert("insert into user(id, name, age, email, address) values(#{user.id}, #{user.name}, #{user.age}, #{user.email}, #{user.address})")
    int saveNew(@Param("user") User user);

    @Select("select id, name, age, email, address from user")
    List<User> list();
}

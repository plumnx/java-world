package com.plumnix.app.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
@KeySequence(value = "user_seq")
public class UserPlus implements Serializable {

    @TableId(type = IdType.INPUT)
    private Long id;

    private String name;

    private int age;

    private String email;

    private String address;

}

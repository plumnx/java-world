DROP TABLE IF EXISTS user;

CREATE TABLE user
(
  id BIGINT(20) NOT NULL COMMENT '主键ID',
  name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
  age INT(11) NULL DEFAULT NULL COMMENT '年龄',
  email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  address VARCHAR(200) NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (id)
);

CREATE TABLE user2
(
  id BIGINT(20) NOT NULL COMMENT '主键ID',
  name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
  age INT(11) NULL DEFAULT NULL COMMENT '年龄',
  email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
  address VARCHAR(200) NULL DEFAULT NULL COMMENT '地址',
  PRIMARY KEY (id)
);

CREATE SEQUENCE user_seq;
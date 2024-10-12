DROP TABLE IF EXISTS tb_info_to_user;
CREATE TABLE tb_info_to_user(
    `info_to_user_id` INT(11) NOT NULL AUTO_INCREMENT  COMMENT '主键' ,
    `identity_info` VARCHAR(50) NOT NULL  DEFAULT '0' COMMENT '用户账号' ,
    `user_no` INT(11)    COMMENT '用户主键' ,
    `identity_type` INT(11)    COMMENT '登录类型:0 用户名登录 1手机登录 2 邮箱登录' ,
    PRIMARY KEY (info_to_user_id)
)  COMMENT = '用户电话关系表';

DROP TABLE IF EXISTS tb_menu;
CREATE TABLE tb_menu(
    `menu_code` VARCHAR(50) NOT NULL   COMMENT '' ,
    `parent_id` INT(11)    COMMENT '' ,
    `menu_id` INT(11)    COMMENT '' ,
    `name` VARCHAR(50)    COMMENT '' ,
    `menuType` INT(11)    COMMENT '' ,
    `num` INT(11)    COMMENT '' ,
    `url` VARCHAR(50)    COMMENT '' ,
    `code` VARCHAR(50)    COMMENT '' ,
    `icon` VARCHAR(50)    COMMENT '' ,
    PRIMARY KEY (menu_code)
)  COMMENT = '菜单';

DROP TABLE IF EXISTS tb_role;
CREATE TABLE tb_role(
    `role_name` VARCHAR(50)    COMMENT '角色名称' ,
    `role_code` VARCHAR(50) NOT NULL   COMMENT '角色代号主键' ,
    PRIMARY KEY (role_code)
)  COMMENT = '角色表';

DROP TABLE IF EXISTS tb_role_to_menu;
CREATE TABLE tb_role_to_menu(
    `role_to_menu_id` INT(11) NOT NULL AUTO_INCREMENT  COMMENT '' ,
    `role_code` VARCHAR(50)    COMMENT '' ,
    `menu_code` VARCHAR(50)    COMMENT '' ,
    PRIMARY KEY (role_to_menu_id)
)  COMMENT = '角色菜单表';

DROP TABLE IF EXISTS tb_user;
CREATE TABLE tb_user(
    `user_no` VARCHAR(50) NOT NULL   COMMENT '' ,
    `mobile` INT(11)    COMMENT '' ,
    `user_name` VARCHAR(50)    COMMENT '' ,
    `pass_word` VARCHAR(50)    COMMENT '' ,
    `email` VARCHAR(50)    COMMENT '' ,
    `create_time` BIGINT(20)    COMMENT '' ,
    `avatar` VARCHAR(50)    COMMENT '' ,
    `status` INT(11)    COMMENT '' ,
    `job` VARCHAR(50)    COMMENT '' ,
    PRIMARY KEY (user_no)
)  COMMENT = '用户表';

DROP TABLE IF EXISTS tb_user_thirdparty;
CREATE TABLE tb_user_thirdparty(
    `user_thirdparty_id` INT(11) NOT NULL AUTO_INCREMENT  COMMENT '' ,
    `open_id` VARCHAR(50)    COMMENT '' ,
    `user_no` VARCHAR(50)    COMMENT '' ,
    `access_token` VARCHAR(50)    COMMENT '' ,
    `provider_type` VARCHAR(50)    COMMENT '' ,
    `status` INT(11)    COMMENT '' ,
    `create_time` BIGINT(20)    COMMENT '' ,
    PRIMARY KEY (user_thirdparty_id)
)  COMMENT = '第三方用户表';

DROP TABLE IF EXISTS tb_user_to_role;
CREATE TABLE tb_user_to_role(
    `user_to_role_id` INT(11) NOT NULL AUTO_INCREMENT  COMMENT '' ,
    `user_no` VARCHAR(50)    COMMENT '' ,
    `role_code` VARCHAR(50)    COMMENT '' ,
    PRIMARY KEY (user_to_role_id)
)  COMMENT = '用户角色关系表';


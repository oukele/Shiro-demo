# Shiro-demo
Shiro的简单使用

#### ShiroDemo  代码形式

#### ShiroDemo1 读取资源配置形式

#### ShiroDemo2 连接数据库形式。

#### 数据库表：
~~~
drop  database shiro;
create  database shiro;
use shiro;

create table users(
	username varchar(10),
	`password` varchar(10)
);

insert into users (username,`password`) values("oukele","oukele"),("ou","ou");

select * from users;

create table user_roles(
	username varchar(10),
	role_name varchar(10),
	permission varchar(50)
);
insert into user_roles(username,role_name,permission) values("oukele","admin","user:delete"),("ou","user","user:select");

select * from user_roles;
~~~

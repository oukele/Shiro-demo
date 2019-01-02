# Shiro-demo
Shiro的简单使用


#### 使用 shiro 的步骤。
+ 设置 域 (规则) JdbcRealm
+ 初始化 securityManager 
+ 使用 shiro 进行认证 subject
+ 登陆成功后，就可以使用shiro 进行权限验证。 checkRole、checkPermission

~~~
package com.oukele;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.beans.PropertyVetoException;

public class ShiroDemo3 {
    //c3p0 连接池
    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    {
        try {
            //数据库驱动
            dataSource.setDriverClass("org.mariadb.jdbc.Driver");
            //连接数据库
            dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/shiro");
            //数据库 用户名 + 密码
            dataSource.setUser("oukele");
            dataSource.setPassword("oukele");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo() {
        //  JdbcRealm 默认 所有 信息 在 数据库中。
        //规则器
        JdbcRealm jdbcRealm = new JdbcRealm();
        //加载 资源
        jdbcRealm.setDataSource(dataSource);
        //修改默认的SQL语法
        // 用户 + 密码 ( 登陆认证 )
        jdbcRealm.setAuthenticationQuery("select password from users where username = ?");
        // 用户角色 ( 权限认证 )
        jdbcRealm.setPermissionsQuery("select permission from user_roles where role_name = ?");
        // 允许 查看权限 默认是 false
        jdbcRealm.setPermissionsLookupEnabled(true);

        //创建主体管理器
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        //设置 规则
        securityManager.setRealm(jdbcRealm);
        //凭证
        UsernamePasswordToken token = new UsernamePasswordToken("ouo", "ouo");
        // 设置 安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        //得到当前执行的一个主体
        Subject subject = SecurityUtils.getSubject();
        //验证  凭证中的 用户 与 密码
        subject.login(token);
        //验证 成功 为 true  失败 为 false
        System.out.println("当前登录状态：" + subject.isAuthenticated());
        //验证 当前 登录用户 的角色 (admin 、 user ...)
        subject.checkRole("admin");
        //验证 当前 登录用户 的资源权限 (update 、 select 、 insert....)
        subject.checkPermission("user:select");

        System.out.println("当前用户 :"+subject.getPrincipal()+"退出登陆.");
        //退出登陆
        subject.logout();
        System.out.println("当前登录状态：" + subject.isAuthenticated());

    }

}
~~~


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

insert into users (username,`password`) values("oukele","oukele"),("ou","ou"),("ouo","ouo");

select * from users;

create table user_roles(
	username varchar(10),
	role_name varchar(10),
	permission varchar(50)
);
insert into user_roles(username,role_name,permission) values("oukele","admin","user:delete"),("ou","user","user:select"),("ouo","admin","user:*");

select * from user_roles;
~~~

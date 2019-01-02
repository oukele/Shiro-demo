package com.oukele;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

import java.beans.PropertyVetoException;

public class ShiroDemo2 {
    //连接池
    ComboPooledDataSource dataSource = new ComboPooledDataSource();

    {
        try {
            dataSource.setDriverClass("org.mariadb.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/shiro");
            dataSource.setUser("oukele");
            dataSource.setPassword("oukele");
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void demo() {

        JdbcRealm jdbcRealm = new JdbcRealm();// 规则
        jdbcRealm.setDataSource(dataSource);//加载资源
        //修改了默认SQL语法
        jdbcRealm.setAuthenticationQuery("select password from users where username = ?");// 验证 用户 + 密码 （登陆认证）
        jdbcRealm.setPermissionsQuery("select permission from user_roles where role_name = ?");// 验证 用户 + 权限 ( 权限认证)
        // 允许看权限 默认 false
        jdbcRealm.setPermissionsLookupEnabled(true);
        //创建主体
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(jdbcRealm);//设置 规则
        //认证器
        UsernamePasswordToken token = new UsernamePasswordToken("ou", "ou");
        SecurityUtils.setSecurityManager(securityManager);//设置安全管理器
        Subject subject = SecurityUtils.getSubject();// 得到一个当前执行的主体
        subject.login(token);//验证 认证器中的用户
        //验证当前数据
        System.out.println(" 登陆状态：" + subject.isAuthenticated());
        //验证当前角色
        subject.checkRole("user");
        //验证角色的资源权限
        subject.checkPermission("user:select");

        System.out.println("当前用户：" + subject.getPrincipal());

        System.out.println("===========退出登陆===========");
        subject.logout();//退出
        System.out.println(" 登陆状态：" + subject.isAuthenticated());

    }

}

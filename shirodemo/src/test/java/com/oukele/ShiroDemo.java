package com.oukele;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class ShiroDemo {

    //规则器
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm();

    @Before
    public void addUser() {
        //添加 规则
        simpleAccountRealm.addAccount("oukele", "oukele","admin");
        simpleAccountRealm.addAccount("oukele1","oukele1","user");
    }

    @Test
    public void demo() {
        //创建主体
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(simpleAccountRealm);//设置 规则
        //认证器
        UsernamePasswordToken token = new UsernamePasswordToken("oukele", "oukele","admin");
        SecurityUtils.setSecurityManager(securityManager);//设置安全管理器
        Subject subject = SecurityUtils.getSubject();// 得到一个当前执行的主体
        subject.login(token);//验证 认证器中的用户
        //验证当前数据
        System.out.println(" 登陆状态：" + subject.isAuthenticated());
        //验证当前角色
        subject.checkRole("admin");
        System.out.println("===========退出登陆===========");
        subject.logout();//退出
        System.out.println(" 登陆状态：" + subject.isAuthenticated());

    }
}

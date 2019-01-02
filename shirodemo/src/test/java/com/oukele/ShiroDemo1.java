package com.oukele;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class ShiroDemo1 {
    @Test
    public void demo() {

        //加载资源文件
        IniRealm realm = new IniRealm("classpath:user.ini");
        //创建主体
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);//设置 规则
        //认证器
        UsernamePasswordToken token = new UsernamePasswordToken("oukele1", "oukele1","user");
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

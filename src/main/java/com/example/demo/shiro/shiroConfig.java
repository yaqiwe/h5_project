package com.example.demo.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.LinkedHashMap;
import java.util.Map;
/*
*@author yaqiwe
*@data 2019-05-21 09:15
*@notes shiro配置
**/
@Configuration
public class shiroConfig{
    @Bean
    public shiroRealm getRealm(){
        return new shiroRealm();
    }
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(getRealm());
        manager.setSessionManager(new SessionEntityDao());
        return manager;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/user/LoginUrl");
        bean.setUnauthorizedUrl("/user/UnauthorizedUrl");

        Map<String,String> map=new LinkedHashMap<>();
        map.put("/user/logIn","anon");//登录
        map.put("/user/WxLogIn","anon");//微信登录
        map.put("/user/createUser","anon");//注册用户
        map.put("/article/selectArticle","anon");//查询文章列表接口
        map.put("/article/getArticle","anon");//查询文章详细信息
        map.put("/image/**","anon");//图片资源
        map.put("/**","authc");
        bean.setFilterChainDefinitionMap(map);
        return bean;
    }
}

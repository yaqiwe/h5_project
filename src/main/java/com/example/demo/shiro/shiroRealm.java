package com.example.demo.shiro;

import com.example.demo.entity.user;
import com.example.demo.repostry.userRepostory;
import com.example.demo.repostry.userTypeRepostory;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
/*
*@author yaqiwe
*@data 2019-05-21 09:15
*@notes shiro验证
**/
public class shiroRealm extends AuthorizingRealm {
    @Autowired
    userTypeRepostory userType;
    @Autowired
    userRepostory users;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName= (String) token.getPrincipal();
        String passWork=new String((char[]) token.getCredentials()) ;
        if (userName.isEmpty() || passWork.isEmpty())
            return null;
        List<user> list=users.findByUserName(userName);
        if (list.size()>0){
            user us=list.get(0);
            SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(us.getUserName(),us.getPassWord(),getName());
            return info;
        }
        return null;
    }
}

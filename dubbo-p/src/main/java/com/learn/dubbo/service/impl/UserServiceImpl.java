package com.learn.dubbo.service.impl;


import com.learn.dubbo.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 通过@DubboService注解 服务发布
 *
 * @Author: cong zhi
 * @CreateDate: 2021/10/5 14:14
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/10/5 14:14
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@DubboService
public class UserServiceImpl implements UserService {

    @Override
    public String queryUser(String s) {
        System.out.println(s);
        System.out.println("=============provider===========" + s);
        return "OK" + s;
    }

    @Override
    public void doKill(String s) {
        System.out.println("=============provider===========" + s);
    }
}

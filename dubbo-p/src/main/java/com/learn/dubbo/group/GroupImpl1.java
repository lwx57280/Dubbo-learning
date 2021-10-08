package com.learn.dubbo.group;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService(group = "goupImpl1")
public class GroupImpl1 implements Group{
    @Override
    public String doSomething(String s) {
        return "GroupImpl1.doSomething"+s;
    }
}

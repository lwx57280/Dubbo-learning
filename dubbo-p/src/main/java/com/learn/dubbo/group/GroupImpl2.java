package com.learn.dubbo.group;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService(group = "groupImpl2")
public class GroupImpl2 implements Group {
    @Override
    public String doSomething(String s) {
        return "GroupImpl2.doSomething"+s;
    }

}

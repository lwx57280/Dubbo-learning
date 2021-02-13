package com.learn.dubbo;

public class DemoServiceImpl implements DemoService {
    @Override
    public String protocolDemo(String msg) {
        return "I'm Protocol demo:" + msg;
    }
}

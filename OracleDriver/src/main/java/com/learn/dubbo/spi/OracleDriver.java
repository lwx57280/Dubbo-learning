package com.learn.dubbo.spi;

public class OracleDriver implements DataBaseDriver {

    @Override
    public String connect(String s) {
        return "begin build Oracle Driver connection";
    }
}

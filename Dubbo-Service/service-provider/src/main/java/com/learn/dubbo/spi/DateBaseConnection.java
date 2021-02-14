package com.learn.dubbo.spi;

import java.util.ServiceLoader;

/**
 * SPI动态加载对应的驱动
 * @Author:         cong zhi
 * @CreateDate:     2021/2/13 19:04
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/13 19:04
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public class DateBaseConnection {

    public static void main(String[] args) {
        ServiceLoader<DataBaseDriver> serviceLoader = ServiceLoader.load(DataBaseDriver.class);
        for (DataBaseDriver dateBaseDriver : serviceLoader) {
            System.out.println(dateBaseDriver.connect("localhost"));
        }
    }

}

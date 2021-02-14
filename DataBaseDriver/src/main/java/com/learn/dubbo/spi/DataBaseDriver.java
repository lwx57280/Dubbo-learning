package com.learn.dubbo.spi;

/**
 * Dubbo SPI机制
 * @Author:         cong zhi
 * @CreateDate:     2021/2/13 17:48
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/13 17:48
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public interface DataBaseDriver {

    String connect(String host);
}

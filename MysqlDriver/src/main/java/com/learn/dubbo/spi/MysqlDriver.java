package com.learn.dubbo.spi;

/**
 * 创建 mysql连接工程（引入接口jar）：
 * @Author:         cong zhi
 * @CreateDate:     2021/2/13 17:56
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/13 17:56
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public class MysqlDriver implements DataBaseDriver {
    @Override
    public String connect(String s) {
        return "begin build Mysql Driver connection";
    }
}

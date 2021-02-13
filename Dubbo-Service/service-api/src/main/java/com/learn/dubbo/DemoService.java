package com.learn.dubbo;

/**
 * 接口2 : dubbo 可以针对不同的服务使用不同的协议
 * @Author:         cong zhi
 * @CreateDate:     2021/2/12 22:39
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/12 22:39
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public interface DemoService {

    String protocolDemo(String msg);
}

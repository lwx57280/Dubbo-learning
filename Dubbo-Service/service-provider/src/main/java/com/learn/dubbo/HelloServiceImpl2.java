package com.learn.dubbo;

/**
 * 服务实现
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/11 15:51
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/11 15:51
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class HelloServiceImpl2 implements HelloService {
    @Override
    public String sayHello(String msg) {
        return "Hello,I'm Service 2:" + msg;

    }
}

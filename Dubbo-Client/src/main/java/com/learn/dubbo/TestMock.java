package com.learn.dubbo;

/**
 * dubbo的降级方式： Mock
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/13 12:53
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/13 12:53
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class TestMock implements HelloService {
    @Override
    public String sayHello(String s) {
        return "系统繁忙！:" + s;
    }
}

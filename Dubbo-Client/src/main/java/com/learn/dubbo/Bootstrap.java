package com.learn.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 客户端调用Dubbo远端服务
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/11 16:31
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/11 16:31
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException, InterruptedException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-client.xml");
//        for (int i = 0; i < 10; i++) {
            // 获取远程服务代理
            HelloService helloService = (HelloService) context.getBean("helloService");

            System.out.println(helloService.sayHello("cong zhi"));
//            Thread.sleep(1000);
//        }

        System.in.read();
    }
}

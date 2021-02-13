package com.learn.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 编写Main方法，用spring容器来启动服务
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/11 16:01
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/11 16:01
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/dubbo-server.xml");
        context.start();
        // 阻塞当前进程
        System.in.read();
    }
}

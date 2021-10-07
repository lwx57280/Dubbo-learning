package com.learn.dubbo.api.config;

import com.learn.dubbo.service.UserService;
import com.learn.dubbo.service.impl.UserServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;

public class ProviderApi {

    public static void main(String[] args) throws IOException {
        // 服务实现
        UserService userService=new UserServiceImpl();

        // 当前应用设置
        ApplicationConfig application= new ApplicationConfig();
        application.setName("dubbo-provider");
        // 连接注册中心配置
        RegistryConfig registryConfig=new RegistryConfig();
        registryConfig.setAddress("zookeeper://192.168.110.105:2181");
        // 服务提供者协议配置
        ProtocolConfig protocol =new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(20880);
        protocol.setThreads(200);

        // 服务发布信息
        ServiceConfig<UserService> serviceConfig =new ServiceConfig<>();
        serviceConfig.setApplication(application);
        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocol);

        serviceConfig.setInterface(UserService.class);
        serviceConfig.setRef(userService);
        serviceConfig.setVersion("1.0.0");
        // 服务发布
        serviceConfig.export();
        System.in.read();
    }
}

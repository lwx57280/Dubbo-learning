package com.learn.dubbo.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
// 作用扫描 @DubboService注解 @DubboRef
@EnableDubbo(scanBasePackages = "com.learn.dubbo")
@PropertySource("classpath:/dubbo-provider.properties")
public class ProviderConfiguration {
}

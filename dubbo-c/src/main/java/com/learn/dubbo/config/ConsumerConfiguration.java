package com.learn.dubbo.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableDubbo(scanBasePackages = "com.leran.dubbo")
@PropertySource("classpath:/dubbo-consumer.properties")
public class ConsumerConfiguration {
}





## 主要内容

1. 为什么需要Dubbo
2. Dubbo的架构
3. Dubbo的使用
4. Dubbo注册中心原理
5. 如何快速启动Dubbo服务
6. 多协议支持
7. 多注册中心支持
8. 启动检查机制
9. 基于集群访问



##  Dubbo初步认识

dubbo中文网站:http://dubbo.apache.org/zh-cn/

dubbo英文网站:http://dubbo.apache.org/en-us/



在传统的远程调用，比如RMI、HTTP协议、WebService等确实能够满足远程调用关系，但是随着用户量的倍增以及系统的复杂性增加，传统的远程调用却满足不了服务治理的需求：

- 地址维护
- 负载均衡
- 限流/容错/降级
- 监控

所以在这一部分，我们引入Dubbo来实现服务治理，我们从三方面讲Dubbo这个RPC远程调用框架。



## 架构的发展

**传统互联网架构**
还记得阿里最初的项目是什么样的结构么？就是LAMP（即，Linux+Apache+MySQL+PHP），Tomcat作为web容器，放置的单体项目包括整套业务的所有逻辑，用户服务，订单服务，支付服务等~

![image-20210207210512578](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211142737.png)



## 分布式架构的演进

如今互联网比较完善的体系就是将业务分离，服务分离，数据库主从设计，读写分离。

![image-20210211142906421](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211142930.png)



### 带来哪些问题

(1)当服务越来越多时，服务 URL 配置管理变得非常困难，F5 硬件负载均衡器的单点压力也越来越大。

此时需要一个服务注册中心，动态的注册和发现服务，使服务的位置透明。

并通过在消费方获取服务提供方地址列表，实现软负载均衡和 Failover，降低对 F5 硬件负载均衡器的依赖，也能减少部分成本。

(2)当进一步发展，服务间依赖关系变得错踪复杂，甚至分不清哪个应用要在哪个应用之前启动，架构师都不能完整的描述应用的架构关系。

这时，需要自动画出应用间的依赖关系图，以帮助架构师理清理关系。

(3)服务的调用量越来越大，服务的容量问题就暴露出来，这个服务需要多少机器支撑？什么时候该加机器？

**为了解决这些问题，第一步，要将服务现在每天的调用量，响应时间，都统计出来，作为容量规划的参考指标。
其次，要可以动态调整权重，在线上，将某台机器的权重一直加大，并在加大的过程中记录响应时间的变化，直到响应时间到达阀值，记录此时的访问量，再以此访问量乘以机器数反推总容量。**

Dubbo的出现就是为了解决这些问题，Dubbo是一个服务治理技术，如何解释服务治理。

### 服务治理概念：

1. 负载
2. 容错
3. 降级

## Dubbo架构

![image-20210211144218367](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212135815.png)



老生常谈，随手掏来一个经典的dubbo架构图，图示上已经写的很清楚了，一共有0~5六个部分：

1. start

provider服务提供者：服务启动

1. register

provider服务提供者然后注册register服务

1. subscribe

Consumer服务消费者：消息订阅subscribe、

1. notify

注册中心会将这些服务通过notify到消费者

1. invoke

invoke这条实线按照图上的说明当然同步的意思了
服务消费者随机调用一个服务地址，失败重试另一个地址

1. count

这是一个监控，图中虚线表明Consumer 和Provider通过异步的方式发送消息至Monitor。Monitor在整个架构中是可选的，Monitor功能需要单独配置，不配置或者配置以后，Monitor挂掉并不会影响服务的调用。

## Dubbo 案例演示

![image-20210211154753593](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212135858.png)

接下来，我们简单的使用一下dubbo~

- 服务端

我这里使用server-api和server-provider作为服务端两个模块

![image-20210211180632413](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211180635.png)



- server-api：服务调用接口
- server-provider：服务提供者

在maven pom文件引入dubbo jar 包依赖

```xml
<!--加入服务接口定义-->
<dependency>
    <groupId>com.learn.dubbo</groupId>
    <artifactId>service-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.6.5</version>
</dependency>
```



在server-api中，就是空实现，用来调用server-provider：
接口1：

```java
package com.learn.dubbo;
/**
 * 接口定义
 * @Author:         cong zhi
 * @CreateDate:     2021/2/11 15:49
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/11 15:49
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public interface HelloService {

    String sayHello(String msg);
}
```



### 真正的实现就是在server-provider中实现：

```java
package com.learn.dubbo;

/**
 * 服务实现
 * @Author: cong zhi
 * @CreateDate: 2021/2/11 15:51
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/11 15:51
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        return "Hello," + msg;

    }
}

```

如何发布服务，需要将需要暴露的服务接口发布出去供客户端调用，需要在java同级目录新建一个resources目录，然后将resoureces目录标记成Test Resoureces Root，然后在esources目录下新建MATE-INF.spring目录，在该目录下添加配置文件dubbo-server.xml文件

###  dubbo的服务端 dubbo-server.xml 配置文件如下

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <!--提供方信息，用于计算依赖关系-->
    <dubbo:application name="dubbo-server" owner="mic"/>

    <!--注册中心 暴露服务地址-->
    <dubbo:registry address="N/A"/>

    <!--用dubbo协议在20880 端口暴露服务-->
    <dubbo:protocol port="20880" name="dubbo"/>

    <!--声明需要暴露的服务接口，指定协议为dubbo-->
    <dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService"/>
  

    <!--对应的服务实现服务-->
    <bean id="helloService" class="com.learn.dubbo.HelloServiceImpl"/>

</beans>
```



### 服务端调用就加载这个配置文件：

```java
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
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-server.xml");
        context.start();
        // 阻塞当前进程
        System.in.read();
    }
}
```

### 启动服务，运行结果如下：

![image-20210211161713642](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211161725.png)



## Dubbo 2.6.5启动报 java.lang.NoClassDefFoundError: io/netty/channel/EventLoopGroup

![image-20210212220934644](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212220945.png)

原因：缺少netty-all的jar包，在pom.xml中添加jar依赖即可，如下：

```xml
<!--学习dubbo 集成zookeeper时运行时报错，需要添加netty依赖-->
<dependency>
    <groupId>io.netty</groupId>
    <artifactId>netty-all</artifactId>
    <version>4.1.32.Final</version>
</dependency>
```



对外发布服务如下：

协议地址 :  ip:port    interfaceName

dubbo://192.168.137.1:20880/com.learn.dubbo.HelloService



## 客户端

### 在maven pom文件引入dubbo jar 包依赖

```xml
<dependency>
    <groupId>com.learn.dubbo</groupId>
    <artifactId>service-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.6.5</version>
</dependency>
```



### dubbo-client.xml 配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!--提供方信息-->
    <dubbo:application name="dubbo-client" owner="mic"/>

    <!--注册中心 暴露服务地址-->
    <dubbo:registry address="N/A"/>
    <!--用dubbo协议在20880 端口暴露服务-->
    <dubbo:protocol name="dubbo" port="20880"/>
    <!--调用dubbo远端服务，需要指定url地址就能发起远端调用-->
    <dubbo:reference id="helloService" interface="com.learn.dubbo.HelloService" url="dubbo://192.168.137.1:20880/com.learn.dubbo.HelloService"/>

</beans>
```



### 客户端dubbo调用远端服务

```java
/**
 * 客户端调用Dubbo远端服务
 * @Author:         cong zhi
 * @CreateDate:     2021/2/11 16:31
 * @UpdateUser:     cong zhi
 * @UpdateDate:     2021/2/11 16:31
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-client.xml");
        HelloService helloService = (HelloService) context.getBean("helloService");

        System.out.println(helloService.sayHello("cong zhi"));
    }
}

```



### 启动客户端，运行结果如下：

![image-20210211170103619](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211170109.png)

### 调用过程

​		dubbo底层基于netty完成远程通信 和TCP协议完成数据交互，然后从服务端拿到相应的服务配置信息，服务端声明需要暴露的服务接口和协议地址，客户端就可以通过指定的服务和协议地址调用远端服务，通过对应的服务找到发布的容器里面service服务地址得到一个Bean，就可以通过Bean反射去调用对应的Method去完成调用，最终返回数据，中间还有一个序列化和反序列过程。这是没有使用注册中心的，如果基于URL的方式还是点对点，也就是没有办法完成服务地址的管理和维护，因此需要用到中间件维护服务地址。



## Dubbo 支持的注册中心

### 引入zookeeper  jar 包

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
```



### dubbo-service.xml 配置文件改成zookeeper 注册中心地址

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <!--提供方信息，用于计算依赖关系-->
    <dubbo:application name="dubbo-server" owner="mic"/>

    <!--zookeeper 注册中心 暴露服务地址-->
    <dubbo:registry  address="zookeeper://192.168.1.101:2181"/>

    <!--用dubbo协议在20880 端口暴露服务-->
    <dubbo:protocol port="20880" name="dubbo"/>

    <!--声明需要暴露的服务接口，指定协议为dubbo，设置版本号1.1.1-->
    <dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService" />

    <!--对应的服务实现服务-->
    <bean id="helloService" class="com.learn.dubbo.HelloServiceImpl"/>

</beans>
```



**Dubbo 集成 zookeeper 踩坑  “java.lang.NoClassDefFoundError: org/apache/curator/RetryPolicy”**

### 版本变化

dubbo 2.6以前的版本，引入zkclient操作zookeeper
dubbo 2.6及以后的版本，引入curator操作zookeeper

![image-20210211174843632](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211174846.png)



客户端  dubbo-client.xml 配置文件改造

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">

    <!--提供方信息-->
    <dubbo:application name="dubbo-client" owner="mic"/>

    <!--zookeeper 注册中心 暴露服务地址-->
    <dubbo:registry  address="zookeeper://192.168.1.101:2181"/>

    <!--调用dubbo远端服务，需要指定url地址就能发起远端调用-->
    <dubbo:reference id="helloService" interface="com.learn.dubbo.HelloService"/>

</beans>
```



启动运行结果如下：

![image-20210211180019201](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210211180041.png)



## Dubbo 节点分析

使用dubbo通信成功，我们注意到，此时，dubbo在注册中心创建了“dubbo”的节点



![image-20210212102258524](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212104507.png)



**Zookeeper节点状态**

![image-20210212102013935](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212104501.png)



## 注册中心原理

![image-20210212230211351](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210213101120.png)



## 关于缓存

注意：在客户端其实是有缓存的概念的，这样不一定就让client每次都请求到zookeeper

```xml
<!--zookeeper 注册中心 暴露服务地址  开启本地缓存-->
<dubbo:registry  address="zookeeper://192.168.1.101:2181" file="D:/dubbo-server"/>
```



运行后，在本地找到缓存文件中缓存服务信息

![image-20210212103936042](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212104515.png)



## Dubbo支持的容器

Spring container (默认)

jetty container

Log4j container



## 服务启动过程

可以通过dubbo提供的main方法启动容器

```java
package com.learn.dubbo;

import com.alibaba.dubbo.container.Main;

/**
 * 通过dubbo提供main方法启动容器
 *
 * @Author: cong zhi
 * @CreateDate: 2021/2/12 10:48
 * @UpdateUser: cong zhi
 * @UpdateDate: 2021/2/12 10:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class DubboMain {

    public static void main(String[] args) {
        // 默认情况下会使用spring容器来启动服务
        Main.main(new String[]{"spring"});
    }
}
```



## 源码分析

```java
public static void main(String[] args) {
    try {
        if (args == null || args.length == 0) {
            String config = ConfigUtils.getProperty("dubbo.container", loader.getDefaultExtensionName());
            args = Constants.COMMA_SPLIT_PATTERN.split(config);
        }

        final List<Container> containers = new ArrayList();

        for(int i = 0; i < args.length; ++i) {
            containers.add(loader.getExtension(args[i]));
        }

        logger.info("Use container type(" + Arrays.toString(args) + ") to run dubbo serivce.");
        if ("true".equals(System.getProperty("dubbo.shutdown.hook"))) {
            Runtime.getRuntime().addShutdownHook(new Thread("dubbo-container-shutdown-hook") {
                public void run() {
                    Iterator var1 = containers.iterator();

                    while(var1.hasNext()) {
                        Container container = (Container)var1.next();

                        try {
                            container.stop();
                            Main.logger.info("Dubbo " + container.getClass().getSimpleName() + " stopped!");
                        } catch (Throwable var8) {
                            Main.logger.error(var8.getMessage(), var8);
                        }

                        try {
                            Main.LOCK.lock();
                            Main.STOP.signal();
                        } finally {
                            Main.LOCK.unlock();
                        }
                    }

                }
            });
        }

        Iterator var12 = containers.iterator();

        while(var12.hasNext()) {
            Container container = (Container)var12.next();
            // 默认使用Spring 容器
            container.start();
            logger.info("Dubbo " + container.getClass().getSimpleName() + " started!");
        }

        System.out.println((new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")).format(new Date()) + " Dubbo service server started!");
    } catch (RuntimeException var10) {
        var10.printStackTrace();
        logger.error(var10.getMessage(), var10);
        System.exit(1);
    }

    try {
        LOCK.lock();
        STOP.await();
    } catch (InterruptedException var8) {
        logger.warn("Dubbo service server stopped, interrupted by other thread!", var8);
    } finally {
        LOCK.unlock();
    }

}
```



## Dubbo支持多协议

- RMI
- hessian （hessian 使用http）
- webservice
- http
- thirft
- Dubbo（默认）Dubbo使用NIO

优势：
1、不需要修改原本的服务的情况下，方便协议的迁移
2、通过增加相应协议的jar包，快速发布



### 在maven pom 文件中引入 hessian 相关依赖

```xml
<!--hessian 相关依赖-->
<dependency>
    <groupId>com.caucho</groupId>
    <artifactId>hessian</artifactId>
    <version>4.0.38</version>
</dependency>
<!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.mortbay.jetty</groupId>
    <artifactId>jetty</artifactId>
    <version>6.1.25</version>
</dependency>
```



### dubbo-service.xml 配置文件改成 hessian协议

```xml
<!--用dubbo协议在20880 端口暴露服务-->
<dubbo:protocol port="20880" name="dubbo"/>
<!--用hessian协议在 8080 端口暴露服务-->
<dubbo:protocol port="8080" name="hessian"/>
<!--声明需要暴露的服务接口，改成 hessian协议，设置版本号1.1.1-->
<dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService" protocol="dubbo,hessian" />
```



### 服务端运行结果如下：

![image-20210212223422787](https://gitee.com/li_VillageHead/note-image/raw/master/img-folder/20210212223459.png)



### 客户端 dubbo-client.xml 配置文件改成

```xml
<!--调用dubbo远端服务，需要指定url地址就能发起远端调用,改成hessian协议-->
<dubbo:reference id="helloService" interface="com.learn.dubbo.HelloService" protocol="hessian"/>
```



## Dubbo 支持多注册中心

### dubbo-service.xml 配置文件改成 hessian协议

```xml
<!--注册中心 暴露服务地址-->
<dubbo:registry id="zk1"  address="zookeeper://192.168.1.101:2181"/>


<dubbo:registry id="zk2"  address="zookeeper://192.168.1.101:2181"/>

<!--用dubbo协议在20880 端口暴露服务-->
<dubbo:protocol port="20880" name="dubbo"/>
<!--用hessian协议在 8080 端口暴露服务-->
<dubbo:protocol port="8080" name="hessian"/>

<!--声明需要暴露的服务接口，Dubbo支持同一个服务多种协议，指定协议为dubbo，hessian -->
<dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService" registry="zk1" protocol="dubbo,hessian"/>

<!--声明需要暴露的服务接口，指定协议为dubbo，设置版本号1.1.2-->
<dubbo:service interface="com.learn.dubbo.DemoService" ref="demoService" registry="zk2" protocol="dubbo"/>
```



### Dubbo 解决服务循环依赖问题，在客户端dubbo-client.xml 配置文件中配置如下：

Dubbo缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，以便上线时，能及早发现问题，默认check=true。

如果你的Spring容器是懒加载的，或者通过API编程延迟引用服务，请关闭check，否则服务临时不可用时，会抛出异常，拿到null引用，如果check=false，总是会返回引用，当服务恢复时，能自动连上。



```xml
<!--调用dubbo远端服务，需要指定url地址就能发起远端调用, 可以通过check="false"关闭检查，比如，测试时，有些服务不关心，或者出现了循环依赖，必须有一方先启动。关闭某个服务的启动时检查：(没有提供者时报错)-->
<dubbo:reference id="helloService" interface="com.learn.dubbo.HelloService" check="false" protocol="hessian"/>
```



## Dubbo基于集群访问，对dubbo-server做负载均衡

dubbo-cluster1.xml 集群配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <!--提供方信息，用于计算依赖关系-->
    <dubbo:application name="dubbo-server" owner="mic"/>

    <!--注册中心 暴露服务地址-->
    <dubbo:registry id="zk2" address="zookeeper://192.168.1.101:2181"/>


    <!--用dubbo协议在20880 端口暴露服务-->
    <dubbo:protocol port="20880" name="dubbo"/>


    <!--声明需要暴露的服务接口，Dubbo支持同一个服务多种协议，指定协议为dubbo，hessian -->
    <dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService" registry="zk2" protocol="dubbo"/>


    <!--对应的服务实现服务-->
    <bean id="helloService" class="com.learn.dubbo.HelloServiceImpl"/>


</beans>
```



dubbo-cluster2.xml 集群配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://code.alibabatech.com/schema/dubbo http://code.alibabatech.com/schema/dubbo/dubbo.xsd">
    <!--提供方信息，用于计算依赖关系-->
    <dubbo:application name="dubbo-server" owner="mic"/>


    <dubbo:registry id="zk1" address="zookeeper://192.168.1.101:2181"/>

    <!--用dubbo协议在20880 端口暴露服务-->
    <dubbo:protocol port="20881" name="dubbo"/>


    <!--声明需要暴露的服务接口，Dubbo支持同一个服务多种协议，指定协议为dubbo，hessian -->
    <dubbo:service interface="com.learn.dubbo.HelloService" ref="helloService" registry="zk1" protocol="dubbo"/>

    <!--对应的服务实现服务-->
    <bean id="helloService" class="com.learn.dubbo.HelloServiceImpl2"/>


</beans>
```



```java
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
public class BootstrapCluster1 {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("META-INF/spring/dubbo-cluster1.xml");
        context.start();
        // 阻塞当前进程
        System.in.read();
    }
}
```
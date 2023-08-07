# spring-cloud-lightweight

## 介绍

**spring-cloud-lightweight** 是一个轻量级的服务治理框架，旨在简化分布式系统中的服务注册与发现、负载均衡等常用功能，使开发人员能够更加便捷地构建分布式微服务应用。

## 软件架构

该框架基于 Spring Cloud 技术栈，通过自定义的核心组件，实现了服务注册与发现、远程服务调用等功能。

## 安装教程

### 服务注册与发现

#### 部署服务端

**免安装**

测试使用，可以直接使用部署好的地址：http://8.131.60.15:5013 

**安装教程**

下载服务端Jar包，下载地址：[点击下载](https://gitee.com/huangjianguo2000/spring-cloud-lightweigh/releases/download/server-v1.0/server-bootstrap-0.0.1-SNAPSHOT.jar)

下载完成以后直接使用java -jar 命令运行。 若需修改端口，可以在统计目录下新增配置文件。

#### 客户端使用

导入pom依赖， 需要配置一个远程仓库地址。

```pom
    <dependencies>
        <dependency>
            <groupId>com.huang.lightweight</groupId>
            <artifactId>lightweight-client-spring-boot-starter</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
    
    <repositories>
        <repository>
            <id>handsomehuang-maven</id>
            <url>https://gitee.com/huangjianguo2000/maven-repository/raw/master</url>
        </repository>
    </repositories>
```

配置注册中心地址：

```java
spring:
  application:
    name: test-produce
  cloud:
    lightweight:
      discovery:
        server-address: http://localhost:5013
```

启动后在注册中心看见服务注册上去即代表服务注册成功。

### 远程服务调用

1. 引入依赖

```pom
	<dependencies>
          <dependency>
            <groupId>com.huang.lightweight</groupId>
            <artifactId>lightweight-liteconnect</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
    </dependencies>
    
    <repositories>
        <repository>
            <id>handsomehuang-maven</id>
            <url>https://gitee.com/huangjianguo2000/maven-repository/raw/master</url>
        </repository>
    </repositories>
```

2. 在启动类上开启**@EnableConnectClients**

```java

@SpringBootApplication
@EnableConnectClients
public class TestConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestConsumerApplication.class, args);
    }

}

```

3. 定义远端接口

```java

@ConnectClient(name = "test-produce", beanName = "helloClient")
public interface HelloClient {

    @GetMapping("/hello")
    String hello();

}

```

4. 使用

```java
@RestController
public class HelloController {

    @Autowired
    private HelloClient helloClient;

    @GetMapping("/hello")
    public String hello(){
        return helloClient.hello();
    }

}
```






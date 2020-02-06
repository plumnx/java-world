# ** Whole Stack For Spring Cloud **

## Nacos Register Server

### 1 Nacos Server

More Information in Nacos Register Sample

### 2 Using Nacos Register For Service Provider

#### 2.1 Service Provider
a. **pom.xml**
```
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

b. **bootstrap.yaml**
```
server:
  port: 8002

spring:
  application:
    name: nacos-discovery-server
  cloud:
    nacos:
      discovery:
        server-addr: 140.143.249.219:8848
        namespace: 1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16
        cluster-name: DEFAULT
```

c.Refer to the dependency Using Annotation
```
@EnableDiscoveryClient
```

d.rest api:  
http://127.0.0.1:8002/hello?name=test

### 3 Using Nacos Register For Service Consumer

#### 3.1 Service Consumer By LoadBalancerClient
a.Follow the steps from 2.1 service provider.    
  
**bootstrap.yaml**
```
server:
  port: 9000

spring:
  application:
    name: nacos-dicovery-client-common
  cloud:
    nacos:
      discovery:
        server-addr: 140.143.249.219:8848
        namespace: 1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16
        cluster-name: DEFAULT
```

b.Using LoadBalancerClient
```
@GetMapping("/test")
public String test() {
    ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-discovery-server");
    String url = serviceInstance.getUri() + "/hello?name=" + "plumnix";

    RestTemplate restTemplate = new RestTemplate();
    String result = restTemplate.getForObject(url, String.class);
    return "Invoke: " + url + ", return: " + result;
}
```

c.rest api:  
http://127.0.0.1:9000/test

#### 3.2 Service Consumer By Feign
a.Follow the steps from 2.1 service provider.    

**pom.xml**
```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```
  
**bootstrap.yaml**
```
server:
  port: 9000

spring:
  application:
    name: nacos-dicovery-client-common
  cloud:
    nacos:
      discovery:
        server-addr: 140.143.249.219:8848
        namespace: 1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16
        cluster-name: DEFAULT
```

Refer to the dependency Using Annotation
```
@EnableDiscoveryClient
@EnableFeignClients
```

b.Using Feign Client
```
@Slf4j
@RestController
static class TestController {

    private final Client client;

    @Autowired
    public TestController(Client client) {
        this.client = client;
    }

    @GetMapping("/test")
    public String test() {
        String result = client.hello("plumnix");
        return "return: " + result;
    }

}

@FeignClient("nacos-discovery-server")
interface Client {

    @GetMapping("/hello")
    String hello(@RequestParam("name") String name);

}
```

c.rest api:  
http://127.0.0.1:9001/test  

#### 3.3 Service Consumer By RestTemplate
a.Follow the steps from 2.1 service provider.    
  
**bootstrap.yaml**
```
server:
  port: 9002

spring:
  application:
    name: nacos-dicovery-client-resttemplate
  cloud:
    nacos:
      discovery:
        server-addr: 140.143.249.219:8848
        namespace: 1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16
        cluster-name: DEFAULT
```

b.Using RestTemplate And LoadBalanced
```
@Slf4j
@RestController
static class TestController {

    private final RestTemplate restTemplate;

    @Autowired
    public TestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/test")
    public String test() {
        String result = restTemplate.getForObject("http://nacos-discovery-server/hello?name=plumnix", String.class);
        return "return: " + result;
    }

}

@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

c.rest api:  
http://127.0.0.1:9002/test  

#### 3.4 Service Consumer By WebClient
a.Follow the steps from 2.1 service provider.    

**pom.xml**
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

**bootstrap.yaml**
```
server:
  port: 9003

spring:
  application:
    name: nacos-dicovery-client-webclient
  cloud:
    nacos:
      discovery:
        server-addr: 140.143.249.219:8848
        namespace: 1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16
        cluster-name: DEFAULT
```

b.Using WebClient
```
@RestController
static class TestController {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/test")
    public Mono<String> test() {
        return webClientBuilder.build().
                get().
                uri("http://nacos-discovery-server/hello?name=plumnix").
                retrieve().
                bodyToMono(String.class);
    }

}

@Bean
@LoadBalanced
public WebClient.Builder loadBalancedWebClientBuilder() {
    return WebClient.builder();
}
```

c.rest api:  
http://127.0.0.1:9003/test  

### 4 reference

Offcial Site  
https://nacos.io/zh-cn/  

### 5 Back to the Top

This is [Back to the Top](../readme.md) link.
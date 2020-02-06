# ** Whole Stack For Spring Cloud **

## Nacos Config Server

### 1 Nacos Server

#### 1.1 Running Nacos In Development
```
docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server
```
Then You can access the service by url below:  
http://140.143.249.219:8848/nacos/index.html

#### 1.2 Running Nacos In Production
Look like this [Cluster Installment](./install.md) link.

### 2 Using Nacos Client

#### 2.1 Nano Configuration For Config Center
```
Namespace: nacos-config-client (1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16)
Data ID: nacos-config-client.yaml
Group: DEFAULT_GROUP
Content [yaml]: 
test:
  title: Title of Nacos-Config-Client 
  aaa: aaa
  bbb: bbb
```

```
http://127.0.0.1:8001/test  
http://127.0.0.1:8001/test2
```

#### 2.2 Nano Configuration For Config Center (Target Profile)
```
Namespace: nacos (1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16)
Data ID: nacos-config-client-dev.yaml
Group: DEFAULT_GROUP
Content [yaml]: 
test:
  profileLabel: dev
```

```
Namespace: nacos (1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16)
Data ID: nacos-config-client-test.yaml
Group: DEFAULT_GROUP
Content [yaml]: 
test:
  profileLabel: test
```

Using Environment Variable:    
```
spring.profiles.active=dev
```

#### 2.3 Nano Configuration For Config Center (Multiply Configurations)
The ext-config override order is the later has the more prior.
```
Namespace: nacos (1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16)
Data ID: actuator.yaml
Group: DEFAULT_GROUP
Content [yaml]: 
management:
  endpoint:
    health:
      show-details: always
    info:
      enabled: true
    shutdown:
      enabled: true
  endpoints:
    web:
      exposure:
        include: shutdown,health,info,metrics
```

```
Namespace: nacos (1cdfe0a0-1bdb-4a3f-a22e-7f0cb845ab16)
Data ID: log.yaml
Group: DEFAULT_GROUP
Content [yaml]: 
logging:
  level: 
    root: info
```

Using it like this:
```
spring:
  cloud:
    nacos:
      config:
        ext-config:
          - data-id: actuator.yaml
            group: DEFAULT_GROUP
            refresh: true
          - data-id: log.yaml
            group: DEFAULT_GROUP
            refresh: true
```

#### 2.4 Nano Configuration For Config Center (Shard Refreshable Configuration)
Equals to the 3.1.5 Multiply configurations   
The shard-dataids and refreshable-dataids override order is the later has the more prior.  

Using it like this:
```
spring:
  cloud:
    nacos:
      config:
        shared-dataids: actuator.yaml,log.yaml
        refreshable-dataids: actuator.yaml,log.yaml
```

### 3 reference

Offcial Site  
https://nacos.io/zh-cn/  

### 4 Back to the Top

This is [Back to the Top](../readme.md) link.
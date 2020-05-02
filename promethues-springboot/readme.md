# **Using Prometheus And Spring Boot Actuator **

## Promethues

### 1.How to used it ?

#### 1.1 dependencies  
```  
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
    <version>1.3.3</version>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.3.3</version>
</dependency>

```
  
#### 1.2 yml configuration  
```
management:
  endpoint:
    metrics:
      enabled: true
    prometheus:
      enabled: true
  endpoints:
    web:
      exposure:
        include: "*"
  metrics:
    export:
      prometheus:
        enabled: true
```

#### 1.3 You can view the prometheus metrics by accessing url:
http://localhost:8080/actuator    
http://localhost:8080/actuator/prometheus  

#### 1.4 Run Prometheus By Docker Image  
```
$ docker pull prom/Prometheus
$ docker run -d \
  -p 9090:9090 \
  --name prometheus \
  -v /opt/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml \
  prom/Prometheus
```

And you can find the Prometheus.yml in resources\metrics：  
(Abd the job_name: prometheus is the Prometheus Server, and job_name: spring-actuator is your application )

```
# my global config
global:
  scrape_interval:     15s # Set the scrape interval to every 15 seconds. Default is every 1 minute.
  evaluation_interval: 15s # Evaluate rules every 15 seconds. The default is every 1 minute.
  # scrape_timeout is set to the global default (10s).

# Load rules once and periodically evaluate them according to the global 'evaluation_interval'.
rule_files:
  # - "first_rules.yml"
  # - "second_rules.yml"

# A scrape configuration containing exactly one endpoint to scrape:
# Here it's Prometheus itself.
scrape_configs:
  # The job name is added as a label `job=<job_name>` to any timeseries scraped from this config.
  - job_name: 'prometheus'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
    - targets: ['192.168.3.13:9090']

  - job_name: 'spring-actuator'
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
    static_configs:
    - targets: ['192.168.3.5:8080']
```

#### 1.4 Run Grafana By Docker Image  
docker run -d --name=grafana -p 3000:3000 grafana/grafana 

Using admin/admin login the grafana  
And configure promethues, and add the Prometheus Dashboard


### 2.0 reference
Prometheus  
https://prometheus.io/  
https://prometheus.io/docs/prometheus/latest/getting_started/    
https://github.com/prometheus/prometheus  

从零搭建Prometheus监控报警系统  
https://www.cnblogs.com/chenqionghe/p/10494868.html

springboot集成prometheus监控  
https://blog.csdn.net/u012129558/article/details/79500455

prometheus+grafana+springboot2监控集成配置  
https://blog.csdn.net/michaelgo/article/details/81709652

Prometheus for Spring Boot  
https://www.jianshu.com/p/f1c9f1868bd5
https://www.jianshu.com/p/aba9894b841c
https://www.jianshu.com/p/f1c9f1868bd5
https://www.jianshu.com/p/68f9e3b58bb1
https://www.jianshu.com/p/69006027ef2d

Spring Boot Actuator:健康检查、审计、统计和监控  
https://bigjar.github.io/2018/08/19/Spring-Boot-Actuator-%E5%81%A5%E5%BA%B7%E6%A3%80%E6%9F%A5%E3%80%81%E5%AE%A1%E8%AE%A1%E3%80%81%E7%BB%9F%E8%AE%A1%E5%92%8C%E7%9B%91%E6%8E%A7/
 
Spring Boot Metrics监控之Prometheus&Grafana  
https://www.jianshu.com/p/afc3759e75b9

在 Kubernetes 中使用 Prometheus 和 Spring Boot 监控您的应用  
https://www.ibm.com/developerworks/cn/cloud/library/monitoring-kubernetes-prometheus/index.html


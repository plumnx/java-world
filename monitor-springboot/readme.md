Skywalking && FileBeat 日志收集

# 1.nacos
sudo docker run --name nacos -d -p 8848:8848 \
    --env MODE=standalone \
    --env SPRING_DATASOURCE_PLATFORM=mysql \
    --env MYSQL_SERVICE_DB_NAME=nacos \
    --env MYSQL_SERVICE_HOST=172.21.16.10 \
    --env MYSQL_SERVICE_PORT=3306 \
    --env MYSQL_SERVICE_USER=root \
    --env MYSQL_SERVICE_PASSWORD=Ab27911564 \:q!
    --env NACOS_AUTH_ENABLE=true \
    nacos/nacos-server:1.2.0

docker run --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server


# 2.skywalking
docker run -d \
--name skywalking-oap \
-p 12800:12800 \
-p 11800:11800 \
-e TZ=Asia/Shanghai \
apache/skywalking-oap-server:6.5.0

docker run -d \
--name skywalking-ui \
-p 8080:8080 \
--link skywalking-oap:skywalking-oap \
-e SW_OAP_ADDRESS=skywalking-oap:12800 \
apache/skywalking-ui:6.5.0


# 3.applications
java -javaagent:C:\Temporary\skywalking-agent\agent\skywalking-agent.jar -Dskywalking.agent.service_name=GatewayServiceApplication -Dskywalking.collector.backend_service=152.136.46.93:11800 -jar C:\Temporary\gateway-service.jar
java -javaagent:C:\Temporary\skywalking-agent\agent\skywalking-agent.jar -Dskywalking.agent.service_name=ProducerServiceApplication -Dskywalking.collector.backend_service=152.136.46.93:11800 -jar C:\Temporary\producer-service.jar
java -javaagent:C:\Temporary\skywalking-agent\agent\skywalking-agent.jar -Dskywalking.agent.service_name=ConsumerServiceApplication -Dskywalking.collector.backend_service=152.136.46.93:11800 -jar C:\Temporary\consumer-service.jar

# dockerfile
# docker build -t producer-service:1.0 .
# docker run -d --name producer-service -p 9001:9001 producer-service:1.0
# docker run -d --name producer-service -v C:\Temporary\logs:/tmp/logs -p 9001:9001 producer-service:1.0

# docker build -t consumer-service:1.0 .
# docker run -d --name consumer-service -p 9002:9002 consumer-service:1.0
# docker run -d --name consumer-service -v C:\Temporary\logs:/tmp/logs -p 9002:9002 consumer-service:1.0


# 4.elasticsearch

# 4.1 network
# docker network create elk-network


# 4.2 elasticsearch
docker pull elasticsearch:7.6.0
docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.6.0
-- docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.6.0


# 4.3 kibana
docker pull kibana:7.6.0
docker run -d --name kibana --link elasticsearch:elasticsearch  -p 5601:5601 kibana:7.6.0
-- docker run -d --name kibana --net elk-network -p 5601:5601 kibana:7.6.0


# 4.4 logstash
docker pull logstash:7.6.0

mkdir /home/logstash
mkdir -p /home/logstash/conf.d
mkdir -p /home/logstash/log

vim /home/logstash/logstash.yml
path.config: /usr/share/logstash/conf.d/*.conf
path.logs: /var/log/logstash

vim /home/logstash/conf.d/test.conf
input {
    beats {
		port => 5044
		codec => "json"
	}
}

filter {
	grok {
		match => { "message" => "(?<date>.*)\|(?<thread>.*)\|(?<level>.*)\|(?<logname>.*)\|(?<keywords>.*)\|(?<traceid>.*)\|(?<content>.*)" }
	}
}

output {
  elasticsearch { hosts => ["172.21.16.10:9200"] }
  stdout { codec => rubydebug }
}

docker run -it -d -p 5044:5044 --name logstash --net elk-network \
-v /home/logstash/logstash.yml:/usr/share/logstash/config/logstash.yml \
-v /home/logstash/conf.d/:/usr/share/logstash/conf.d/ \
-v /home/logstash/log/:/var/log/logstash/ \
logstash:7.6.0


# 4.5 filebeat
mkdir -p /home/filebeat
mkdir -p /home/filebeat/log
docker pull elastic/filebeat:7.6.0

# 官方配置文档
cd /home/filebeat
#curl -L -O https://raw.githubusercontent.com/elastic/beats/7.1/deploy/docker/filebeat.docker.yml
#cp /home/filebeat/filebeat.docker.yml /home/filebeat/filebeat.yml

# vim /home/filebeat/filebeat.yml
filebeat.inputs:
- type: log
  paths:
    - /var/log/*

  #multiline.pattern: '^\['
  #multiline.negate: true
  #multiline.match: after

output.logstash:
  hosts: ["172.21.16.10:5044"]

docker run --name filebeat --net elk-network -d \
    -v /home/filebeat/log:/var/log/:ro \
    -v /home/filebeat/filebeat.yml:/usr/share/filebeat/filebeat.yml \
    elastic/filebeat:7.6.0

# log sample:
22:39:15.844|http-nio-9001-exec-1|INFO |c.p.cloud.api.ProducerController|will|TID:19.38.15837647556250001|ProducerController.nameApi name = will
22:39:15.845|http-nio-9001-exec-1|INFO |c.p.cloud.service.ProducerService|will|TID:19.38.15837647556250001|ProducerService.nameMethod name = will
22:39:16.599|http-nio-9001-exec-2|INFO |c.p.cloud.api.ProducerController|will|TID:19.39.15837647565910001|ProducerController.nameApi name = will
22:39:16.601|http-nio-9001-exec-2|INFO |c.p.cloud.service.ProducerService|will|TID:19.39.15837647565910001|ProducerService.nameMethod name = will
22:39:17.123|http-nio-9001-exec-3|INFO |c.p.cloud.api.ProducerController|will|TID:19.40.15837647571170001|ProducerController.nameApi name = will
22:39:17.125|http-nio-9001-exec-3|INFO |c.p.cloud.service.ProducerService|will|TID:19.40.15837647571170001|ProducerService.nameMethod name = will
22:39:17.514|http-nio-9001-exec-4|INFO |c.p.cloud.api.ProducerController|will|TID:19.41.15837647575080001|ProducerController.nameApi name = will
22:39:17.515|http-nio-9001-exec-4|INFO |c.p.cloud.service.ProducerService|will|TID:19.41.15837647575080001|ProducerService.nameMethod name = will
22:39:17.946|http-nio-9001-exec-5|INFO |c.p.cloud.api.ProducerController|will|TID:19.42.15837647579370001|ProducerController.nameApi name = will
22:39:17.950|http-nio-9001-exec-5|INFO |c.p.cloud.service.ProducerService|will|TID:19.42.15837647579370001|ProducerService.nameMethod name = will
22:39:18.362|http-nio-9001-exec-6|INFO |c.p.cloud.api.ProducerController|will|TID:19.43.15837647583570001|ProducerController.nameApi name = will
22:39:18.364|http-nio-9001-exec-6|INFO |c.p.cloud.service.ProducerService|will|TID:19.43.15837647583570001|ProducerService.nameMethod name = will
22:39:18.729|http-nio-9001-exec-7|INFO |c.p.cloud.api.ProducerController|will|TID:19.44.15837647587240001|ProducerController.nameApi name = will
22:39:18.732|http-nio-9001-exec-7|INFO |c.p.cloud.service.ProducerService|will|TID:19.44.15837647587240001|ProducerService.nameMethod name = will

(?<date>.*)\|(?<thread>.*)\|(?<level>.*)\|(?<logname>.*)\|(?<traceid>.*)\|(?<message>.*)

# clear memory
echo 1 > /proc/sys/vm/drop_caches

引用：
1.Docker安装部署ELK教程 (Elasticsearch+Kibana+Logstash+Filebeat)
https://www.cnblogs.com/fbtop/p/11005469.html

2.使用docker部署filebeat和logstash
https://www.cnblogs.com/time-read/p/10981731.html

3.logback日志文件位置动态指定
https://www.cnblogs.com/lori/p/11888911.html









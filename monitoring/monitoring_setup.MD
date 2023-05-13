####### DOWNLOAD JMX EXPORTER #######
* mkdir jmx-eporter
* wget https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/0.18.0/jmx_prometheus_javaagent-0.18.0.jar
* Download the exporter YAML config file: wget https://raw.githubusercontent.com/prometheus/jmx_exporter/main/example_configs/kafka-2_0_0.yml
* Stop Zookeeper and Kafka
* In the KAFKA SERVER START window: export KAFKA_OPTS=-javaagent:/Ankit/jmxexporter/jmx_prometheus_javaagent-0.18.0.jar=8080:/Ankit/jmxexporter/kafka-2_0_0.yml
* Launch Zookeeper
* Start Kafka
* Visit: 172.XX.XX.XX:8080

###### PROMETHEUS ######
* In prometheus directory, download prometheus:
* cd prometheus
* wget https://github.com/prometheus/prometheus/releases/download/v2.43.0/prometheus-2.43.0.linux-amd64.tar.gz
* tar -xvf prometheus-2.43.0.linux-amd64.tar.gz
  
* Edit the prometheus.yml file:
    In the target add the redis-exporter host and port:
    161.XX.XX.XXX:8080
    
    Keep Job name as "kafka"
    
* Start prometheus
  ./prometheus --web.listen-address=161.XX.XX.XXX:9001
* Visit: http://161.XX.XX.XXX:9001/graph



####### GRAFANA #############
* wget https://dl.grafana.com/enterprise/release/grafana-enterprise-9.4.7.linux-amd64.tar.gz
* tar -xvf grafana-enterprise-9.4.7.linux-amd64.tar.gz
* Edit the conf/defaults.ini file: Add the IP address
* Start Grafan: bin/grafana-server --config conf/defaults.ini
* Visit: http://161.XX.XX.XXX:3000/login
* Username: admin
* Password: admin
* Add data source as prometheus
* Dashboard ID: 721



########## ADD FEW MORE METRICS IN GRAFANA #############
1. Number of messages in all topics:
    * Copy any of the panel
    * Click on Edit panel
    * In the query, add: kafka_server_brokertopicmetrics_messagesin_total{topic!=""}
    * Edit the name of panel: No. of Messages in Topics

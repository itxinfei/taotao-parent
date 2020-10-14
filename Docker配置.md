### 安装Docker
`yum install -y docker`

### 1、安装redis
`docker pull redis:latest`

`docker run -itd --name mongo -p 27017:27017 mongo`

### 2、安装mongodb
`docker pull mongo:latest`

`docker run -itd --name mongo -p 27017:27017 mongo`

### 3、安装memcached
`docker pull memcached:1.5.16`

`docker run --name my-memcache -p 11211:11211 -d memcached:1.5.16`

### 4、安装Activemq
`docker pull  webcenter/activemq`

`docker run -d --name activemq -p 61616:61616 -p 8162:8161 webcenter/activemq`

### 5、安装FastDFS
`docker pull delron/fastdfs`

`docker run -d --network=host --name tracker -v /home/tracker:/var/fdfs delron/fastdfs tracker`

`docker run -d --network=host --name storage -e TRACKER_SERVER=192.168.8.6:22122 -v /home/storage:/var/fdfs -e GROUP_NAME=group1 delron/fastdfs storage`

### 6、安装Solr
`docker run --name mysolr -d -p 8983:8983 solr`

`docker  exec  -it  实例id  /bin/bash`

`cp -r server/solr/configsets/_default/conf /var/solr/data/collection1`

`cp -r server/solr/configsets/_default/conf /var/solr/data/collection2`

`cp -r server/solr/configsets/_default/conf /var/solr/data/collection3`

### 7、安装Zookeeper
`docker pull zookeeper:3.5`

`docker run -itd --name zk -p 2181:2181 zookeeper:3.5`




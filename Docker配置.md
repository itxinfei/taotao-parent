# Docker 部署配置指南

本文档介绍淘淘商城依赖的中间件如何通过 Docker 快速部署，并提供完整的 Docker Compose 编排方案。

## 安装 Docker

```bash
# CentOS/RHEL
yum install -y docker
systemctl start docker
systemctl enable docker

# Ubuntu/Debian
apt-get install -y docker.io
systemctl start docker
systemctl enable docker
```

## 一、核心中间件部署（项目必需）

淘淘商城运行依赖以下中间件：**MySQL、Redis、ZooKeeper、Solr、ActiveMQ、FastDFS**。

### 1. MySQL（数据库）

```bash
docker pull mysql:5.7

docker run -itd --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=taotao \
  --restart=always \
  mysql:5.7

# 验证
docker exec -it mysql mysql -uroot -proot -e "show databases;"
```

### 2. Redis（缓存）

```bash
docker pull redis:latest

docker run -itd --name redis \
  -p 6379:6379 \
  --restart=always \
  redis

# 验证
docker exec -it redis redis-cli ping   # 返回 PONG
```

### 3. ZooKeeper（Dubbo 注册中心）

```bash
docker pull zookeeper:3.5

docker run -itd --name zk \
  -p 2181:2181 \
  --restart=always \
  zookeeper:3.5

# 验证
docker exec -it zk zkCli.sh -server localhost:2181 ls /
```

### 4. Solr（搜索服务）

```bash
docker pull solr:4.10

docker run --name mysolr -d -p 8983:8983 solr:4.10

# 创建 collection1（需进入容器复制配置）
docker exec -it mysolr /bin/bash

# 容器内执行：
cp -r server/solr/configsets/_default/conf /opt/solr/server/solr/collection1
exit

# 重启容器使 collection 生效
docker restart mysolr

# 验证
curl "http://localhost:8983/solr/collection1/select?q=*:*"
```

> 说明：项目默认连接 `http://<host>:8983/solr/collection1`，请确保 collection 名称与
> `taotao-search-service` 中 `applicationContext-solr.xml` 配置一致。

### 5. ActiveMQ（消息队列）

```bash
docker pull webcenter/activemq:5.14.3

docker run -d --name activemq \
  -p 61616:61616 \        # OpenWire 协议端口（项目使用）
  -p 8161:8161 \          # 管理台端口
  webcenter/activemq:5.14.3

# 验证
# 浏览器访问 http://localhost:8161 （默认账号 admin/admin）
```

> 说明：项目通过 `tcp://<host>:61616` 连接 ActiveMQ，`applicationContext-activemq.xml`
> 中配置的 brokerURL 需与此端口一致。

### 6. FastDFS（文件存储）

```bash
docker pull delron/fastdfs

# 启动 tracker
docker run -d --network=host --name tracker \
  -v /home/tracker:/var/fdfs \
  delron/fastdfs tracker

# 启动 storage（将 TRACKER_SERVER 改为本机 IP）
docker run -d --network=host --name storage \
  -e TRACKER_SERVER=<本机IP>:22122 \
  -v /home/storage:/var/fdfs \
  -e GROUP_NAME=group1 \
  delron/fastdfs storage

# 验证
docker exec -it tracker fdfs_monitor /etc/fdfs/client.conf
```

> 说明：项目通过 `tracker_server=<host>:22122`（`taotao-manager-web/src/main/resources/resource/client.conf`）
> 连接 FastDFS。

## 二、可选中间件（教程相关，非项目必需）

以下中间件在部分教程流程中使用，淘淘商城核心功能不依赖，按需部署。

### 7. MongoDB

```bash
docker pull mongo:latest

docker run -itd --name mongo \
  -p 27017:27017 \
  --restart=always \
  mongo
```

### 8. Memcached

```bash
docker pull memcached:1.5.16

docker run --name my-memcache \
  -p 11211:11211 \
  -d memcached:1.5.16
```

## 三、Docker Compose 一键编排（推荐）

在项目根目录创建 `docker-compose.yml`，一键启动全部核心中间件：

```yaml
version: "3"
services:
  mysql:
    image: mysql:5.7
    container_name: taotao-mysql
    ports:
      - "3306:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: taotao
    volumes:
      - ./data/mysql:/var/lib/mysql
      - ./Doc/taotao.sql:/docker-entrypoint-initdb.d/taotao.sql  # 自动初始化数据库
    restart: always

  redis:
    image: redis:latest
    container_name: taotao-redis
    ports:
      - "6379:6379"
    restart: always

  zookeeper:
    image: zookeeper:3.5
    container_name: taotao-zk
    ports:
      - "2181:2181"
    restart: always

  solr:
    image: solr:4.10
    container_name: taotao-solr
    ports:
      - "8983:8983"
    restart: always

  activemq:
    image: webcenter/activemq:5.14.3
    container_name: taotao-activemq
    ports:
      - "61616:61616"
      - "8161:8161"
    restart: always
```

启动：

```bash
docker-compose up -d
docker-compose ps          # 查看状态
docker-compose logs -f     # 查看日志
```

> 启动后需按上文第 4 节手动创建 Solr 的 `collection1`，并导入数据库初始化脚本
> （若未使用 initdb 自动导入，则执行 `mysql -u root -p taotao < Doc/taotao.sql`）。

## 四、部署后检查清单

| 中间件 | 检查命令 | 预期结果 |
|--------|----------|----------|
| MySQL | `docker exec -it mysql mysql -uroot -proot -e "show databases;"` | 包含 `taotao` 库 |
| Redis | `docker exec -it redis redis-cli ping` | `PONG` |
| ZooKeeper | `docker exec -it zk zkCli.sh -server localhost:2181 ls /` | 输出根节点列表 |
| Solr | `curl "http://localhost:8983/solr/collection1/select?q=*:*"` | 返回 JSON 结果 |
| ActiveMQ | 浏览器访问 `http://localhost:8161` | 显示管理台登录页 |
| FastDFS | `docker exec -it tracker fdfs_monitor /etc/fdfs/client.conf` | `storage server count > 0` |

## 五、注意事项

1. **host 网络模式**：FastDFS 的 tracker/storage 使用 `--network=host`，仅适用于 Linux 环境
2. **数据持久化**：生产环境务必挂载数据卷（`-v`），避免容器删除后数据丢失
3. **端口冲突**：部署前检查 3306/6379/2181/8983/61616 等端口未被占用
4. **时区问题**：容器默认 UTC 时区，如需北京时间可添加 `-e TZ=Asia/Shanghai`
5. **启动顺序**：ZooKeeper 需在 service 模块启动前就绪，否则 Dubbo 无法注册

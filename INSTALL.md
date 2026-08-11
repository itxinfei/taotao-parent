# 淘淘商城安装与运维指南

本文档涵盖淘淘商城的完整安装、部署与日常运维操作。

## 环境要求

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 1.7+ | 推荐 JDK 1.8 |
| Maven | 3.2+ | 构建工具 |
| MySQL | 5.7+ | 数据库 |
| Redis | 3.0+ | 缓存服务（可选） |
| ZooKeeper | 3.4+ | Dubbo注册中心（可选） |
| Solr | 4.10+ | 搜索服务（可选） |
| ActiveMQ | 5.11+ | 消息队列（可选） |
| FastDFS | 5.0+ | 文件存储（可选） |
| Tomcat | 7.0+ | Web服务器 |

> 外部中间件（Redis/ZooKeeper/Solr/ActiveMQ/FastDFS）可通过 Docker 快速部署，见 [Docker配置.md](Docker配置.md)。

## 安装步骤

### 1. 克隆项目

```bash
git clone https://gitee.com/itxinfei/taotao-parent.git
cd taotao-parent
```

### 2. 安装依赖软件

#### 2.1 JDK安装

- **Windows**: 下载 JDK 1.8 安装包并安装
- **Linux**:
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-8-jdk

# CentOS/RHEL
sudo yum install java-1.8.0-openjdk
```

#### 2.2 Maven安装

```bash
# 下载Maven 3.6+
wget https://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz

# 解压
tar -zxvf apache-maven-3.6.3-bin.tar.gz -C /opt/

# 配置环境变量
export MAVEN_HOME=/opt/apache-maven-3.6.3
export PATH=$MAVEN_HOME/bin:$PATH
```

#### 2.3 MySQL安装

```bash
# Ubuntu/Debian
sudo apt-get install mysql-server-5.7

# CentOS/RHEL
sudo yum install mysql-community-server

# 启动服务
sudo systemctl start mysql
sudo systemctl enable mysql

# 设置密码
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'your_password';
```

### 3. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE taotao CHARACTER SET utf8 COLLATE utf8_general_ci;"

# 导入SQL脚本
mysql -u root -p taotao < docs/taotao.sql
```

### 4. 配置数据源

编辑以下文件中的数据库连接信息：

**taotao-manager-service**: `src/main/resources/properties/db.properties`
**taotao-content-service**: `src/main/resources/properties/db.properties`
**taotao-search-service**: `src/main/resources/properties/db.properties`
**taotao-sso-service**: `src/main/resources/properties/db.properties`
**taotao-order-service**: `src/main/resources/properties/db.properties`

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/taotao?characterEncoding=utf-8
jdbc.username=root
jdbc.password=your_password
```

### 5. 配置外部服务（可选）

#### 5.1 Redis配置

修改各 service 模块下的 `spring/applicationContext-jedis.xml`:

```xml
<!-- 将 172.168.20.221 修改为你的Redis地址 -->
<bean id="jedisPool" class="redis.clients.jedis.JedisPool">
    <constructor-arg name="host" value="localhost"/>
    <constructor-arg name="port" value="6379"/>
</bean>
```

#### 5.2 ZooKeeper配置

修改各模块下的 spring 配置文件中的 Dubbo 注册中心地址：

```xml
<!-- 将 172.168.20.221 修改为你的ZooKeeper地址 -->
<dubbo:registry protocol="zookeeper" address="zookeeper://localhost:2181"/>
```

#### 5.3 Solr配置

修改 `taotao-search-service/src/main/resources/spring/applicationContext-solr.xml`:

```xml
<!-- 将地址修改为你的Solr地址 -->
<bean id="httpSolrServer" class="org.apache.solr.client.solrj.impl.HttpSolrServer">
    <constructor-arg name="baseURL" value="http://localhost:8983/solr/collection1"/>
</bean>
```

#### 5.4 ActiveMQ配置

修改 `taotao-manager-service` 与 `taotao-search-service` 下的 `spring/applicationContext-activemq.xml`:

```xml
<!-- 将地址修改为你的ActiveMQ地址 -->
<bean id="targetConnectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
    <property name="brokerURL" value="tcp://localhost:61616"/>
</bean>
```

#### 5.5 FastDFS配置

修改 `taotao-manager-web/src/main/resources/resource/client.conf` 中的 tracker 地址：

```properties
tracker_server=localhost:22122
```

#### 5.6 邮件告警配置（可选）

搜索系统在发生异常时会发送告警邮件，通过系统属性或环境变量配置（避免硬编码凭据）：

```bash
# 方式一：系统属性（JVM启动参数）
-Dmail.from=your_account@163.com -Dmail.authCode=your_auth_code -Dmail.to=admin@example.com

# 方式二：环境变量
export MAIL_FROM=your_account@163.com
export MAIL_AUTH_CODE=your_auth_code
export MAIL_TO=admin@example.com
```

未配置时，邮件功能自动跳过并记录警告日志，不影响系统运行。

### 6. Maven编译

```bash
# 进入项目目录
cd taotao-parent

# 先安装父POM到本地仓库
mvn install -N

# 编译所有模块（跳过测试）
mvn clean install -DskipTests

# 仅编译验证（不打包、速度快）
mvn compile test-compile
```

## 运行方式

### 方式一：使用Maven Tomcat插件（推荐）

```bash
# 打开多个终端窗口，按顺序启动

# 终端1：启动后台管理服务（Dubbo 20880）
cd taotao-manager/taotao-manager-service
mvn tomcat7:run

# 终端2：启动内容服务（Dubbo 20881）
cd taotao-content/taotao-content-service
mvn tomcat7:run

# 终端3：启动搜索服务（Dubbo 20882）
cd taotao-search/taotao-search-service
mvn tomcat7:run

# 终端4：启动SSO服务（Dubbo 20883）
cd taotao-sso/taotao-sso-service
mvn tomcat7:run

# 终端5：启动订单服务（Dubbo 20884）
cd taotao-order/taotao-order-service
mvn tomcat7:run

# 终端6：后台管理Web（8081）
cd taotao-manager-web
mvn tomcat7:run

# 终端7：门户网站（8082）
cd taotao-portal-web
mvn tomcat7:run
```

### 方式二：部署到独立Tomcat

```bash
# 1. 将war包复制到Tomcat webapps目录
cp taotao-manager-web/target/taotao-manager-web.war /path/to/tomcat/webapps/
cp taotao-portal-web/target/taotao-portal-web.war /path/to/tomcat/webapps/

# 2. 启动Tomcat
cd /path/to/tomcat/bin
./startup.sh  # Linux
startup.bat   # Windows
```

## 端口说明

> `service` 模块通过 Dubbo 协议对外提供 RPC 服务，`web` 模块通过 HTTP 对外提供服务。

### Dubbo 端口（RPC）

| 模块 | Dubbo端口 | 配置位置 |
|------|-----------|----------|
| taotao-manager-service | 20880 | applicationContext-service.xml |
| taotao-content-service | 20881 | applicationContext-service.xml |
| taotao-search-service | 20882 | applicationContext-service.xml |
| taotao-sso-service | 20883 | applicationContext-service.xml |
| taotao-order-service | 20884 | applicationContext-service.xml |

### HTTP 端口（Tomcat）

| 模块 | 默认端口 | 配置位置 |
|------|----------|----------|
| taotao-manager-web | 8081 | pom.xml |
| taotao-portal-web | 8082 | pom.xml |
| taotao-search-web | 8085 | pom.xml |
| taotao-item-web | 8086 | pom.xml |
| taotao-sso-web | 8088 | pom.xml |
| taotao-cart-web | 8089 | pom.xml |
| taotao-order-web | 8091 | pom.xml |

### 中间件端口

| 服务 | 默认端口 |
|------|----------|
| MySQL | 3306 |
| Redis | 6379 |
| ZooKeeper | 2181 |
| Solr | 8983 |
| ActiveMQ | 61616（管理台 8161） |
| FastDFS tracker | 22122 |

## 访问地址

| 模块 | 地址 |
|------|------|
| 后台管理 | http://localhost:8081 |
| 门户网站 | http://localhost:8082 |
| 搜索页面 | http://localhost:8085 |
| 商品详情 | http://localhost:8086 |
| SSO登录 | http://localhost:8088 |
| 购物车 | http://localhost:8089 |
| 订单中心 | http://localhost:8091 |

## 运维指南

### 日志

各模块日志由 log4j 输出到 `${catalina.home}/logs/`：

| 日志文件 | 级别 | 内容 |
|----------|------|------|
| `taotao-xxx.log` | INFO | 常规业务日志（按天滚动） |
| `taotao-xxx-error.log` | ERROR | 错误日志（独立文件，便于排查） |

```bash
# 实时查看日志
tail -f ${catalina.home}/logs/taotao-manager-web.log

# 查看错误日志
grep ERROR ${catalina.home}/logs/taotao-manager-web-error.log
```

### 服务健康检查

```bash
# 1. 检查各端口是否监听
netstat -tlnp | grep -E "8081|20880|2181|6379|8983|61616"

# 2. 检查 Dubbo 服务注册（ZooKeeper）
zkCli.sh -server localhost:2181
ls /dubbo/com.taotao.service.ItemService/providers

# 3. 检查 Solr 索引
curl "http://localhost:8983/solr/collection1/select?q=*:*&rows=1"

# 4. 检查 Redis 缓存
redis-cli -h localhost ping
redis-cli -h localhost keys "ITEM_INFO*"
```

### 数据备份

```bash
# MySQL 全量备份
mysqldump -u root -p taotao > taotao_backup_$(date +%Y%m%d).sql

# Redis 持久化文件（rdb/aof）定期备份
cp /var/lib/redis/dump.rdb /backup/redis/
```

### 常见性能优化

- MySQL：开启慢查询日志，为 `tb_item`、`tb_order` 等大表建立索引
- Redis：合理设置缓存过期时间（商品缓存 `ITEM_EXPIRE` 默认 86400 秒）
- Solr：定期执行 optimize 合并索引段
- JVM：生产环境建议 `-Xms1g -Xmx1g -XX:+UseConcMarkSweepGC`

## 常见问题排查

### 问题1：Maven依赖下载失败

**现象**：编译时提示找不到依赖包

**解决方案**：
```bash
# 更新Maven索引
mvn clean install -U -DskipTests

# 检查Maven镜像配置
cat ~/.m2/settings.xml

# 推荐使用阿里云镜像
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>central</mirrorOf>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

### 问题2：端口被占用

**现象**：启动时提示端口已被占用

**解决方案**：
```bash
# 查找占用进程
netstat -ano | findstr :8081  # Windows
netstat -tlnp | grep 8081      # Linux

# 杀死进程
taskkill /F /PID <pid>  # Windows
kill -9 <pid>           # Linux
```

### 问题3：数据库连接失败

**现象**：启动时提示无法连接数据库

**检查项**：
1. MySQL服务是否启动
2. 数据库用户名密码是否正确
3. 数据库端口是否正确（默认3306）
4. 防火墙是否允许连接

```bash
# 测试数据库连接
mysql -h localhost -u root -p -P 3306 taotao
```

### 问题4：Dubbo服务无法注册

**现象**：服务启动后无法注册到ZooKeeper

**检查项**：
1. ZooKeeper服务是否启动
2. ZooKeeper地址配置是否正确
3. 网络是否可达
4. 各 service 模块 Dubbo 端口（20880-20884）是否冲突

```bash
# 测试ZooKeeper连接
telnet localhost 2181
# 或
echo stat | nc localhost 2181
```

### 问题5：日志文件无法写入

**现象**：启动时报错无法写入日志文件

**解决方案**：
```bash
# 创建日志目录
mkdir -p /usr/local/tomcat/logs
chown -R tomcat:tomcat /usr/local/tomcat/logs
```

### 问题6：web 模块提示 No provider available

**现象**：后台管理或门户无法调用服务

**解决方案**：
1. 确认 5 个 service 模块已启动（Dubbo 20880-20884）
2. 确认 web 模块 `dubbo:reference` 的接口与 service 发布的接口一致
3. 查看 web 模块错误日志确认具体报错

## Docker部署（可选）

各中间件的Docker部署方式请参考 [Docker配置.md](Docker配置.md)。

## 目录结构说明

```
taotao-parent
├── docs/                        # 文档、SQL脚本与图片
│   ├── taotao.sql               # 数据库初始化脚本
│   └── ...
├── taotao-common/               # 通用工具类
├── taotao-manager/              # 后台管理服务
│   ├── taotao-manager-dao       # DAO层
│   ├── taotao-manager-pojo      # POJO类
│   ├── taotao-manager-interface # Dubbo接口
│   └── taotao-manager-service   # 服务实现
├── taotao-manager-web/          # 后台管理前端
├── taotao-portal-web/           # 门户网站
├── taotao-content/              # 内容服务
├── taotao-search/               # 搜索服务
├── taotao-search-web/           # 搜索前端
├── taotao-item-web/             # 商品详情页
├── taotao-sso/                  # 单点登录服务
├── taotao-sso-web/              # SSO前端
├── taotao-cart-web/             # 购物车
├── taotao-order/                # 订单服务
└── taotao-order-web/            # 订单前端
```

## 开发建议

1. **开发环境**：推荐使用 IntelliJ IDEA 或 Eclipse
2. **代码规范**：遵循阿里巴巴Java开发手册
3. **调试技巧**：使用远程调试连接Tomcat
4. **日志查看**：日志文件位于 `${catalina.home}/logs/`

## 技术支持

如遇问题，可通过以下方式获取帮助：
- QQ群：661543188
- 邮箱：747011882@qq.com
- 提交Issue：在项目仓库提交Issue

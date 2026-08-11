<p align="center">
  <img src="docs/logo.png" alt="淘淘商城" width="180"/>
</p>

<h1 align="center">🛒 淘淘商城</h1>

<p align="center">
  <b>基于 Dubbo + Spring + MyBatis 的分布式 B2C 电商平台</b>
</p>

<p align="center">
  <a href="https://gitee.com/itxinfei/taotao-parent">
    <img src="https://img.shields.io/badge/Gitee-心飞为你飞-1890ff?style=flat-square&logo=gitee" alt="Gitee">
  </a>
  <a href="https://qm.qq.com/cgi-bin/qm/qr?k=9yLlyD1dRBL97xmBKw43zRt0-6xg8ohb&jump_from=webapi">
    <img src="https://img.shields.io/badge/QQ群-661543188-orange?style=flat-square&logo=tencentqq" alt="QQ群">
  </a>
  <a href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=f0hLSE9OTkdHTT8ODlEcEBI">
    <img src="https://img.shields.io/badge/邮箱-747011882@qq.com-blue?style=flat-square&logo=mail.ru" alt="邮箱">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img src="https://img.shields.io/badge/License-Apache%202.0-4CAF50?style=flat-square" alt="License">
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/JDK-1.7%2B-brightgreen?style=flat-square" alt="JDK">
  <img src="https://img.shields.io/badge/Maven-3.2%2B-yellowgreen?style=flat-square" alt="Maven">
  <img src="https://img.shields.io/badge/Spring-4.2.4-6DB33F?style=flat-square&logo=spring" alt="Spring">
  <img src="https://img.shields.io/badge/Dubbo-2.5.3-red?style=flat-square" alt="Dubbo">
  <img src="https://img.shields.io/badge/MyBatis-3.2.8-orange?style=flat-square" alt="MyBatis">
  <img src="https://img.shields.io/badge/Redis-3.0%2B-DC382D?style=flat-square&logo=redis" alt="Redis">
  <img src="https://img.shields.io/badge/Solr-4.10.3-blue?style=flat-square" alt="Solr">
  <img src="https://img.shields.io/badge/ActiveMQ-5.11.2-00BFFF?style=flat-square" alt="ActiveMQ">
</p>

---

> ✨ 淘淘商城是一套完整的 **分布式电商解决方案**，覆盖商品、搜索、购物车、订单、单点登录、内容管理
> 六大核心业务域，采用 **服务化拆分 + 注册中心 + 消息异步 + 缓存加速** 的经典分布式架构。
>
> 既适合作为 **Java 分布式开发的学习案例**，也可作为中小型电商项目的基础框架。

---

## 📚 培训教学文档

从零开始系统学习本项目：

| 编号 | 文档 | 内容 |
|------|------|------|
| 00 | [学习路线与文档导航](docs/README.md) | 六阶段学习路线、知识地图 |
| 01 | [项目介绍与系统架构](docs/01-项目介绍与系统架构.md) | 架构分层、模块划分、技术选型 |
| 02 | [环境搭建与项目部署](docs/02-环境搭建与项目部署.md) | 中间件安装、编译启动、联调验证 |
| 03 | [核心模块业务讲解](docs/03-核心模块业务讲解.md) | 六大核心业务完整链路 |
| 04 | [关键技术点原理与实践](docs/04-关键技术点原理与实践.md) | Dubbo/Redis/Solr/MQ 原理实战 |
| 05 | [二次开发指南与常见问题](docs/05-二次开发指南与常见问题.md) | 新增功能套路、FAQ 排错 |
| 06 | [数据库设计详解](docs/06-数据库设计详解.md) | 11 张核心表与设计思想 |
| 07 | [面试题集锦](docs/07-面试题集锦.md) | 五大类高频面试题与答题技巧 |
| 08 | [分布式事务与一致性专题](docs/08-分布式事务与一致性专题.md) | 分布式事务方案与最终一致 |
| 09 | [电商高级功能实战](docs/09-电商高级功能实战.md) | 秒杀、优惠券等进阶实战 |
| 10 | [项目答辩与简历指导](docs/10-项目答辩与简历指导.md) | 简历写法、答辩话术 |
| 11 | [项目目录结构与模块说明](docs/11-项目目录结构与模块说明.md) | 全目录地图与配置速查 |

---

## 🚀 功能特性

| 业务域 | 核心功能 |
|--------|----------|
| 🛍️ 后台管理 | 商品管理、类目管理、内容发布、规格参数、订单处理 |
| 🏠 门户网站 | 首页展示、商品检索、商品详情、购物车、下单结算 |
| 🔐 单点登录 | 跨系统登录态管理（Token + Redis） |
| 🔍 搜索服务 | 基于 Solr 的商品全文检索（高亮、分页） |
| 📢 内容管理 | 广告位、分类内容的动态发布 |
| ⚡ 异步解耦 | ActiveMQ 消息驱动索引同步与页面静态化 |

---

## 🏗️ 系统架构

<p align="center">
  <img src="docs/系统架构.png" alt="系统架构" width="90%"/>
  <br/>
  <em>系统架构图</em>
</p>

### 服务调用关系

```
浏览器
  │
  ├── taotao-manager-web  (后台管理，8081)
  ├── taotao-portal-web   (门户网站，8082)
  ├── taotao-search-web   (搜索前端，8085)
  ├── taotao-item-web     (商品详情，8086)
  ├── taotao-sso-web      (SSO前端，8088)
  ├── taotao-cart-web     (购物车，8089)
  └── taotao-order-web    (订单前端，8091)
          │  Dubbo RPC（经 ZooKeeper 注册中心）
          ▼
  ├── taotao-manager-service  (20880)  商品/类目/规格
  ├── taotao-content-service  (20881)  内容管理
  ├── taotao-search-service   (20882)  搜索服务
  ├── taotao-sso-service      (20883)  单点登录
  └── taotao-order-service    (20884)  订单服务
          │
          ▼
  MySQL ─ Redis ─ Solr ─ ActiveMQ ─ FastDFS
```

### 外部依赖

| 中间件 | 用途 |
|--------|------|
| 🗄️ ZooKeeper | Dubbo 服务注册与发现 |
| ⚡ Redis | 缓存、Session 共享、订单号生成 |
| 🔍 Solr | 商品全文检索索引库 |
| ✉️ ActiveMQ | 商品变更消息通知（索引同步、页面静态化） |
| 🖼️ FastDFS | 商品图片分布式存储 |
| 💾 MySQL | 业务数据持久化 |

---

## 🧰 技术栈

<p align="center">
  <img src="https://img.shields.io/badge/Spring-4.2.4-6DB33F?style=for-the-badge&logo=spring" alt="Spring">
  <img src="https://img.shields.io/badge/Dubbo-2.5.3-red?style=for-the-badge&logo=apache" alt="Dubbo">
  <img src="https://img.shields.io/badge/MyBatis-3.2.8-orange?style=for-the-badge" alt="MyBatis">
</p>

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Framework / Spring MVC | 4.2.4 | 核心框架 / Web MVC |
| MyBatis + PageHelper | 3.2.8 / 5.1.2 | 持久层 / 分页插件 |
| Dubbo | 2.5.3 | 分布式服务治理 |
| ZooKeeper | 3.4.7 | 服务注册与发现 |
| Redis（Jedis） | 3.0+ / 2.9.0 | 分布式缓存 |
| Solr | 4.10.3 | 全文搜索引擎 |
| ActiveMQ | 5.11.2 | 消息中间件 |
| FastDFS | 5.0+ | 分布式文件存储 |
| FreeMarker | 2.3.23 | 页面静态化 |
| Druid | 1.0.9 | 数据库连接池（含监控） |
| Quartz | 2.2.2 | 任务调度（订单超时关闭） |
| FastJSON / Jackson | 1.2.44 / 2.4.2 | JSON 序列化 |

---

## ⚡ 快速开始

### 1. 环境准备

| 类型 | 软件 | 版本要求 |
|------|------|----------|
| ✅ 必装 | JDK | 1.7+（推荐 1.8） |
| ✅ 必装 | Maven | 3.2+ |
| ✅ 必装 | MySQL | 5.7+ |
| ✅ 必装 | Tomcat | 7.0+ |
| ☑️ 可选 | Redis | 3.0+（缓存服务） |
| ☑️ 可选 | ZooKeeper | 3.4+（Dubbo 注册中心） |
| ☑️ 可选 | Solr | 4.10+（搜索服务） |
| ☑️ 可选 | ActiveMQ | 5.11+（消息队列） |
| ☑️ 可选 | FastDFS | 5.0+（文件存储） |

### 2. 初始化数据库

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE taotao CHARACTER SET utf8 COLLATE utf8_general_ci;"

# 导入 SQL 脚本
mysql -u root -p taotao < docs/taotao.sql
```

### 3. 配置数据源

编辑各 `service` 模块下的 `src/main/resources/properties/db.properties`：

```properties
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/taotao?characterEncoding=utf-8
jdbc.username=root
jdbc.password=your_password
```

### 4. 编译项目

```bash
# 安装父 POM
mvn install -N

# 编译所有模块（跳过测试）
mvn clean install -DskipTests

# 仅编译验证（快速）
mvn compile test-compile
```

### 5. 部署运行

**方式一：Maven Tomcat 插件（推荐）**

```bash
# 终端 1 - 后台管理服务（Dubbo 20880）
cd taotao-manager/taotao-manager-service && mvn tomcat7:run

# 终端 2 - 后台管理 Web（8081）
cd taotao-manager-web && mvn tomcat7:run

# 终端 3 - 门户网站（8082）
cd taotao-portal-web && mvn tomcat7:run
```

**方式二：独立 Tomcat**

将各模块 `target/*.war` 复制到 Tomcat `webapps` 目录，按启动顺序启动。详见 [INSTALL.md](INSTALL.md)。

### 6. 启动顺序与端口

> ⚠️ `service` 模块使用 **Dubbo 协议端口**（RPC 通信），`web` 模块使用 **Tomcat 端口**（HTTP 访问）。
> 必须先启动全部 Service，再启动 Web。

| 顺序 | 模块 | 端口 | 类型 | 说明 |
|------|------|------|------|------|
| 1 | taotao-manager-service | 20880 | Dubbo | 商品/类目/规格服务 |
| 2 | taotao-content-service | 20881 | Dubbo | 内容服务 |
| 3 | taotao-search-service | 20882 | Dubbo | 搜索服务 |
| 4 | taotao-sso-service | 20883 | Dubbo | 单点登录服务 |
| 5 | taotao-order-service | 20884 | Dubbo | 订单服务 |
| 6 | taotao-manager-web | 8081 | HTTP | 后台管理 |
| 7 | taotao-portal-web | 8082 | HTTP | 门户网站 |
| 8 | taotao-search-web | 8085 | HTTP | 搜索前端 |
| 9 | taotao-item-web | 8086 | HTTP | 商品详情 |
| 10 | taotao-sso-web | 8088 | HTTP | SSO 前端 |
| 11 | taotao-cart-web | 8089 | HTTP | 购物车 |
| 12 | taotao-order-web | 8091 | HTTP | 订单前端 |

---

## 📁 项目结构

```
taotao-parent
├── docs/                             # 📚 文档与资源（教学文档 + SQL + 图片）
├── taotao-common/                    # 🔧 [工具层] 通用工具类 & POJO
│
├── taotao-manager/                   # ⚙️ [服务层] 后台管理
│   ├── taotao-manager-dao/           #   数据访问层 (Mapper)
│   ├── taotao-manager-pojo/          #   数据模型
│   ├── taotao-manager-interface/     #   Dubbo 接口定义
│   └── taotao-manager-service/       #   Dubbo 服务实现
│
├── taotao-manager-web/               # 🖥️ [Web层] 后台管理系统
├── taotao-portal-web/                # 🖥️ [Web层] 门户网站
├── taotao-search-web/                # 🖥️ [Web层] 搜索前端
├── taotao-item-web/                  # 🖥️ [Web层] 商品详情
├── taotao-sso-web/                   # 🖥️ [Web层] 登录/注册
├── taotao-cart-web/                  # 🖥️ [Web层] 购物车
├── taotao-order-web/                 # 🖥️ [Web层] 订单前端
│
├── taotao-content/                   # ⚙️ [服务层] 内容管理
│   ├── taotao-content-interface/
│   └── taotao-content-service/
├── taotao-search/                    # ⚙️ [服务层] 搜索服务
│   ├── taotao-search-interface/
│   └── taotao-search-service/
├── taotao-sso/                       # ⚙️ [服务层] 单点登录
│   ├── taotao-sso-interface/
│   └── taotao-sso-service/
└── taotao-order/                     # ⚙️ [服务层] 订单服务
    ├── taotao-order-interface/
    └── taotao-order-service/
```

---

## 🌐 访问地址

| 模块 | 地址 |
|------|------|
| 后台管理 | [http://localhost:8081](http://localhost:8081) |
| 门户网站 | [http://localhost:8082](http://localhost:8082) |
| 搜索页面 | [http://localhost:8085](http://localhost:8085) |
| 商品详情 | [http://localhost:8086](http://localhost:8086) |
| SSO 登录 | [http://localhost:8088](http://localhost:8088) |
| 购物车 | [http://localhost:8089](http://localhost:8089) |
| 订单中心 | [http://localhost:8091](http://localhost:8091) |

---

## ⚙️ 配置说明

### 外部服务

| 服务 | 默认地址 | 配置位置 |
|------|----------|----------|
| MySQL | `localhost:3306` | `properties/db.properties`（各 service 模块） |
| Redis | `172.168.20.221:6379` | `spring/applicationContext-jedis.xml`（各 service 模块） |
| ZooKeeper | `172.168.20.221:2181` | spring 配置文件（各 web/service 模块） |
| Solr | `http://172.168.20.221:8983/solr/collection1` | `spring/applicationContext-solr.xml`（search-service） |
| ActiveMQ | `tcp://172.168.20.221:61616` | `spring/applicationContext-activemq.xml`（manager/search-service） |
| FastDFS | `172.168.20.221:22122` | `resource/client.conf`（manager-web） |

> 💡 本地运行（无外部服务）时，将所有地址改为 `localhost`。

### 业务配置

| 配置项 | 说明 | 配置位置 |
|--------|------|----------|
| 商品缓存过期时间 | `ITEM_EXPIRE=86400`（秒） | `resource.properties`（manager-service） |
| 静态页面输出路径 | `HTML_OUT_PATH` | `resource.properties`（item-web） |
| 图片服务器地址 | `IMAGE_SERVER_URL` | `resource.properties`（manager-web） |
| 购物车 Cookie Key | `CART_KEY=TT_CART` | `resource.properties`（cart/order-web） |
| 登录 Token Key | `TOKEN_KEY=TT_TOKEN` | `resource.properties`（order/sso-web） |
| 搜索每页条数 | `SEARCH_RESULT_ROWS=60` | `resource.properties`（search-web） |
| 邮件告警凭据 | `mail.from` / `mail.authCode` / `mail.to` | 系统属性或环境变量（search-web） |

---

## 🛠️ 部署与运维

| 场景 | 指引 |
|------|------|
| 本地部署 | [INSTALL.md](INSTALL.md) —— 完整安装部署指南 |
| Docker 部署 | [Docker配置.md](Docker配置.md) —— 中间件容器化编排 |
| 版本变更 | [CHANGELOG.md](CHANGELOG.md) —— 更新日志 |

### 日志

各模块日志由 log4j 输出到 `${catalina.home}/logs/`：

| 日志文件 | 级别 | 内容 |
|----------|------|------|
| `taotao-xxx.log` | INFO | 常规业务日志 |
| `taotao-xxx-error.log` | ERROR | 错误日志（独立文件） |

### 常用运维命令

```bash
# 检查服务端口占用
netstat -ano | findstr :8081          # Windows
netstat -tlnp | grep -E "8081|20880"  # Linux

# 查看 Dubbo 服务注册情况
zkCli.sh -server localhost:2181
ls /dubbo

# 查看 Solr 索引
curl "http://localhost:8983/solr/collection1/select?q=*:*"

# 检查 Redis 缓存
redis-cli -h localhost keys "ITEM_INFO*"
```

---

## ❓ 常见问题

<details>
<summary><b>🔧 Maven 编译失败？</b></summary>

**原因**：依赖下载失败或版本冲突

```bash
mvn clean install -U -DskipTests
```

</details>

<details>
<summary><b>🚫 服务无法启动？</b></summary>

**原因**：端口被占用或外部服务未启动

```bash
# 检查端口占用
netstat -ano | findstr :8081

# 启动外部服务
redis-server
zkServer.sh start
solr start
activemq start
```

</details>

<details>
<summary><b>💾 数据库连接失败？</b></summary>

**原因**：配置错误或 MySQL 未启动

```bash
net start mysql
mysql -u root -p taotao
```

</details>

<details>
<summary><b>🔌 Dubbo 服务找不到（No provider）？</b></summary>

**原因**：service 模块未启动或 ZooKeeper 不可达

1. 确认 5 个 service 模块按顺序启动完成
2. 确认 `dubbo:registry` 的 ZooKeeper 地址正确
3. 确认各 web 模块 `dubbo:reference` 接口与 service 发布的接口一致
4. 查看 service 模块日志确认是否注册成功

</details>

<details>
<summary><b>🖼️ 图片上传失败？</b></summary>

**原因**：FastDFS 未启动或 `client.conf` 配置错误

1. 确认 FastDFS tracker/storage 已启动
2. 确认 `resource/client.conf` 中 tracker 地址正确
3. 邮件告警需配置 `mail.from`、`mail.authCode`、`mail.to` 系统属性或环境变量

</details>

<details>
<summary><b>📨 商品上架后搜索不到？</b></summary>

**原因**：ActiveMQ 未启动或索引未同步

1. 确认 ActiveMQ 已启动（`tcp://172.168.20.221:61616`）
2. 到 Solr 查询确认是否有新索引：`curl "http://localhost:8983/solr/collection1/select?q=*:*"`
3. 后台"索引库管理"手动全量导入兜底

</details>

---

## 📸 界面预览

| 页面 | 截图 |
|------|------|
| 登录页面 | ![登录](https://images.gitee.com/uploads/images/2020/1015/180448_1a29e444_800553.png) |
| 后台管理 | ![后台](https://images.gitee.com/uploads/images/2020/1015/175959_62133f32_800553.png) |
| 商品搜索 | ![搜索](https://images.gitee.com/uploads/images/2020/1015/180057_5730639c_800553.png) |

---

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！请阅读 [CONTRIBUTING.md](CONTRIBUTING.md)。

## 📄 许可证

本项目基于 **Apache License 2.0** 开源，详见 [LICENSE](LICENSE)。

---

> 💡 淘淘商城已完结，部分技术可能已过时，但架构思想与业务设计依然经典，适合作为学习和参考项目。

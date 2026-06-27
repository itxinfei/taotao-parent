![](Doc/logo.png)

<p align="center">
  <a href="https://gitee.com/itxinfei">
    <img alt="gitee" src="https://img.shields.io/badge/心飞为你飞-gitee-green">
  </a>
  <a href="https://qm.qq.com/cgi-bin/qm/qr?k=9yLlyD1dRBL97xmBKw43zRt0-6xg8ohb&jump_from=webapi">
    <img alt="QQ群" src="https://img.shields.io/badge/QQ群-863662849-red">
  </a>
  <a href="http://mail.qq.com/cgi-bin/qm_share?t=qm_mailme&email=f0hLSE9OTkdHTT8ODlEcEBI">
    <img alt="mail" src="https://img.shields.io/badge/mail-747011882@qq.com-red">
  </a>
  <a href=" ">
    <img alt="JDK" src="https://img.shields.io/badge/JDK-1.7%2B-brightgreen">
  </a>
  <a href=" ">
    <img alt="maven" src="https://img.shields.io/badge/maven-3.2%2B-yellowgreen">
  </a>
  <a href=" ">
    <img alt="license" src="https://img.shields.io/badge/license-Apache%202.0-green">
  </a>
</p>

淘淘商城是一个基于 **Dubbo + Spring + MyBatis** 的分布式 B2C 电商平台，提供完整的商品管理、搜索、购物车、订单、单点登录等功能。适合作为 Java 分布式架构的学习案例和中小型电商项目的基础框架。

## 功能特性

- **后台管理**：商品管理、类目管理、内容发布、规格参数、订单处理
- **门户网站**：首页展示、商品检索、商品详情、购物车、下单结算
- **单点登录**：跨系统登录态管理
- **搜索服务**：基于 Solr 的商品全文检索
- **内容管理**：广告位、分类内容的动态发布

## 系统架构

![系统架构](Doc/系统架构.png)

![功能列表](Doc/功能列表.png)

## 技术栈

| 技术 | 用途 |
|------|------|
| Spring Framework 4.3 | 核心框架 |
| Spring MVC 4.3 | Web MVC 框架 |
| MyBatis 3.3 | 持久层框架 |
| Dubbo 2.6 | 分布式服务治理 |
| ZooKeeper 3.4 | 服务注册与发现 |
| Redis 3.0 | 分布式缓存 |
| Solr 6.6 | 全文搜索引擎 |
| ActiveMQ 5.15 | 消息中间件 |
| FastDFS 5.0 | 分布式文件存储 |
| FreeMarker 2.3 | 页面静态化 |
| Druid 1.1 | 数据库连接池 |
| Quartz 2.2 | 任务调度 |
| SLF4J + Log4j | 日志框架 |

## 快速开始

### 环境准备

- JDK 1.7+, Maven 3.2+, MySQL 5.7+, Tomcat 7+
- Redis / Solr / ZooKeeper / ActiveMQ / FastDFS（按需安装，详见 [INSTALL.md](INSTALL.md)）

### 部署

```bash
# 1. 初始化数据库
mysql -u root -p < Doc/taotao.sql

# 2. 编译项目
mvn clean install -DskipTests

# 3. 按依赖顺序部署 war 包到 Tomcat
#    先启动 service 模块，再启动 web 模块
```

详细步骤请参考 [INSTALL.md](INSTALL.md)。

## 项目结构

```
taotao-parent
├── taotao-common               # 通用工具
├── taotao-manager/             # 后台管理服务层
│   ├── taotao-manager-dao
│   ├── taotao-manager-pojo
│   ├── taotao-manager-interface
│   └── taotao-manager-service
├── taotao-manager-web          # 后台管理 Web
├── taotao-portal-web           # 门户网站
├── taotao-content/             # 内容服务
├── taotao-search/              # 搜索服务
├── taotao-search-web           # 搜索前端
├── taotao-item-web             # 商品详情页
├── taotao-sso/                 # 单点登录服务
├── taotao-sso-web              # SSO 前端
├── taotao-cart-web             # 购物车
├── taotao-order/               # 订单服务
└── taotao-order-web            # 订单前端
```

## 界面预览

| 页面 | 截图 |
|------|------|
| 登录页面 | ![登录](https://images.gitee.com/uploads/images/2020/1015/180448_1a29e444_800553.png) |
| 后台管理 | ![后台](https://images.gitee.com/uploads/images/2020/1015/175959_62133f32_800553.png) |
| 商品搜索 | ![搜索](https://images.gitee.com/uploads/images/2020/1015/180057_5730639c_800553.png) |

## 贡献

欢迎提交 Issue 和 Pull Request！请阅读 [CONTRIBUTING.md](CONTRIBUTING.md)。

## 许可证

本项目基于 Apache License 2.0 开源，详见 [LICENSE](LICENSE)。

- http://localhost:8082/ taotao-portal-web
- http://localhost:8083/ taotao-content
- http://localhost:8084/ taotao-search-service
- http://localhost:8085/ taotao-search-web 
- http://localhost:8086/ taotao-item-web 
- http://localhost:8087/ taotao-sso-service 
- http://localhost:8088/ taotao-sso-web 
- http://localhost:8089/ taotao-cart-web
- http://localhost:8090/ taotao-order
- http://localhost:8091/ taotao-order-web

> 淘淘商城已完结，部分技术可能已过时，适合作为学习和参考项目。

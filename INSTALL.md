# 安装指南

## 环境要求

| 组件 | 版本 |
|------|------|
| JDK | 1.7+ |
| Maven | 3.2+ |
| MySQL | 5.7+ |
| Redis | 3.0+ |
| Solr | 6.6+ |
| ZooKeeper | 3.4+ |
| ActiveMQ | 5.15+ |
| FastDFS | 5.0+ |
| Tomcat | 7.0+ |

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/itxinfei/taotao-parent.git
cd taotao-parent
```

### 2. 初始化数据库

```bash
mysql -u root -p < Doc/taotao.sql
```

### 3. 配置数据源

编辑各 service 模块下的 `db.properties`，修改数据库连接信息：

```properties
jdbc.url=jdbc:mysql://localhost:3306/taotao?characterEncoding=utf-8
jdbc.username=root
jdbc.password=root
```

### 4. 配置外部服务

各 web 模块下的 `resource.properties` 和 service 模块下的 `resource.properties` 中包含了 Redis、Solr、ZooKeeper、ActiveMQ、FastDFS 等服务的地址配置，请根据实际部署环境修改。

### 5. Maven 编译

```bash
mvn clean install -DskipTests
```

### 6. 部署顺序

淘淘商城采用 Dubbo 分布式架构，各服务之间存在依赖关系，推荐按以下顺序启动：

| 启动顺序 | 模块 | 端口 | 说明 |
|---------|------|------|------|
| 1 | taotao-manager-service | 8083 | Dubbo 服务提供者 |
| 2 | taotao-content-service | 8080 | 内容服务 |
| 3 | taotao-search-service | 8084 | 搜索服务 |
| 4 | taotao-sso-service | 8087 | 单点登录服务 |
| 5 | taotao-order-service | 8090 | 订单服务 |
| 6 | taotao-manager-web | 8081 | 后台管理 |
| 7 | taotao-portal-web | 8082 | 门户网站 |
| 8 | taotao-search-web | 8085 | 搜索前端 |
| 9 | taotao-item-web | 8086 | 商品详情 |
| 10 | taotao-sso-web | 8088 | SSO 前端 |
| 11 | taotao-cart-web | 8089 | 购物车 |
| 12 | taotao-order-web | 8091 | 订单前端 |

### 7. Docker 部署

各中间件的 Docker 部署方式请参考 [Docker配置.md](Docker配置.md)。

## 系统架构

![系统架构](Doc/系统架构.png)

## 项目结构

```
taotao-parent
├── taotao-common                 # 通用工具类、POJO
├── taotao-manager                # 后台管理服务
│   ├── taotao-manager-dao        # 数据访问层
│   ├── taotao-manager-pojo       # POJO 类
│   ├── taotao-manager-interface  # Dubbo 接口
│   ├── taotao-manager-service    # Dubbo 服务实现
├── taotao-manager-web            # 后台管理 Web 层
├── taotao-portal-web             # 门户网站
├── taotao-content                # 内容管理服务
├── taotao-search                 # 搜索服务
│   ├── taotao-search-interface   # 搜索接口
│   ├── taotao-search-service     # 搜索服务实现
├── taotao-search-web             # 搜索前端
├── taotao-item-web               # 商品详情页
├── taotao-sso                    # 单点登录
│   ├── taotao-sso-interface      # SSO 接口
│   ├── taotao-sso-service        # SSO 服务实现
├── taotao-sso-web                # SSO 前端
├── taotao-cart-web               # 购物车
├── taotao-order                  # 订单服务
│   ├── taotao-order-interface    # 订单接口
│   ├── taotao-order-service      # 订单服务实现
├── taotao-order-web              # 订单前端
```

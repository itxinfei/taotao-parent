## taotao-search - 搜索服务

### 模块概述

基于Solr实现的全文搜索服务，提供商品搜索、分页、排序等功能。

### 模块结构

```
taotao-search/
├── taotao-search-interface/     # Dubbo接口定义
│   └── src/main/java/com/taotao/search/
│       ├── dao/SearchDao.java    # 搜索DAO接口
│       └── service/SearchService.java # 搜索服务接口
└── taotao-search-service/        # Dubbo服务实现
    └── src/main/java/com/taotao/search/
        ├── dao/impl/SearchDaoImpl.java    # DAO实现
        ├── service/impl/SearchServiceImpl.java # 服务实现
        └── listener/MyMessageListener.java    # ActiveMQ消息监听
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 商品搜索 | 支持关键词全文检索 |
| 分页查询 | 支持分页和排序 |
| 索引更新 | 监听ActiveMQ消息更新索引 |

### 技术栈

- **框架**: Spring 4.2.4
- **服务治理**: Dubbo 2.5.3
- **搜索引擎**: Solr 4.10.3
- **消息队列**: ActiveMQ 5.11.2

### 启动方式

```bash
cd taotao-search/taotao-search-service
mvn tomcat7:run

# 默认端口：8084
```

### 依赖关系

- **依赖**: `taotao-common`
- **被依赖**: `taotao-search-web`, `taotao-portal-web`

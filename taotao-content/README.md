## taotao-content - 内容管理服务

### 模块概述

提供网站内容管理功能，包括广告位管理、内容分类管理等。

### 模块结构

```
taotao-content/
├── taotao-content-interface/    # Dubbo接口定义
│   └── src/main/java/com/taotao/content/service/
│       ├── ContentService.java           # 内容服务接口
│       └── ContentCategoryService.java   # 内容分类服务接口
└── taotao-content-service/       # Dubbo服务实现
    └── src/main/java/com/taotao/content/service/impl/
        ├── ContentServiceImpl.java           # 内容服务实现
        └── ContentCategoryServiceImpl.java   # 分类服务实现
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 内容CRUD | 内容的增删改查 |
| 分类管理 | 内容分类的树形结构管理 |
| 缓存支持 | 使用Redis缓存热门内容 |

### 技术栈

- **框架**: Spring 4.2.4 + MyBatis 3.2.8
- **服务治理**: Dubbo 2.5.3
- **缓存**: Redis 3.0+

### 启动方式

```bash
cd taotao-content/taotao-content-service
mvn tomcat7:run

# 默认端口：8080
```

### 依赖关系

- **依赖**: `taotao-common`
- **被依赖**: `taotao-portal-web`, `taotao-manager-web`

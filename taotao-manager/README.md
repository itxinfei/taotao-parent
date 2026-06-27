## taotao-manager - 后台管理服务层

### 模块概述

该模块是淘淘商城的核心服务层，提供商品管理、类目管理、规格参数等后台管理功能的业务逻辑实现。

### 模块结构

```
taotao-manager/
├── taotao-manager-dao/        # 数据访问层
│   └── src/main/java/com/taotao/mapper/
│       └── *.java              # MyBatis Mapper接口
├── taotao-manager-pojo/        # 数据模型层
│   └── src/main/java/com/taotao/pojo/
│       └── *.java              # 数据库实体类
├── taotao-manager-interface/   # Dubbo接口定义
│   └── src/main/java/com/taotao/service/
│       └── *.java              # 服务接口定义
└── taotao-manager-service/     # Dubbo服务实现
    └── src/main/java/com/taotao/service/impl/
        └── *.java              # 服务实现类
```

### 核心服务

| 服务接口 | 功能说明 |
|----------|----------|
| `ItemService` | 商品管理服务（CRUD） |
| `ItemCatService` | 商品类目服务 |
| `ItemParamService` | 规格参数服务 |
| `ItemDescService` | 商品描述服务 |

### 技术栈

- **框架**: Spring 4.2.4 + MyBatis 3.2.8
- **服务治理**: Dubbo 2.5.3
- **数据库**: MySQL 5.7+
- **缓存**: Redis 3.0+

### 启动方式

```bash
# 使用Maven Tomcat插件启动
cd taotao-manager/taotao-manager-service
mvn tomcat7:run

# 默认端口：8083
```

### 依赖关系

- **依赖**: `taotao-common`, `taotao-manager-pojo`
- **被依赖**: `taotao-manager-web`

## taotao-sso - 单点登录服务

### 模块概述

该模块实现分布式系统的单点登录功能，支持跨多个Web应用的统一身份认证。

### 模块结构

```
taotao-sso/
├── taotao-sso-interface/       # Dubbo接口定义
│   └── src/main/java/com/taotao/sso/service/
│       └── UserService.java     # 用户服务接口
└── taotao-sso-service/          # Dubbo服务实现
    └── src/main/java/com/taotao/sso/service/impl/
        └── UserServiceImpl.java # 用户服务实现
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 用户注册 | 创建新用户账号，密码MD5加密存储 |
| 用户登录 | 验证用户名密码，生成Token存入Redis |
| Token验证 | 根据Token获取用户信息 |
| 用户登出 | 使Token失效 |
| 数据校验 | 检查用户名/手机号/邮箱是否已注册 |

### 技术栈

- **框架**: Spring 4.2.4 + MyBatis 3.2.8
- **服务治理**: Dubbo 2.5.3
- **缓存**: Redis 3.0+
- **Token机制**: UUID + Redis过期策略

### 启动方式

```bash
cd taotao-sso/taotao-sso-service
mvn tomcat7:run

# 默认端口：8087
```

### 依赖关系

- **依赖**: `taotao-common`
- **被依赖**: `taotao-sso-web`, `taotao-cart-web`, `taotao-order-web`

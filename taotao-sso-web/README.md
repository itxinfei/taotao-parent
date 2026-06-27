## taotao-sso-web - SSO前端

### 模块概述

单点登录前端模块，提供用户登录、注册页面和相关API。

### 模块结构

```
taotao-sso-web/
├── src/main/java/com/taotao/sso/controller/
│   └── UserController.java      # 用户控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/             # JSP页面
    ├── js/                      # JavaScript文件
    └── css/                     # 样式文件
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 用户登录 | 用户名密码登录 |
| 用户注册 | 创建新账号 |
| 数据校验 | 实时验证用户名/手机号/邮箱 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: jQuery
- **模板引擎**: JSP
- **跨域**: CORS + JSONP

### 启动方式

```bash
cd taotao-sso-web
mvn tomcat7:run

# 默认端口：8088
# 访问地址：http://localhost:8088
```

### 依赖关系

- **依赖**: `taotao-sso-interface`, `taotao-common`

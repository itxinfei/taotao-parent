## taotao-order-web - 订单前端

### 模块概述

订单管理前端模块，提供订单创建、查询、结算等功能。

### 模块结构

```
taotao-order-web/
├── src/main/java/com/taotao/order/controller/
│   └── OrderController.java      # 订单控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/             # JSP页面
    ├── js/                      # JavaScript文件
    └── css/                     # 样式文件
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 订单创建 | 从购物车结算创建订单 |
| 订单列表 | 用户订单列表展示 |
| 订单详情 | 订单详细信息展示 |
| 订单管理 | 后台订单管理（发货等） |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: jQuery
- **模板引擎**: JSP
- **单点登录**: Cookie验证

### 启动方式

```bash
cd taotao-order-web
mvn tomcat7:run

# 默认端口：8091
# 访问地址：http://localhost:8091
```

### 依赖关系

- **依赖**: `taotao-order-interface`, `taotao-sso-interface`, `taotao-cart-web`, `taotao-common`

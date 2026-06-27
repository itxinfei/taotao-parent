## taotao-cart-web - 购物车

### 模块概述

购物车模块，提供商品添加、删除、数量修改等功能。

### 模块结构

```
taotao-cart-web/
├── src/main/java/com/taotao/cart/controller/
│   └── CartController.java      # 购物车控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/             # JSP页面
    ├── js/                      # JavaScript文件
    └── css/                     # 样式文件
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 添加商品 | 将商品加入购物车 |
| 查看购物车 | 展示购物车商品列表 |
| 修改数量 | 修改购物车商品数量 |
| 删除商品 | 从购物车删除商品 |
| 数据同步 | Cookie + Redis双存储方案 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: jQuery
- **模板引擎**: JSP
- **缓存**: Redis（登录用户）
- **Cookie**: 未登录用户购物车存储

### 启动方式

```bash
cd taotao-cart-web
mvn tomcat7:run

# 默认端口：8089
# 访问地址：http://localhost:8089
```

### 依赖关系

- **依赖**: `taotao-manager-interface`, `taotao-sso-interface`, `taotao-common`

## taotao-item-web - 商品详情页

### 模块概述

商品详情页面模块，提供商品详情展示和静态页生成功能。

### 模块结构

```
taotao-item-web/
├── src/main/java/com/taotao/item/controller/
│   └── ItemController.java      # 商品详情控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/             # JSP页面
    └── html/                    # 静态HTML页面（FreeMarker生成）
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 商品详情 | 展示商品详细信息 |
| 静态页生成 | 商品静态页面生成和访问 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **模板引擎**: JSP + FreeMarker
- **消息队列**: ActiveMQ（接收商品添加消息）

### 启动方式

```bash
cd taotao-item-web
mvn tomcat7:run

# 默认端口：8086
# 访问地址：http://localhost:8086
```

### 依赖关系

- **依赖**: `taotao-manager-interface`, `taotao-common`

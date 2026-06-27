## taotao-portal-web - 门户网站

### 模块概述

淘淘商城门户网站前端模块，提供首页展示、商品检索、用户中心等功能。

### 模块结构

```
taotao-portal-web/
├── src/main/java/com/taotao/portal/controller/
│   ├── PageController.java          # 页面跳转控制器
│   └── UserCenterController.java    # 用户中心控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/                 # JSP页面
    ├── js/                          # JavaScript文件
    ├── css/                         # 样式文件
    └── images/                      # 图片资源
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 首页展示 | 轮播图、商品分类、热门商品 |
| 商品搜索 | 关键词搜索入口 |
| 用户中心 | 用户信息展示和管理 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: jQuery
- **模板引擎**: JSP
- **单点登录**: Cookie + JSONP

### 启动方式

```bash
cd taotao-portal-web
mvn tomcat7:run

# 默认端口：8082
# 访问地址：http://localhost:8082
```

### 依赖关系

- **依赖**: `taotao-search-interface`, `taotao-content-interface`, `taotao-sso-interface`, `taotao-common`

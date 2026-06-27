## taotao-search-web - 搜索前端

### 模块概述

商品搜索页面模块，提供商品搜索结果展示功能。

### 模块结构

```
taotao-search-web/
├── src/main/java/com/taotao/search/controller/
│   ├── PageController.java      # 页面跳转控制器
│   └── SearchController.java    # 搜索功能控制器
└── src/main/webapp/
    ├── WEB-INF/jsp/             # JSP页面
    ├── js/                      # JavaScript文件
    └── css/                     # 样式文件
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 搜索首页 | 搜索框和热门搜索词 |
| 结果展示 | 商品列表、分页、排序 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: jQuery
- **模板引擎**: JSP

### 启动方式

```bash
cd taotao-search-web
mvn tomcat7:run

# 默认端口：8085
# 访问地址：http://localhost:8085
```

### 依赖关系

- **依赖**: `taotao-search-interface`, `taotao-common`

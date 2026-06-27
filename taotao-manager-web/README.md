## taotao-manager-web - 后台管理前端

### 模块概述

淘淘商城后台管理系统的前端模块，提供商品管理、类目管理、内容管理等管理功能的Web界面。

### 模块结构

```
taotao-manager-web/
├── src/main/java/com/taotao/controller/  # Spring MVC控制器
│   ├── ItemController.java       # 商品管理控制器
│   ├── ItemCatController.java    # 类目管理控制器
│   ├── PageController.java       # 页面跳转控制器
│   └── PictureController.java    # 图片上传控制器
├── src/main/webapp/              # Web资源
│   ├── WEB-INF/jsp/              # JSP页面
│   ├── js/                       # JavaScript文件
│   ├── css/                      # 样式文件
│   └── plugins/                  # 第三方插件
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 商品管理 | 商品列表、添加、编辑、删除、上下架 |
| 类目管理 | 商品类目树形展示和管理 |
| 图片上传 | 使用FastDFS上传商品图片 |
| 规格参数 | 商品规格参数模板管理 |

### 技术栈

- **框架**: Spring MVC 4.2.4
- **前端框架**: EasyUI
- **模板引擎**: JSP
- **文件存储**: FastDFS

### 启动方式

```bash
cd taotao-manager-web
mvn tomcat7:run

# 默认端口：8081
# 访问地址：http://localhost:8081
```

### 依赖关系

- **依赖**: `taotao-manager-interface`, `taotao-common`

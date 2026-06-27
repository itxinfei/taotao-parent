## taotao-common - 通用工具模块

### 模块概述

提供项目通用的工具类、POJO对象和常量定义，供其他模块引用。

### 模块结构

```
taotao-common/
├── src/main/java/com/taotao/common/
│   ├── pojo/          # 公共POJO对象
│   │   ├── TaotaoResult.java       # 统一响应结果
│   │   ├── EasyUIDataGridResult.java # EasyUI分页结果
│   │   ├── EasyUITreeNode.java      # 树形节点
│   │   ├── SearchResult.java        # 搜索结果
│   │   └── SearchItem.java          # 搜索商品项
│   └── utils/         # 工具类
│       ├── JsonUtils.java           # JSON处理工具
│       ├── CookieUtils.java         # Cookie操作工具
│       └── IDUtils.java             # ID生成工具
```

### 核心类说明

| 类名 | 功能说明 |
|------|----------|
| `TaotaoResult` | 统一API响应封装，包含status、msg、data |
| `EasyUIDataGridResult` | EasyUI DataGrid组件的分页数据格式 |
| `EasyUITreeNode` | 树形结构数据节点（用于商品类目） |
| `JsonUtils` | JSON序列化/反序列化工具 |
| `CookieUtils` | Cookie读写工具（支持中文编码） |
| `IDUtils` | 唯一ID生成工具（基于UUID和时间戳） |

### 技术栈

- **JSON处理**: FastJSON
- **日志**: SLF4J

### 使用方式

在其他模块的pom.xml中添加依赖：

```xml
<dependency>
    <groupId>com.taotao</groupId>
    <artifactId>taotao-common</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 被依赖模块

- `taotao-manager`
- `taotao-content`
- `taotao-search`
- `taotao-sso`
- `taotao-order`
- 所有Web模块

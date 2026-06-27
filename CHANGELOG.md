# 更新日志

## [2.0.0] - 2026-06-27

### 新增

- 所有 web 模块添加 DailyRollingFileAppender 日志文件输出，错误日志独立文件
- 所有模块配置 Druid 监控（stat 过滤器）

### 优化

- Druid 连接池调优：maxActive=50、initialSize=10、maxWait=60000、连接有效性检测
- Redis 客户端添加连接超时和读取超时配置（2000ms）
- 事务回滚加固：所有写事务方法添加 `rollback-for="java.lang.Exception"`
- 全局替换 `e.printStackTrace()` 为 SLF4J `logger.error()`
- 日志级别调整：rootLogger 从 DEBUG 改为 INFO（manager-web 为 WARN）
- 仓库清理：移除非必要文件，完善 .gitignore
- 文档补充：INSTALL.md、CONTRIBUTING.md、CHANGELOG.md
- README 重构：优化项目描述、技术栈、快速开始等内容

### 修复

- ItemServiceImpl 空指针异常
- ContentServiceImpl 错误导入
- item-edit.jsp 价格计算错误
- Docker配置.md 中 Redis/MongoDB 命令错误

## [1.0.0] - 2020-10

### 功能

- 后台管理系统：商品、订单、类目、规格、内容管理
- 门户网站：商品浏览、搜索、购物车、下单
- 单点登录系统
- 搜索系统（Solr）
- 商品静态化（FreeMarker）
- Quartz 定时任务

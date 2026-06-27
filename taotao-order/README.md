## taotao-order - 订单服务

### 模块概述

提供订单创建、查询、状态管理等功能的服务层模块。

### 模块结构

```
taotao-order/
├── taotao-order-interface/      # Dubbo接口定义
│   └── src/main/java/com/taotao/order/service/
│       └── OrderService.java     # 订单服务接口
└── taotao-order-service/         # Dubbo服务实现
    └── src/main/java/com/taotao/order/
        ├── service/OrderServiceImpl.java # 订单服务实现
        └── job/OrderJob.java             # Quartz定时任务
```

### 核心功能

| 功能 | 说明 |
|------|------|
| 创建订单 | 生成订单号，插入订单主表和明细表 |
| 查询订单 | 根据用户ID分页查询订单列表 |
| 订单详情 | 查询订单详细信息（含商品明细） |
| 超时关闭 | Quartz定时任务关闭超时未支付订单 |

### 技术栈

- **框架**: Spring 4.2.4 + MyBatis 3.2.8
- **服务治理**: Dubbo 2.5.3
- **缓存**: Redis 3.0+
- **任务调度**: Quartz 2.2.2

### 启动方式

```bash
cd taotao-order/taotao-order-service
mvn tomcat7:run

# 默认端口：8090
```

### 依赖关系

- **依赖**: `taotao-common`
- **被依赖**: `taotao-order-web`

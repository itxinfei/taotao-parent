### 淘淘商城
这个淘淘商城是根据原始资料进行的整理，虽然以现在的时间点来看这套教程比较老了，但是如果认真去学习的话还是能学到很多东西，而且配套资料真的是太全了，笔记写得也很详细。
- 学好Java，用多久你会升职加薪、当上总经理、出任CEO、迎娶白富美、走上人生巅峰！想想还有点小激动。
- 祝大家技术更好，开心工作，快乐玩耍。

### 功能描述
- 后台管理系统：管理商品、订单、类目、商品规格属性、用户管理以及内容发布等功能。
- 前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作。
- 会员系统：用户可以在该系统中查询已下的订单、收藏的商品、我的优惠券、团购等信息。
- 订单系统：提供下单、查询订单、修改订单状态、定时处理订单。
- 搜索系统：提供商品的搜索功能。
- 单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息。

### 涉及技术
- 缓存： redis集群
- 搜索 ：solr集群
- 模板：FreeMarker
- 数据库：mysql、mycat技术
- 视图框架：Spring MVC 4.3
- 持久层框架：MyBatis 3.3
- 核心框架：Spring Framework 4.3
- 定时器：Quartz
- 消息中间件：ActiveMQ
- 数据库连接池：Druid 1.1
- 日志管理：SLF4J 1.7、Log4j
- 服务中间件：dubbo
- 分布式管理：zookpeer
- 图片服务器：FastDFS

### 运行环境

- mysql: localhost:3306

- zookeeper: 172.168.20.221:2181

- activemq: 172.168.20.221:61616

- solr: 172.168.20.221:8983

- redis: 172.168.20.221:6379

- FastDFS: 172.168.20.221:22122  

- 图片预览：172.168.20.221:8888

### 项目端口

- http://localhost:8080/ taotao-manager

- http://localhost:8081/ taotao-manager-web

- http://localhost:8082/ taotao-portal-web

- http://localhost:8083/ taotao-content

- http://localhost:8084/ taotao-search-service

- http://localhost:8085/ taotao-search-web 

- http://localhost:8086/ taotao-item-web 

- http://localhost:8087/ taotao-sso-service 

- http://localhost:8088/ taotao-sso-web 

- http://localhost:8089/ taotao-cart-web

- http://localhost:8090/ taotao-order

- http://localhost:8091/ taotao-order-web

我的服务都是Docker运行的，不熟悉Docker自己去学习一下吧，感觉还是很简单，精通就有点难。





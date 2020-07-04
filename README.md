## 淘淘商城

### **所涉及技术：**
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

### 部署环境说明：
#### 1.Solr-Cloud集群环境说明
- 3台zookpeer
```
server.1=192.168.206.10:2891:4881

server.2=192.168.206.10:2892:4882

server.3=192.168.206.10:2893:5883
```
- 4台tomcat环境
```
tomcat-1    9091         9001            9011
tomcat-2    9092         9002            9012
tomcat-3    9093         9003            9013
tomcat-4    9094         9004            9014


```

#### 2.FastDFS
- Nginx服务
- FastDFS服务
#### 2.Redis集群
- 6台Redis
```
root       1653      1  0 04:37 ?        00:00:21 /usr/local/redis/bin/redis-server 192.168.206.10:6379            
root      95518      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7001 [cluster]
root      95523      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7002 [cluster]
root      95528      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7003 [cluster]
root      95533      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7004 [cluster]
root      95538      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7005 [cluster]
root      95543      1  0 09:21 ?        00:00:00 ./redis-server 192.168.206.10:7006 [cluster]
```
### 开发环境及包结构
[![环境](http://oy3l6utxs.bkt.clouddn.com/%E7%8E%AF%E5%A2%83.jpg "环境")](http://oy3l6utxs.bkt.clouddn.com/%E7%8E%AF%E5%A2%83.jpg "环境")
### 商城首页
[![商城首页](http://oy3l6utxs.bkt.clouddn.com/%E6%B7%98%E6%B7%98%E7%BD%91%E4%B8%8A%E5%95%86%E5%9F%8E%E9%A1%B5%E9%9D%A2.png "商城首页")](http://oy3l6utxs.bkt.clouddn.com/%E6%B7%98%E6%B7%98%E7%BD%91%E4%B8%8A%E5%95%86%E5%9F%8E%E9%A1%B5%E9%9D%A2.png "商城首页")
### 商品搜索页面
[![商品搜索页面](http://oy3l6utxs.bkt.clouddn.com/%E5%95%86%E5%93%81%E6%90%9C%E7%B4%A2.png "商品搜索页面")](http://oy3l6utxs.bkt.clouddn.com/%E5%95%86%E5%93%81%E6%90%9C%E7%B4%A2.png "商品搜索页面")
### 商品详情页面
[![商品详情页面](http://oy3l6utxs.bkt.clouddn.com/%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85%E9%A1%B5.png "商品详情页面")](http://oy3l6utxs.bkt.clouddn.com/%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85%E9%A1%B5.png "商品详情页面")
### 购物车页面
[![购物车页面](http://oy3l6utxs.bkt.clouddn.com/%E8%B4%AD%E7%89%A9%E8%BD%A6%E9%A1%B5%E9%9D%A2.png "购物车页面")](http://oy3l6utxs.bkt.clouddn.com/%E8%B4%AD%E7%89%A9%E8%BD%A6%E9%A1%B5%E9%9D%A2.png "购物车页面")
### 商城登录页面
[![商场登录页面](http://oy3l6utxs.bkt.clouddn.com/%E7%99%BB%E5%BD%95%E9%A1%B5%E9%9D%A2.png "商场登录页面")](http://oy3l6utxs.bkt.clouddn.com/%E7%99%BB%E5%BD%95%E9%A1%B5%E9%9D%A2.png "商场登录页面")
### 商场后台管理系统
[![商场后台管理系统界面](http://oy3l6utxs.bkt.clouddn.com/%E6%B7%98%E6%B7%98%E5%95%86%E5%9F%8E%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F.png "商场后台管理系统界面")](http://oy3l6utxs.bkt.clouddn.com/%E6%B7%98%E6%B7%98%E5%95%86%E5%9F%8E%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F.png "商场后台管理系统界面")
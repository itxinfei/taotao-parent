package com.taotao.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品服务实现类
 * 实现商品管理相关的业务操作，包含缓存机制和消息通知
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    /**
     * 商品Mapper，用于商品基础信息的数据库操作
     */
    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 商品描述Mapper，用于商品描述的数据库操作
     */
    @Autowired
    private TbItemDescMapper itemDescMapper;

    /**
     * 商品规格参数Mapper，用于商品规格参数的数据库操作
     */
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;

    /**
     * Redis客户端，用于缓存操作
     */
    @Autowired
    private JedisClient jedisClient;

    /**
     * 商品信息缓存键前缀，配置于properties文件
     */
    @Value("${ITEM_INFO}")
    private String ITEM_INFO;

    /**
     * 商品信息缓存过期时间（秒），配置于properties文件
     */
    @Value("${ITEM_EXPIRE}")
    private Integer ITEM_EXPIRE;

    /**
     * JMS消息模板，用于发送消息
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 商品添加消息队列主题，用于通知搜索和静态页服务
     */
    @Resource(name = "itemAddTopic")
    private Destination destination;

    /**
     * 根据商品ID获取商品基础信息
     * 实现逻辑：
     * 1. 优先从Redis缓存中查询，缓存键格式：ITEM_INFO:itemId:BASE
     * 2. 缓存命中则直接返回，缓存未命中则从数据库查询
     * 3. 数据库查询结果写入缓存，并设置过期时间
     * 4. Redis操作异常不影响主流程，仅记录日志
     * 
     * @param itemId 商品ID
     * @return 商品实体对象，若不存在返回null
     */
    @Override
    public TbItem getItemById(long itemId) {
        // 查询数据库之前先查询缓存
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":BASE");
            if (!StringUtils.isBlank(json)) {
                return JSON.parseObject(json, TbItem.class);
            }
        } catch (Exception e) {
            logger.error("查询Redis缓存(商品基础信息)失败", e);
        }
        
        // 缓存未命中，从数据库查询
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        
        // 将查询结果添加到缓存
        try {
            jedisClient.set(ITEM_INFO + ":" + itemId + ":BASE", JSON.toJSONString(tbItem));
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":BASE", ITEM_EXPIRE);
        } catch (Exception e) {
            logger.error("写入Redis缓存(商品基础信息)失败", e);
        }
        
        return tbItem;
    }

    /**
     * 根据商品ID获取商品描述信息
     * 实现逻辑：
     * 1. 优先从Redis缓存中查询，缓存键格式：ITEM_INFO:itemId:DESC
     * 2. 缓存命中则直接返回，缓存未命中则从数据库查询
     * 3. 数据库查询结果写入缓存，并设置过期时间
     * 4. Redis操作异常不影响主流程，仅记录日志
     * 
     * @param itemId 商品ID
     * @return 商品描述对象，若不存在返回null
     */
    @Override
    public TbItemDesc getItemDescById(long itemId) {
        // 查询数据库之前先查询缓存
        try {
            String json = jedisClient.get(ITEM_INFO + ":" + itemId + ":DESC");
            if (!StringUtils.isBlank(json)) {
                return JSON.parseObject(json, TbItemDesc.class);
            }
        } catch (Exception e) {
            logger.error("查询Redis缓存(商品描述)失败", e);
        }
        
        // 缓存未命中，从数据库查询
        TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
        
        // 将查询结果添加到缓存
        try {
            jedisClient.set(ITEM_INFO + ":" + itemId + ":DESC", JSON.toJSONString(tbItemDesc));
            jedisClient.expire(ITEM_INFO + ":" + itemId + ":DESC", ITEM_EXPIRE);
        } catch (Exception e) {
            logger.error("写入Redis缓存(商品描述)失败", e);
        }
        
        return tbItemDesc;
    }

    /**
     * 根据商品ID获取商品规格参数
     * 通过商品ID查询商品规格参数表，一个商品对应一条规格参数记录
     * 
     * @param id 商品ID
     * @return 商品规格参数对象，若不存在返回null
     */
    @Override
    public TbItemParamItem getItemParamById(Long id) {
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(id);
        List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
        TbItemParamItem itemParamItem = null;
        if (list != null && !list.isEmpty()) {
            itemParamItem = list.get(0);
        }
        return itemParamItem;
    }

    /**
     * 分页查询商品列表
     * 使用PageHelper进行分页，查询所有商品（不分状态）
     * 
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 分页结果，包含商品列表和总记录数
     */
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        // 设置分页信息
        PageHelper.startPage(page, rows);
        
        // 执行查询（查询所有商品）
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        
        // 封装分页结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        
        return result;
    }

    /**
     * 添加商品
     * 实现逻辑：
     * 1. 生成商品ID（使用IDUtils生成唯一ID）
     * 2. 补全商品基础信息（状态设为1-正常，设置创建时间和更新时间）
     * 3. 插入商品基础信息到数据库
     * 4. 插入商品描述信息到数据库
     * 5. 发送ActiveMQ消息，通知搜索服务和静态页生成服务
     * 
     * @param tbItem 商品基础信息
     * @param desc 商品描述
     * @return 操作结果，成功返回TaotaoResult.ok()
     * @throws Exception 添加过程中发生异常时抛出
     */
    @Override
    public TaotaoResult createItem(TbItem tbItem, String desc) throws Exception {
        // 生成商品ID
        final long itemId = IDUtils.genItemId();
        
        // 补全商品属性
        tbItem.setId(itemId);
        tbItem.setStatus((byte) 1);  // 1-正常，2-下架，3-删除
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());
        
        // 插入商品基础信息
        itemMapper.insert(tbItem);
        
        // 添加商品描述
        insertItemDesc(itemId, desc);
        
        // 发送ActiveMQ消息，通知搜索和静态页服务
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(itemId + "");
                return textMessage;
            }
        });
        
        return TaotaoResult.ok();
    }

    /**
     * 更新商品信息
     * 同时更新商品基础信息、描述，删除缓存，发送MQ消息同步搜索
     * 
     * @param tbItem 商品基础信息
     * @param desc 商品描述
     * @return 操作结果
     */
    @Override
    public TaotaoResult updateItem(TbItem tbItem, String desc) {
        // 更新商品基础信息
        tbItem.setUpdated(new Date());
        itemMapper.updateByPrimaryKeySelective(tbItem);

        // 更新商品描述
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(tbItem.getId());
        itemDesc.setItemDesc(desc);
        itemDesc.setUpdated(new Date());
        itemDescMapper.updateByPrimaryKeySelective(itemDesc);

        // 删除缓存
        try {
            jedisClient.expire(ITEM_INFO + ":" + tbItem.getId() + ":BASE", 0);
            jedisClient.expire(ITEM_INFO + ":" + tbItem.getId() + ":DESC", 0);
        } catch (Exception e) {
            logger.error("删除Redis缓存(商品信息)失败", e);
        }

        // 发送MQ消息同步搜索
        final long itemId = tbItem.getId();
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(itemId + "");
            }
        });

        return TaotaoResult.ok();
    }

    /**
     * 删除商品
     * 将指定商品的状态设置为3（删除）
     * 
     * @param ids 商品ID列表，逗号分隔
     * @return 操作结果
     */
    @Override
    public TaotaoResult deleteItem(String ids) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TbItem tbItem = new TbItem();
            tbItem.setId(Long.parseLong(id));
            tbItem.setStatus((byte) 3);
            tbItem.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(tbItem);

            try {
                jedisClient.expire(ITEM_INFO + ":" + id + ":BASE", 0);
                jedisClient.expire(ITEM_INFO + ":" + id + ":DESC", 0);
            } catch (Exception e) {
                logger.error("删除Redis缓存(商品信息)失败", e);
            }
        }
        return TaotaoResult.ok();
    }

    /**
     * 下架商品
     * 将指定商品的状态设置为2（下架）
     * 
     * @param ids 商品ID列表，逗号分隔
     * @return 操作结果
     */
    @Override
    public TaotaoResult instock(String ids) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TbItem tbItem = new TbItem();
            tbItem.setId(Long.parseLong(id));
            tbItem.setStatus((byte) 2);
            tbItem.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(tbItem);

            try {
                jedisClient.expire(ITEM_INFO + ":" + id + ":BASE", 0);
            } catch (Exception e) {
                logger.error("删除Redis缓存(商品基础信息)失败", e);
            }
        }
        return TaotaoResult.ok();
    }

    /**
     * 上架商品
     * 将指定商品的状态设置为1（正常）
     * 
     * @param ids 商品ID列表，逗号分隔
     * @return 操作结果
     */
    @Override
    public TaotaoResult reshelf(String ids) {
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TbItem tbItem = new TbItem();
            tbItem.setId(Long.parseLong(id));
            tbItem.setStatus((byte) 1);
            tbItem.setUpdated(new Date());
            itemMapper.updateByPrimaryKeySelective(tbItem);

            try {
                jedisClient.expire(ITEM_INFO + ":" + id + ":BASE", 0);
            } catch (Exception e) {
                logger.error("删除Redis缓存(商品基础信息)失败", e);
            }
        }
        return TaotaoResult.ok();
    }

    /**
     * 获取商品描述
     * 
     * @param itemId 商品ID
     * @return 包含商品描述的TaotaoResult
     */
    @Override
    public TaotaoResult getItemDesc(long itemId) {
        TbItemDesc itemDesc = getItemDescById(itemId);
        return TaotaoResult.ok(itemDesc);
    }

    /**
     * 获取商品规格参数
     * 
     * @param itemId 商品ID
     * @return 包含商品规格参数的TaotaoResult
     */
    @Override
    public TaotaoResult getItemParam(long itemId) {
        TbItemParamItem itemParam = getItemParamById(itemId);
        return TaotaoResult.ok(itemParam);
    }

    /**
     * 插入商品描述信息
     * 
     * @param itemId 商品ID
     * @param desc 商品描述内容
     */
    private void insertItemDesc(long itemId, String desc) {
        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        itemDescMapper.insert(itemDesc);
    }
}

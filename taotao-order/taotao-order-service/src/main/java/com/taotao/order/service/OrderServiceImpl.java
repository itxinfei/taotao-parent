package com.taotao.order.service;

import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbOrderMapper;

/**
 * 订单服务实现类
 * 处理订单创建、查询、状态更新等核心逻辑
 * 订单号使用Redis的incr命令生成，保证分布式环境唯一
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 订单状态：未付款
     */
    private static final Integer ORDER_STATUS_UNPAID = 1;

    /**
     * 订单状态：已付款
     */
    private static final Integer ORDER_STATUS_PAID = 2;

    /**
     * 订单状态：已发货
     */
    private static final Integer ORDER_STATUS_SHIPPED = 4;

    /**
     * 订单状态：已关闭
     */
    private static final Integer ORDER_STATUS_CLOSED = 6;

    /**
     * 订单Mapper，用于订单数据库操作
     */
    @Autowired
    private TbOrderMapper tbOrderMapper;

    @Autowired
    private TbOrderItemMapper tbOrderItemMapper;

    @Autowired
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;

    @Value("${ORDER_ID_BEGIN_VALUE}")
    private String ORDER_ID_BEGIN_VALUE;

    @Value("${ORDER_ITEM_ID_GEN_KEY}")
    private String ORDER_ITEM_ID_GEN_KEY;

    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
            jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_BEGIN_VALUE);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        orderInfo.setOrderId(orderId);
        orderInfo.setStatus(ORDER_STATUS_UNPAID);
        orderInfo.setCreateTime(new Date());
        orderInfo.setUpdateTime(new Date());
        tbOrderMapper.insert(orderInfo);

        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            String oid = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();
            tbOrderItem.setId(oid);
            tbOrderItem.setOrderId(orderId);
            tbOrderItemMapper.insert(tbOrderItem);
        }

        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        tbOrderShippingMapper.insert(orderShipping);

        return TaotaoResult.ok(orderId);
    }

    @Override
    public EasyUIDataGridResult getOrderList(Long userId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        TbOrderExample example = new TbOrderExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("create_time desc");

        List<TbOrder> list = tbOrderMapper.selectByExample(example);
        PageInfo<TbOrder> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public EasyUIDataGridResult getAllOrders(Integer page, Integer rows, Integer status) {
        PageHelper.startPage(page, rows);

        TbOrderExample example = new TbOrderExample();
        if (status != null) {
            example.createCriteria().andStatusEqualTo(status);
        }
        example.setOrderByClause("create_time desc");

        List<TbOrder> list = tbOrderMapper.selectByExample(example);
        PageInfo<TbOrder> pageInfo = new PageInfo<>(list);

        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public TaotaoResult updateOrderStatus(String orderId, Integer status, String shippingName, String shippingCode) {
        TbOrder order = tbOrderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return TaotaoResult.build(400, "订单不存在");
        }

        order.setStatus(status);
        order.setUpdateTime(new Date());

        if (status == 4) {
            order.setShippingName(shippingName);
            order.setShippingCode(shippingCode);
            order.setConsignTime(new Date());
        }

        tbOrderMapper.updateByPrimaryKeySelective(order);
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getOrderDetail(String orderId) {
        TbOrder order = tbOrderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            return TaotaoResult.build(400, "订单不存在");
        }

        TbOrderItemExample itemExample = new TbOrderItemExample();
        itemExample.createCriteria().andOrderIdEqualTo(orderId);
        List<TbOrderItem> orderItems = tbOrderItemMapper.selectByExample(itemExample);

        TbOrderShipping orderShipping = tbOrderShippingMapper.selectByPrimaryKey(orderId);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(order.getOrderId());
        orderInfo.setPayment(order.getPayment());
        orderInfo.setPaymentType(order.getPaymentType());
        orderInfo.setPostFee(order.getPostFee());
        orderInfo.setStatus(order.getStatus());
        orderInfo.setCreateTime(order.getCreateTime());
        orderInfo.setUpdateTime(order.getUpdateTime());
        orderInfo.setPaymentTime(order.getPaymentTime());
        orderInfo.setConsignTime(order.getConsignTime());
        orderInfo.setEndTime(order.getEndTime());
        orderInfo.setCloseTime(order.getCloseTime());
        orderInfo.setShippingName(order.getShippingName());
        orderInfo.setShippingCode(order.getShippingCode());
        orderInfo.setUserId(order.getUserId());
        orderInfo.setBuyerMessage(order.getBuyerMessage());
        orderInfo.setBuyerNick(order.getBuyerNick());
        orderInfo.setBuyerRate(order.getBuyerRate());
        orderInfo.setOrderItems(orderItems);
        orderInfo.setOrderShipping(orderShipping);

        return TaotaoResult.ok(orderInfo);
    }
}

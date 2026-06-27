package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private TbOrderMapper tbOrderMapper;

    @Mock
    private TbOrderItemMapper tbOrderItemMapper;

    @Mock
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Mock
    private JedisClient jedisClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        java.lang.reflect.Field genKey = OrderServiceImpl.class.getDeclaredField("ORDER_ID_GEN_KEY");
        genKey.setAccessible(true);
        genKey.set(orderService, "ORDER_ID_GEN_KEY");
        java.lang.reflect.Field beginValue = OrderServiceImpl.class.getDeclaredField("ORDER_ID_BEGIN_VALUE");
        beginValue.setAccessible(true);
        beginValue.set(orderService, "ORDER_ID_BEGIN_VALUE");
        java.lang.reflect.Field itemGenKey = OrderServiceImpl.class.getDeclaredField("ORDER_ITEM_ID_GEN_KEY");
        itemGenKey.setAccessible(true);
        itemGenKey.set(orderService, "ORDER_ITEM_ID_GEN_KEY");
    }

    @Test
    public void testCreateOrderWithExistingRedisKey() {
        when(jedisClient.exists("ORDER_ID_GEN_KEY")).thenReturn(true);
        when(jedisClient.incr("ORDER_ID_GEN_KEY")).thenReturn(10001L);
        when(jedisClient.incr("ORDER_ITEM_ID_GEN_KEY")).thenReturn(1L);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayment("100.00");
        orderInfo.setPaymentType(1);
        orderInfo.setUserId(1L);
        orderInfo.setBuyerNick("test_user");
        orderInfo.setBuyerMessage("请尽快发货");

        List<TbOrderItem> orderItems = new ArrayList<>();
        TbOrderItem item = new TbOrderItem();
        item.setItemId("12345");
        item.setNum(1);
        item.setPrice(10000L);
        orderItems.add(item);
        orderInfo.setOrderItems(orderItems);

        TbOrderShipping shipping = new TbOrderShipping();
        shipping.setReceiverName("张三");
        shipping.setReceiverPhone("13800138000");
        orderInfo.setOrderShipping(shipping);

        TaotaoResult result = orderService.createOrder(orderInfo);

        assertEquals(Integer.valueOf(200), result.getStatus());
        assertEquals("10001", result.getData().toString());
        verify(tbOrderMapper, times(1)).insert(orderInfo);
        verify(tbOrderItemMapper, times(1)).insert(any(TbOrderItem.class));
        verify(tbOrderShippingMapper, times(1)).insert(any(TbOrderShipping.class));
        assertEquals("OrderId should be set", "10001", orderInfo.getOrderId());
        assertEquals("Status should be 1 (unpaid)", Integer.valueOf(1), orderInfo.getStatus());
    }

    @Test
    public void testCreateOrderWithoutExistingRedisKey() {
        when(jedisClient.exists("ORDER_ID_GEN_KEY")).thenReturn(false);
        when(jedisClient.incr("ORDER_ID_GEN_KEY")).thenReturn(1L);
        when(jedisClient.incr("ORDER_ITEM_ID_GEN_KEY")).thenReturn(1L);

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setPayment("50.00");
        orderInfo.setPaymentType(2);

        TbOrderShipping shipping = new TbOrderShipping();
        orderInfo.setOrderShipping(shipping);
        orderInfo.setOrderItems(new ArrayList<TbOrderItem>());

        TaotaoResult result = orderService.createOrder(orderInfo);

        assertEquals(Integer.valueOf(200), result.getStatus());
        verify(jedisClient, times(1)).set("ORDER_ID_GEN_KEY", "ORDER_ID_BEGIN_VALUE");
    }
}

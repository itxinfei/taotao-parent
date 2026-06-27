package com.taotao.order.service;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * 订单服务接口
 * <p>
 * 提供订单创建、查询等核心功能。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface OrderService {

    /**
     * 创建订单
     * <p>
     * 生成订单号（Redis incr）、插入订单主表/明细表/物流表。
     * </p>
     *
     * @param orderInfo 订单信息
     * @return 包含订单号的结果
     */
    TaotaoResult createOrder(OrderInfo orderInfo);

    /**
     * 根据用户ID分页查询订单列表
     *
     * @param userId 用户ID
     * @param page   页码
     * @param rows   每页记录数
     * @return 分页结果
     */
    EasyUIDataGridResult getOrderList(Long userId, Integer page, Integer rows);

    /**
     * 根据订单ID查询订单详情
     * <p>
     * 包含订单商品明细和物流信息。
     * </p>
     *
     * @param orderId 订单ID
     * @return 订单详情（OrderInfo对象）
     */
    TaotaoResult getOrderDetail(String orderId);

    /**
     * 分页查询所有订单（后台管理用）
     *
     * @param page   页码
     * @param rows   每页记录数
     * @param status 订单状态（可选，传null查询全部）
     * @return 分页结果
     */
    EasyUIDataGridResult getAllOrders(Integer page, Integer rows, Integer status);

    /**
     * 更新订单状态（后台管理用）
     *
     * @param orderId      订单ID
     * @param status       新状态
     * @param shippingName 物流公司名
     * @param shippingCode 物流单号
     * @return 操作结果
     */
    TaotaoResult updateOrderStatus(String orderId, Integer status, String shippingName, String shippingCode);
}

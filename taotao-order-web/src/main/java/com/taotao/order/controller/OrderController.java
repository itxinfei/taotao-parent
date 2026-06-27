package com.taotao.order.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
import com.taotao.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单Controller
 * <p>
 * 处理订单确认页展示、订单创建、订单列表和订单详情等核心流程，
 * 用户信息由{@link com.taotao.order.interceptor.LoginInterceptor LoginInterceptor}提供。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class OrderController {

    /** 购物车在Cookie中的Key */
    @Value("${CART_KEY}")
    private String CART_KEY;

    /** 订单服务 */
    @Resource
    private OrderService orderService;

    /**
     * 展示订单确认页面
     * <p>
     * 从Cookie中读取购物车商品列表，展示在订单确认页面供用户核对。
     * 此接口要求用户必须已登录（由LoginInterceptor拦截器保证）。
     * </p>
     *
     * @param request HTTP请求
     * @return 订单确认页面
     */
    @RequestMapping("/order/order-cart")
    public String showOrderCart(HttpServletRequest request) {
        List<TbItem> cartList = getCartItemList(request);
        request.setAttribute("cartList", cartList);
        return "order-cart";
    }

    /**
     * 从Cookie中获取购物车商品列表
     *
     * @param request HTTP请求
     * @return 购物车商品列表，Cookie中无数据则返回空列表
     */
    private List<TbItem> getCartItemList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)) {
            return new ArrayList<TbItem>();
        }
        return JSON.parseArray(json, TbItem.class);
    }

    /**
     * 创建订单
     * <p>
     * 1. 从request中获取由LoginInterceptor存入的用户信息<br>
     * 2. 调用订单服务生成订单<br>
     * 3. 清空购物车Cookie<br>
     * 4. 返回订单成功页面，包含订单号、支付金额和预计送达日期
     * </p>
     *
     * @param orderInfo 订单信息（包含商品明细、收货地址、支付方式等）
     * @param model     数据模型
     * @param request   HTTP请求
     * @param response  HTTP响应，用于清空购物车Cookie
     * @return 订单成功页面
     */
    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model, HttpServletRequest request,
                              HttpServletResponse response) {
        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        if (user != null) {
            orderInfo.setUserId(user.getId());
            orderInfo.setBuyerNick(user.getUsername());
        }

        TaotaoResult result = orderService.createOrder(orderInfo);

        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, CART_KEY, "[]", 0, true);
        }

        model.addAttribute("orderId", result.getData().toString());
        model.addAttribute("payment", orderInfo.getPayment());

        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        model.addAttribute("date", dateTime.toString("yyyy-MM-dd"));

        return "success";
    }

    /**
     * 展示用户订单列表
     * <p>
     * 获取当前登录用户的分页订单列表，按创建时间倒序排列。
     * </p>
     *
     * @param page    页码，默认第1页
     * @param rows    每页记录数，默认10条
     * @param request HTTP请求
     * @param model   数据模型
     * @return 订单列表页面
     */
    @RequestMapping("/order/list")
    public String showOrderList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer rows,
                                HttpServletRequest request, Model model) {
        TbUser user = (TbUser) request.getAttribute("USER_INFO");
        if (user == null) {
            return "redirect:/page/login";
        }

        EasyUIDataGridResult result = orderService.getOrderList(user.getId(), page, rows);
        model.addAttribute("orderList", result.getRows());
        model.addAttribute("totalPages", (result.getTotal() + rows - 1) / rows);
        model.addAttribute("page", page);
        return "order-list";
    }

    /**
     * 展示订单详情
     * <p>
     * 根据订单ID查询订单基本信息、商品明细和物流信息。
     * </p>
     *
     * @param orderId 订单ID
     * @param model   数据模型
     * @return 订单详情页面
     */
    @RequestMapping("/order/detail/{orderId}")
    public String showOrderDetail(@PathVariable String orderId, Model model) {
        TaotaoResult result = orderService.getOrderDetail(orderId);
        if (result.getStatus() != 200) {
            model.addAttribute("message", "订单不存在");
            return "error/exception";
        }

        OrderInfo orderInfo = (OrderInfo) result.getData();

        // 订单状态映射
        String statusText = getStatusText(orderInfo.getStatus());
        model.addAttribute("order", orderInfo);
        model.addAttribute("statusText", statusText);
        model.addAttribute("orderItems", orderInfo.getOrderItems());
        model.addAttribute("orderShipping", orderInfo.getOrderShipping());
        return "order-detail";
    }

    /**
     * 后台管理-订单列表页
     *
     * @param page  页码
     * @param rows  每页记录数
     * @param status 订单状态（可选）
     * @param model 数据模型
     * @return 订单列表页
     */
    @RequestMapping("/admin/order/list/view")
    public String adminOrderListView(@RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "15") Integer rows,
                                      Integer status, Model model) {
        EasyUIDataGridResult result = orderService.getAllOrders(page, rows, status);
        model.addAttribute("orderList", result.getRows());
        model.addAttribute("totalPages", (result.getTotal() + rows - 1) / rows);
        model.addAttribute("page", page);
        return "admin-order-list";
    }

    /**
     * 后台管理-订单详情页
     *
     * @param orderId 订单ID
     * @param model   数据模型
     * @return 订单详情页
     */
    @RequestMapping("/admin/order/detail/{orderId}/view")
    public String adminOrderDetailView(@PathVariable String orderId, Model model) {
        TaotaoResult result = orderService.getOrderDetail(orderId);
        if (result.getStatus() == 200) {
            OrderInfo orderInfo = (OrderInfo) result.getData();
            model.addAttribute("order", orderInfo);
            model.addAttribute("orderItems", orderInfo.getOrderItems());
            model.addAttribute("orderShipping", orderInfo.getOrderShipping());
        }
        return "admin-order-detail";
    }

    /**
     * 后台管理-发货页面
     *
     * @param orderId 订单ID
     * @param model   数据模型
     * @return 发货页面
     */
    @RequestMapping("/admin/order/ship/view")
    public String adminShipView(String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "admin-order-ship";
    }

    /**
     * 后台管理-分页查询订单列表JSON
     *
     * @param page   页码
     * @param rows   每页记录数
     * @param status 订单状态（可选）
     * @return EasyUI分页结果
     */
    @RequestMapping("/admin/order/list")
    @ResponseBody
    public EasyUIDataGridResult adminOrderList(@RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "30") Integer rows,
                                                Integer status) {
        return orderService.getAllOrders(page, rows, status);
    }

    /**
     * 后台管理-查询订单详情JSON
     *
     * @param orderId 订单ID
     * @return 订单详情
     */
    @RequestMapping("/admin/order/detail/{orderId}")
    @ResponseBody
    public TaotaoResult adminOrderDetail(@PathVariable String orderId) {
        return orderService.getOrderDetail(orderId);
    }

    /**
     * 后台管理-发货
     *
     * @param orderId      订单ID
     * @param shippingName 物流公司名
     * @param shippingCode 物流单号
     * @return 操作结果
     */
    @RequestMapping(value = "/admin/order/ship", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult adminShipOrder(String orderId, String shippingName, String shippingCode) {
        return orderService.updateOrderStatus(orderId, 4, shippingName, shippingCode);
    }

    /**
     * 后台管理-关闭订单
     *
     * @param orderId 订单ID
     * @return 操作结果
     */
    @RequestMapping(value = "/admin/order/close", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult adminCloseOrder(String orderId) {
        return orderService.updateOrderStatus(orderId, 6, null, null);
    }

    /**
     * 将订单状态码转换为中文文本
     *
     * @param status 状态码
     * @return 中文状态描述
     */
    private String getStatusText(Integer status) {
        if (status == null) {
            return "未知";
        }
        switch (status) {
            case 1:
                return "未付款";
            case 2:
                return "已付款";
            case 3:
                return "未发货";
            case 4:
                return "已发货";
            case 5:
                return "交易成功";
            case 6:
                return "交易关闭";
            default:
                return "未知";
        }
    }
}

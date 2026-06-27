<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>订单详情 - 淘淘商城</title>
<style>
body { padding: 20px; font-family: "Microsoft YaHei", sans-serif; }
.section { margin-bottom: 20px; }
.section h4 { border-bottom: 1px solid #ddd; padding-bottom: 8px; margin-bottom: 15px; }
.info-table td { padding: 6px 10px; }
.info-table td.label-td { font-weight: bold; width: 120px; color: #666; }
table { width: 100%; border-collapse: collapse; margin-bottom: 15px; }
table th, table td { border: 1px solid #ddd; padding: 8px; text-align: center; }
table th { background: #f5f5f5; }
.info-table td { text-align: left; }
.btn { display: inline-block; padding: 6px 16px; margin: 2px; font-size: 14px; text-decoration: none; border-radius: 3px; color: #fff; }
.btn-default { background: #ccc; color: #333; }
.btn-success { background: #5cb85c; }
.alert { padding: 15px; border: 1px solid transparent; border-radius: 4px; }
.alert-warning { color: #8a6d3b; background: #fcf8e3; border-color: #faebcc; }
</style>
</head>
<body>
<div class="container-fluid">
    <h3>订单详情</h3>
    <a href="/admin/order/list/view.html" class="btn btn-default" style="margin-bottom:15px;">&laquo; 返回列表</a>

    <c:if test="${empty order}">
        <div class="alert alert-warning">订单不存在</div>
    </c:if>

    <c:if test="${not empty order}">
    <div class="section">
        <h4>基本信息</h4>
        <table class="info-table">
            <tr><td class="label-td">订单号：</td><td>${order.orderId}</td></tr>
            <tr><td class="label-td">订单状态：</td><td>
                <c:choose>
                    <c:when test="${order.status==1}">未付款</c:when>
                    <c:when test="${order.status==2}">已付款</c:when>
                    <c:when test="${order.status==3}">未发货</c:when>
                    <c:when test="${order.status==4}">已发货</c:when>
                    <c:when test="${order.status==5}">交易成功</c:when>
                    <c:when test="${order.status==6}">交易关闭</c:when>
                </c:choose>
            </td></tr>
            <tr><td class="label-td">实付金额：</td><td>￥<fmt:formatNumber value="${order.payment/100}" pattern="0.00"/></td></tr>
            <tr><td class="label-td">支付方式：</td><td>${order.paymentType==1?'货到付款':'在线支付'}</td></tr>
            <tr><td class="label-td">买家昵称：</td><td>${order.buyerNick}</td></tr>
            <tr><td class="label-td">买家留言：</td><td>${order.buyerMessage}</td></tr>
            <tr><td class="label-td">下单时间：</td><td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
            <c:if test="${not empty order.paymentTime}">
            <tr><td class="label-td">付款时间：</td><td><fmt:formatDate value="${order.paymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
            </c:if>
            <c:if test="${not empty order.consignTime}">
            <tr><td class="label-td">发货时间：</td><td><fmt:formatDate value="${order.consignTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
            </c:if>
            <c:if test="${not empty order.endTime}">
            <tr><td class="label-td">完成时间：</td><td><fmt:formatDate value="${order.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
            </c:if>
        </table>
    </div>

    <div class="section">
        <h4>商品明细</h4>
        <table>
            <thead><tr><th>商品ID</th><th>商品标题</th><th>单价</th><th>数量</th><th>小计</th></tr></thead>
            <tbody>
                <c:forEach items="${orderItems}" var="item">
                <tr>
                    <td>${item.itemId}</td>
                    <td>${item.title}</td>
                    <td>￥<fmt:formatNumber value="${item.price/100}" pattern="0.00"/></td>
                    <td>${item.num}</td>
                    <td>￥<fmt:formatNumber value="${item.totalFee/100}" pattern="0.00"/></td>
                </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="section">
        <h4>收货信息</h4>
        <c:if test="${not empty orderShipping}">
        <table class="info-table">
            <tr><td class="label-td">收货人：</td><td>${orderShipping.receiverName}</td></tr>
            <tr><td class="label-td">手机号：</td><td>${orderShipping.receiverPhone}</td></tr>
            <tr><td class="label-td">收货地址：</td><td>${orderShipping.receiverState} ${orderShipping.receiverCity} ${orderShipping.receiverDistrict} ${orderShipping.receiverAddress}</td></tr>
            <tr><td class="label-td">物流公司：</td><td>${orderShipping.shippingName}</td></tr>
            <tr><td class="label-td">物流单号：</td><td>${orderShipping.shippingCode}</td></tr>
        </table>
        </c:if>
    </div>

    <div style="margin-top:20px;">
        <c:if test="${order.status==3}">
            <a href="/admin/order/ship/view.html?orderId=${order.orderId}" class="btn btn-success">发货</a>
        </c:if>
    </div>
    </c:if>
</div>
</body>
</html>
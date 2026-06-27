<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>订单管理 - 淘淘商城</title>
<style>
body { padding: 20px; font-family: "Microsoft YaHei", sans-serif; }
table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
table th { background: #f5f5f5; text-align: center; border: 1px solid #ddd; padding: 8px; }
table td { text-align: center; border: 1px solid #ddd; padding: 8px; }
table tr:hover { background: #f9f9f9; }
.btn { display: inline-block; padding: 4px 10px; margin: 0 2px; font-size: 12px; text-decoration: none; border-radius: 3px; color: #fff; }
.btn-info { background: #5bc0de; }
.btn-success { background: #5cb85c; }
.btn-warning { background: #f0ad4e; }
.btn-primary { background: #337ab7; color: #fff; border: none; padding: 6px 16px; border-radius: 3px; cursor: pointer; }
.pagination { margin: 0; padding: 0; list-style: none; }
.pagination li { display: inline; }
.pagination li a { display: inline-block; padding: 6px 12px; border: 1px solid #ddd; text-decoration: none; color: #337ab7; }
.pagination li.active a { background: #337ab7; color: #fff; border-color: #337ab7; }
.pagination li.disabled a { color: #ccc; pointer-events: none; }
.search-box { margin-bottom: 20px; }
.form-control { padding: 6px 12px; border: 1px solid #ddd; border-radius: 3px; }
</style>
</head>
<body>
<div class="container-fluid">
    <h3>订单管理</h3>
    <div class="search-box">
        <form class="form-inline" action="/admin/order/list/view.html" method="get">
            <div class="form-group">
                <select name="status" class="form-control">
                    <option value="">全部状态</option>
                    <option value="1" ${param.status=='1'?'selected':''}>未付款</option>
                    <option value="2" ${param.status=='2'?'selected':''}>已付款</option>
                    <option value="3" ${param.status=='3'?'selected':''}>未发货</option>
                    <option value="4" ${param.status=='4'?'selected':''}>已发货</option>
                    <option value="5" ${param.status=='5'?'selected':''}>交易成功</option>
                    <option value="6" ${param.status=='6'?'selected':''}>交易关闭</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
    </div>
    <table>
        <thead>
            <tr>
                <th>订单号</th>
                <th>实付金额</th>
                <th>支付方式</th>
                <th>买家昵称</th>
                <th>状态</th>
                <th>下单时间</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${orderList}" var="order">
            <tr>
                <td>${order.orderId}</td>
                <td>￥<fmt:formatNumber value="${order.payment/100}" pattern="0.00"/></td>
                <td>${order.paymentType==1?'货到付款':'在线支付'}</td>
                <td>${order.buyerNick}</td>
                <td>
                    <c:choose>
                        <c:when test="${order.status==1}">未付款</c:when>
                        <c:when test="${order.status==2}">已付款</c:when>
                        <c:when test="${order.status==3}">未发货</c:when>
                        <c:when test="${order.status==4}">已发货</c:when>
                        <c:when test="${order.status==5}">交易成功</c:when>
                        <c:when test="${order.status==6}">交易关闭</c:when>
                    </c:choose>
                </td>
                <td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <a href="/admin/order/detail/${order.orderId}/view.html" class="btn btn-info">详情</a>
                    <c:if test="${order.status==3}">
                        <a href="/admin/order/ship/view.html?orderId=${order.orderId}" class="btn btn-success">发货</a>
                    </c:if>
                    <c:if test="${order.status<6 && order.status>=1}">
                        <a href="javascript:closeOrder('${order.orderId}')" class="btn btn-warning">关闭</a>
                    </c:if>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty orderList}">
            <tr><td colspan="7">暂无订单数据</td></tr>
            </c:if>
        </tbody>
    </table>
    <nav>
        <ul class="pagination">
            <li ${page<=1?'class="disabled"':''}><a href="/admin/order/list/view.html?page=${page-1}&status=${param.status}">&laquo;</a></li>
            <c:forEach begin="1" end="${totalPages}" var="i">
            <li ${page==i?'class="active"':''}><a href="/admin/order/list/view.html?page=${i}&status=${param.status}">${i}</a></li>
            </c:forEach>
            <li ${page>=totalPages?'class="disabled"':''}><a href="/admin/order/list/view.html?page=${page+1}&status=${param.status}">&raquo;</a></li>
        </ul>
    </nav>
</div>
<script src="/js/jquery-1.6.4.js"></script>
<script>
function closeOrder(orderId){
    if(confirm('确认关闭订单 '+orderId+' 吗？')){
        $.post('/admin/order/close.html', {orderId:orderId}, function(data){
            if(data.status == 200){
                alert('订单已关闭');
                location.reload();
            }
        });
    }
}
</script>
</body>
</html>
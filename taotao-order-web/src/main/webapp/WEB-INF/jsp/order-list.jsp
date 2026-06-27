<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta name="format-detection" content="telephone=no" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>我的订单 -淘淘商城</title>
<link rel="stylesheet" type="text/css" href="/css/base.css" media="all" />
<link type="text/css" rel="stylesheet" href="/css/order-commons.css" source="widget" />
<script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="/js/base.js"></script>
</head>
<body id="mainframe">
<jsp:include page="commons/shortcut.jsp" />
<div class="w w1 header clearfix">
	<div id="logo"><a href="/"><img src="/images/taotao-logo.gif" alt="淘淘商城"></a></div>
</div>
<div class="w main">
	<div class="m m3 msop">
		<div class="mt"><h3>我的订单</h3></div>
		<div class="mc">
			<c:choose>
				<c:when test="${empty orderList}">
					<div style="text-align:center;padding:50px 0;">
						<p style="font-size:14px;color:#999;">您还没有任何订单</p>
						<p style="margin-top:20px;"><a href="http://localhost:8085" style="color:#E4393C;">去逛逛 &gt;&gt;</a></p>
					</div>
				</c:when>
				<c:otherwise>
					<table style="width:100%;border-collapse:collapse;margin-top:10px;">
						<tr style="background:#f7f7f7;height:35px;text-align:center;font-weight:bold;">
							<td style="width:25%;">订单编号</td>
							<td style="width:15%;">订单金额</td>
							<td style="width:15%;">订单状态</td>
							<td style="width:25%;">下单时间</td>
							<td style="width:20%;">操作</td>
						</tr>
						<c:forEach items="${orderList}" var="order">
							<tr style="height:45px;text-align:center;border-bottom:1px solid #eee;">
								<td><a href="/order/detail/${order.orderId}.html" style="color:#005ea7;">${order.orderId}</a></td>
								<td style="color:#E4393C;">￥<fmt:formatNumber groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" value="${order.payment}"/></td>
								<td>
									<c:choose>
										<c:when test="${order.status == 1}">未付款</c:when>
										<c:when test="${order.status == 2}">已付款</c:when>
										<c:when test="${order.status == 3}">未发货</c:when>
										<c:when test="${order.status == 4}">已发货</c:when>
										<c:when test="${order.status == 5}">交易成功</c:when>
										<c:when test="${order.status == 6}">交易关闭</c:when>
										<c:otherwise>未知</c:otherwise>
									</c:choose>
								</td>
								<td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td><a href="/order/detail/${order.orderId}.html" style="color:#005ea7;">查看详情</a></td>
							</tr>
						</c:forEach>
					</table>
					<div style="margin-top:20px;text-align:right;padding-right:20px;">
						<span style="color:#999;">
							共${totalPages}页&nbsp;&nbsp;当前第${page}页&nbsp;&nbsp;
							<c:if test="${page > 1}">
								<a href="/order/list.html?page=${page-1}" style="color:#005ea7;">上一页</a>&nbsp;
							</c:if>
							<c:if test="${page < totalPages}">
								<a href="/order/list.html?page=${page+1}" style="color:#005ea7;">下一页</a>
							</c:if>
						</span>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
<jsp:include page="commons/footer.jsp" />
</body>
</html>

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
<title>订单详情 -淘淘商城</title>
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
		<div class="mt">
			<h3>订单详情</h3>
			<span style="float:right;margin-top:-25px;margin-right:10px;">
				<a href="/order/list.html" style="color:#005ea7;">&laquo; 返回订单列表</a>
			</span>
		</div>
		<div class="mc">
			<div style="padding:15px 20px;border-bottom:1px solid #eee;">
				<p><strong>订单编号：</strong>${order.orderId}</p>
				<p><strong>订单状态：</strong><span style="color:#E4393C;">${statusText}</span></p>
				<p><strong>下单时间：</strong><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></p>
				<p><strong>订单金额：</strong><span style="color:#E4393C;font-size:16px;font-weight:bold;">￥<fmt:formatNumber groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" value="${order.payment}"/></span></p>
			</div>

			<h4 style="padding:10px 20px;background:#f7f7f7;margin:0;border-bottom:1px solid #eee;">商品信息</h4>
			<table style="width:100%;border-collapse:collapse;">
				<tr style="height:35px;text-align:center;font-weight:bold;background:#f7f7f7;">
					<td style="width:10%;">商品图片</td>
					<td style="width:40%;">商品名称</td>
					<td style="width:15%;">单价</td>
					<td style="width:10%;">数量</td>
					<td style="width:15%;">小计</td>
				</tr>
				<c:forEach items="${orderItems}" var="item">
					<tr style="height:80px;text-align:center;border-bottom:1px solid #eee;">
						<td><img src="${item.picPath}" width="60" height="60" style="border:1px solid #ddd;" /></td>
						<td style="text-align:left;padding-left:20px;">
							<a href="http://localhost:8085/item/${item.itemId}.html" style="color:#005ea7;">${item.title}</a>
						</td>
						<td>￥<fmt:formatNumber groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" value="${item.price / 100}"/></td>
						<td>${item.num}</td>
						<td style="color:#E4393C;">￥<fmt:formatNumber groupingUsed="false" maxFractionDigits="2" minFractionDigits="2" value="${item.totalFee / 100}"/></td>
					</tr>
				</c:forEach>
			</table>

			<c:if test="${not empty orderShipping}">
				<h4 style="padding:10px 20px;background:#f7f7f7;margin:0;border-bottom:1px solid #eee;">收货信息</h4>
				<div style="padding:15px 20px;border-bottom:1px solid #eee;line-height:24px;">
					<p><strong>收货人：</strong>${orderShipping.receiverName}</p>
					<p><strong>手机号码：</strong>${orderShipping.receiverMobile}</p>
					<p><strong>收货地址：</strong>${orderShipping.receiverState}${orderShipping.receiverCity}${orderShipping.receiverDistrict}${orderShipping.receiverAddress}</p>
				</div>
			</c:if>

			<div style="padding:20px;text-align:center;">
				<a href="/order/list.html" style="display:inline-block;padding:8px 30px;background:#E4393C;color:#fff;border-radius:3px;text-decoration:none;">返回订单列表</a>
			</div>
		</div>
	</div>
</div>
<jsp:include page="commons/footer.jsp" />
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>订单发货 - 淘淘商城</title>
<style>
body { padding: 20px; font-family: "Microsoft YaHei", sans-serif; }
.form-group { margin-bottom: 15px; }
.form-group label { display: inline-block; width: 100px; font-weight: bold; text-align: right; margin-right: 10px; }
.form-control { padding: 6px 12px; border: 1px solid #ddd; border-radius: 3px; width: 250px; }
.btn { display: inline-block; padding: 6px 16px; margin: 2px; font-size: 14px; text-decoration: none; border-radius: 3px; cursor: pointer; border: none; }
.btn-primary { background: #337ab7; color: #fff; }
.btn-default { background: #ccc; color: #333; }
</style>
</head>
<body>
<div style="padding:0 20px;">
    <h3>订单发货</h3>
    <form id="shipForm" action="/admin/order/ship.html" method="post" style="width:500px;margin-top:20px;">
        <div class="form-group">
            <label>订单号：</label>
            <span style="padding:6px 0;display:inline-block;">${orderId}</span>
            <input type="hidden" name="orderId" value="${orderId}" />
        </div>
        <div class="form-group">
            <label>物流公司：</label>
            <select name="shippingName" class="form-control" required>
                <option value="">请选择</option>
                <option value="顺丰快递">顺丰快递</option>
                <option value="圆通快递">圆通快递</option>
                <option value="中通快递">中通快递</option>
                <option value="韵达快递">韵达快递</option>
                <option value="申通快递">申通快递</option>
                <option value="EMS">EMS</option>
            </select>
        </div>
        <div class="form-group">
            <label>物流单号：</label>
            <input type="text" name="shippingCode" class="form-control" required />
        </div>
        <div class="form-group" style="padding-left:110px;">
            <button type="submit" class="btn btn-primary">确认发货</button>
            <a href="/admin/order/list/view.html" class="btn btn-default">返回</a>
        </div>
    </form>
</div>
<script>
document.getElementById('shipForm').onsubmit = function(){
    var data = {
        orderId: this.orderId.value,
        shippingName: this.shippingName.value,
        shippingCode: this.shippingCode.value
    };
    var xhr = new XMLHttpRequest();
    xhr.open('POST', this.action, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function(){
        var result = JSON.parse(xhr.responseText);
        if(result.status == 200){
            alert('发货成功');
            location.href = '/admin/order/list/view.html';
        } else {
            alert('发货失败：' + result.msg);
        }
    };
    var params = 'orderId=' + encodeURIComponent(data.orderId) +
                 '&shippingName=' + encodeURIComponent(data.shippingName) +
                 '&shippingCode=' + encodeURIComponent(data.shippingCode);
    xhr.send(params);
    return false;
};
</script>
</body>
</html>
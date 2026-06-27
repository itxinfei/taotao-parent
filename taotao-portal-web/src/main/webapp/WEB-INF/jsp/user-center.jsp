<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>个人中心 - 淘淘商城</title>
<style>
body { background: #f5f5f5; padding: 20px; font-family: "Microsoft YaHei", sans-serif; }
.container { max-width: 800px; margin: 0 auto; }
.user-box { background: #fff; border: 1px solid #e5e5e5; padding: 30px; margin-bottom: 20px; border-radius: 4px; }
.user-box h4 { border-bottom: 1px solid #eee; padding-bottom: 12px; margin-bottom: 20px; color: #333; }
.form-group { margin-bottom: 15px; }
.form-group label { font-weight: normal; color: #666; display: inline-block; width: 80px; text-align: right; margin-right: 10px; }
.form-control { padding: 6px 12px; border: 1px solid #ddd; border-radius: 3px; width: 280px; }
.btn { display: inline-block; padding: 6px 16px; border: none; border-radius: 3px; cursor: pointer; text-decoration: none; }
.btn-primary { background: #337ab7; color: #fff; }
.alert { padding: 15px; border: 1px solid transparent; border-radius: 4px; }
.alert-danger { color: #a94442; background: #f2dede; border-color: #ebccd1; }
.avatar { width: 80px; height: 80px; border-radius: 40px; background: #f0f0f0; display: inline-block; text-align: center; line-height: 80px; font-size: 32px; color: #999; }
.form-control-static { padding: 6px 0; margin: 0; }
</style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="user-box" id="userInfoBox" style="display:none;">
            <div style="text-align:center;margin-bottom:20px;">
                <div class="avatar" id="avatarText">U</div>
                <h3 id="usernameDisplay"></h3>
            </div>
            <h4>基本信息</h4>
            <form id="userForm" onsubmit="return false;">
                <input type="hidden" name="id" id="userId" />
                <div class="form-group">
                    <label>用户名</label>
                    <span class="form-control-static" id="usernameField"></span>
                </div>
                <div class="form-group">
                    <label>手机号</label>
                    <input type="text" class="form-control" name="phone" id="phoneField" />
                </div>
                <div class="form-group">
                    <label>邮箱</label>
                    <input type="email" class="form-control" name="email" id="emailField" />
                </div>
                <div class="form-group" style="padding-left:90px;">
                    <button type="button" class="btn btn-primary" onclick="updateUserInfo()">保存修改</button>
                </div>
            </form>
        </div>
        <div class="alert alert-danger" id="loginPrompt" style="display:none;text-align:center;">
            <h4>您尚未登录</h4>
            <p style="margin-top:10px;"><a href="/page/login.html" class="btn btn-primary">立即登录</a></p>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/jquery-1.6.4.js"></script>
<script type="text/javascript" src="/js/jquery.cookie.js"></script>
<script>
var SSO_URL = "http://localhost:8088";

function loadUserInfo() {
    var token = $.cookie("TT_TOKEN");
    if (!token) {
        $("#loginPrompt").show();
        return;
    }
    $.ajax({
        url: SSO_URL + "/user/token/" + token,
        dataType: "jsonp",
        success: function(data) {
            if (data.status == 200) {
                var user = data.data;
                $("#userId").val(user.id);
                $("#usernameField").text(user.username);
                $("#usernameDisplay").text(user.username);
                $("#phoneField").val(user.phone || '');
                $("#emailField").val(user.email || '');
                $("#avatarText").text(user.username.charAt(0).toUpperCase());
                $("#userInfoBox").show();
            } else {
                $("#loginPrompt").show();
            }
        },
        error: function() {
            $("#loginPrompt").show();
        }
    });
}

function updateUserInfo() {
    var data = {
        id: $("#userId").val(),
        phone: $("#phoneField").val(),
        email: $("#emailField").val()
    };
    $.ajax({
        url: SSO_URL + "/user/update",
        type: "POST",
        data: data,
        dataType: "json",
        success: function(result) {
            if (result.status == 200) {
                alert("保存成功");
            } else {
                alert("保存失败：" + (result.msg || "未知错误"));
            }
        },
        error: function() {
            alert("网络错误");
        }
    });
}

$(function() {
    loadUserInfo();
});
</script>
</body>
</html>
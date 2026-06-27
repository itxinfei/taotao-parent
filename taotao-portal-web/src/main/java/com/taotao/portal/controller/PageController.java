package com.taotao.portal.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面跳转Controller
 * <p>
 * 处理门户系统的登录、注册等页面跳转请求，
 * 将请求转发到SSO单点登录系统对应的页面。
 * 由于门户DispatcherServlet配置为*.html后缀拦截，
 * 所有请求路径必须以此结尾。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class PageController {

    /** SSO单点登录系统URL */
    @Value("${SSO_URL}")
    private String SSO_URL;

    /**
     * 跳转到SSO登录页面
     * <p>
     * 接收当前页面的URL作为回调参数，登录成功后SSO会重定向回该地址，
     * 并在URL后附加token参数用于跨域登录态同步。
     * </p>
     *
     * @param request HTTP请求，用于获取回调URL参数
     * @return 重定向到SSO登录页面
     */
    @RequestMapping("/page/login")
    public String showLogin(HttpServletRequest request) {
        String redirectUrl = request.getParameter("url");
        if (redirectUrl == null) {
            redirectUrl = "http://localhost:8082/index.html";
        }
        return "redirect:" + SSO_URL + "/page/login?url=" + redirectUrl;
    }

    /**
     * 跳转到SSO注册页面
     *
     * @return 重定向到SSO注册页面
     */
    @RequestMapping("/page/register")
    public String showRegister() {
        return "redirect:" + SSO_URL + "/page/register";
    }
}

package com.taotao.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面控制
 */
@Controller
public class PageController {
    /**
     * 登录首页
     * 浏览器：http://localhost:8088/显示登录页面
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {
        return "login";
    }

    /**
     * 跳到用户注册
     *
     * @return
     */
    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }

    /**
     * 跳到用户登录
     *
     * @param url
     * @param model
     * @return
     */
    @RequestMapping("/page/login")
    public String showLogin(String url, Model model) {
        model.addAttribute("redirect", url);
        return "login";
    }
}

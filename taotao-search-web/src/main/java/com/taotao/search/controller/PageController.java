package com.taotao.search.controller;

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
     * 浏览器：http://localhost:8085 显示搜索页面
     *
     * @return
     */
    @RequestMapping("/index")
    public String search() {
        return "search";
    }
}

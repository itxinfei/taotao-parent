package com.taotao.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 搜索页面控制器
 * 负责处理搜索页面的访问请求
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class PageController {

    /**
     * 跳转到搜索首页
     * 访问地址：http://localhost:8085/index
     * 
     * @param model Spring MVC模型对象，用于传递数据到视图
     * @return 视图名称，对应 /WEB-INF/jsp/search.jsp
     */
    @RequestMapping("/index")
    public String search(Model model) {
        return "search";
    }
}

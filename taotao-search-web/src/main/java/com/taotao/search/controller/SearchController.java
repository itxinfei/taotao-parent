package com.taotao.search.controller;

import com.taotao.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.common.pojo.SearchResult;

import javax.annotation.Resource;

/**
 * 商品搜索功能
 */
@Controller
public class SearchController {

    @Resource
    private SearchService searchService;

    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1") Integer page, Model model) {
        try {
            queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
            //调用服务执行查询
            SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("itemList", searchResult.getItemList());
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回逻辑视图
        return "search";
    }
}

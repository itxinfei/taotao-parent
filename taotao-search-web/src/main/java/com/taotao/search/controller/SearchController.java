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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商品搜索控制器
 * 处理商品搜索请求，调用搜索服务获取搜索结果并展示到页面
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class SearchController {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    /**
     * 搜索服务，用于执行商品搜索
     */
    @Resource
    private SearchService searchService;

    /**
     * 每页搜索结果数量，配置于properties文件
     */
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    /**
     * 执行商品搜索
     * 接收搜索关键词和页码参数，调用搜索服务获取结果并传递给页面
     * 
     * @param queryString 搜索关键词
     * @param page 页码，默认值为1
     * @param model Spring MVC模型对象，用于传递数据到视图
     * @return 视图名称，对应 /WEB-INF/jsp/search.jsp
     */
    @RequestMapping("/search")
    public String search(@RequestParam("q") String queryString,
                         @RequestParam(defaultValue = "1") Integer page, Model model) {
        try {
            // 处理URL编码问题，将ISO-8859-1编码转换为UTF-8
            queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
            
            // 调用搜索服务执行查询
            SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            
            // 将搜索结果传递给页面
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("itemList", searchResult.getItemList());
            model.addAttribute("page", page);
        } catch (Exception e) {
            logger.error("搜索商品失败", e);
        }
        
        // 返回搜索结果页面
        return "search";
    }
}

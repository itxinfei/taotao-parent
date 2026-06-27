package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 实现数据导入功能
 */
@Controller
public class IndexManagerController {

    private static final Logger logger = LoggerFactory.getLogger(IndexManagerController.class);

    @Resource
    private SearchItemService searchItemService;

    @RequestMapping("/index/import")
    @ResponseBody
    public TaotaoResult importIndex() {
        try {
            TaotaoResult result = searchItemService.importItemsToIndex();
            return result;
        } catch (Exception e) {
            logger.error("导入索引库失败", e);
            return TaotaoResult.build(500, "导入数据失败");
        }

    }
}
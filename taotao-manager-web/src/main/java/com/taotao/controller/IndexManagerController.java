package com.taotao.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 实现数据导入功能
 */
@Controller
public class IndexManagerController {

    @Resource
    private SearchItemService searchItemService;

    @RequestMapping("/index/import")
    @ResponseBody
    public TaotaoResult importIndex() {
        try {
            TaotaoResult result = searchItemService.importItemsToIndex();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "导入数据失败");
        }

    }
}
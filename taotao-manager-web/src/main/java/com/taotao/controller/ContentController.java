package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController
public class ContentController {

    @Reference
    private ContentService contentSerive;

    @RequestMapping("/content/query/list")
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
        EasyUIDataGridResult result = contentSerive.getContentList(categoryId, page, rows);
        return result;
    }

    @RequestMapping("/content/save")
    public TaotaoResult addContent(TbContent content) {
        TaotaoResult result = contentSerive.addContent(content);
        return result;
    }

    @RequestMapping("/rest/content/edit")
    public TaotaoResult updateContent(TbContent content) {
        TaotaoResult result = contentSerive.updateContent(content);
        return result;
    }

    @RequestMapping("/content/delete")
    public TaotaoResult deleteContent(String ids) {
        TaotaoResult result = contentSerive.deleteContent(ids);
        return result;
    }

    @RequestMapping("/content/getContent")
    public TaotaoResult getContent(Long id) {
        TaotaoResult result = contentSerive.getContent(id);
        return result;
    }
}

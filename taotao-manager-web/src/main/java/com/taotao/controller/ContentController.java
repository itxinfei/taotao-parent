package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 内容管理
 */
@RestController
public class ContentController {

    @Resource
    private ContentService contentSerive;

    /**
     * 查询内容列表
     *
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/content/query/list")
    public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows) {
        EasyUIDataGridResult result = contentSerive.getContentList(categoryId, page, rows);
        return result;
    }

    /**
     * 添加内容
     *
     * @param content
     * @return
     */
    @RequestMapping("/content/save")
    public TaotaoResult addContent(TbContent content) {
        TaotaoResult result = contentSerive.addContent(content);
        return result;
    }

    /**
     * 编辑内容
     *
     * @param content
     * @return
     */
    @RequestMapping("/rest/content/edit")
    public TaotaoResult updateContent(TbContent content) {
        TaotaoResult result = contentSerive.updateContent(content);
        return result;
    }

    /**
     * 删除内容
     *
     * @param ids
     * @return
     */
    @RequestMapping("/content/delete")
    public TaotaoResult deleteContent(String ids) {
        TaotaoResult result = contentSerive.deleteContent(ids);
        return result;
    }

    /**
     * 根据ID查询内容
     *
     * @param id
     * @return
     */
    @RequestMapping("/content/getContent")
    public TaotaoResult getContent(Long id) {
        TaotaoResult result = contentSerive.getContent(id);
        return result;
    }
}

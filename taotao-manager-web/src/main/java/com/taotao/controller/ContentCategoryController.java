package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容分类管理
 */
@RestController
public class ContentCategoryController {

    @Resource
    private ContentCategoryService contentCategoryService;

    /**
     * 内容分类列表
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    public List<EasyUITreeNode> getContentCategoryList(
            @RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
        return list;
    }

    /**
     * 创建内容分类
     *
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/content/category/create")
    public TaotaoResult addContentCategory(Long parentId, String name) {
        TaotaoResult taotaoResult = contentCategoryService.addContentCategory(parentId, name);
        return taotaoResult;
    }

    /**
     * 更新内容分类
     *
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/content/category/update")
    public TaotaoResult updateContentCategory(Long id, String name) {
        TaotaoResult taotaoResult = contentCategoryService.updateContentCategory(id, name);
        return taotaoResult;
    }

    /**
     * 删除内容分类
     *
     * @param id
     * @return
     */
    @RequestMapping("/content/category/delete/")
    public TaotaoResult deleteContentCategory(Long id) {
        TaotaoResult taotaoResult = contentCategoryService.deleteContentCategory(id);
        return taotaoResult;
    }
}


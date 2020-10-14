package com.taotao.controller;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类管理
 */
@Controller
public class ItemCatController {

    @Resource
    private ItemCatService itemCatService;

    /**
     * 初始化tree请求的url：/item/cat/list
     * 参数：
     * long id（父节点id）
     * 返回值：json。数据格式
     * List<EasyUITreeNode>
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
        return list;
    }
}

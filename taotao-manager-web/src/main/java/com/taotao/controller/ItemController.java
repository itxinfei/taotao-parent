package com.taotao.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理
 */
@Controller
public class ItemController {

    @Reference
    private ItemService itemService;

    /**
     * 根据ID查询商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId) {
        return this.itemService.getItemById(itemId);
    }

    /**
     * 商品列表
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows) {
        return itemService.getItemList(page, rows);
    }

    /**
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem item, String desc) {
        try {
            TaotaoResult result = itemService.createItem(item, desc);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, "添加商品失败");
        }
    }

    /**
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/item/param/list")
    public EasyUIDataGridResult getItemParamList(int page, int rows) {
        return null;
    }
}

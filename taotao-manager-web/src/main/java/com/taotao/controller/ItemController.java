package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品管理
 * 自己完成商品修改、商品删除、上架下架。
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Resource
    private ItemService itemService;

    /**
     * 根据ID查询商品
     *
     * @param itemId
     * @return
     */
    @RequestMapping("/{itemId}")
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
    @RequestMapping("/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows) {
        return itemService.getItemList(page, rows);
    }

    /**
     * 添加商品
     *
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
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
     * 规格参数
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/param/list")
    @ResponseBody
    public EasyUIDataGridResult getItemParamList(int page, int rows) {
        return itemService.getItemList(page, rows);
    }
}

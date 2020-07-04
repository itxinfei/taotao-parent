package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ResponseBody
    @RequestMapping("/item/{itemId}")
    public TbItem getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @ResponseBody
    @RequestMapping("/item/list")
    public EasyUIDataGridResult getItemList(int page,int rows){
        return itemService.getItemList(page,rows);
    }

    @ResponseBody
    @RequestMapping(value = "item/save",method = RequestMethod.POST)
    public TaotaoResult addItem(TbItem item,String desc){

        try {
            TaotaoResult result = itemService.createItem(item,desc);
            return  result;
        } catch (Exception e) {
            e.printStackTrace();
            return  TaotaoResult.build(500,"添加商品失败");
        }
    }
}

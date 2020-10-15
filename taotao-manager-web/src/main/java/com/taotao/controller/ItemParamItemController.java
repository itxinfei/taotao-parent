package com.taotao.controller;

import com.taotao.service.ItemParamItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 商品规格和商品-展示规格参数
 */
@Controller
public class ItemParamItemController {

    @Resource
    private ItemParamItemService itemParamItemService;

    /**
     * @param itemId
     * @param model
     * @return
     */
    @RequestMapping("/showitem/{itemId}")
    public String showItemParam(@PathVariable Long itemId, Model model) {
        String string = itemParamItemService.getItemParamByItemId(itemId);
        model.addAttribute("itemParam", string);
        return "item";
    }
}

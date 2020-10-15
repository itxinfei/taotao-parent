package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUIResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import com.taotao.service.ItemParamService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品规则参数
 */
@Controller
@RequestMapping("/item/param")
public class ItemParamController {

    @Resource
    private ItemParamService itemParamService;

    /**
     * @param itemCatId
     * @return
     */
    @RequestMapping("/query/itemcatid/{itemCatId}")
    @ResponseBody
    public TaotaoResult getItemParamByCid(@PathVariable Long itemCatId) {
        TaotaoResult result = itemParamService.getItemParamByCid(itemCatId);
        return result;
    }

    /**
     * @param cid
     * @param paramData
     * @return
     */
    @RequestMapping("/save/{cid}")
    @ResponseBody
    public TaotaoResult insertItemParam(@PathVariable Long cid, String paramData) {
        //创建pojo对象
        TbItemParam itemParam = new TbItemParam();
        itemParam.setItemCatId(cid);
        itemParam.setParamData(paramData);
        TaotaoResult result = itemParamService.insertItemParam(itemParam);
        return result;
    }

    /**
     * 规格参数列表
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public EasyUIResult findByList(int page, int rows) {
        EasyUIResult byList = itemParamService.findByList(page, rows);
        return byList;
    }
}

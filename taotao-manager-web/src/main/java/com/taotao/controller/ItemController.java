package com.taotao.controller;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 商品管理控制器
 * 提供商品CRUD操作的REST API接口
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    /**
     * 商品服务，用于处理商品相关业务逻辑
     */
    @Resource
    private ItemService itemService;

    /**
     * 根据商品ID查询商品信息
     * 
     * @param itemId 商品ID
     * @return 商品实体对象
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable long itemId) {
        return this.itemService.getItemById(itemId);
    }

    /**
     * 分页查询商品列表
     * 
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 分页结果，包含商品列表和总记录数
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(int page, int rows) {
        return itemService.getItemList(page, rows);
    }

    /**
     * 添加商品
     * 同时保存商品基础信息和商品描述，并发送消息通知搜索服务和静态页生成服务
     * 
     * @param item 商品基础信息
     * @param desc 商品描述（HTML格式）
     * @return 操作结果
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult addItem(TbItem item, String desc) {
        try {
            TaotaoResult result = itemService.createItem(item, desc);
            return result;
        } catch (Exception e) {
            logger.error("添加商品失败", e);
            return TaotaoResult.build(500, "添加商品失败");
        }
    }

    /**
     * 修改商品信息
     * 
     * @param item 商品基础信息
     * @param desc 商品描述（HTML格式）
     * @return 操作结果
     */
    @RequestMapping(value = "/rest/item/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateItem(TbItem item, String desc) {
        try {
            return itemService.updateItem(item, desc);
        } catch (Exception e) {
            logger.error("修改商品失败", e);
            return TaotaoResult.build(500, "修改商品失败");
        }
    }

    /**
     * 删除商品（支持批量删除）
     * 
     * @param ids 商品ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @RequestMapping(value = "/rest/item/delete", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult deleteItem(String ids) {
        try {
            return itemService.deleteItem(ids);
        } catch (Exception e) {
            logger.error("删除商品失败", e);
            return TaotaoResult.build(500, "删除商品失败");
        }
    }

    /**
     * 下架商品（支持批量下架）
     * 
     * @param ids 商品ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @RequestMapping(value = "/rest/item/instock", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult instock(String ids) {
        try {
            return itemService.instock(ids);
        } catch (Exception e) {
            logger.error("下架商品失败", e);
            return TaotaoResult.build(500, "下架商品失败");
        }
    }

    /**
     * 上架商品（支持批量上架）
     * 
     * @param ids 商品ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    @RequestMapping(value = "/rest/item/reshelf", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult reshelf(String ids) {
        try {
            return itemService.reshelf(ids);
        } catch (Exception e) {
            logger.error("上架商品失败", e);
            return TaotaoResult.build(500, "上架商品失败");
        }
    }

    /**
     * 查询商品描述信息
     * 
     * @param itemId 商品ID
     * @return 操作结果，包含商品描述信息
     */
    @RequestMapping("/rest/item/query/item/desc/{itemId}")
    @ResponseBody
    public TaotaoResult getItemDesc(@PathVariable Long itemId) {
        try {
            return itemService.getItemDesc(itemId);
        } catch (Exception e) {
            logger.error("查询商品描述失败", e);
            return TaotaoResult.build(500, "查询商品描述失败");
        }
    }

    /**
     * 查询商品规格参数
     * 
     * @param itemId 商品ID
     * @return 操作结果，包含商品规格参数信息
     */
    @RequestMapping("/rest/item/param/item/query/{itemId}")
    @ResponseBody
    public TaotaoResult getItemParam(@PathVariable Long itemId) {
        try {
            return itemService.getItemParam(itemId);
        } catch (Exception e) {
            logger.error("查询商品规格参数失败", e);
            return TaotaoResult.build(500, "查询商品规格参数失败");
        }
    }
}

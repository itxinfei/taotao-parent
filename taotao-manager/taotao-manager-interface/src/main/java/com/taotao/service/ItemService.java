package com.taotao.service;


import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;

/**
 * 商品服务接口
 * 提供商品管理相关的业务操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface ItemService {

    /**
     * 根据商品ID获取商品基础信息
     * 优先从Redis缓存中获取，缓存不存在时从数据库查询
     * 
     * @param itemId 商品ID
     * @return 商品实体对象，若不存在返回null
     */
    TbItem getItemById(long itemId);

    /**
     * 根据商品ID获取商品描述信息
     * 优先从Redis缓存中获取，缓存不存在时从数据库查询
     * 
     * @param itemId 商品ID
     * @return 商品描述对象，若不存在返回null
     */
    TbItemDesc getItemDescById(long itemId);

    /**
     * 根据商品ID获取商品规格参数
     * 
     * @param id 商品ID
     * @return 商品规格参数对象，若不存在返回null
     */
    TbItemParamItem getItemParamById(Long id);

    /**
     * 分页查询商品列表
     * 
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 分页结果，包含商品列表和总记录数
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * 添加商品
     * 同时保存商品基础信息、商品描述，并发送消息通知搜索和静态页生成服务
     * 
     * @param tbItem 商品基础信息
     * @param desc 商品描述
     * @return 操作结果，成功返回TaotaoResult.ok()
     * @throws Exception 添加过程中发生异常时抛出
     */
    TaotaoResult createItem(TbItem tbItem, String desc) throws Exception;

    /**
     * 更新商品信息
     * 同时更新商品基础信息和描述，并发送消息通知搜索服务更新索引
     * 
     * @param tbItem 商品基础信息
     * @param desc 商品描述
     * @return 操作结果，成功返回TaotaoResult.ok()
     */
    TaotaoResult updateItem(TbItem tbItem, String desc);

    /**
     * 删除商品
     * 将商品状态标记为删除（status=3）
     * 
     * @param ids 商品ID列表，多个ID用逗号分隔
     * @return 操作结果，成功返回TaotaoResult.ok()
     */
    TaotaoResult deleteItem(String ids);

    /**
     * 下架商品
     * 将商品状态标记为下架（status=2）
     * 
     * @param ids 商品ID列表，多个ID用逗号分隔
     * @return 操作结果，成功返回TaotaoResult.ok()
     */
    TaotaoResult instock(String ids);

    /**
     * 上架商品
     * 将商品状态恢复为正常（status=1）
     * 
     * @param ids 商品ID列表，多个ID用逗号分隔
     * @return 操作结果，成功返回TaotaoResult.ok()
     */
    TaotaoResult reshelf(String ids);

    /**
     * 获取商品描述
     * 
     * @param itemId 商品ID
     * @return 包含商品描述对象的TaotaoResult
     */
    TaotaoResult getItemDesc(long itemId);

    /**
     * 获取商品规格参数
     * 
     * @param itemId 商品ID
     * @return 包含商品规格参数对象的TaotaoResult
     */
    TaotaoResult getItemParam(long itemId);
}

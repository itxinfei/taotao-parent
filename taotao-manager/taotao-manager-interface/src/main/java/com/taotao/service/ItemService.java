package com.taotao.service;


import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;

/**
 * 商品
 */
public interface ItemService {
    /**
     * 根据商品id获取商品信息
     *
     * @param itemId
     * @return
     */
    TbItem getItemById(long itemId);

    /**
     * 根据商品id查询商品描述
     *
     * @param itemId
     * @return
     */
    TbItemDesc getItemDescById(long itemId);

    /**
     * 根据商品id取规格参数
     *
     * @param id
     * @return
     */
    TbItemParamItem getItemParamById(Long id);


    /**
     * 分页查询商品信息
     *
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    /**
     * 添加商品
     *
     * @param tbItem
     * @param desc
     * @return
     * @throws Exception
     */
    TaotaoResult createItem(TbItem tbItem, String desc) throws Exception;
}

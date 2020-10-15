package com.taotao.search.mapper;

import com.taotao.common.pojo.SearchItem;

import java.util.List;

/**
 * 商品搜索
 */
public interface SearchItemMapper {

    //获取搜索商品列表
    List<SearchItem> getSearchItemList();

    //根据ID获取商品
    SearchItem getItemById(long itemId);
}

package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

/**
 * 商品搜索服务接口
 * 提供商品搜索相关的业务操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SearchService {

    /**
     * 根据关键词搜索商品
     * 
     * @param queryString 搜索关键词
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 搜索结果，包含商品列表、总记录数和总页数
     * @throws Exception 搜索过程中发生异常时抛出
     */
    SearchResult search(String queryString, int page, int rows) throws Exception;
}

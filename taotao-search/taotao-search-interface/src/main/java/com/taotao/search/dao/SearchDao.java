package com.taotao.search.dao;

import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;

/**
 * 搜索Dao接口
 * 提供Solr搜索相关的数据访问操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface SearchDao {

    /**
     * 根据SolrQuery查询商品
     * 
     * @param query Solr查询对象
     * @return 搜索结果，包含商品列表和总记录数
     * @throws Exception 查询过程中发生异常时抛出
     */
    SearchResult search(SolrQuery query) throws Exception;
}

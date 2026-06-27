package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;

/**
 * 商品搜索服务实现类
 * 实现商品搜索相关的业务操作，基于Solr实现全文搜索
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 搜索Dao，用于执行Solr查询
     */
    @Autowired
    private SearchDao searchDao;

    /**
     * 根据关键词搜索商品
     * 实现逻辑：
     * 1. 构建SolrQuery查询对象
     * 2. 设置查询条件、分页参数、默认搜索域
     * 3. 配置高亮显示（对商品标题进行高亮）
     * 4. 调用Dao执行查询
     * 5. 计算总页数并封装返回结果
     * 
     * @param queryString 搜索关键词
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 搜索结果，包含商品列表、总记录数和总页数
     * @throws Exception 搜索过程中发生异常时抛出
     */
    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        // 创建SolrQuery查询对象
        SolrQuery query = new SolrQuery();
        
        // 设置查询条件
        query.setQuery(queryString);
        
        // 设置分页条件（页码最小值为1，每页记录数最小值为10）
        if (page < 1) {
            page = 1;
        }
        query.setStart((page - 1) * rows);
        if (rows < 1) {
            rows = 10;
        }
        query.setRows(rows);
        
        // 设置默认搜索域为商品标题
        query.set("df", "item_title");
        
        // 设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<em>");
        query.setHighlightSimplePost("</em>");
        
        // 调用Dao执行查询
        SearchResult searchResult = searchDao.search(query);
        
        // 计算总页数
        long totalNumber = searchResult.getRecordCount();
        long pages = totalNumber / rows;
        if (totalNumber % rows > 0) {
            pages++;
        }
        searchResult.setTotalPages(pages);
        
        return searchResult;
    }
}

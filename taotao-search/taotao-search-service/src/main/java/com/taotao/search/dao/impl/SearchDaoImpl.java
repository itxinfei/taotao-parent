package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;

/**
 * 搜索Dao实现类
 * 实现Solr搜索相关的数据访问操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Repository
public class SearchDaoImpl implements SearchDao {

    /**
     * Solr服务器客户端，用于执行Solr查询
     */
    @Autowired
    private SolrServer solrServer;

    /**
     * 根据SolrQuery查询商品
     * 实现逻辑：
     * 1. 执行Solr查询，获取QueryResponse
     * 2. 从Response中获取文档列表和总记录数
     * 3. 遍历文档列表，将每个SolrDocument转换为SearchItem对象
     * 4. 处理高亮显示，优先使用高亮标题
     * 5. 封装SearchResult并返回
     * 
     * @param query Solr查询对象
     * @return 搜索结果，包含商品列表和总记录数
     * @throws Exception 查询过程中发生异常时抛出
     */
    @Override
    public SearchResult search(SolrQuery query) throws Exception {
        // 执行Solr查询
        QueryResponse response = solrServer.query(query);
        
        // 获取查询结果文档列表和总记录数
        SolrDocumentList solrDocumentList = response.getResults();
        long numFound = solrDocumentList.getNumFound();
        
        // 初始化搜索结果对象
        SearchResult searchResult = new SearchResult();
        searchResult.setRecordCount(numFound);
        List<SearchItem> itemList = new ArrayList<>();
        
        // 遍历文档列表，转换为SearchItem对象
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItem searchItem = new SearchItem();
            searchItem.setId((String) solrDocument.get("id"));
            
            // 处理商品图片（取第一张）
            String image = (String) solrDocument.get("item_image");
            if (!StringUtils.isBlank(image)) {
                image = image.split(",")[0];
            }
            searchItem.setImage(image);
            
            searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
            searchItem.setItem_desc((String) solrDocument.get("item_desc"));
            searchItem.setPrice((long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            
            // 处理高亮显示的标题
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size() > 0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            searchItem.setTitle(title);
            
            itemList.add(searchItem);
        }
        
        // 设置商品列表并返回
        searchResult.setItemList(itemList);
        return searchResult;
    }
}

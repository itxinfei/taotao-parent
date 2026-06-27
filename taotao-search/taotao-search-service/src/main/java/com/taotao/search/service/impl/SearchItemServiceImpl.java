package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现数据导入功能
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    private static final Logger logger = LoggerFactory.getLogger(SearchItemServiceImpl.class);
    @Autowired
    private SearchItemMapper searchItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult importItemsToIndex() {
        //1、先查询所有商品数据
        try {
            List<SearchItem> itemList = searchItemMapper.getSearchItemList();
            //2、遍历商品数据添加到索引库
            for (SearchItem searchItem : itemList) {
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                document.setField("id", searchItem.getId());
                document.setField("item_title", searchItem.getTitle());
                document.setField("item_sell_point", searchItem.getSell_point());
                document.setField("item_price", searchItem.getPrice());
                document.setField("item_image", searchItem.getImage());
                document.setField("item_category_name", searchItem.getCategory_name());
                document.setField("item_desc", searchItem.getItem_desc());
                solrServer.add(document);
            }
            solrServer.commit();
            return TaotaoResult.ok();
        } catch (Exception e) {
            logger.error("导入商品数据到索引库失败", e);
            return TaotaoResult.build(500, "导入数据失败！");
        }
    }
}

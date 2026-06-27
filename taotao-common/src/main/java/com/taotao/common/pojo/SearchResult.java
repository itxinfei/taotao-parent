package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索结果封装类
 * 用于封装商品搜索的分页结果
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class SearchResult implements Serializable {

    /**
     * 总页数
     */
    private long totalPages;

    /**
     * 总记录数
     */
    private long recordCount;

    /**
     * 商品搜索结果列表
     */
    private List<SearchItem> itemList;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }


}

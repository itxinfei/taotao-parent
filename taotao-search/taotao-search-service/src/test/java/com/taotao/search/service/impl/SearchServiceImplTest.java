package com.taotao.search.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SearchServiceImplTest {

    @Mock
    private SearchDao searchDao;

    @InjectMocks
    private SearchServiceImpl searchService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchPageCalculation() throws Exception {
        SearchResult mockResult = new SearchResult();
        mockResult.setRecordCount(95);
        mockResult.setItemList(new ArrayList<SearchItem>());
        when(searchDao.search(any(SolrQuery.class))).thenReturn(mockResult);

        SearchResult result = searchService.search("手机", 1, 10);

        assertEquals("Total pages should be 10 for 95 records with 10 per page", 10, result.getTotalPages());
    }

    @Test
    public void testSearchExactPageBoundary() throws Exception {
        SearchResult mockResult = new SearchResult();
        mockResult.setRecordCount(100);
        mockResult.setItemList(new ArrayList<SearchItem>());
        when(searchDao.search(any(SolrQuery.class))).thenReturn(mockResult);

        SearchResult result = searchService.search("手机", 1, 20);

        assertEquals("Total pages should be 5 for 100 records with 20 per page", 5, result.getTotalPages());
    }

    @Test
    public void testSearchSingleRecord() throws Exception {
        SearchResult mockResult = new SearchResult();
        mockResult.setRecordCount(1);
        mockResult.setItemList(new ArrayList<SearchItem>());
        when(searchDao.search(any(SolrQuery.class))).thenReturn(mockResult);

        SearchResult result = searchService.search("手机", 1, 10);

        assertEquals("Total pages should be 1 for single record", 1, result.getTotalPages());
    }

    @Test
    public void testSearchWithDefaultPageAndRows() throws Exception {
        SearchResult mockResult = new SearchResult();
        mockResult.setRecordCount(0);
        mockResult.setItemList(new ArrayList<SearchItem>());
        when(searchDao.search(any(SolrQuery.class))).thenReturn(mockResult);

        SearchResult result = searchService.search("手机", 0, 0);

        assertEquals("Total pages should be 0 for no records", 0, result.getTotalPages());
    }
}

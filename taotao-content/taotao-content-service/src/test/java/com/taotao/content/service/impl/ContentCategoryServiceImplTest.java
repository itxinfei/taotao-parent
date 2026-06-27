package com.taotao.content.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ContentCategoryServiceImplTest {

    @Mock
    private TbContentCategoryMapper contentCategoryMapper;

    @InjectMocks
    private ContentCategoryServiceImpl contentCategoryService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteContentCategoryParentHasActiveChild() {
        TbContentCategory target = new TbContentCategory();
        target.setId(10L);
        target.setParentId(5L);
        target.setIsParent(false);
        when(contentCategoryMapper.selectByPrimaryKey(10L)).thenReturn(target);

        TbContentCategory parent = new TbContentCategory();
        parent.setId(5L);
        parent.setIsParent(true);
        when(contentCategoryMapper.selectByPrimaryKey(5L)).thenReturn(parent);

        List<TbContentCategory> siblings = new ArrayList<>();
        TbContentCategory sibling = new TbContentCategory();
        sibling.setId(12L);
        sibling.setParentId(5L);
        sibling.setStatus(1);
        siblings.add(sibling);
        when(contentCategoryMapper.selectByExample(any(TbContentCategoryExample.class))).thenReturn(siblings);

        TaotaoResult result = contentCategoryService.deleteContentCategory(10L);

        assertEquals(Integer.valueOf(200), result.getStatus());
        verify(contentCategoryMapper, times(1)).updateByPrimaryKey(target);
        verify(contentCategoryMapper, never()).updateByPrimaryKey(parent);
    }

    @Test
    public void testDeleteContentCategoryParentHasNoActiveChild() {
        TbContentCategory target = new TbContentCategory();
        target.setId(10L);
        target.setParentId(5L);
        target.setIsParent(false);
        when(contentCategoryMapper.selectByPrimaryKey(10L)).thenReturn(target);

        TbContentCategory parent = new TbContentCategory();
        parent.setId(5L);
        parent.setIsParent(true);
        when(contentCategoryMapper.selectByPrimaryKey(5L)).thenReturn(parent);

        List<TbContentCategory> siblings = new ArrayList<>();
        TbContentCategory sibling = new TbContentCategory();
        sibling.setId(12L);
        sibling.setParentId(5L);
        sibling.setStatus(2);
        siblings.add(sibling);
        when(contentCategoryMapper.selectByExample(any(TbContentCategoryExample.class))).thenReturn(siblings);

        TaotaoResult result = contentCategoryService.deleteContentCategory(10L);

        assertEquals(Integer.valueOf(200), result.getStatus());
        verify(contentCategoryMapper, times(1)).updateByPrimaryKey(target);
        verify(contentCategoryMapper, times(1)).updateByPrimaryKey(parent);
        assertEquals("Parent isParent should be false", false, parent.getIsParent());
    }

    @Test
    public void testDeleteContentCategoryWithRecursiveChildren() {
        TbContentCategory target = new TbContentCategory();
        target.setId(10L);
        target.setParentId(5L);
        target.setIsParent(true);
        when(contentCategoryMapper.selectByPrimaryKey(10L)).thenReturn(target);

        List<TbContentCategory> children = new ArrayList<>();
        TbContentCategory child = new TbContentCategory();
        child.setId(11L);
        child.setParentId(10L);
        child.setIsParent(false);
        child.setStatus(1);
        children.add(child);
        when(contentCategoryMapper.selectByExample(any(TbContentCategoryExample.class))).thenReturn(children);

        TbContentCategory parent = new TbContentCategory();
        parent.setId(5L);
        parent.setIsParent(false);
        when(contentCategoryMapper.selectByPrimaryKey(5L)).thenReturn(parent);

        TaotaoResult result = contentCategoryService.deleteContentCategory(10L);

        assertEquals(Integer.valueOf(200), result.getStatus());
        verify(contentCategoryMapper, times(1)).updateByPrimaryKey(target);
        verify(contentCategoryMapper, atLeastOnce()).updateByPrimaryKey(child);
    }
}

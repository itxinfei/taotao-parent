package com.taotao.service;

import com.taotao.common.pojo.EasyUITreeNode;

import java.util.List;

/**
 * 商品分类服务接口
 * 提供商品分类管理相关的业务操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface ItemCatService {

    /**
     * 根据父分类ID查询子分类列表
     * 
     * @param parentId 父分类ID，顶级分类为0
     * @return 子分类树形节点列表
     */
    List<EasyUITreeNode> getItemCatList(long parentId);
}

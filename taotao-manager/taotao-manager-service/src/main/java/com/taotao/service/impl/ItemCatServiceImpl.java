package com.taotao.service.impl;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类服务实现类
 * 实现商品分类管理相关的业务操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    /**
     * 商品分类Mapper，用于商品分类的数据库操作
     */
    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 根据父分类ID查询子分类列表
     * 将数据库查询结果转换为EasyUITreeNode格式，便于前端树形组件展示
     * 
     * @param parentId 父分类ID，顶级分类为0
     * @return 子分类树形节点列表
     */
    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(example);

        List<EasyUITreeNode> easyUITreeNodeList = new ArrayList<>();
        for (TbItemCat itemCat : list) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(itemCat.getId());
            easyUITreeNode.setText(itemCat.getName());
            easyUITreeNode.setState(itemCat.getIsParent() ? "closed" : "open");
            easyUITreeNodeList.add(easyUITreeNode);
        }
        return easyUITreeNodeList;
    }
}


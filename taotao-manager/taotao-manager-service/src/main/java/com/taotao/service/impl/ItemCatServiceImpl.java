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


@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
   private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> list = itemCatMapper.selectByExample(example);

        List<EasyUITreeNode> easyUITreeNodeList = new ArrayList<>();
        for (TbItemCat itemCat :list){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(itemCat.getId());
            easyUITreeNode.setText(itemCat.getName());
            easyUITreeNode.setState(itemCat.getIsParent()?"closed":"open");
            easyUITreeNodeList.add(easyUITreeNode);
        }

        return easyUITreeNodeList;
    }
}

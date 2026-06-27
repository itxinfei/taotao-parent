package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理服务实现类
 * 提供内容分类的增删改查操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    /**
     * 分类状态：正常
     */
    private static final Integer STATUS_NORMAL = 1;

    /**
     * 分类状态：删除
     */
    private static final Integer STATUS_DELETED = 2;

    /**
     * 内容分类Mapper，用于数据库操作
     */
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 展示内容分类
     *
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //创建一个查询类
        TbContentCategoryExample contentCategoryExample = new TbContentCategoryExample();
        //设置查询条件
        Criteria criteria = contentCategoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //查询
        List<TbContentCategory> categoryList = contentCategoryMapper.selectByExample(contentCategoryExample);
        //将categoryList转换为List<EasyUITreeNode>
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : categoryList) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setState(contentCategory.getIsParent() ? "closed" : "open");
            resultList.add(easyUITreeNode);
        }
        return resultList;
    }

    /**
     * 添加内容分类
     *
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public TaotaoResult addContentCategory(long parentId, String name) {
        //实例化一个对象
        TbContentCategory contentCategory = new TbContentCategory();
        //填充属性值
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        // 设置状态为正常
        contentCategory.setStatus(STATUS_NORMAL);
        //刚添加的节点肯定不是父节点
        contentCategory.setIsParent(false);
        //数据库中现在默认的都是1，所以这里我们也写成1
        contentCategory.setSortOrder(1);
        //保存当前操作时间
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入节点到数据库
        contentCategoryMapper.saveAndGetId(contentCategory);
        //添加一个节点需要判断父节点是不是叶子节点，如果父节点是叶子节点的话，
        //需要改成父节点状态
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        return TaotaoResult.ok(contentCategory);
    }

    /**
     * 更新内容分类
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        //通过id查询节点对象
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //判断新的name值与原来的值是否相同，如果相同则不用更新
        if (name != null && name.equals(contentCategory.getName())) {
            return TaotaoResult.ok();
        }
        contentCategory.setName(name);
        //设置更新时间
        contentCategory.setUpdated(new Date());
        //更新数据库
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        //返回结果
        return TaotaoResult.ok();
    }

    /**
     * 通过父节点id来查询所有子节点，这是抽离出来的公共方法
     *
     * @param parentId
     * @return
     */
    private List<TbContentCategory> getContentCategoryListByParentId(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        return list;
    }

    /**
     * 递归删除子节点
     * 
     * @param parentId 父节点ID
     */
    private void deleteNode(long parentId) {
        List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
        for (TbContentCategory contentCategory : list) {
            contentCategory.setStatus(STATUS_DELETED);
            contentCategoryMapper.updateByPrimaryKey(contentCategory);
            if (contentCategory.getIsParent()) {
                deleteNode(contentCategory.getId());
            }
        }
    }

    /**
     * 删除内容分类
     * 逻辑删除，将状态改为删除状态，并级联删除所有子节点
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    @Override
    public TaotaoResult deleteContentCategory(long id) {
        // 删除分类，将状态改为删除状态
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setStatus(STATUS_DELETED);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        
        // 如果是父节点，级联删除所有子节点
        if (contentCategory.getIsParent()) {
            deleteNode(contentCategory.getId());
        }
        
        // 判断父节点是否还有活跃子节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
        List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
        
        boolean hasActiveChild = false;
        for (TbContentCategory tbContentCategory : list) {
            if (tbContentCategory.getStatus().equals(STATUS_NORMAL)) {
                hasActiveChild = true;
                break;
            }
        }
        //如果没有子节点了
        if (!hasActiveChild) {
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return TaotaoResult.ok();
    }
}

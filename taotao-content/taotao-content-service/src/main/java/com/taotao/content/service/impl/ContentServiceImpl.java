package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.service.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;

/**
 * 内容管理服务实现类
 * <p>
 * 处理内容的增删改查操作，并使用Redis缓存首页广告位内容（INDEX_CONTENT）。
 * 缓存以Hash结构存储，key为内容分类ID，value为内容列表JSON。
 * 新增/更新/删除内容时同步清理对应分类的缓存。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class ContentServiceImpl implements ContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    /** 内容Mapper */
    @Autowired
    private TbContentMapper contentMapper;

    /** Redis客户端 */
    @Autowired
    private JedisClient jedisClient;

    /** 首页内容在Redis中的Hash Key */
    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    /**
     * 根据分类ID查询内容列表
     * <p>
     * 实现缓存穿透保护：<br>
     * 1. 先查询Redis缓存，命中则直接返回，避免数据库查询<br>
     * 2. 缓存未命中则查询数据库，并将结果写入缓存（异步容错，不影响主流程）<br>
     * 3. 缓存操作异常时降级为直接查询数据库
     * </p>
     *
     * @param cid 内容分类ID
     * @return 内容列表
     */
    @Override
    public List<TbContent> getContentListByCid(long cid) {
        try {
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
                return list;
            }
        } catch (Exception e) {
            logger.error("读取Redis缓存(内容分类ID={})失败", cid, e);
        }

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        List<TbContent> list = contentMapper.selectByExample(example);

        try {
            String json = JsonUtils.objectToJson(list);
            jedisClient.hset(INDEX_CONTENT, cid + "", json);
        } catch (Exception e) {
            logger.error("写入Redis缓存(内容分类ID={})失败", cid, e);
        }

        return list;
    }

    /**
     * 分页查询内容列表
     *
     * @param categoryId 内容分类ID
     * @param page       页码
     * @param rows       每页记录数
     * @return 分页结果
     */
    @Override
    public EasyUIDataGridResult getContentList(long categoryId, int page, int rows) {
        PageHelper.startPage(page, rows);
        TbContentExample example = new TbContentExample();
        Criteria createCriteria = example.createCriteria();
        createCriteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);

        PageInfo<TbContent> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 添加内容
     * <p>
     * 新增内容后删除对应分类的Redis缓存，确保首页展示最新数据。
     * </p>
     *
     * @param content 待添加的内容
     * @return 操作结果
     */
    @Override
    public TaotaoResult addContent(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);
        jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
        return TaotaoResult.ok();
    }

    /**
     * 更新内容
     * <p>
     * 先查询旧内容获取分类ID，删除对应Redis缓存后再更新数据库，
     * 避免因分类变更导致旧分类缓存残留。
     * </p>
     *
     * @param content 待更新的内容
     * @return 操作结果
     */
    @Override
    public TaotaoResult updateContent(TbContent content) {
        TbContent oldContent = contentMapper.selectByPrimaryKey(content.getId());
        if (oldContent != null) {
            jedisClient.hdel(INDEX_CONTENT, oldContent.getCategoryId().toString());
        }
        content.setUpdated(new Date());
        contentMapper.updateByPrimaryKey(content);
        return TaotaoResult.ok();
    }

    /**
     * 批量删除内容
     * <p>
     * 逐条删除内容并清理对应分类的Redis缓存。
     * 先查内容获取分类ID再删除，避免直接删除后无法获取分类信息。
     * </p>
     *
     * @param ids 待删除的内容ID，多个ID用逗号分隔
     * @return 操作结果
     */
    @Override
    public TaotaoResult deleteContent(String ids) {
        String[] idList = ids.split(",");
        for (String id : idList) {
            TbContent content = contentMapper.selectByPrimaryKey(Long.valueOf(id));
            if (content != null) {
                jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
            }
            contentMapper.deleteByPrimaryKey(Long.valueOf(id));
        }
        return TaotaoResult.ok();
    }

    /**
     * 根据ID获取内容
     *
     * @param id 内容ID
     * @return 内容信息
     */
    @Override
    public TaotaoResult getContent(long id) {
        TbContent content = contentMapper.selectByPrimaryKey(id);
        return TaotaoResult.ok(content);
    }
}

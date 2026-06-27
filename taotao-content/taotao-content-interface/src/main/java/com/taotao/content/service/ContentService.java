package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

/**
 * 内容管理服务接口
 * 提供内容管理相关的业务操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface ContentService {

    /**
     * 分页查询内容列表
     * 
     * @param categoryId 内容分类ID
     * @param page 页码（从1开始）
     * @param rows 每页记录数
     * @return 分页结果，包含内容列表和总记录数
     */
    EasyUIDataGridResult getContentList(long categoryId, int page, int rows);

    /**
     * 添加内容
     * 
     * @param content 内容对象
     * @return 操作结果
     */
    TaotaoResult addContent(TbContent content);

    /**
     * 修改内容
     * 
     * @param content 内容对象
     * @return 操作结果
     */
    TaotaoResult updateContent(TbContent content);

    /**
     * 删除内容（支持批量删除）
     * 
     * @param ids 内容ID字符串，多个ID用逗号分隔
     * @return 操作结果
     */
    TaotaoResult deleteContent(String ids);

    /**
     * 获取单个内容信息
     * 
     * @param id 内容ID
     * @return 操作结果，data字段包含内容对象
     */
    TaotaoResult getContent(long id);

    /**
     * 根据内容分类ID获取内容列表（用于首页展示）
     * 从Redis缓存中获取，缓存不存在时从数据库查询并写入缓存
     * 
     * @param cid 内容分类ID
     * @return 内容列表
     */
    List<TbContent> getContentListByCid(long cid);
}

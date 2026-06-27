package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * EasyUI树形节点封装类
 * 用于封装EasyUI Tree组件所需的节点信息
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class EasyUITreeNode implements Serializable {

    /**
     * 节点ID
     */
    private long id;

    /**
     * 节点显示文本
     */
    private String text;

    /**
     * 节点状态：open（展开）或closed（折叠）
     */
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}

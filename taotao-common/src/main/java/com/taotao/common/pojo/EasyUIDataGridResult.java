package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * EasyUI DataGrid分页结果封装类
 * 用于封装EasyUI DataGrid组件所需的分页数据
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class EasyUIDataGridResult implements Serializable {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 当前页数据列表
     */
    private List rows;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

}

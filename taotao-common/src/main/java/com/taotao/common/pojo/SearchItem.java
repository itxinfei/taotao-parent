package com.taotao.common.pojo;

import java.io.Serializable;

/**
 * 搜索商品实体类
 * 用于封装商品搜索结果中的商品信息
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class SearchItem implements Serializable {

    /**
     * 商品ID
     */
    private String id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品卖点
     */
    private String sell_point;

    /**
     * 商品价格（单位：分）
     */
    private long price;

    /**
     * 商品图片地址，多个图片以逗号分隔
     */
    private String image;

    /**
     * 商品分类名称
     */
    private String category_name;

    /**
     * 商品描述
     */
    private String item_desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSell_point() {
        return sell_point;
    }

    public void setSell_point(String sell_point) {
        this.sell_point = sell_point;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

}

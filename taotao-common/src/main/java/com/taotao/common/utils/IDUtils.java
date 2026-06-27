package com.taotao.common.utils;

import java.util.Random;

/**
 * ID生成工具类
 * 提供基于时间戳的唯一ID生成策略
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class IDUtils {

    /**
     * 生成图片文件名
     * 规则：当前时间戳(毫秒) + 3位随机数，不足3位前面补0
     * 
     * @return 图片文件名，格式为13位时间戳+3位随机数
     */
    public static String genImageName() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上三位随机数
        Random random = new Random();
        int end3 = random.nextInt(999);
        //如果不足三位前面补0
        String str = millis + String.format("%03d", end3);

        return str;
    }

    /**
     * 生成商品ID
     * 规则：当前时间戳(毫秒) + 2位随机数，不足2位前面补0
     * 
     * @return 商品ID，格式为13位时间戳+2位随机数
     */
    public static long genItemId() {
        //取当前时间的长整形值包含毫秒
        long millis = System.currentTimeMillis();
        //加上两位随机数
        Random random = new Random();
        int end2 = random.nextInt(99);
        //如果不足两位前面补0
        String str = millis + String.format("%02d", end2);
        long id = new Long(str);
        return id;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++)
            System.out.println(genItemId());
    }
}

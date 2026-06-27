package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.common.utils.JsonUtils;
import com.taotao.utils.FastDFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图片上传控制器
 * 提供商品图片上传服务，将图片上传至FastDFS服务器
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    /**
     * 图片服务器地址，配置于properties文件
     */
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    /**
     * 上传图片
     * 
     * @param uploadFile 上传的图片文件
     * @return JSON格式的上传结果，包含error状态和url地址
     */
    @RequestMapping("/pic/upload")
    @ResponseBody
    public String picUpload(MultipartFile uploadFile) {
        try {
            // 接收上传的文件，取扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            
            // 上传到图片服务器
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/client.conf");
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
            url = IMAGE_SERVER_URL + url;
            
            // 响应上传图片的url
            Map result = new HashMap<>();
            result.put("error", 0);
            result.put("url", url);
            logger.info("图片上传成功，地址：{}", url);
            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            logger.error("图片上传失败", e);
            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}

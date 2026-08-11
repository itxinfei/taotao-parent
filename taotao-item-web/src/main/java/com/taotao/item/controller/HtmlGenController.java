package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 商品详情页面静态化
 */
@Controller
public class HtmlGenController {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 静态页面输出路径，配置于properties文件
     */
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;

    @RequestMapping("genhtml")
    @ResponseBody
    public String genHtml() throws Exception {
        //生成静态页面
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate("hello.ftl");
        Map<String, Object> data = new HashMap<>();
        data.put("hello", "spring freemarker test");
        // 输出到配置的静态页面目录，避免硬编码路径
        Writer out = new FileWriter(new File(HTML_OUT_PATH + "test.html"));
        try {
            template.process(data, out);
        } finally {
            out.close();
        }
        //返回结果
        return "OK";
    }
}

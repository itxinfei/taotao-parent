package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户管理控制器
 * 提供用户注册、登录、登出、信息查询和更新等功能的REST API接口
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
@CrossOrigin(origins = "*", allowCredentials = "true")
public class UserController {

    /**
     * 用户服务，用于处理用户相关业务逻辑
     */
    @Resource
    private UserService userService;

    /**
     * Token在Cookie中的键名，配置于properties文件
     */
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 校验用户数据是否可用
     * 用于注册时验证用户名、邮箱、手机号是否已被注册
     * 
     * @param param 待校验的参数值（用户名、邮箱或手机号）
     * @param type 校验类型：1-用户名，2-手机号，3-邮箱
     * @return TaotaoResult，data字段为布尔值，表示是否可用
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
        TaotaoResult result = userService.checkUserData(param, type);
        return result;
    }

    /**
     * 用户注册
     * 创建新用户账号，密码经过MD5加密后存储
     * 
     * @param tbUser 用户信息对象，包含用户名、密码、邮箱、手机号
     * @return TaotaoResult，成功时status为200，失败时包含错误信息
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser) {
        TaotaoResult register = userService.register(tbUser);
        return register;
    }

    /**
     * 用户登录
     * 验证用户名和密码，登录成功后生成Token并写入Cookie
     * 
     * @param username 用户名
     * @param password 密码（明文）
     * @param request HTTP请求对象，用于获取Cookie
     * @param response HTTP响应对象，用于设置Cookie
     * @return TaotaoResult，成功时data字段为Token值
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password,
                              HttpServletRequest request, HttpServletResponse response) {
        TaotaoResult result = userService.login(username, password);
        if ("200".equals(result.getStatus())) {
            // 将Token写入Cookie
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    /**
     * 根据Token获取用户信息
     * 支持JSONP跨域调用，用于前端单点登录场景
     * 
     * @param token 用户登录Token
     * @param callback JSONP回调函数名（可选）
     * @return TaotaoResult，包含用户信息；若提供callback则返回JSONP格式
     */
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = userService.getUserByToken(token);

        if (org.apache.commons.lang3.StringUtils.isNoneBlank(callback)) {
            // 支持JSONP跨域
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    /**
     * 用户登出
     * 清除Redis中的Token信息，使Token失效
     * 
     * @param token 用户登录Token
     * @return TaotaoResult，成功时status为200
     */
    @RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token) {
        TaotaoResult result = userService.logout(token);
        return result;
    }

    /**
     * 更新用户信息
     * 更新用户的邮箱、手机号、昵称等信息
     * 
     * @param tbUser 用户信息对象，必须包含userId
     * @return TaotaoResult，成功时status为200
     */
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateUser(TbUser tbUser) {
        try {
            TaotaoResult result = userService.updateUser(tbUser);
            return result;
        } catch (Exception e) {
            return TaotaoResult.build(500, "更新用户信息失败");
        }
    }
}

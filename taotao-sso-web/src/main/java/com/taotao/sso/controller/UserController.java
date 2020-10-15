package com.taotao.sso.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户登录
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 数据校验
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public TaotaoResult checkUserData(@PathVariable String param, @PathVariable Integer type) {
        TaotaoResult result = userService.checkUserData(param, type);
        return result;
    }

    /**
     * 用户注册
     *
     * @param tbUser
     * @return
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser tbUser) {
        TaotaoResult register = userService.register(tbUser);
        return register;
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult login(String username, String password,
                              HttpServletRequest request, HttpServletResponse response) {
        TaotaoResult result = userService.login(username, password);
        if ("200".equals(result.getStatus())) {
            //把token写入到cookie

            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    /**
     * 通过token获取用户信息
     *
     * @param token
     * @param callback
     * @return
     */
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = userService.getUserByToken(token);

        if (org.apache.commons.lang3.StringUtils.isNoneBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    /**
     * 用户登出
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult logout(@PathVariable String token) {
        TaotaoResult result = userService.logout(token);
        return result;
    }
}

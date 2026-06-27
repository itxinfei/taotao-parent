package com.taotao.order.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 登录拦截器
 * <p>
 * 拦截需要登录才能访问的请求（如订单确认、订单创建），
 * 校验用户登录状态，未登录则重定向到SSO登录页。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class LoginInterceptor implements HandlerInterceptor {

    /** Token在Cookie中的存储Key，需与SSO系统保持一致 */
    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /** SSO单点登录系统的URL前缀 */
    @Value("${SSO_URL}")
    private String SSO_URL;

    /** 用户服务，用于验证Token有效性 */
    @Resource
    private UserService userService;

    /**
     * 前置拦截处理
     * <p>
     * 处理逻辑：<br>
     * 1. 先检查URL参数中是否有token（SSO登录成功后的回跳场景）<br>
     * 2. 如果有URL中的token，验证其有效性，有效则写入Cookie并重定向到干净URL<br>
     * 3. 从Cookie中获取token，如果为空则重定向到SSO登录页<br>
     * 4. 调用SSO服务验证token有效性，过期则重定向到SSO登录页<br>
     * 5. 验证通过后将用户信息存入request属性，放行请求
     * </p>
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  被拦截的处理器
     * @return true放行，false拦截
     * @throws Exception 处理异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestURL = request.getRequestURL().toString();

        /*
         * 处理SSO登录成功后回跳的场景：
         * SSO登录成功会在URL后附加token参数，此处读取并验证该token，
         * 验证通过后写入当前域名的Cookie，解决跨域Cookie不共享问题
         */
        String tokenFromUrl = request.getParameter("token");
        if (StringUtils.isNotBlank(tokenFromUrl)) {
            TaotaoResult result = userService.getUserByToken(tokenFromUrl);
            if (result.getStatus() == 200) {
                CookieUtils.setCookie(request, response, TOKEN_KEY, tokenFromUrl, 604800, true);
                response.sendRedirect(requestURL);
                return false;
            }
        }

        // 从Cookie中获取登录Token
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);

        // Token不存在，重定向到SSO登录页，并将当前URL作为回调参数传递
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            return false;
        }

        // 调用SSO服务验证Token有效性
        TaotaoResult result = userService.getUserByToken(token);

        // Token已过期或无效，重定向到SSO登录页
        if (result.getStatus() != 200) {
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            return false;
        }

        // Token有效，将用户信息存入request，供后续Controller使用
        TbUser user = (TbUser) result.getData();
        request.setAttribute("USER_INFO", user);
        return true;
    }

    /**
     * 后置处理
     *
     * @param request      HTTP请求
     * @param response     HTTP响应
     * @param handler      被拦截的处理器
     * @param modelAndView 模型和视图
     * @throws Exception 处理异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * 完成处理（视图渲染后调用）
     *
     * @param request  HTTP请求
     * @param response HTTP响应
     * @param handler  被拦截的处理器
     * @param ex       异常信息
     * @throws Exception 处理异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}

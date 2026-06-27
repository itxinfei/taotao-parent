package com.taotao.order.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器
 * <p>
 * 所有Controller未捕获的异常由此处理器统一处理，
 * 记录错误日志并跳转到友好的错误提示页面，避免将异常堆栈暴露给用户。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, Exception e) {
        logger.error("订单系统发生异常, URI: {}", request.getRequestURI(), e);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "当前网络出现故障，请稍后重试！");
        modelAndView.setViewName("error/exception");
        return modelAndView;
    }
}

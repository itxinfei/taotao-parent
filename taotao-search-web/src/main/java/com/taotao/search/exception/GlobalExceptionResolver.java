package com.taotao.search.exception;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.search.utils.StackTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.search.utils.SendMail;

/**
 * 全局异常处理器
 * 实现Spring MVC的HandlerExceptionResolver接口，统一处理应用中抛出的异常
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 日志记录器
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);

    /**
     * 处理控制器抛出的异常
     * 处理流程：
     * 1. 记录异常日志
     * 2. 异步发送异常通知邮件
     * 3. 返回错误页面给用户
     * 
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param handler 处理请求的处理器（控制器方法）
     * @param e 抛出的异常
     * @return ModelAndView，包含错误页面视图和错误消息
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                         Object handler, final Exception e) {
        // 记录异常日志
        logger.error("系统发生异常", e);
        
        // 异步发送异常通知邮件
        Runnable mailRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    SendMail.sendEmail("搜索系统出现异常", StackTrace.getStackTrace(e));
                } catch (MessagingException e1) {
                    logger.error("发送异常通知邮件失败", e1);
                }
            }
        };
        Thread mailThread = new Thread(mailRunnable);
        mailThread.start();
        
        // 返回错误页面
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", "当前网络出现故障，请稍后重试！");
        modelAndView.setViewName("error/exception");
        
        return modelAndView;
    }

}

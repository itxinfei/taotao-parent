package com.taotao.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cookie工具类
 * 提供Cookie的读取、设置和删除操作
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public final class CookieUtils {

    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    /**
     * 获取Cookie的值，不进行URL解码
     * 
     * @param request HttpServletRequest对象
     * @param cookieName Cookie名称
     * @return Cookie值，若不存在返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * 获取Cookie的值，可选择是否进行URL解码
     * 
     * @param request HttpServletRequest对象
     * @param cookieName Cookie名称
     * @param isDecoder 是否进行URL解码（UTF-8编码）
     * @return Cookie值，若不存在返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(cookieList[i].getValue(), "UTF-8");
                    } else {
                        retValue = cookieList[i].getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Cookie解码失败", e);
        }
        return retValue;
    }

    /**
     * 获取Cookie的值，使用指定编码进行URL解码
     * 
     * @param request HttpServletRequest对象
     * @param cookieName Cookie名称
     * @param encodeString 解码使用的字符编码
     * @return Cookie值，若不存在返回null
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (int i = 0; i < cookieList.length; i++) {
                if (cookieList[i].getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(cookieList[i].getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("Cookie解码失败", e);
        }
        return retValue;
    }

    /**
     * 设置Cookie的值，不设置生效时间（默认浏览器关闭即失效），不编码
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * 设置Cookie的值，在指定时间内生效，不编码
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param cookieMaxage Cookie生效的最大秒数，-1表示浏览器关闭即失效
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxage, false);
    }

    /**
     * 设置Cookie的值，不设置生效时间，使用UTF-8编码
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param isEncode 是否进行URL编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * 设置Cookie的值，在指定时间内生效，使用UTF-8编码
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param cookieMaxage Cookie生效的最大秒数，-1表示浏览器关闭即失效
     * @param isEncode 是否进行URL编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, isEncode);
    }

    /**
     * 设置Cookie的值，在指定时间内生效，使用指定编码
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param cookieMaxage Cookie生效的最大秒数，-1表示浏览器关闭即失效
     * @param encodeString 编码使用的字符编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,
                                 String cookieValue, int cookieMaxage, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxage, encodeString);
    }

    /**
     * 删除指定名称的Cookie
     * 
     * @param request HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @param cookieName Cookie名称
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response,
                                    String cookieName) {
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效（使用UTF-8编码）
     * 
     * @param request HttpServletRequest对象，用于获取域名
     * @param response HttpServletResponse对象，用于设置Cookie
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param cookieMaxage Cookie生效的最大秒数，-1表示浏览器关闭即失效
     * @param isEncode 是否进行URL编码
     */
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (null != request) {
                String domainName = getDomainName(request);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error("设置Cookie失败", e);
        }
    }

    /**
     * 设置Cookie的值，并使其在指定时间内生效（使用指定编码）
     * 
     * @param request HttpServletRequest对象，用于获取域名
     * @param response HttpServletResponse对象，用于设置Cookie
     * @param cookieName Cookie名称
     * @param cookieValue Cookie值
     * @param cookieMaxage Cookie生效的最大秒数，-1表示浏览器关闭即失效
     * @param encodeString 编码使用的字符编码
     */
    private static final void doSetCookie(HttpServletRequest request, HttpServletResponse response,
                                          String cookieName, String cookieValue, int cookieMaxage, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            if (cookieMaxage > 0)
                cookie.setMaxAge(cookieMaxage);
            if (null != request) {
                String domainName = getDomainName(request);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            logger.error("设置Cookie(编码)失败", e);
        }
    }

    /**
     * 获取Cookie的域名
     * 从请求URL中提取域名，支持www.xxx.com.cn、xxx.com、xxx.cn等格式
     * 
     * @param request HttpServletRequest对象
     * @return Cookie域名，若无法获取返回空字符串
     */
    private static final String getDomainName(HttpServletRequest request) {
        String domainName = null;

        String serverName = request.getRequestURL().toString();
        if (serverName == null || serverName.equals("")) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                // www.xxx.com.cn 格式
                domainName = "." + domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len <= 3 && len > 1) {
                // xxx.com 或 xxx.cn 格式
                domainName = "." + domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }

        if (domainName != null && domainName.indexOf(":") > 0) {
            String[] ary = domainName.split("\\:");
            domainName = ary[0];
        }
        return domainName;
    }

}

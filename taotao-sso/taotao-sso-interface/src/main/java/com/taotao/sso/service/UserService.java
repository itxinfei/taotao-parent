package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

/**
 * 用户服务接口
 * 提供用户注册、登录、登出、信息查询和更新等功能
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public interface UserService {

    /**
     * 校验用户数据是否可用
     * 用于注册时验证用户名、邮箱、手机号是否已被注册
     * 
     * @param data 待校验的数据（用户名、邮箱或手机号）
     * @param type 校验类型：1-用户名，2-手机号，3-邮箱
     * @return TaotaoResult，data字段为布尔值，表示是否可用
     */
    TaotaoResult checkUserData(String data, int type);

    /**
     * 用户注册
     * 创建新用户账号，密码经过MD5加密后存储
     * 
     * @param tbUser 用户信息对象，包含用户名、密码、邮箱、手机号
     * @return TaotaoResult，成功时status为200
     */
    TaotaoResult register(TbUser tbUser);

    /**
     * 用户登录
     * 验证用户名和密码，登录成功后生成Token并存储到Redis
     * 
     * @param username 用户名
     * @param password 密码（明文）
     * @return TaotaoResult，成功时data字段为Token值
     */
    TaotaoResult login(String username, String password);

    /**
     * 根据Token获取用户信息
     * 从Redis中读取Token对应的用户信息
     * 
     * @param token 用户登录Token
     * @return TaotaoResult，data字段为TbUser对象
     */
    TaotaoResult getUserByToken(String token);

    /**
     * 用户登出
     * 清除Redis中的Token信息，使Token失效
     * 
     * @param token 用户登录Token
     * @return TaotaoResult，成功时status为200
     */
    TaotaoResult logout(String token);

    /**
     * 更新用户信息
     * 更新用户的邮箱、手机号、昵称等信息
     * 
     * @param tbUser 用户信息对象，必须包含userId
     * @return TaotaoResult，成功时status为200
     */
    TaotaoResult updateUser(TbUser tbUser);
}

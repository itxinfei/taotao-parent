package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.service.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;

/**
 * 用户服务实现类
 * 提供用户注册、登录、登出、信息查询和更新等功能
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用户Mapper，用于数据库操作
     */
    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * Redis客户端，用于存储用户Session信息
     */
    @Autowired
    private JedisClient jedisClient;

    /**
     * 用户Session在Redis中的键前缀，配置于properties文件
     */
    @Value("${USER_SESSION}")
    private String USER_SESSION;

    /**
     * Session过期时间（秒），配置于properties文件
     */
    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;

    /**
     * 校验类型：用户名
     */
    private static final int CHECK_USERNAME = 1;

    /**
     * 校验类型：手机号
     */
    private static final int CHECK_PHONE = 2;

    /**
     * 校验类型：邮箱
     */
    private static final int CHECK_EMAIL = 3;

    /**
     * 校验用户数据是否可用
     * 检查用户名、手机号或邮箱是否已被注册
     * 
     * @param data 待校验的数据（用户名、邮箱或手机号）
     * @param type 校验类型：1-用户名，2-手机号，3-邮箱
     * @return TaotaoResult，data字段为布尔值，表示是否可用
     */
    @Override
    public TaotaoResult checkUserData(String data, int type) {
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        
        // 根据校验类型设置查询条件
        if (type == CHECK_USERNAME) {
            criteria.andUsernameEqualTo(data);
        } else if (type == CHECK_PHONE) {
            criteria.andPhoneEqualTo(data);
        } else if (type == CHECK_EMAIL) {
            criteria.andEmailEqualTo(data);
        } else {
            return TaotaoResult.build(400, "所传参数非法！");
        }
        
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return TaotaoResult.ok(false);
        }
        return TaotaoResult.ok(true);
    }

    /**
     * 用户注册
     * 创建新用户账号，密码经过MD5加密后存储
     * 
     * @param tbUser 用户信息对象，包含用户名、密码、邮箱、手机号
     * @return TaotaoResult，成功时status为200，失败时包含错误信息
     */
    @Override
    public TaotaoResult register(TbUser tbUser) {
        // 校验用户名
        if (StringUtils.isBlank(tbUser.getUsername())) {
            return TaotaoResult.build(400, "用户名不能为空！");
        }
        TaotaoResult taotaoResult = checkUserData(tbUser.getUsername(), CHECK_USERNAME);
        if (!(Boolean) taotaoResult.getData()) {
            return TaotaoResult.build(400, "用户名不能重复！");
        }
        
        // 校验密码
        if (StringUtils.isBlank(tbUser.getPassword())) {
            return TaotaoResult.build(400, "密码不能为空！");
        }
        
        // 校验手机号（可选）
        if (StringUtils.isNotBlank(tbUser.getPhone())) {
            taotaoResult = checkUserData(tbUser.getPhone(), CHECK_PHONE);
            if (!(Boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "电话不能重复！");
            }
        }
        
        // 校验邮箱（可选）
        if (StringUtils.isNotBlank(tbUser.getEmail())) {
            taotaoResult = checkUserData(tbUser.getEmail(), CHECK_EMAIL);
            if (!(Boolean) taotaoResult.getData()) {
                return TaotaoResult.build(400, "邮箱不能重复！");
            }
        }
        
        // 填充属性
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());
        
        // 密码MD5加密
        String md5Str = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(md5Str);
        
        // 插入数据库
        tbUserMapper.insert(tbUser);
        return TaotaoResult.ok();
    }

    /**
     * 用户登录
     * 验证用户名和密码，登录成功后生成Token并存储到Redis
     * 
     * @param username 用户名
     * @param password 密码（明文）
     * @return TaotaoResult，成功时data字段为Token值
     */
    @Override
    public TaotaoResult login(String username, String password) {
        // 1. 根据用户名查询用户
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        
        if (list == null || list.size() == 0) {
            return TaotaoResult.build(400, "用户名或密码不正确！");
        }
        
        // 2. 验证密码（MD5加密比对）
        TbUser user = list.get(0);
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return TaotaoResult.build(400, "用户名或密码不正确！");
        }
        
        // 3. 生成Token（UUID）
        String token = UUID.randomUUID().toString();
        
        // 4. 将用户信息存入Redis（不包含密码）
        user.setPassword(null);
        jedisClient.set(USER_SESSION + ":" + token, JSON.toJSONString(user));
        
        // 5. 设置过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        
        // 6. 返回Token
        return TaotaoResult.ok(token);
    }

    /**
     * 根据Token获取用户信息
     * 从Redis中读取Token对应的用户信息，并刷新过期时间
     * 
     * @param token 用户登录Token
     * @return TaotaoResult，data字段为TbUser对象
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
        String json = jedisClient.get(USER_SESSION + ":" + token);
        
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "token已过期！");
        }
        
        // 将JSON转换为用户对象
        TbUser user = JSON.parseObject(json, TbUser.class);
        
        // 刷新Token过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        
        return TaotaoResult.ok(user);
    }

    /**
     * 用户登出
     * 将Token过期时间设置为0，使Token立即失效
     * 
     * @param token 用户登录Token
     * @return TaotaoResult，成功时status为200
     */
    @Override
    public TaotaoResult logout(String token) {
        jedisClient.expire(USER_SESSION + ":" + token, 0);
        return TaotaoResult.ok();
    }

    /**
     * 更新用户信息
     * 更新用户的邮箱、手机号、昵称等信息，密码更新时需重新加密
     * 
     * @param tbUser 用户信息对象，必须包含userId
     * @return TaotaoResult，成功时status为200
     */
    @Override
    public TaotaoResult updateUser(TbUser tbUser) {
        TbUser oldUser = tbUserMapper.selectByPrimaryKey(tbUser.getId());
        if (oldUser == null) {
            return TaotaoResult.build(400, "用户不存在");
        }

        // 如果更新密码，需要重新加密
        if (StringUtils.isNotBlank(tbUser.getPassword())) {
            tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
        } else {
            tbUser.setPassword(null);
        }

        // 更新时间戳
        tbUser.setUpdated(new Date());
        
        // 更新用户信息（选择性更新）
        tbUserMapper.updateByPrimaryKeySelective(tbUser);

        return TaotaoResult.ok();
    }
}

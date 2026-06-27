package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 购物车Controller
 * <p>
 * 采用Cookie + Redis双存储方案：
 * 未登录用户购物车存储在Cookie中，
 * 登录用户额外同步到Redis，实现跨设备数据共享。
 * </p>
 *
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
@Controller
public class CartController {

    /** 商品信息服务 */
    @Autowired
    private ItemService itemService;

    /** SSO用户服务，用于获取登录用户信息 */
    @Autowired
    private UserService userService;

    /** Redis连接池 */
    @Autowired
    private JedisPool jedisPool;

    /** 购物车在Cookie中的存储Key */
    @Value("${CART_KEY}")
    private String CART_KEY;

    /** 购物车Cookie过期时间，单位：秒 */
    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    /** 购物车在Redis中的Hash Key前缀 */
    @Value("${CART_REDIS_KEY}")
    private String CART_REDIS_KEY;

    /**
     * 添加商品到购物车
     * <p>
     * 1. 从Cookie中获取现有购物车列表<br>
     * 2. 判断商品是否已在购物车中，存在则增加数量，不存在则新增<br>
     * 3. 将购物车列表写回Cookie<br>
     * 4. 若用户已登录，同步购物车到Redis
     * </p>
     *
     * @param itemId  商品ID
     * @param num     购买数量
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 添加成功页面
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num,
                              HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> cartItemList = getCartItemList(request);

        boolean flag = false;
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(tbItem.getNum() + num);
                flag = true;
                break;
            }
        }

        if (!flag) {
            TbItem tbItem = itemService.getItemById(itemId);
            tbItem.setNum(num);
            String image = tbItem.getImage();
            if (!StringUtils.isBlank(image)) {
                String[] images = image.split(",");
                tbItem.setImage(images[0]);
            }
            cartItemList.add(tbItem);
        }

        CookieUtils.setCookie(request, response, CART_KEY, JSON.toJSONString(cartItemList),
                CART_EXPIRE, true);

        syncCartToRedis(request, cartItemList);

        return "cartSuccess";
    }

    /**
     * 从Cookie中获取购物车商品列表
     *
     * @param request HTTP请求
     * @return 购物车商品列表，若Cookie中无数据则返回空列表
     */
    private List<TbItem> getCartItemList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)) {
            return new ArrayList<TbItem>();
        }
        return JSON.parseArray(json, TbItem.class);
    }

    /**
     * 从请求中获取已登录用户信息
     * <p>
     * 读取TT_TOKEN Cookie中的Token值，调用SSO服务验证Token有效性，
     * 若Token有效则返回用户对象，否则返回null。
     * 此操作不应影响主业务流程，异常时静默处理。
     * </p>
     *
     * @param request HTTP请求
     * @return 登录用户信息，未登录或Token过期返回null
     */
    private TbUser getUserFromToken(HttpServletRequest request) {
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN", false);
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            TaotaoResult result = userService.getUserByToken(token);
            if (result != null && result.getStatus() == 200) {
                return (TbUser) result.getData();
            }
        } catch (Exception e) {
            // 查询用户信息失败不影响主业务流程
        }
        return null;
    }

    /**
     * 同步购物车到Redis
     * <p>
     * 仅当用户已登录时执行同步，将Cookie中的购物车数据同步到Redis，
     * Redis中以Hash结构存储，key格式为"${CART_REDIS_KEY}:${userId}"，
     * field为商品ID，value为商品JSON字符串。
     * </p>
     *
     * @param request      HTTP请求
     * @param cartItemList 购物车商品列表
     */
    private void syncCartToRedis(HttpServletRequest request, List<TbItem> cartItemList) {
        TbUser user = getUserFromToken(request);
        if (user == null) {
            return;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String key = CART_REDIS_KEY + ":" + user.getId();
            for (TbItem item : cartItemList) {
                jedis.hset(key, item.getId().toString(), JSON.toJSONString(item));
            }
            jedis.expire(key, 604800);
        } catch (Exception e) {
            // Redis同步失败不影响主业务流程
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 展示购物车列表页面
     * <p>
     * 从Cookie中加载购物车列表，若用户已登录则合并Redis中的购物车数据。
     * Redis中的数据用于补充Cookie中不存在但Redis中存在的商品，
     * 确保用户跨设备登录后数据完整。
     * </p>
     *
     * @param request HTTP请求
     * @return 购物车列表页面
     */
    @RequestMapping("/cart/cart")
    public String showCartList(HttpServletRequest request) {
        List<TbItem> cartItemList = getCartItemList(request);

        TbUser user = getUserFromToken(request);
        if (user != null) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                String key = CART_REDIS_KEY + ":" + user.getId();
                List<String> jsonList = jedis.hvals(key);
                if (jsonList != null) {
                    for (String json : jsonList) {
                        TbItem redisItem = JSON.parseObject(json, TbItem.class);
                        boolean found = false;
                        for (TbItem cookieItem : cartItemList) {
                            if (cookieItem.getId() == redisItem.getId().longValue()) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            cartItemList.add(redisItem);
                        }
                    }
                }
            } catch (Exception e) {
                // Redis加载失败不影响主业务流程
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }

        request.setAttribute("cartList", cartItemList);
        return "cart";
    }

    /**
     * 更新购物车商品数量
     *
     * @param itemId  商品ID
     * @param num     更新后的数量
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 操作结果
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public TaotaoResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num,
                                       HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> cartItemList = getCartItemList(request);
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(num);
                break;
            }
        }

        CookieUtils.setCookie(request, response, CART_KEY, JSON.toJSONString(cartItemList),
                CART_EXPIRE, true);

        syncCartToRedis(request, cartItemList);

        return TaotaoResult.ok();
    }

    /**
     * 从购物车中删除商品
     * <p>
     * 从Cookie和Redis中同时删除指定商品，保持两端数据一致。
     * </p>
     *
     * @param itemId  商品ID
     * @param request HTTP请求
     * @param response HTTP响应
     * @return 重定向到购物车列表页面
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {
        List<TbItem> cartItemList = getCartItemList(request);
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                cartItemList.remove(tbItem);
                break;
            }
        }

        CookieUtils.setCookie(request, response, CART_KEY, JSON.toJSONString(cartItemList),
                CART_EXPIRE, true);

        TbUser user = getUserFromToken(request);
        if (user != null) {
            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                String key = CART_REDIS_KEY + ":" + user.getId();
                jedis.hdel(key, itemId.toString());
            } catch (Exception e) {
                // Redis删除失败不影响主业务流程
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
        }

        return "redirect:/cart/cart.html";
    }
}

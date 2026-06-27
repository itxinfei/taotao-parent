package com.taotao.order.job;

import com.taotao.mapper.TbOrderMapper;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 订单超时关闭定时任务
 * 由Quartz调度器定期执行，扫描所有未付款且创建时间超过2天的订单，
 * 将其状态更新为已关闭状态
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class OrderJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderJob.class);

    /**
     * 订单状态：未付款
     */
    private static final Integer ORDER_STATUS_UNPAID = 1;

    /**
     * 订单状态：已关闭
     */
    private static final Integer ORDER_STATUS_CLOSED = 6;

    /**
     * 订单Mapper，用于订单数据库操作
     */
    @Autowired
    private TbOrderMapper orderMapper;

    /**
     * 超时阈值：2天（毫秒）
     */
    private static final long TIMEOUT_MILLIS = 2 * 24 * 3600 * 1000L;

    /**
     * 执行订单超时关闭任务
     * <p>
     * 查询所有未付款且超过2天的订单，逐条更新为关闭状态。
     * 执行结果通过日志记录，便于监控任务执行情况。
     * </p>
     */
    public void execute() {
        logger.info("订单超时关闭任务开始执行");

        TbOrderExample example = new TbOrderExample();
        example.createCriteria()
                .andStatusEqualTo(ORDER_STATUS_UNPAID)
                .andCreateTimeLessThan(new Date(System.currentTimeMillis() - TIMEOUT_MILLIS));

        List<TbOrder> orderList = orderMapper.selectByExample(example);

        for (TbOrder order : orderList) {
            order.setStatus(ORDER_STATUS_CLOSED);
            order.setCloseTime(new Date());
            orderMapper.updateByPrimaryKey(order);
            logger.info("关闭超时订单: {}", order.getOrderId());
        }

        logger.info("订单超时关闭任务结束, 共关闭{}个订单", orderList.size());
    }
}

package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ActiveMQ消息监听器
 * 接收并处理ActiveMQ发送的消息
 * 
 * @author taotao
 * @version 1.0.0
 * @since 2024-01-01
 */
public class MyMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    /**
     * 处理接收到的消息
     * 
     * @param message ActiveMQ消息对象
     */
    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            logger.info("接收到ActiveMQ消息：{}", text);
        } catch (JMSException e) {
            logger.error("接收ActiveMQ消息失败", e);
        }
    }

}
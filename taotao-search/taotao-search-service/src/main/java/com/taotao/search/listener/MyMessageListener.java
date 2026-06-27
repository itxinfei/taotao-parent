package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接收ActiveMQ发送的消息
 */
public class MyMessageListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MyMessageListener.class);

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            System.out.println(text);
        } catch (JMSException e) {
            logger.error("接收ActiveMQ消息失败", e);
        }
    }

}
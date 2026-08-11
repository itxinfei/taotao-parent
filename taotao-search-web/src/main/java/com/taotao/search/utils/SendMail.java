package com.taotao.search.utils;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送邮件
 * <p>
 * 邮箱账号、授权码及收件人地址均通过系统属性或环境变量注入，
 * 避免在源码中硬编码敏感凭据。
 * </p>
 */
public class SendMail {

    private static final Logger logger = LoggerFactory.getLogger(SendMail.class);

    /** 发件邮箱地址 */
    private static final String MAIL_FROM = getConfig("mail.from", "MAIL_FROM");

    /** 发件邮箱授权码（非登录密码） */
    private static final String MAIL_AUTH_CODE = getConfig("mail.authCode", "MAIL_AUTH_CODE");

    /** 收件人邮箱地址 */
    private static final String MAIL_TO = getConfig("mail.to", "MAIL_TO");

    /** SMTP服务器 */
    private static final String SMTP_HOST = "smtp.163.com";

    /** SMTP端口 */
    private static final String SMTP_PORT = "25";

    /**
     * 从系统属性或环境变量读取配置，保持 JDK 1.7 兼容
     *
     * @param property 系统属性名
     * @param envName  环境变量名
     * @return 配置值，未配置返回空字符串
     */
    private static String getConfig(String property, String envName) {
        String value = System.getProperty(property);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        String envValue = System.getenv(envName);
        return envValue == null ? "" : envValue;
    }

    /**
     * 校验邮件配置是否完整
     */
    private static boolean isConfigReady() {
        return !MAIL_FROM.isEmpty() && !MAIL_AUTH_CODE.isEmpty() && !MAIL_TO.isEmpty();
    }

    //发送一个邮件
    public static void sendEmail(String subject, String text) throws MessagingException {
        if (!isConfigReady()) {
            logger.warn("邮件配置缺失，跳过发送（需配置 mail.from/mail.authCode/mail.to 或对应环境变量）");
            return;
        }
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");//设置访问smtp服务器需要认证
        properties.setProperty("mail.transport.protocol", "smtp"); //设置访问服务器的协议
        properties.setProperty("mail.host", SMTP_HOST);
        properties.setProperty("mail.smtp.port", SMTP_PORT);

        Session session = Session.getDefaultInstance(properties);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(MAIL_FROM));
        msg.setSubject(subject); //设置邮件主题
        msg.setText(text); //设置邮件内容

        Transport trans = session.getTransport();
        trans.connect(SMTP_HOST, Integer.parseInt(SMTP_PORT), MAIL_FROM, MAIL_AUTH_CODE);
        trans.sendMessage(msg, new Address[]{new InternetAddress(MAIL_TO)}); //发送邮件

        trans.close(); //关闭连接
    }

    //群发一封邮件
    public static void groupSendEmail(String subject, String content) throws MessagingException {
        if (!isConfigReady()) {
            logger.warn("邮件配置缺失，跳过发送（需配置 mail.from/mail.authCode/mail.to 或对应环境变量）");
            return;
        }
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", SMTP_HOST);
        props.setProperty("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MAIL_FROM, MAIL_AUTH_CODE);
            }
        });

        Message msg = new MimeMessage(session);
        //邮件发送者
        msg.setFrom(new InternetAddress(MAIL_FROM));
        msg.setSubject(subject);
        //注意第二个参数要写成"text/html;charset=utf-8"，表明这是一封html邮件
        msg.setContent(content, "text/html;charset=utf-8");
        //要群发给哪些邮箱
        msg.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(MAIL_TO));
        Transport.send(msg);
    }

    public static void main(String[] args) {
        try {
            groupSendEmail("出现了异常，请及时处理！", "内容管理系统出现重大错误，请及时进行处理");
        } catch (MessagingException e) {
            logger.error("发送群发邮件失败", e);
        }
    }
}

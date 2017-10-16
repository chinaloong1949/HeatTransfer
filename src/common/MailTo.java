/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author yk
 */
public class MailTo extends JFrame {

    JTextArea textArea = new JTextArea("");
    JTextField contactAddress = new JTextField("");
    JButton sendButton = new JButton("发送");
    String emailAddress;

    public MailTo(String emailBox) {
        this.emailAddress = emailBox;
        this.setTitle("发送邮件");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        bottomPanel.setLayout(new BorderLayout());

        JLabel jLabel1 = new JLabel("您的联系方式：");
        Box box1 = Box.createHorizontalBox();
        box1.add(jLabel1);
        box1.add(contactAddress);
        topPanel.add(box1, BorderLayout.CENTER);

        textArea.setToolTipText("在此输入你遇到的问题");
        bottomPanel.add(textArea, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.SOUTH);

        splitPane1.add(topPanel);
        splitPane1.add(bottomPanel);

        this.add(splitPane1);
        this.setVisible(true);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenWidth = toolkit.getScreenSize().width;
        int screenHeight = toolkit.getScreenSize().height;
        this.setLocation(screenWidth / 4, screenHeight / 4);
        this.setSize(screenWidth / 2, screenHeight / 2);

        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                send();
            }
        });

    }

    public void send() {
        String content = textArea.getText() + "发送人信息：" + contactAddress.getText();

        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost("smtp.163.com");
        mailInfo.setMailServerPort("25");
        mailInfo.setValidate(true);
        mailInfo.setUserName("soft20141224@163.com");
        mailInfo.setPassword("20141224");
        mailInfo.setFromAddress("soft20141224@163.com");
        mailInfo.setToAddress(emailAddress);
        mailInfo.setSubject("问题反馈");
        mailInfo.setContent(content);

        SimpleMailSender sms = new SimpleMailSender();
        sms.sendTextMail(mailInfo);

    }

    class MyAuthenticator extends Authenticator {

        String userName = null;
        String password = null;

        public MyAuthenticator() {

        }

        public MyAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        protected PasswordAuthentication getPasswordAuthenticator() {
            return new PasswordAuthentication(userName, password);
        }

    }

    class MailSenderInfo {

        private String mailServerHost = null;
        private String mailServerPort = "25";
        private String fromAddress;
        private String toAddress;
        private String userName = "soft20141224@163.com";
        private String password = "20141224";
        private boolean validate = true;//是否需要验证
        private String subject;//邮件主题
        private String content;//邮件文本内容
        private String[] attachFileNames;//邮件附件的文件名

        //获得邮件会话属性
        public Properties getProperties() {
            Properties p = new Properties();
            p.put("mail.smtp.host", this.getMailServerHost());
            p.put("mail.smtp.port", this.getMailServerPort());
            p.put("mail.transport.protocol", "smtp");
            //p.put("mail.smtp.auth", validate ? "true" : "false");
            if (isValidate()) {
                p.put("mail.smtp.auth", "true");
            } else {
                p.put("mail.smtp.auth", "false");
            }
            p.put("mail.smtp.user", userName);
            p.put("mail.smtp.password", password);

            return p;
        }

        /**
         * @return the mailServerHost
         */
        public String getMailServerHost() {
            return mailServerHost;
        }

        /**
         * @param mailServerHost the mailServerHost to set
         */
        public void setMailServerHost(String mailServerHost) {
            this.mailServerHost = mailServerHost;
        }

        /**
         * @return the mailServerPort
         */
        public String getMailServerPort() {
            return mailServerPort;
        }

        /**
         * @param mailServerPort the mailServerPort to set
         */
        public void setMailServerPort(String mailServerPort) {
            this.mailServerPort = mailServerPort;
        }

        /**
         * @return the fromAddress
         */
        public String getFromAddress() {
            return fromAddress;
        }

        /**
         * @param fromAddress the fromAddress to set
         */
        public void setFromAddress(String fromAddress) {
            this.fromAddress = fromAddress;
        }

        /**
         * @return the toAddress
         */
        public String getToAddress() {
            return toAddress;
        }

        /**
         * @param toAddress the toAddress to set
         */
        public void setToAddress(String toAddress) {
            this.toAddress = toAddress;
        }

        /**
         * @return the userName
         */
        public String getUserName() {
            return userName;
        }

        /**
         * @param userName the userName to set
         */
        public void setUserName(String userName) {
            this.userName = userName;
        }

        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password the password to set
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * @return the validate
         */
        public boolean isValidate() {
            return validate;
        }

        /**
         * @param validate the validate to set
         */
        public void setValidate(boolean validate) {
            this.validate = validate;
        }

        /**
         * @return the subject
         */
        public String getSubject() {
            return subject;
        }

        /**
         * @param subject the subject to set
         */
        public void setSubject(String subject) {
            this.subject = subject;
        }

        /**
         * @return the content
         */
        public String getContent() {
            return content;
        }

        /**
         * @param content the content to set
         */
        public void setContent(String content) {
            this.content = content;
        }

        /**
         * @return the attachFileNames
         */
        public String[] getAttachFileNames() {
            return attachFileNames;
        }

        /**
         * @param attachFileNames the attachFileNames to set
         */
        public void setAttachFileNames(String[] attachFileNames) {
            this.attachFileNames = attachFileNames;
        }

    }

    class SimpleMailSender {

        //该类作用是将mailSenderInfo发送出去
        public boolean sendTextMail(MailSenderInfo mailSenderInfo) {
            MyAuthenticator authenticator = null;
            Properties pro = mailSenderInfo.getProperties();
            //判断是否需要身份验证
            //if (mailSenderInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailSenderInfo.getUserName(), mailSenderInfo.getPassword());
            //}
            //根据邮件会话属性和密码验证器构造一个发送邮件的Session
            Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
            try {
                //根据Session创建一个邮件消息
                Message mailMessage = new MimeMessage(sendMailSession);
                //创建邮件发送者地址
                Address from = new InternetAddress(mailSenderInfo.getFromAddress());
                //设置邮件消息的发送者
                mailMessage.setFrom(from);
                //创建邮件的接收者地址，并设置到邮件消息中
                Address to = new InternetAddress(mailSenderInfo.getToAddress());
                mailMessage.setRecipient(Message.RecipientType.TO, to);
                //设置邮件消息的主题
                mailMessage.setSubject(mailSenderInfo.getSubject());
                mailMessage.setSentDate(new Date());
                mailMessage.setText(mailSenderInfo.getContent());
                //此处如果直接使用Transport.send(mailMessage);会报错，原因不清楚，在项目foxmail中这样使用不会报错
                Transport transport = sendMailSession.getTransport("smtp");
                transport.connect(mailSenderInfo.getMailServerHost(), mailSenderInfo.getUserName(), mailSenderInfo.getPassword());
                transport.sendMessage(mailMessage, mailMessage.getAllRecipients());

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}

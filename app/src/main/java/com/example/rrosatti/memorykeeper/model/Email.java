package com.example.rrosatti.memorykeeper.model;

import java.util.Properties;
import java.util.Random;

/**
 * Created by root on 26/10/17.
 */

public class Email {
    private final static String arrayPossible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String myEmail;
    private String toUser; //After do this with Array
    private String subject;
    private String text;
    private Properties props = new Properties();

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public Email(){
        this.props.put("mail.smtp.host", "smtp.gmail.com");
        this.props.put("mail.smtp.socketFactory.port", "465");
        this.props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        this.props.put("mail.smtp.auth", "true");

        this.props.put("mail.smtp.port", "465");
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText() {
        Random random = new Random();
        String textFinal = "";
        int number;

        for(int i = 0; i<8; i++) {
            number = random.nextInt(63);
            textFinal += arrayPossible.charAt(number);
        }

        this.text = textFinal;
    }
}

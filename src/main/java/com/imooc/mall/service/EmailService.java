package com.imooc.mall.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);

    Boolean saveEmailToRedis(String emailAddress, String verificationCode);

    Boolean checkEmailAndCode(String emailAddress, String verificationCode);
}

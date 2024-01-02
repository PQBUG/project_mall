package com.imooc.mall.service.impl;

import com.imooc.mall.common.Constant;
import com.imooc.mall.service.EmailService;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {

//    @Autowired
//    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(Constant.EMAIL_FROM);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
//        mailSender.send(simpleMailMessage);
    }

    @Override
    public Boolean saveEmailToRedis(String emailAddress, String verificationCode) {
        RedissonClient client = Redisson.create();
        RBucket<String> bucket = client.getBucket(emailAddress);
        boolean exists = bucket.isExists();
        if (!exists) {
            bucket.set(verificationCode, 60, TimeUnit.SECONDS);
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkEmailAndCode(String emailAddress, String verificationCode) {
        RedissonClient client = Redisson.create();
        RBucket<String> bucket = client.getBucket(emailAddress);
        boolean exists = bucket.isExists();
        if (exists) {
            String code = bucket.get();
            return code.equals(verificationCode);
        }
        return false;
    }
}

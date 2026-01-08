package com.rishikesh.user.service;

import com.rishikesh.user.dto.EmailReqDto;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class Services {


    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redis;

    public Services(JavaMailSender javaMailSender, StringRedisTemplate redis) {
        this.javaMailSender = javaMailSender;
        this.redis = redis;
    }

    public void sendEmail (String to, String subject, String body){
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject(subject);
            simpleMailMessage.setText(body);
            javaMailSender.send(simpleMailMessage);

    }

    @Async
    public void storeOtp(String email, String otp) {
        redis.opsForValue().set("otp:" + email, otp, 600, TimeUnit.SECONDS);
        redis.opsForValue().set("otp:attempts:" + email, "3", 600, TimeUnit.SECONDS);
    }

    public String getOtp(String email) {

        return redis.opsForValue().get("otp:" + email);
    }

    public Long decrementAttempts(String email) {
        return redis.opsForValue().decrement("otp:attempts:" + email);
    }


    public boolean checkOtp(EmailReqDto emailReq) {

        String otp = getOtp(emailReq.getEmail());

        if (otp == null) {

             return false;
        }

        String keyOtp = "otp:" + emailReq.getEmail();
        String keyAttempts = "otp:attempts:" + emailReq.getEmail();

        Long remaining = decrementAttempts(emailReq.getEmail());
        if (remaining == null || remaining < 0) {
            // attempts exhausted => clean up

            redis.delete(keyOtp);
            redis.delete(keyAttempts);
            return false;
        }


        boolean match = otp.equals(emailReq.getOtp());

        if (match) {
            // SUCCESS → delete keys so OTP cannot be reused
            redis.delete(keyOtp);
            redis.delete(keyAttempts);
            return true;
        } else {
            if (remaining == 0) {
                // last attempt failed → clean up
                redis.delete(keyOtp);
                redis.delete(keyAttempts);
            }

            return false;
        }

    }
}


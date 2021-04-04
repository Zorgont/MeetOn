package com.example.meetontest.mail;

public interface EmailService {
    void sendSimpleMessage(String to,String subject,String text);
}

//package com.capgemini.wsb.fitnesstracker.mail.internal;
//
//import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
//import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Profile;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//class EmailSenderImpl implements EmailSender {
//    private final JavaMailSender javaMailSender;
//    @Override
//    public void send(EmailDto email) {
//        final SimpleMailMessage simpleEmail = new SimpleMailMessage();
//        simpleEmail.setTo(email.toAddress());
//        simpleEmail.setSubject(email.subject());
//        simpleEmail.setText(email.content());
//        javaMailSender.send(simpleEmail);
//    }
//}
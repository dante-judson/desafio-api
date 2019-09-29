package com.judson.desafio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Value("${spring.mail.username}")
	private String appEmail;
	
	@Autowired
	private MailSender sender;
	
	public void sendEmail(String body, String to, String subject) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(subject);
		message.setTo(to);
		message.setFrom(appEmail);
		
		sender.send(message);
		
	}

}

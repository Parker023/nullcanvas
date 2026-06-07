package com.nullcanvas.blogapp.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	private final JavaMailSender mailSender;

	@Value("${app.mail.from}")
	private String fromEmail;

	public void sendVerificationOtp(String toEmail, String otp) {

		try {

			SimpleMailMessage message = getSimpleMailMessage(toEmail, otp);

			mailSender.send(message);

			log.info("Verification OTP email sent to {}", toEmail);

		}
		catch (Exception ex) {

			log.error("Failed to send verification email to {}", toEmail, ex);

			throw new RuntimeException("Failed to send email");
		}
	}

	private SimpleMailMessage getSimpleMailMessage(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(fromEmail);

		message.setTo(toEmail);

		message.setSubject("Verify your NullCanvas account");

		message.setText("""
				Welcome to NullCanvas!

				Your verification OTP is:

				%s

				This OTP will expire in 5 minutes.

				If you did not request this,
				please ignore this email.
				""".formatted(otp));
		return message;
	}

}
package com.nullcanvas.blogapp.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Sends transactional emails, currently the account-verification OTP.
 *
 * <p>
 * Wraps {@link JavaMailSender} so callers do not deal with message construction, and
 * surfaces send failures as an exception they can react to.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

	/** Spring's mail sender used to dispatch the SMTP message. */
	private final JavaMailSender mailSender;

	/** The "from" address shown on outgoing mail, taken from configuration. */
	@Value("${app.mail.from}")
	private String fromEmail;

	/**
	 * Sends the verification OTP to the given recipient.
	 * @param toEmail the recipient's email address
	 * @param otp the one-time password to include in the message
	 * @throws RuntimeException if the message could not be sent
	 */
	public void sendVerificationOtp(String toEmail, String otp) {

		try {

			// Build the pre-formatted verification message.
			SimpleMailMessage message = getSimpleMailMessage(toEmail, otp);

			mailSender.send(message);

			log.info("Verification OTP email sent to {}", toEmail);

		}
		catch (Exception ex) {
			// Log the cause but surface a generic failure so callers don't leak SMTP
			// details.
			log.error("Failed to send verification email to {}", toEmail, ex);

			throw new RuntimeException("Failed to send email");
		}
	}

	/**
	 * Builds the verification email with the sender, recipient, subject and OTP body.
	 * @param toEmail the recipient's email address
	 * @param otp the OTP to embed in the body
	 * @return the ready-to-send mail message
	 */
	private SimpleMailMessage getSimpleMailMessage(String toEmail, String otp) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(fromEmail);

		message.setTo(toEmail);

		message.setSubject("Verify your NullCanvas account");

		// Interpolate the OTP into the templated body; the 5-minute notice mirrors the
		// OTP cache TTL.
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
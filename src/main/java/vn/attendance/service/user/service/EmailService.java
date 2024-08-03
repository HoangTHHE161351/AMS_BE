package vn.attendance.service.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleMessage(String to, String header, String name, String subject,
                                  String content, String link, int option) throws MessagingException {
        Context context = new Context();
        context.setVariable("header", header);
        context.setVariable("name", name);
        context.setVariable("content", content);
        context.setVariable("link", link);

        String emailBody;

        switch (option){
            case 2:
                emailBody = templateEngine.process("SendEmailOTP", context);
                break;
            case 3:
                emailBody = templateEngine.process("SendEmailCameraAccess", context);
                break;
            default:
                emailBody = templateEngine.process("SendEmail", context);
        }
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(emailBody);

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(emailBody, true);
//        message.setRecipients(MimeMessage.RecipientType.TO, to);
//        message.setSubject(subject);
//        message.setContent(emailBody, "text/html; charset=utf-8");
        // Thêm hình ảnh đính kèm
        ClassPathResource image = new ClassPathResource("static/images/ams-high-resolution-logo.png");
        helper.addInline("logo", image);

        emailSender.send(message);
    }

    //Ma hoa email ve dang exam****il@gmail.com
    public String maskEmail(String email) {

        // Tách phần tên người dùng và phần domain
        int atIndex = email.indexOf("@");
        if (atIndex <= 0) {
            throw new IllegalArgumentException("Invalid email format");
        }

        String username = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        // Tính toán số lượng ký tự để giữ nguyên và số ký tự thay thế
        int usernameLength = username.length();
        if (usernameLength <= 2) {
            throw new IllegalArgumentException("Email username is too short to mask");
        }

        int numToShow = Math.min(3, usernameLength / 2); // số ký tự đầu để giữ nguyên
        int numToEnd = 2; // số ký tự cuối để giữ nguyên
        int numToMask = usernameLength - numToShow - numToEnd; // số ký tự để thay thế

        // Xây dựng tên người dùng đã được mã hóa
        StringBuilder maskedUsername = new StringBuilder();
        maskedUsername.append(username.substring(0, numToShow));
        for (int i = 0; i < numToMask; i++) {
            maskedUsername.append('*');
        }
        maskedUsername.append(username.substring(usernameLength - numToEnd));

        // Kết hợp lại phần tên người dùng đã mã hóa với domain
        return maskedUsername.toString() + domain;
    }
}

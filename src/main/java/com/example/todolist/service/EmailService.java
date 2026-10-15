package com.example.todolist.service;

import com.example.todolist.model.Todo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendTodoNotificationEmail(String recipient, Todo todo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("todo", todo);
            String htmlContent = templateEngine.process("mail/todo-notification", context);

            helper.setTo(recipient);
            helper.setSubject("New Todo Created: " + todo.getTitle());
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("[EMAIL ASYNC] Notification email successfully sent to {}", recipient);
        } catch (MessagingException e) {
            log.error("[EMAIL ERROR] Failed to send email to {}: {}", recipient, e.getMessage());
        }
    }

}

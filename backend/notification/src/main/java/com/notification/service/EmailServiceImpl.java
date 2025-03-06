package com.notification.service;

import com.common.NotificationDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @KafkaListener(topics = "order-placed-topic", groupId = "order-notifications")
    public void sendOrderPlacedEmail(NotificationDto notificationDto) {
        sendEmail(notificationDto, "Order Confirmation", "Your order has been placed successfully!");
    }

    @KafkaListener(topics = "order-delivered-topic", groupId = "order-notifications")
    public void sendOrderDeliveredEmail(NotificationDto notificationDto) {
        sendEmail(notificationDto, "Order Delivered", "Your order has been successfully delivered!");
    }

    @KafkaListener(topics = "order-cancelled-topic", groupId = "order-notifications")
    public void sendOrderCancelledEmail(NotificationDto notificationDto) {
        sendEmail(notificationDto, "Order Cancelled", "Your order has been cancelled.");
    }

    private void sendEmail(NotificationDto notificationDto, String subject, String messageBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(notificationDto.getTo());
            helper.setSubject(subject + ": " + notificationDto.getProductName());
            helper.setFrom("sudeev.divakar@gmail.com");

            String emailContent = "<html><body>" +
                    "<p>Dear Customer,</p>" +
                    "<p>" + messageBody + "</p>" +
                    "<p><strong>Order Details:</strong></p>" +
                    "<ul>" +
                    "<li><strong>Product:</strong> " + notificationDto.getProductName() + "</li>" +
                    "<li><strong>Quantity:</strong> " + notificationDto.getQuantity() + "</li>" +
                    "<li><strong>Total Amount:</strong> INR " + notificationDto.getAmount() + "</li>" +
                    "<li><strong>Delivery Address:</strong> " + notificationDto.getAddress() + "</li>" +
                    "</ul>" +
                    "<br><p>Best Regards,<br><strong>Sudeev's Application :D</strong></p>" +
                    "</body></html>";

            helper.setText(emailContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}

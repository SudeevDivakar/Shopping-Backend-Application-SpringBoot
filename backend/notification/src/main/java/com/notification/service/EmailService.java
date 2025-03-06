package com.notification.service;

import com.common.NotificationDto;

public interface EmailService {
    public void sendOrderPlacedEmail(NotificationDto notificationDto);
}

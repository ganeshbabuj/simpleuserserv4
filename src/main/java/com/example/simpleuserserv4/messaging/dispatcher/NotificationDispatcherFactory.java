package com.example.simpleuserserv4.messaging.dispatcher;

import com.example.simpleuserserv4.resource.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationDispatcherFactory {

    public NotificationDispatcher createNotificationDispatcher(NotificationType notificationType) {
        switch (notificationType) {
            case EMAIL:
                return new EmailNotificationDispatcher();
            case SMS:
                return new SMSNotificationDispatcher();
            default:
                throw new UnsupportedOperationException();
        }
    }


}

package com.example.simpleuserserv4.messaging.listener;

import com.example.simpleuserserv4.messaging.dispatcher.NotificationDispatcher;
import com.example.simpleuserserv4.messaging.dispatcher.NotificationDispatcherFactory;
import com.example.simpleuserserv4.resource.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    NotificationDispatcherFactory notificationDispatcherFactory;

    @JmsListener(destination = "notificationQueue", containerFactory = "myFactory")
    public void receiveMessage(Notification notification) {

        System.out.println("\n** NOTIFICATION | " + notification.getRecipient() + " | RECEIVED | " + notification + " **\n");

        NotificationDispatcher notificationDispatcher = notificationDispatcherFactory.createNotificationDispatcher(notification.getType());
        notificationDispatcher.dispatch(notification);

    }

}

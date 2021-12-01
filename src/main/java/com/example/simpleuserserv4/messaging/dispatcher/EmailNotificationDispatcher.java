package com.example.simpleuserserv4.messaging.dispatcher;

import com.example.simpleuserserv4.messaging.NotificationLogBook;
import com.example.simpleuserserv4.resource.Notification;

public class EmailNotificationDispatcher implements NotificationDispatcher {

    @Override
    public void dispatch(Notification notification) {

        try {
            // intentional to demonstrate asynchronous message processing
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            //ignore
        }

        System.out.println("\n** NOTIFICATION | " + notification.getRecipient() + " | DISPATCHED | " + notification + " **\n");

        // Just for demonstration. Avoid using singleton. instead use Spring bean
        // Get NotificationLogBook instance
        NotificationLogBook notificationLogBook = NotificationLogBook.getInstance();
        notificationLogBook.addEntry(notification);

    }
}

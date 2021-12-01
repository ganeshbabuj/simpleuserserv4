package com.example.simpleuserserv4.messaging;

import com.example.simpleuserserv4.resource.Notification;

// Singleton with double check locking
public class NotificationLogBook {

    private static NotificationLogBook instance;

    // private constructor to prevent creating objects by outside world
    private NotificationLogBook() {
    }

    public static NotificationLogBook getInstance() {

        if (instance == null) {
            // synchronized block to for thread safety
            // using synchronized block instead of the method for more granular lock
            synchronized (NotificationLogBook.class) {
                // double check - if the previous thread has already initialized
                if (instance == null) {
                    // create the instance
                    instance = new NotificationLogBook();
                }
            }
        }

        return instance;
    }

    public void addEntry(Notification notification) {

        System.out.println("NOTIFICATION | " + notification.getRecipient() + " | ADDING TO LOGBOOK "
                + " | OBJECT HASHCODE: " + System.identityHashCode(this));
    }
}

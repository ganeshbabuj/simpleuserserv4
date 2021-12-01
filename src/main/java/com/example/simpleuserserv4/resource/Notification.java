package com.example.simpleuserserv4.resource;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Notification {

    private NotificationType type;
    private String recipient;
    private String message;

    public Notification(NotificationType type, String recipient, String message) {
        this.type = type;
        this.recipient = recipient;
        this.message = message;
    }
}

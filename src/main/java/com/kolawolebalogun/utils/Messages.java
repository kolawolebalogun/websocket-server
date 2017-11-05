package com.kolawolebalogun.utils;

import com.kolawolebalogun.domain.Message;

import javax.websocket.Session;
import java.time.LocalTime;

import static java.lang.String.format;

/**
 * Created by KolawoleBalogun on 9/19/17.
 */
public class Messages {
    public static Message objectify(String content) {
        return objectify(content, "Duke Bot", LocalTime.now().toString());
    }

    public static Message objectify(String content, String sender) {
        return objectify(content, sender, LocalTime.now().toString());
    }

    public static Message objectify(String content, Session session) {
        return new Message(content, session.getUserProperties().get("userName").toString(), LocalTime.now().toString());
    }

    public static Message objectify(String content, String sender, String received) {
        return new Message(content, sender, received);
    }
}

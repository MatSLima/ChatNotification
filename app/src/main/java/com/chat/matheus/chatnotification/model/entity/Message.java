package com.chat.matheus.chatnotification.model.entity;

import java.io.Serializable;

public class Message implements Serializable {
    private int id;
    private UserChat userChat;
    private String message;

    public Message() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserChat getUserChat() {
        return userChat;
    }

    public void setUserChat(UserChat userChat) {
        this.userChat = userChat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", userChat=" + userChat +
                ", message='" + message + '\'' +
                '}';
    }
}



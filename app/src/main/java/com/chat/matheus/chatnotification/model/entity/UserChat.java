package com.chat.matheus.chatnotification.model.entity;


import com.chat.matheus.chatnotification.model.entity.Chat;

import java.util.List;

public class UserChat {
    private int id;
    private Chat chat;
    private User user;
    private List<Message> messages;

    public UserChat() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

}

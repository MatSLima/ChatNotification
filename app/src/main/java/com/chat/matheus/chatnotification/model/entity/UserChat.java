package com.chat.matheus.chatnotification.model.entity;


import com.chat.matheus.chatnotification.model.entity.Chat;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;
import java.util.List;

@DatabaseTable
public class UserChat {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private int id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Chat chat;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private User user;

    @ForeignCollectionField(eager = true)
    private Collection<Message> listMessages;

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

    public Collection<Message> getListMessages() {
        return listMessages;
    }

    public void setListMessages(Collection<Message> listMessages) {
        this.listMessages = listMessages;
    }
}

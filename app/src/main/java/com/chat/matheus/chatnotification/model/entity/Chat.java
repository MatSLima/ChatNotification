package com.chat.matheus.chatnotification.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@DatabaseTable
public class Chat implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private Date createdAt;

    @ForeignCollectionField(eager = true)
    private Collection<UserChat> listaUsers;

    private List<User> users;

    public Chat() {
        super();
        this.users = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<UserChat> getListaUsers() {
        return listaUsers;
    }

    public void setListaUsers(Collection<UserChat> listaUsers) {
        this.listaUsers = listaUsers;
    }

    @Override
    public String toString() {
        return name;
    }
}

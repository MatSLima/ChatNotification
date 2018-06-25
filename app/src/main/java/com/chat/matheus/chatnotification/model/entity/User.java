package com.chat.matheus.chatnotification.model.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@DatabaseTable
public class User implements Serializable {
    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private int id;

    @DatabaseField
    private String login;

    @DatabaseField
    private String senha;

    @ForeignCollectionField(eager = true)
    private Collection<UserChat> listaChats;

    private List<Chat> chats;

    public User() {
        this.chats = new ArrayList<>();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public Collection<UserChat> getListaChats() {
        return listaChats;
    }

    public void setListaChats(Collection<UserChat> listaChats) {
        this.listaChats = listaChats;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", senha='" + senha + '\'' +
                ", chats=" + chats +
                '}';
    }
}


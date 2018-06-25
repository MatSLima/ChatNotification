package com.chat.matheus.chatnotification.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.Message;
import com.chat.matheus.chatnotification.model.entity.UserChat;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Aluno on 16/04/2018.
 */

public class MyORMLiteHelper extends OrmLiteSqliteOpenHelper {

    //nome do banco de dados
    private static final String DATABASE_NAME = "chat.db";

    //Versão do banco de dados
    private static final int DATABASE_VERSION = 8;

    //Variávewl singleton
    private static MyORMLiteHelper mInstance = null;

    //DAO para tabelas e objetos
    Dao<Chat, Integer> daoChat;
    Dao<Message, Integer> daoMessage;
    Dao<UserChat, Integer> daoUserChat;

    public MyORMLiteHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public static MyORMLiteHelper getInstance(Context ctx){
        if(mInstance == null){
            mInstance =  new MyORMLiteHelper(ctx);
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Chat.class);
            TableUtils.createTable(connectionSource, Message.class);
            TableUtils.createTable(connectionSource, UserChat.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Chat.class, true);
            TableUtils.dropTable(connectionSource, Message.class, true);
            TableUtils.createTable(connectionSource, UserChat.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        onCreate(sqLiteDatabase, connectionSource);
    }


    public Dao<Chat, Integer> getChatDao() throws SQLException {
        if(daoChat==null){
            daoChat = getDao(Chat.class);
        }
        return daoChat;
    }

    public Dao<Message, Integer> getMessageDao() throws SQLException {
        if(daoMessage==null){
            daoMessage = getDao(Message.class);
        }
        return daoMessage;
    }

    public Dao<UserChat, Integer> getUserChatDao() throws SQLException {
        if(daoUserChat==null){
            daoUserChat = getDao(UserChat.class);
        }
        return daoUserChat;
    }

}

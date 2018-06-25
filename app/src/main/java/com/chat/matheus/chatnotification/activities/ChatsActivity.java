package com.chat.matheus.chatnotification.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.chat.matheus.chatnotification.Configuration;
import com.chat.matheus.chatnotification.model.dao.MyORMLiteHelper;
import com.chat.matheus.chatnotification.model.entity.UserChat;
import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.util.JsonDateDeserializer;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatsActivity extends AppCompatActivity {

    User user;
    ListView listLayout;
    ArrayAdapter<Chat> adapterChat;
    Map<Integer, Chat> mapChats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Bundle param = getIntent().getExtras();
        user = (User) param.getSerializable("user");
        listLayout = findViewById(R.id.listLayout);

        try {
            buscarChats();
//            buscarChatsORM();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void adicionar(View v) {
        Intent it = new Intent(this, AddChatActivity.class);
        it.putExtra("user", user);
        startActivityForResult(it, 100);
    }

    private void buscarChats() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
        String json = gson.toJson(user);

        String jsonDeResposta = HttpConnector.connect(Configuration.SERVER_IP + "/v1/api/chat", json);
        Chat[] cArray = gson.fromJson(jsonDeResposta, Chat[].class);
        List<Chat> chats = new ArrayList<>(Arrays.asList(cArray));

        adapterChat = new ArrayAdapter<Chat>(
                this,
                android.R.layout.simple_list_item_1,
                chats
        );

        listLayout.setAdapter(adapterChat);
        listLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Chat entry = (Chat) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ChatsActivity.this, MessageActivity.class);
                intent.putExtra("chat", entry);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    private void atualizarChats() {
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDateDeserializer()).create();
            String json = gson.toJson(user);

            String jsonDeResposta = HttpConnector.connect(Configuration.SERVER_IP + "/v1/api/chat", json);
            Chat[] cArray = gson.fromJson(jsonDeResposta, Chat[].class);
            List<Chat> chats = new ArrayList<>(Arrays.asList(cArray));

            Dao chatDAO = MyORMLiteHelper.getInstance(this.getApplicationContext()).getChatDao();
            for(Chat c : chats) {
                Chat chatAtual = mapChats.get(c.getId());
                if (chatAtual != null) {
                    chatAtual.setName(c.getName());
                    chatDAO.update(chatAtual);
                } else {
                    chatDAO.create(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void buscarChatsORM() {
        try {
            Dao chatDAO = MyORMLiteHelper.getInstance(this.getApplicationContext()).getChatDao();

            QueryBuilder<Chat, Integer> chatQuery = MyORMLiteHelper.getInstance(this.getApplicationContext()).getChatDao().queryBuilder();
            QueryBuilder<UserChat, Integer> userChatQuery = MyORMLiteHelper.getInstance(this.getApplicationContext()).getUserChatDao().queryBuilder();

            userChatQuery.where().eq("user_id", user.getId());
            chatQuery.join(userChatQuery);

            List<Chat> chats = chatQuery.query();
            this.setChatMap(chats);

            adapterChat = new ArrayAdapter<Chat>(
                    this,
                    android.R.layout.simple_list_item_1,
                    chats
            );

            listLayout.setAdapter(adapterChat);
            listLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Chat entry = (Chat) parent.getAdapter().getItem(position);
                    Intent intent = new Intent(ChatsActivity.this, MessageActivity.class);
                    intent.putExtra("chat", entry);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Nenhum dado foi inserido!", Toast.LENGTH_SHORT).show();
        }else if(resultCode == RESULT_OK){
            if(requestCode == 100){
                Toast.makeText(this, "Chat Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
                this.buscarChats();
            }
        }
    }

    public void setChatMap(List<Chat> chats) {
        mapChats = new HashMap<>();
        for (Chat c : chats) {
            mapChats.put(c.getId(), c);
        }
    }
}

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
import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.util.JsonDateDeserializer;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    User user;
    ListView listLayout;
    ArrayAdapter<Chat> adapterChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        Bundle param = getIntent().getExtras();
        user = (User) param.getSerializable("user");
        listLayout = findViewById(R.id.listLayout);

        try {
            buscarChats();
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
}

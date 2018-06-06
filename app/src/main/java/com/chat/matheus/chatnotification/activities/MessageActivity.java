package com.chat.matheus.chatnotification.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.Message;
import com.chat.matheus.chatnotification.model.entity.User;
import com.chat.matheus.chatnotification.model.entity.UserChat;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MessageActivity extends AppCompatActivity {

    User user;
    Chat chat;
    UserChat userChat;
    LinearLayout messages;
    EditText message;
    ScrollView scroll;
    Socket cliente;
    PrintStream saida;
    private Thread workingthread = null;

    final Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            Bundle b;
            b = msg.getData();

            String name = String.valueOf(b.getString("name"));
            String message = String.valueOf(b.getString("message"));

            if(!name.equals(user.getLogin())) {
                addMessage(message, Gravity.LEFT, name);
            } else {
                addMessage(message, Gravity.RIGHT, name);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messages = findViewById(R.id.messages);
        message = findViewById(R.id.message);
        scroll = findViewById(R.id.scroll);

        Bundle param = getIntent().getExtras();
        user = (User) param.getSerializable("user");
        chat = (Chat) param.getSerializable("chat");

        userChat = new UserChat();
        userChat.setChat(this.chat);
        userChat.setUser(this.user);

        Gson gson = new Gson();
        String json = gson.toJson(userChat);

        try {
            String jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/user/chat", json);
            JSONObject obj = new JSONObject(jsonDeResposta);
            userChat.setId((Integer) obj.get("id"));

            json = gson.toJson(this.chat);
            jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/message/messages", json);
            Message[] cArray = gson.fromJson(jsonDeResposta, Message[].class);
            List<Message> messages = new ArrayList<>(Arrays.asList(cArray));

            for(Message m : messages) {
                if(m.getUserChat().getUser().getId() == this.user.getId()) {
                    addMessage(m.getMessage(), Gravity.RIGHT, m.getUserChat().getUser().getLogin());
                } else {
                    addMessage(m.getMessage(), Gravity.LEFT, m.getUserChat().getUser().getLogin());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //THREAD DE NOTIFICAÇÕES
        try {
            cliente = new Socket("172.28.4.92", 12345);
            System.out.println("O cliente se conectou ao servidor!");

            workingthread = new JobThread(mHandler);
            workingthread.start();
            saida = new PrintStream(cliente.getOutputStream());
            saida.println("{'id':" + this.chat.getId() + "}"); //Envia identificação de qual chat é pertencente
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void adicionar(View v) {
        Intent it = new Intent(this, AddUserActivity.class);
        it.putExtra("chat", chat);
        startActivityForResult(it, 200);
    }

    public class JobThread extends Thread{

        private Handler hd;

        public JobThread(Handler msgHandler){
            this.hd = msgHandler;
        }
        public void run() {
            Bundle b = new Bundle(4);
            Scanner s = null;
            try {
                s = new Scanner(cliente.getInputStream());
                while (s.hasNextLine()) {
                    String json = s.nextLine();
                    JSONObject obj = new JSONObject(json);
                    String name = (String) obj.get("user");
                    String message = (String) obj.get("message");
                    b.putString("name", name);
                    b.putString("message", message);

                    android.os.Message msg = hd.obtainMessage();
                    msg.setData(b);
                    hd.sendMessage(msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void enviar(View v) {
        if (!message.getText().toString().isEmpty()) {
            saida.println(
                    "{'id':" + this.chat.getId() + ", " +
                    "'message': '" + message.getText().toString() + "', " +
                    "'user': '" + this.user.getLogin() + "' " +
                    "}");

            Message m = new Message();
            m.setMessage(message.getText().toString());
            m.setUserChat(this.userChat);

            Gson gson = new Gson();
            String json = gson.toJson(m);
            System.out.println("JSON: " + json);
            String jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/message", json);
            System.out.println(jsonDeResposta);
            message.setText("");
        }
    }

    private void addMessage(String text, int gravity, String user) {
        TextView newMessage = new TextView(this);
        newMessage.setText(user + "\n" + text);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 20, 0, 0 );

        llp.gravity = gravity;
        switch (gravity) {
            case Gravity.RIGHT:
                newMessage.setBackgroundColor(Color.rgb(210,247,197));
                break;
            case Gravity.LEFT:
                newMessage.setBackgroundColor(Color.rgb(218,224,234));
                break;
        }

        newMessage.setLayoutParams(llp);
        newMessage.setPadding(10, 10, 10, 10);
        messages.addView(newMessage);
        scroll.post(new Runnable() {
            @Override
            public void run() {
                if((scroll.getScrollY() * 100) / scroll.getChildAt(0).getHeight() > 80) {
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "Nenhum dado foi inserido!", Toast.LENGTH_SHORT).show();
        }else if(resultCode == RESULT_OK){
            if(requestCode == 200){
                Toast.makeText(this, "Usuário Adicionado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

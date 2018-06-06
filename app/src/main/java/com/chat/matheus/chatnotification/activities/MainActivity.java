package com.chat.matheus.chatnotification.activities;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText login, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void login(View v) {
        try {
            User u = new User();
            u.setLogin(login.getText().toString());
            u.setSenha(password.getText().toString());

            Gson gson = new Gson();
            String json = gson.toJson(u);

            String jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/user", json);

            JSONObject obj = new JSONObject(jsonDeResposta);
            u.setId((Integer) obj.get("id"));

            if(u.getId() > 0) {
                Intent it = new Intent(this, ChatsActivity.class);
                it.putExtra("user", u);
                startActivity(it);
                return;
            }

            Toast.makeText(getApplicationContext(), "Não foi encontrado nenhum usuário com este login e senha", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    public void novo(View v) {
        Intent it = new Intent(this, UserActivity.class);
        startActivity(it);
        return;
    }

}

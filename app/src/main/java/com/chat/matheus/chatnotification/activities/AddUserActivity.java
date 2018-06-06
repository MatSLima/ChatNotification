package com.chat.matheus.chatnotification.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddUserActivity extends AppCompatActivity {

    Chat chat;
    EditText userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Bundle param = getIntent().getExtras();

        chat = (Chat) param.getSerializable("chat");
        userName = findViewById(R.id.userName);
    }

    public void salvar(View v){
        Intent it = new Intent();
        if(!validarCampos()) {
            Toast.makeText(getApplicationContext(), "É necessário preencher todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        User u = new User();
        u.setLogin(this.userName.getText().toString());
        u.getChats().add(this.chat);

        Gson gson = new Gson();
        String json = gson.toJson(u);

        String jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/user/chat/new", json);
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonDeResposta);
            u.setId((Integer) obj.get("id"));

            if(u.getId() > 0) {
                it.putExtra("user", u);
                setResult(RESULT_OK, it);
                finish();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "Não existe um usuário com este nome", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean validarCampos() {
        if (this.userName.getText().toString() == null || this.userName.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }
}

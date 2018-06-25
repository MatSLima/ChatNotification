package com.chat.matheus.chatnotification.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.matheus.chatnotification.Configuration;
import com.chat.matheus.chatnotification.util.HttpConnector;
import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;
import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class AddChatActivity extends AppCompatActivity {

    EditText chatName;
    Chat chat = new Chat();
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat);

        Bundle param = getIntent().getExtras();

        user = (User) param.getSerializable("user");
        chatName = findViewById(R.id.chatName);
    }

    public void salvar(View v){
        Intent it = new Intent();
        if(!validarCampos()) {
            Toast.makeText(getApplicationContext(), "É necessário preencher todos os campos", Toast.LENGTH_LONG).show();
            return;
        }

        this.chat.setName(chatName.getText().toString());
        this.chat.getUsers().add(this.user);

        Gson gson = new Gson();
        String json = gson.toJson(chat);

        String jsonDeResposta = HttpConnector.connect(Configuration.SERVER_IP + "/v1/api/chat/new", json);
        JSONObject obj = null;
        try {
            obj = new JSONObject(jsonDeResposta);
            chat.setId((Integer) obj.get("id"));

            if(chat.getId() > 0) {
                it.putExtra("chat", chat);
                setResult(RESULT_OK, it);
                finish();
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean validarCampos() {
        if (this.chatName.getText().toString() == null || this.chatName.getText().toString().isEmpty()) {
            return false;
        }

        return true;
    }
}

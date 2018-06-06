package com.chat.matheus.chatnotification;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chat.matheus.chatnotification.model.entity.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserActivity extends AppCompatActivity {

    EditText cadLogin, cadConfSenha, cadSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        cadLogin = findViewById(R.id.cadLogin);
        cadSenha = findViewById(R.id.cadSenha);
        cadConfSenha = findViewById(R.id.cadConfSenha);
    }

    public void salvar(View v) {
        if (this.validateData()) {
            User u = new User();
            u.setLogin(cadLogin.getText().toString());
            u.setSenha(cadSenha.getText().toString());

            Gson gson = new Gson();
            String json = gson.toJson(u);

            String jsonDeResposta = HttpConnector.connect("http://172.28.4.92:8080/r8api/v1/api/user/new", json);
            JSONObject obj = null;
            try {
                obj = new JSONObject(jsonDeResposta);
                u.setId((Integer) obj.get("id"));

                if(u.getId() > 0) {
                    Intent it = new Intent(this, ChatsActivity.class);
                    it.putExtra("user", u);
                    startActivity(it);
                    return;
                } else {
                    Toast.makeText(getApplicationContext(), "Este usuário já existe", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public boolean validateData() {
        if (cadLogin.getText().toString().isEmpty() ||
                cadSenha.getText().toString().isEmpty() ||
                    cadConfSenha.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "É necessário preencher todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!cadSenha.getText().toString().equals(cadConfSenha.getText().toString())) {
            Toast.makeText(getApplicationContext(), "As senhas precisam ser iguais", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}

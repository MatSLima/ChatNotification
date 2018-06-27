package com.chat.matheus.chatnotification.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chat.matheus.chatnotification.R;
import com.chat.matheus.chatnotification.model.entity.Chat;

import java.util.ArrayList;


/**
 * Created by Aluno on 16/05/2018.
 */

public class AdapterChat extends BaseAdapter {

    ArrayList<Chat> listChat;
    LayoutInflater inflate;

    public AdapterChat(Context ctx, ArrayList<Chat> lista){
        listChat = lista;
        inflate = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return listChat.size();
    }

    @Override
    public Chat getItem(int position) {
        return listChat.get(position);
    }


    public void remove(Chat c){
        listChat.remove(c);
        notifyDataSetChanged();
    }

    public void add(Chat c){
        listChat.add(c);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Chat c = listChat.get(position);

        convertView = inflate.inflate(R.layout.item_list_view_chat, null);

        TextView tvIdCategoria = convertView.findViewById(R.id.tvIdCategoria);
        tvIdCategoria.setText(String.valueOf(c.getId()));

        TextView tvNome = convertView.findViewById(R.id.nomeChat);
        if(c.getName().equals("")) {
            tvNome.setText("Sem nome");
        }else{
            tvNome.setText(c.getName());
        }

        return convertView;
    }
}

package com.chat.matheus.chatnotification.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Aluno on 09/04/2018.
 */

public class HttpConnector {
    public static String connect(String urlS, String json) {
        URL url = null;
        try {
            url = new URL(urlS);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST"); //fala que quer um post
            connection.setRequestProperty("Content-type", "application/json"); //fala o que vai mandar
            connection.setDoOutput(true); //fala que voce vai enviar algo
            PrintStream printStream = new PrintStream(connection.getOutputStream());

            printStream.println(json); //seta o que voce vai enviar

            connection.connect(); //envia para o servidor

            Scanner s = new Scanner(connection.getInputStream());
            String jsonDeResposta = "";
            while(s.hasNext()) {
                jsonDeResposta += s.next() + " ";
            }

            return jsonDeResposta;
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

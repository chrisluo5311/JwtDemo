package com.example.logindemo.service;

import com.example.logindemo.tgbot.enums.TelegramInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Optional;

@Slf4j
@Service
public class TgService {


    public void sendMessage(String message){
        sendMessage(TelegramInfo.JWT_DEMO_ERROR_BOT,null,message);
    }

    public static void sendMessage(TelegramInfo telegramInfo, String title, String message) {
        try {
            String TG_POST_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String botToken = telegramInfo.getToken();
            String chatId = telegramInfo.getChatId();
            String text = URLEncoder.encode(Optional.ofNullable(title).orElse("") + message, "UTF-8");

            TG_POST_URL = String.format(TG_POST_URL, botToken, chatId, text);

            URL url = new URL(TG_POST_URL);

            URLConnection conn = url.openConnection();

            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String response = sb.toString();
            log.info(telegramInfo.getName() +",發送訊息: "+ message);
        } catch (Exception e) {
            log.error(telegramInfo.getName() + " fail");
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        TgService.sendMessage(TelegramInfo.JWT_DEMO_ERROR_BOT,"测试","大兽打吉");
    }

}

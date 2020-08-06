package com.sportbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SportBot extends TelegramLongPollingBot {
    private final String BotUsername="adwsportt_bot";
    private final String BotToken="";
    SendMessage sendMessage;
    Message message;
    static Object not=new Object();
    public void onUpdateReceived(Update update) {
        message=update.getMessage();
        sendMessage=new SendMessage();
        if(message.getText().equals("/matches")) {
            sendMes();
        }
    }

    public void sendMes(){
        MathcesTaker mt = new MathcesTaker();
        mt.start();
        synchronized (MathcesTaker.lock) {
            while (true) {
                try {
                    System.out.println(Thread.currentThread());
                    MathcesTaker.lock.wait();
                    sendMessage.setChatId(message.getChatId());
                    sendMessage.setText(MathcesTaker.matches.toString());
                    execute(sendMessage);
                    MathcesTaker.lock.notify();
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getBotUsername() {
        return BotUsername;
    }

    public String getBotToken() {
        return BotToken;
    }
}

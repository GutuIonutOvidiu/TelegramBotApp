package com.guio.guio;

import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramBot extends TelegramWebhookBot {
    private final String botUsername;
    private final String botToken;
    private boolean admin;

    public TelegramBot(String botUsername, String botToken, boolean admin) {
        this.admin = admin;
        this.botUsername = botUsername;
        this.botToken = botToken;
    }

    public boolean isAdmin() {
        return this.admin;
    }


    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println("update rec on bot");
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            // Reply with a "Hello" message if the bot receives a text message
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Hello!");

            try {
                execute(message); // Sends the message to the user
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return "";
    }

}
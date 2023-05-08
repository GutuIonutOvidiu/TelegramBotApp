package com.guio.guio;

import com.guio.guio.TelegramBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService {
    private final Map<String, TelegramBot> bots;

    public TelegramBotService() {
        System.out.println("salut");
        this.bots = new ConcurrentHashMap<>();
        addBot("Ionut_G","5830485236:AAFGD2OQH-BT8KgPXP-z8lPE8OhU1gDp52s");
    }

    public void addBot(String botUsername, String botToken) {
        System.out.println("salut");
        TelegramBot bot = new TelegramBot(botUsername, botToken);
        bots.put(botToken, bot);
        setWebhook(bot, "https://005e-2a02-2f0b-a301-8d00-3149-8695-79e4-66a6.ngrok-free.app/webhook/" + botToken);
    }

    public void setWebhook(TelegramBot bot, String webhookUrl) {
        SetWebhook setWebhook = new SetWebhook();
        setWebhook.setUrl(webhookUrl);
        try {
            bot.execute(setWebhook);
            System.out.println("set webhook done");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void removeBot(String botToken) {
        bots.remove(botToken);
    }

    public TelegramBot getBotByToken(String botToken) {
        return bots.get(botToken);
    }

    public Map<String, TelegramBot> getBots() {
        return bots;
    }

}

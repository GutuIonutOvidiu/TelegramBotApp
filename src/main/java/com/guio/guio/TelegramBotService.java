package com.guio.guio;

import com.guio.guio.TelegramBot;
import com.guio.guio.entity.TelegramUser;
import com.guio.guio.service.TelegramUserService;
import jakarta.persistence.Column;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotService {
    private final Map<String, TelegramBot> bots;


    private final TelegramUserService telegramUserService;
    @Autowired
    public TelegramBotService(@Value("${ADMIN_TOKEN}") String adminToken,TelegramUserService telegramUserService) {
        this.telegramUserService=telegramUserService;
        System.out.println("salut");
        this.bots = new ConcurrentHashMap<>();
        String firstName = "Ionut";
        String secondName = "Gutu";
        String userName = "ionut_gutu";
        String emailadress = "ionutgutu@gmail.com";
        addBot(firstName,secondName,userName,emailadress,adminToken, true);
    }


    public void addBot(String firstName,String secondName,String botUsername,String emailAdress, String botToken, boolean isAdmin) {
        System.out.println("Add bot:" + botUsername + " " + botToken);
        TelegramBot bot = new TelegramBot(botUsername, botToken, isAdmin);
        bots.put(botToken, bot);
        setWebhook(bot, "https://1568-188-26-140-36.ngrok-free.app/webhook/" + botToken);
        try {
            this.telegramUserService.addUser(new TelegramUser(firstName,secondName,botUsername,emailAdress,botToken,isAdmin));
        }catch (Exception e){
            if(isAdmin){
                System.out.println("Admin it is already register");
            }else {
                throw e;
            }
        }
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

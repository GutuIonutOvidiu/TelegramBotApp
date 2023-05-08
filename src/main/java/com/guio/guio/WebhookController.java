package com.guio.guio;

import com.guio.guio.TelegramBot;
import com.guio.guio.TelegramBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired
    private TelegramBotService telegramBotService;


    //public com.guio.guio.WebhookController(com.guio.guio.TelegramBotService telegramBotService) {
    //    this.telegramBotService = telegramBotService;
    //}

    @PostMapping("/{botToken}")
    public void handleUpdate(@PathVariable String botToken, @RequestBody Update update) {
        System.out.println("here");
        System.out.println(update.toString());
        TelegramBot bot = telegramBotService.getBotByToken(botToken);
        if (bot != null) {
            bot.onWebhookUpdateReceived(update);
        }
    }

    @PostMapping("/start")
    public void handleUpdate(@RequestBody Update update) {
        System.out.println("ping");
        telegramBotService.addBot("admin", "5830485236:AAFGD2OQH-BT8KgPXP-z8lPE8OhU1gDp52s");
    }

    @PostMapping("/hello")
    public void handleUpdateHello(@RequestBody Update update) {
        TelegramBot bot = telegramBotService.getBotByToken("5830485236:AAFGD2OQH-BT8KgPXP-z8lPE8OhU1gDp52s");
        bot.onWebhookUpdateReceived(update);
    }
}

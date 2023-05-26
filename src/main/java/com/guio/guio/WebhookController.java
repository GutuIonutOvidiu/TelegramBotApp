package com.guio.guio;

import com.guio.guio.entity.TelegramUser;
import com.guio.guio.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    Map<String, ButtonEvent> button_event = new HashMap<String, ButtonEvent>();  //one event per each token
//    private Map<Long, String> userCallbackDataMap;

    @Autowired
    private TelegramBotService telegramBotService;

    @PostMapping("/{botToken}")
    public BotApiMethod<?> onUpdateReceived(@PathVariable String botToken, @RequestBody Update update) throws TelegramApiException {
        TelegramBot bot = telegramBotService.getBotByToken(botToken);
        SendMessage message = new SendMessage();
        if (update.hasCallbackQuery()) {
            // Create an AnswerCallbackQuery to send as a response to the callback
            return processButtonEvent(botToken, update, bot, message);
        } else {
            if (this.button_event.containsKey(botToken)) {
                ButtonEvent buttonEvent = this.button_event.get(botToken);
                switch (buttonEvent) {
                    case REGISTER_NEW_USER:
                        return registerNewUser(botToken, update, message);
                    case CREATE_NEW_TASK:

                    }
                    this.button_event.remove(botToken);
            } else {
                String chatId = update.getMessage().getChatId().toString();
                message.setChatId(chatId);
                if (bot.isAdmin()) {
                    return displayMenuAdmin(chatId);
                } else {
                    return displayMenuNoAdim(chatId);
                }
            }
        }
        return null;
    }

    private SendMessage registerNewUser(String botToken, Update update, SendMessage message) throws TelegramApiException {
        TelegramBot bot;
        String chatId = update.getMessage().getChatId().toString();
        message.setChatId(chatId);
        String[] userDetalis = update.getMessage().getText().split(" ");
        if (userDetalis.length == 5) {
            bot = telegramBotService.getBotByToken(botToken);
            try{
                this.telegramBotService.addBot(userDetalis[0],userDetalis[1],userDetalis[2],userDetalis[3], userDetalis[4], false);
                message.setText("Congratulation you register a new user with this userName:" + userDetalis[2]);
//            bot.execute(createButtonForUser(chatId));
                bot.execute(message);
            }catch (Exception e){
                message.setText("User can't be register");
                bot.execute(message);
            }
            return displayMenuAdmin(chatId);
        } else {
            message.setText("The format of date is incorect, pleas try enter just 5 date delimitated by one single space, you are insert " + userDetalis.length + "dates");
        }
        return message;
    }

    private SendMessage processButtonEvent(String botToken, Update update, TelegramBot bot, SendMessage message) throws TelegramApiException {
        String optionOnButton = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();

        switch (optionOnButton) {
            case "Register new user":
                message.setChatId(chatId);
                if (bot.isAdmin() && update.getMessage() == null) {
                    message.setText("Enter the following data one by one in the following order: \n1.first name;\n2.second,\n3.user name,\n4.email adress,\n5.user token \nAll this delimited by a single space");
                    this.button_event.put(botToken, ButtonEvent.REGISTER_NEW_USER);
                    return message;
                } else if (!bot.isAdmin()) {
                    message.setText("You are not authorized to add a new user");
                    bot.execute(message);
                    return displayMenuNoAdim(chatId);
                }
            case "Create new task":
            case "Tasks in processing":
            case "Tasks canceled":
            case "Tasks completed":
            case "Raking user score":
            case "Group users":
            default:
        }
        return null;
    }

    private SendMessage displayMenuAdmin(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Here is your buttons:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Register new user");
        button1.setCallbackData("Register new user");
        rowInline1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Group users");
        button2.setCallbackData("Group users");
        rowInline1.add(button2);

        rowsInline.add(rowInline1);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Create new task");
        button3.setCallbackData("Create new task");
        rowInline2.add(button3);

        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Tasks in processing");
        button4.setCallbackData("Tasks in processing");
        rowInline2.add(button4);

        // Set the keyboard to the markup
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Tasks canceled");
        button5.setCallbackData("Tasks canceled");
        rowInline3.add(button5);

        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("Tasks completed");
        button6.setCallbackData("Tasks completed");
        rowInline3.add(button6);
        rowsInline.add(rowInline3);

        List<InlineKeyboardButton> rowInline4 = new ArrayList<>();
        InlineKeyboardButton button7 = new InlineKeyboardButton();
        button7.setText("Raking user score");
        button7.setCallbackData("Raking user score");
        rowInline4.add(button7);

        // Set the keyboard to the markup
        rowsInline.add(rowInline4);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }

    private SendMessage displayAllUsers(String chatId){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("This is all users TelegramBot group:");
        HashMap<Long, String> users = new HashMap<>();
          Message message1 = new Message();
          users.put(message1.getFrom().getId(), message1.getFrom().getUserName());
          InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
          List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

          for (Map.Entry<Long, String> user : users.entrySet()) {
              List<InlineKeyboardButton> rowInline = new ArrayList<>();
              InlineKeyboardButton userButton = new InlineKeyboardButton();
              userButton.setText(user.getValue());
              userButton.setCallbackData("User_" + user.getKey());
              rowsInline.add(rowInline);
          }
          markupInline.setKeyboard(rowsInline);

        return message;
    }

    private SendMessage displayMenuNoAdim(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Here is your buttons:");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline1 = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Create new task");
        button1.setCallbackData("Create new task");
        rowInline1.add(button1);

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Tasks in processing");
        button2.setCallbackData("Tasks in processing");
        rowInline1.add(button2);

        // Set the keyboard to the markup
        rowsInline.add(rowInline1);

        List<InlineKeyboardButton> rowInline2 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Tasks canceled");
        button3.setCallbackData("Tasks canceled");
        rowInline2.add(button3);


        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Tasks completed");
        button4.setCallbackData("Tasks completed");
        rowInline2.add(button4);

        // Set the keyboard to the markup
        rowsInline.add(rowInline2);

        List<InlineKeyboardButton> rowInline3 = new ArrayList<>();
        InlineKeyboardButton button5 = new InlineKeyboardButton();
        button5.setText("Group users");
        button5.setCallbackData("Group users");
        rowInline3.add(button5);

        InlineKeyboardButton button6 = new InlineKeyboardButton();
        button6.setText("Raking user score");
        button6.setCallbackData("Raking user score");
        rowInline3.add(button6);

        // Set the keyboard to the markup
        rowsInline.add(rowInline3);

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }

    private SendMessage createButtonForUser(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("User: ");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        String username = "Ion";
        button1.setText("User: Ion");
        button1.setCallbackData("User: " + username);
        rowInline.add(button1);

        rowsInline.add(rowInline); // Add the row to the rowsInline list
        markupInline.setKeyboard(rowsInline); // Set the rowsInline as the keyboard in markupInline

        message.setReplyMarkup(markupInline); // Set markupInline as the reply markup in the message

        System.out.println("Button created for user: " + username);

        return message;
    }


    @PostMapping("/start")
    public void handleUpdate(@RequestBody Update update) {
        System.out.println("ping");

    }

    @PostMapping("/hello")
    public void handleUpdateHello(@RequestBody Update update) {
        TelegramBot bot = telegramBotService.getBotByToken("5830485236:AAFGD2OQH-BT8KgPXP-z8lPE8OhU1gDp52s");
        bot.onWebhookUpdateReceived(update);
    }

}

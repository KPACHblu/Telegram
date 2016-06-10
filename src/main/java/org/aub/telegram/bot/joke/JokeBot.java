package org.aub.telegram.bot.joke;

import org.aub.telegram.bot.stats.Entry;
import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JokeBot extends TelegramLongPollingBot {
    private static final String TAG = "JokeBot";
    private JokeDb jokeDb = new JokeDb();
    private StatisticService statisticService = new StatisticService();

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getReplyKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
        if (update.getMessage().getText().contains("Password@!")) {
            sendMessage.setText("All size: " + statisticService.getAllStats().size());
        } else {
            sendMessage.setText(jokeDb.getRandomJoke());
        }

        try {
            sendMessage(sendMessage);
            statisticService.addStat(new Entry(update.getMessage().getText(), "", update.getMessage().getFrom().getId().toString(), new Date()));
        } catch (Exception e) {
            BotLogger.error(TAG, e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return "WhiskyJokeBot";
    }

    @Override
    public String getBotToken() {
        return "224600281:AAFvaW9oMwl23qXcXfGWoeKLeSVwJmrq_xM";
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Следующий");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}

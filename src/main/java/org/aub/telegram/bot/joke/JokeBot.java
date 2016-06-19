package org.aub.telegram.bot.joke;

import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

public class JokeBot extends TelegramLongPollingBot {
    private static final String TAG = "JokeBot";
    private JokeDao jokeDao = new JokeDao();
    private StatisticService statisticService = new StatisticService();

    @Override
    public void onUpdateReceived(Update update) {
        if (statisticService.sendStatisticIfNeeded(update, this)) {
            return;
        }
        sendJoke(update.getMessage().getFrom().getId());

    }

    @Override
    public String getBotUsername() {
        return "WhiskyJokeBot";
    }

    @Override
    public String getBotToken() {
        return "224600281:AAFvaW9oMwl23qXcXfGWoeKLeSVwJmrq_xM";
    }

    private void sendJoke(Integer userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getReplyKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        sendMessage.setText(jokeDao.getRandomJoke());
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e.getMessage());
        }
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

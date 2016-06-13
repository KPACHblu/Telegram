package org.aub.telegram.bot.aphorism;

import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

public class AphorismBot extends TelegramLongPollingBot {
    private static final String TAG = "AphorismBot";

    private StatisticService statisticService = new StatisticService();
    private AphorismDao aphorismDao = new AphorismDao();

    @Override
    public void onUpdateReceived(Update update) {
        if (statisticService.sendStatisticIfNeeded(update, this)) {
            return;
        }
        sendAphorismImage(update.getMessage().getFrom().getId());
    }

    @Override
    public String getBotUsername() {
        return "WhiskyAphorismBot";
    }

    @Override
    public String getBotToken() {
        return "224469994:AAGpU6fdzrussTf6JHpnjGk_qpy4MBtfDVI";
    }

    private void sendAphorismImage(Integer userId) {
        SendPhoto sendPhoto = new SendPhoto();
        String imageNamePath = aphorismDao.getRandomImagePath();
        sendPhoto.setNewPhoto(imageNamePath, imageNamePath);
        sendPhoto.setChatId(String.valueOf(userId));
        sendPhoto.setReplayMarkup(getReplyKeyboardMarkup());
        try {
            sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
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
        //TODO fix
        keyboardRow.add("Следующий");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}


package org.aub.telegram.bot.flag;

import org.aub.telegram.bot.flag.model.Country;
import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlagBot extends TelegramLongPollingBot {
    private static final String TAG = "FlagBot";

    private Map<String, String> userToAnswer = new HashMap<>();
    private StatisticService statisticService = new StatisticService();
    private FlagDao flagDao = new FlagDao();
    @Override
    public void onUpdateReceived(Update update) {
        if (statisticService.sendStatisticIfNeeded(update, this)) {
            return;
        }
        String userId = update.getMessage().getFrom().getId().toString();
        String correctAnswer = userToAnswer.get(userId);
        if (correctAnswer == null) {
            sendRandomFlag(userId);
        } else {
            String messageText = update.getMessage().getText();
            if (correctAnswer.equals(messageText)) {
                sendMessage("Correct", userId, null);
                sendRandomFlag(userId);
            } else {
                sendMessage("incorrect. Try more!", userId, null);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "WhiskyFlagBot";
    }

    @Override
    public String getBotToken() {
        return "239602673:AAEG6rRBvq8dGHrET_gOV0i8wY576QAzkQk";
    }

    private void sendMessage(String message, String userId, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setReplayMarkup(keyboard);
        sendMessage.setChatId(userId);
        sendMessage.setText(message);
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private void sendRandomFlag(String userId) {
        SendPhoto sendPhoto = new SendPhoto();
        Country country = flagDao.getRandomCountry();
        userToAnswer.put(userId, country.getName());
        sendPhoto.setNewPhoto(country.getFlagPath(), country.getFlagPath());
        sendPhoto.setChatId(userId);
        sendPhoto.setReplayMarkup(getReplyKeyboardMarkup(country.getName()));
        try {
            System.out.println(sendPhoto.getReplayMarkup().toJson().toString());
            sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            BotLogger.error(TAG, e);
        }
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(String correctAnswer) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<String> answers = flagDao.getAnswers(correctAnswer);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        System.out.println(answers.get(0));
        keyboardRow.add(answers.get(0));
        keyboardRow.add(answers.get(1));
        keyboard.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add(answers.get(2));
        keyboardRow.add(answers.get(3));
        keyboard.add(keyboardRow);


        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}

package org.aub.telegram.bot.joke;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.botan.sdk.Botan;
import jersey.repackaged.com.google.common.collect.ImmutableMap;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.util.ArrayList;
import java.util.List;

public class JokeBot extends TelegramLongPollingBot {
    private static final String TAG = "JokeBot";
    private JokeDb jokeDb = new JokeDb();
    private Botan botan;

    public JokeBot() {
        try (CloseableHttpAsyncClient client = HttpAsyncClients.createDefault()) {
            client.start();
            botan = new Botan(client, new ObjectMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setText(jokeDb.getRandomJoke());
        sendMessage.setReplayMarkup(getReplyKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
        try {
            sendMessage(sendMessage);
            botan.track("pE5rfY-CM0rQBsN9_7wS8jYG42a:1yml", update.getMessage().getFrom().getId().toString(), ImmutableMap.of("some_metric:", 100, "another_metric", 500), update.getMessage().getText()).get();
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

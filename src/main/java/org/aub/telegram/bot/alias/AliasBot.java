package org.aub.telegram.bot.alias;

import org.aub.telegram.bot.alias.model.Game;
import org.aub.telegram.bot.alias.model.Round;
import org.aub.telegram.bot.alias.model.Team;
import org.aub.telegram.bot.stats.StatisticService;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.nio.charset.Charset;
import java.util.*;

public class AliasBot extends TelegramLongPollingBot {
    private static final String TAG = "AliasBot";
    private static final String COMMAND_START_GAME = "Start Game";

    private StatisticService statisticService = new StatisticService();
    private Map<Integer, Game> userToGame = new HashMap<>();
    private AliasDao aliasDao = new AliasDao();

    @Override
    public void onUpdateReceived(Update update) {
        if (statisticService.sendStatisticIfNeeded(update, this)) {
            return;
        }
        sendSome(update);
    }

    @Override
    public String getBotUsername() {
        return "WhiskyAliasBot";
    }

    @Override
    public String getBotToken() {
        return "122934009:AAHEM7mI69BojtIsVrg6kyD7lDaDTrmdQh4";
    }

    private void sendSome(Update update) {
        String messageText = update.getMessage().getText();
        Integer userId = update.getMessage().getFrom().getId();
        // Start new game
        if (messageText.equalsIgnoreCase(COMMAND_START_GAME)) {
            userToGame.put(userId, new Game());
            sendTeamNumber(userId);
            return;
        }
        // Choose number of teams
        if (isNumeric(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                int teamsNumber = Integer.parseInt(messageText);
                game.addNewTeams(teamsNumber);
                sendReadyForTeam(userId, game.getCurrentTeam().getName());
                return;
            } else {
                //TODO
            }
        }
        if ("go".equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                game.startNewRound();
                sendWord(userId);
            }
            return;
        }
        if ("skip".equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                if(!game.isCurrentRoundFinished()) {
                    Round lastRound = game.getCurrentTeam().getLastRound();
                    lastRound.getWordToResult().put(lastRound.getLastAskedWord(), false);
                    sendWord(userId);
                } else {
                    sendTimeOver(userId);
                }
            }
            return;
        }
        if ("correct".equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                if (!game.isCurrentRoundFinished()) {
                    addPointToTeam(userId);
                    Round lastRound = game.getCurrentTeam().getLastRound();
                    lastRound.getWordToResult().put(lastRound.getLastAskedWord(), true);
                    sendWord(userId);
                } else {
                    sendTimeOver(userId);
                }
            }
            return;
        }

        if ("Next Round".equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                game.changeTeam();
                sendReadyForTeam(userId, game.getCurrentTeam().getName());
            }
            return;
        }

        sendMenu(userId);
    }

    private void sendTimeOver(Integer userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setReplayMarkup(getNextRoundKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        Game game = userToGame.get(userId);
        Map<String, Boolean> questionToResult = game.getCurrentTeam().getLastRound().getWordToResult();
        StringBuilder sb = new StringBuilder("Your result is\n");
        for (Map.Entry<String, Boolean> entry: questionToResult.entrySet()) {
            sb.append(entry.getKey()).append(" - ").append(entry.getValue()).append("\n");

        }
        sb.append("\n<b> Your score is " + game.getCurrentTeam().getPoints() + " points</b>");
        sendMessage.setText("<b>Time is over!</b>\n" + sb.toString());
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }

    }

    private void sendMenu(Integer userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getStartGameKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        byte[] emojiBytes = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x8E, (byte)0xB2};
        String emojiAsString = new String(emojiBytes, Charset.forName("UTF-8"));

        sendMessage.setText(emojiAsString + " Let's play?");
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private void addPointToTeam(Integer userId) {
        Game game = userToGame.get(userId);
        if (game != null) {
            Team currentTeam = game.getCurrentTeam();
            currentTeam.setPoints(currentTeam.getPoints() + 1);
        }
    }

    private void sendWord(Integer userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getSkipAndCorrectKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        Game game = userToGame.get(userId);
        String randomWord = aliasDao.getRandomWord();
        game.getCurrentTeam().getLastRound().setLastAskedWord(randomWord);
        sendMessage.setText(randomWord);
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }


    private void sendReadyForTeam(Integer userId, String teamName) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getGoKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        sendMessage.setText(teamName + ", are you ready?");
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }


    private void sendTeamNumber(Integer userId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplayMarkup(getReplyKeyboardMarkup());
        sendMessage.setChatId(String.valueOf(userId));
        sendMessage.setText("How many teams?");
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("1");
        keyboardRow.add("2");
        keyboardRow.add("3");
        keyboard.add(keyboardRow);
        keyboardRow = new KeyboardRow();
        keyboardRow.add("4");
        keyboardRow.add("5");
        keyboardRow.add("6");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getGoKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Go");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getSkipAndCorrectKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Skip");
        keyboardRow.add("Correct");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getStartGameKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Start Game");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getNextRoundKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("Next Round");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }

}

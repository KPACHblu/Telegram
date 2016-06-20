package org.aub.telegram.bot.alias;

import org.aub.telegram.bot.alias.model.Game;
import org.aub.telegram.bot.alias.model.Round;
import org.aub.telegram.bot.alias.model.Team;
import org.aub.telegram.bot.alias.util.StringUtil;
import org.aub.telegram.bot.stats.StatisticService;
import org.aub.telegram.bot.util.Property;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.nio.charset.Charset;
import java.util.*;

public class AliasBot extends TelegramLongPollingBot {
    private static final String TAG = "AliasBot";
    private String COMMAND_START_GAME;
    private String COMMAND_GO;
    private String COMMAND_SKIP;
    private String COMMAND_CORRECT;
    private String COMMAND_NEXT_ROUND;
    private String COMMAND_FINISH_GAME;

    private StatisticService statisticService = new StatisticService();
    private Map<Integer, Game> userToGame = new HashMap<>();
    private AliasDao aliasDao = new AliasDao();
    private Property lang;

    public AliasBot() {
        lang = new Property("alias-bot/lang_ru.properties");
        initCommands();
    }

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

    private void initCommands() {
        COMMAND_START_GAME = lang.getProperty("button.startGame");
        COMMAND_GO = lang.getProperty("button.go");
        COMMAND_SKIP = lang.getProperty("button.skip");
        COMMAND_CORRECT = lang.getProperty("button.correct");
        COMMAND_NEXT_ROUND = lang.getProperty("button.nextRound");
        COMMAND_FINISH_GAME = lang.getProperty("button.finishGame");
    }

    private void sendSome(Update update) {
        String messageText = update.getMessage().getText();
        Integer userId = update.getMessage().getFrom().getId();
        // Start new game
        if (COMMAND_START_GAME.equalsIgnoreCase(messageText)) {
            userToGame.put(userId, new Game());
            sendTeamNumber(userId);
            return;
        }
        // Choose number of teams
        if (isNumeric(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                int teamsNumber = Integer.parseInt(messageText);
                String[] teamNames = new String[teamsNumber];
                String teamBaseName = lang.getProperty("menu.team");
                for (int i=0; i<teamsNumber; i++) {
                    teamNames[i] = teamBaseName +" " + (i+1);
                }
                game.addNewTeams(teamNames);
                sendReadyForTeam(userId, game.getCurrentTeam().getName());
                return;
            } else {
                //TODO
            }
        }
        if (COMMAND_GO.equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                game.startNewRound();
                sendWord(userId);
            }
            return;
        }
        if (COMMAND_SKIP.equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                decreasePointFromTeam(userId);
                Round lastRound = game.getCurrentTeam().getLastRound();
                lastRound.getWordToResult().put(lastRound.getLastAskedWord(), false);
                if(!game.isCurrentRoundFinished()) {
                    sendWord(userId);
                } else {
                    sendTimeOver(userId);
                }
            }
            return;
        }
        if (COMMAND_CORRECT.equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                addPointToTeam(userId);
                Round lastRound = game.getCurrentTeam().getLastRound();
                lastRound.getWordToResult().put(lastRound.getLastAskedWord(), true);
                if (!game.isCurrentRoundFinished()) {
                    sendWord(userId);
                } else {
                    sendTimeOver(userId);
                }
            }
            return;
        }

        if (COMMAND_NEXT_ROUND.equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                if (game.isGameFinished()) {
                    sendWinMessage(userId);
                } else {
                    game.changeTeam();
                    sendReadyForTeam(userId, game.getCurrentTeam().getName());
                }

            }
            return;
        }

        if (COMMAND_FINISH_GAME.equalsIgnoreCase(messageText)) {
            Game game = userToGame.get(userId);
            if (game != null) {
                userToGame.remove(userId);
                sendStartGame(userId);
            }
            return;
        }

        sendStartGame(userId);
    }

    private void sendMessage(String message, Integer userId, ReplyKeyboard keyboard) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableHtml(true);
        sendMessage.setReplayMarkup(keyboard);
        sendMessage.setChatId(String.valueOf(userId));
        sendMessage.setText(message);
        try {
            sendMessage(sendMessage);
        } catch (Exception e) {
            BotLogger.error(TAG, e);
        }
    }

    private void sendTimeOver(Integer userId) {
        Game game = userToGame.get(userId);
        Map<String, Boolean> questionToResult = game.getCurrentTeam().getLastRound().getWordToResult();
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.STYLE_BOLD_START).append(lang.getProperty("menu.timeIsOver")).append(StringUtil.STYLE_BOLD_END).append(StringUtil.NEXT_LINE).append(StringUtil.NEXT_LINE);
        String trueSign = byteToString(new byte[]{(byte)0xE2, (byte)0x9C, (byte)0x85});
        String falseSign = byteToString(new byte[]{(byte)0xE2, (byte)0x9D, (byte)0x8E});

        for (Map.Entry<String, Boolean> entry: questionToResult.entrySet()) {
            sb.append(entry.getValue()? trueSign : falseSign).append(" ").append(entry.getKey()).append(StringUtil.NEXT_LINE);
        }
        sb.append(StringUtil.NEXT_LINE).append(StringUtil.STYLE_BOLD_START);
        sb.append(lang.getPropertyParam("menu.yourScore", game.getCurrentTeam().getPoints())).append(StringUtil.STYLE_BOLD_END);

        sendMessage(sb.toString(), userId, getNextRoundKeyboardMarkup());
    }

    private void sendWinMessage(Integer userId) {
        Game game = userToGame.get(userId);
        List<Team> winners = game.getWinners();
        StringBuilder message = new StringBuilder(byteToString(new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x8F, (byte)0x81}));
        if (winners.size() == 1) {
            message.append(lang.getPropertyParam("menu.theWinnerIs")).append(StringUtil.NEXT_LINE);
            message.append(StringUtil.STYLE_BOLD_START).append(winners.get(0).getName());
            message.append(StringUtil.HYPHEN).append(lang.getPropertyParam("menu.numberOfPoints", winners.get(0).getPoints())).append(StringUtil.STYLE_BOLD_END);

        } else {
            message.append(lang.getProperty("menu.theWinnersAre"));
            message.append(StringUtil.NEXT_LINE);
            for (Team current : winners) {
                message.append(StringUtil.STYLE_BOLD_START).append(current.getName()).append(StringUtil.HYPHEN);
                message.append(lang.getPropertyParam("menu.numberOfPoints", current.getPoints()));
                message.append(StringUtil.STYLE_BOLD_END).append(StringUtil.NEXT_LINE);
            }
        }
        userToGame.remove(userId);
        sendMessage(message.toString(), userId, getStartGameKeyboardMarkup());

    }

    private void sendStartGame(Integer userId) {
        byte[] emojiBytes = new byte[]{(byte)0xF0, (byte)0x9F, (byte)0x8E, (byte)0xB2};
        String message = byteToString(emojiBytes) + lang.getProperty("menu.letsPlay");

        sendMessage(message, userId, getStartGameKeyboardMarkup());
    }

    private void addPointToTeam(Integer userId) {
        Game game = userToGame.get(userId);
        if (game != null) {
            Team currentTeam = game.getCurrentTeam();
            currentTeam.setPoints(currentTeam.getPoints() + 1);
        }
    }

    private void decreasePointFromTeam(Integer userId) {
        Game game = userToGame.get(userId);
        if (game != null) {
            Team currentTeam = game.getCurrentTeam();
            currentTeam.setPoints(currentTeam.getPoints() - 1);
        }
    }

    private void sendWord(Integer userId) {
        String randomWord = aliasDao.getRandomWord();
        Game game = userToGame.get(userId);
        game.getCurrentTeam().getLastRound().setLastAskedWord(randomWord);

        sendMessage(randomWord, userId, getSkipAndCorrectKeyboardMarkup());
    }


    private void sendReadyForTeam(Integer userId, String teamName) {
        sendMessage(lang.getPropertyParam("menu.areYouReady", teamName), userId, getGoKeyboardMarkup());
    }


    private void sendTeamNumber(Integer userId) {
        sendMessage(lang.getProperty("menu.howManyTeams"), userId, getReplyKeyboardMarkup());
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add("1");
        keyboardRow.add("2");
        keyboardRow.add("3");
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add("4");
        keyboardRow2.add("5");
        keyboardRow2.add("6");

        return getKeyBoardMarkup(Arrays.asList(keyboardRow, keyboardRow2));
    }

    private ReplyKeyboardMarkup getGoKeyboardMarkup() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(lang.getProperty("button.go"));

        return getKeyBoardMarkup(Arrays.asList(keyboardRow));
    }

    private ReplyKeyboardMarkup getSkipAndCorrectKeyboardMarkup() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(lang.getProperty("button.skip"));
        keyboardRow.add(lang.getProperty("button.correct"));

        return getKeyBoardMarkup(Arrays.asList(keyboardRow));
    }

    private ReplyKeyboardMarkup getStartGameKeyboardMarkup() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(lang.getProperty("button.startGame"));

        return getKeyBoardMarkup(Arrays.asList(keyboardRow));
    }

    private ReplyKeyboardMarkup getNextRoundKeyboardMarkup() {
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(lang.getProperty("button.finishGame"));
        keyboardRow.add(lang.getProperty("button.nextRound"));

        return getKeyBoardMarkup(Arrays.asList(keyboardRow));
    }

    private ReplyKeyboardMarkup getKeyBoardMarkup(List<KeyboardRow> keyboard) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;

    }

    private static boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }

    private static String byteToString(byte[] bytes) {
        return new String(bytes, Charset.forName("UTF-8"));
    }

}

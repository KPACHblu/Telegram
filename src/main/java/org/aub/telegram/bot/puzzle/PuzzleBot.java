package org.aub.telegram.bot.puzzle;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.*;

public class PuzzleBot extends TelegramLongPollingBot {
    private Map<Integer, Puzzle> userToPuzzleMap;
    private List<Puzzle> allPuzzles;
    private Random random = new Random();

    public PuzzleBot() {
        userToPuzzleMap = new HashMap<>();
        allPuzzles = new ArrayList<>();
        allPuzzles.add(new Puzzle("И зимой и летом одним цветом", "елка"));
        allPuzzles.add(new Puzzle("Висит груша нельзя скушать", "лампа"));
        allPuzzles.add(new Puzzle("Один удар - четыре дырки", "вилка"));
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("New request");
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        Integer userId = update.getMessage().getFrom().getId();
        if (update.getMessage().getText().contains("загадка")) {
            Puzzle puzzle = getRandomPuzzle();
            userToPuzzleMap.put(userId, puzzle);
            sendMessage.setText(puzzle.getQuestion());
        } else {
            Puzzle puzzle = userToPuzzleMap.get(userId);
            if (puzzle != null) {
                if (puzzle.getAnswer().equalsIgnoreCase(update.getMessage().getText())) {
                    sendMessage.setText("Верно!");
                    userToPuzzleMap.remove(userId);
                } else {
                    sendMessage.setText("Неверно");
                }
            } else {
                puzzle = getRandomPuzzle();
                userToPuzzleMap.put(userId, puzzle);
                sendMessage.setText(puzzle.getQuestion());
            }
        }
        sendMessage.setChatId(String.valueOf(update.getMessage().getFrom().getId()));
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "BrainTeaserBot";
    }

    @Override
    public String getBotToken() {
        return "";
    }

    private Puzzle getRandomPuzzle() {
        return allPuzzles.get(random.nextInt(allPuzzles.size()));
    }
}


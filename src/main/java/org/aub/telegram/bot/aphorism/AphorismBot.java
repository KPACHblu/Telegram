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
import java.util.Random;

public class AphorismBot extends TelegramLongPollingBot {
    private static final String TAG = "AphorismBot";
    private static final String IMAGE_PATH = "src/main/resources/aphorism-bot/";

    private Random random = new Random();
    private StatisticService statisticService = new StatisticService();

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
        String imageName = getRandomAphorismImage();
        sendPhoto.setNewPhoto(IMAGE_PATH + imageName, imageName);
        sendPhoto.setChatId(String.valueOf(userId));
        sendPhoto.setReplayMarkup(getReplyKeyboardMarkup());
        try {
            sendPhoto(sendPhoto);
        } catch (TelegramApiException e) {
            BotLogger.error(TAG, e.getMessage());
        }
    }

    private String getRandomAphorismImage() {
        return ALL_APHORISM_IMAGES[random.nextInt(ALL_APHORISM_IMAGES.length)];
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        //TODO fix
        keyboardRow.add("Next");
        keyboard.add(keyboardRow);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private static final String[] ALL_APHORISM_IMAGES = {
            "0001.jpg", "0002.jpg", "0003.jpg", "0004.jpg", "0005.jpg", "0006.jpg", "0007.jpg", "0008.jpg", "0009.jpg", "0010.jpg", "0011.jpg", "0012.jpg", "0013.jpg", "0014.jpg", "0015.jpg", "0016.jpg", "0017.jpg", "0018.jpg", "0019.jpg", "0020.jpg", "0021.jpg", "0022.jpg", "0023.jpg", "0024.jpg", "0025.jpg", "0026.jpg", "0027.jpg", "0028.jpg", "0029.jpg", "0030.jpg", "0031.jpg", "0032.jpg", "0033.jpg", "0034.jpg", "0035.jpg", "0036.jpg", "0037.jpg", "0038.jpg", "0039.jpg", "0040.jpg", "0041.jpg", "0042.jpg", "0043.jpg", "0044.jpg", "0045.jpg", "0046.jpg", "0047.jpg", "0048.jpg", "0049.jpg", "0050.jpg", "0051.jpg", "0052.jpg", "0053.jpg", "0054.jpg", "0055.jpg", "0056.jpg", "0057.jpg", "0058.jpg", "0059.jpg", "0060.jpg", "0061.jpg", "0062.jpg", "0063.jpg", "0064.jpg", "0065.jpg", "0066.jpg", "0067.jpg", "0068.jpg", "0069.jpg", "0070.jpg", "0071.jpg", "0072.jpg", "0073.jpg", "0074.jpg", "0075.jpg", "0076.jpg", "0077.jpg", "0078.jpg", "0079.jpg", "0080.jpg", "0081.jpg", "0082.jpg", "0083.jpg", "0084.jpg", "0085.jpg", "0086.jpg", "0087.jpg", "0088.jpg", "0089.jpg", "0090.jpg", "0091.jpg", "0092.jpg", "0093.jpg", "0094.jpg", "0095.jpg", "0096.jpg", "0097.jpg", "0098.jpg", "0099.jpg", "0100.jpg", "0101.jpg", "0102.jpg", "0103.jpg", "0104.jpg", "0105.jpg", "0106.jpg", "0107.jpg", "0108.jpg", "0109.jpg", "0110.jpg", "0111.jpg", "0112.jpg", "0113.jpg", "0114.jpg", "0115.jpg", "0116.jpg", "0117.jpg", "0118.jpg", "0119.jpg", "0120.jpg", "0121.jpg", "0122.jpg", "0123.jpg", "0124.jpg", "0125.jpg", "0126.jpg", "0127.jpg", "0128.jpg", "0129.jpg", "0130.jpg", "0131.jpg", "0132.jpg", "0133.jpg", "0134.jpg", "0135.jpg", "0136.jpg", "0137.jpg", "0138.jpg", "0139.jpg", "0140.jpg", "0141.jpg", "0142.jpg", "0143.jpg", "0144.jpg", "0145.jpg", "0146.jpg", "0147.jpg", "0148.jpg", "0149.jpg", "0150.jpg", "0151.jpg", "0152.jpg", "0153.jpg", "0154.jpg", "0155.jpg", "0156.jpg", "0157.jpg", "0158.jpg", "0159.jpg", "0160.jpg", "0161.jpg", "0162.jpg", "0163.jpg", "0164.jpg", "0165.jpg", "0166.jpg", "0167.jpg", "0168.jpg", "0169.jpg", "0170.jpg", "0171.jpg", "0172.jpg", "0173.jpg", "0174.jpg", "0175.jpg", "0176.jpg", "0177.jpg", "0178.jpg", "0179.jpg", "0180.jpg", "0181.jpg", "0182.jpg", "0183.jpg", "0184.jpg", "0185.jpg", "0186.jpg", "0187.jpg", "0188.jpg", "0189.jpg", "0190.jpg", "0191.jpg", "0192.jpg", "0193.jpg", "0194.jpg", "0195.jpg", "0196.jpg", "0197.jpg", "0198.jpg", "0199.jpg", "0200.jpg", "0201.jpg", "0202.jpg", "0203.jpg", "0204.jpg", "0205.jpg", "0206.jpg", "0207.jpg", "0208.jpg", "0209.jpg", "0210.jpg", "0211.jpg", "0212.jpg", "0213.jpg", "0214.jpg", "0215.jpg", "0216.jpg", "0217.jpg", "0218.jpg", "0219.jpg", "0220.jpg", "0221.jpg", "0222.jpg", "0223.jpg", "0224.jpg", "0225.jpg", "0226.jpg", "0227.jpg", "0228.jpg", "0229.jpg", "0230.jpg", "0231.jpg", "0232.jpg", "0233.jpg", "0234.jpg", "0235.jpg", "0236.jpg", "0237.jpg", "0238.jpg", "0239.jpg", "0240.jpg", "0241.jpg", "0242.jpg", "0243.jpg", "0244.jpg", "0245.jpg", "0246.jpg", "0247.jpg", "0248.jpg", "0249.jpg", "0250.jpg", "0251.jpg", "0252.jpg", "0253.jpg", "0254.jpg", "0255.jpg", "0256.jpg", "0257.jpg", "0258.jpg", "0259.jpg", "0260.png", "0261.png", "0262.png", "0263.png", "0264.png", "0265.png", "0266.png", "0267.png", "0268.png", "0269.png", "0270.png", "0271.jpg", "0272.jpg", "0273.jpg", "0274.jpg", "0275.jpg", "0276.jpg", "0277.jpg", "0278.jpg", "0279.jpg", "0280.jpg", "0281.jpg", "0282.jpg", "0283.jpg", "0284.gif", "0285.jpg", "0286.jpg", "0287.jpg", "0288.jpg",
    };
}

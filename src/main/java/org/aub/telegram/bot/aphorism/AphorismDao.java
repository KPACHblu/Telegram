package org.aub.telegram.bot.aphorism;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.util.Random;

public class AphorismDao {
    private static final String TAG = "AphorismDao";
    private static final String BOT_RESOURCE_FOLDER = "resources/aphorism-bot/";
    private Random random = new Random();
    private String[] allImages;

    public AphorismDao() {
        allImages = getAllFileNames();
        BotLogger.info(TAG, "All image files a loaded. Size:" + allImages.length);
    }

    public String getRandomImagePath() {
        return BOT_RESOURCE_FOLDER + allImages[random.nextInt(allImages.length - 1)];
    }

    private String[] getAllFileNames() {
        File imageFolder = new File(BOT_RESOURCE_FOLDER);
        System.out.println("Image Folder read:"+imageFolder.canRead());
        File[] listOfFiles = imageFolder.listFiles();
        System.out.println("!!!:"+listOfFiles);
        String[] result = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                result[i] = listOfFiles[i].getName();
            }
        }
        return result;
    }
}

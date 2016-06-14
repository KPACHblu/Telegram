package org.aub.telegram.bot.aphorism;

import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.util.Random;

public class AphorismDao {
    private static final String TAG = "AphorismDao";
    //Relative path of resource folder
    private static final String BOT_RESOURCE_FOLDER = "resources/aphorism-bot/";
    private static final String OPENSHIFT_REPO_DIR_VAR = "OPENSHIFT_REPO_DIR";
    private static final String OPENSHIFT_REPO_DIY_FOLDER = "diy/";
    private Random random = new Random();
    private String imageFolderPath;
    private String[] allImages;

    public AphorismDao() {
        //Get absolute path to image folder
        imageFolderPath = getOpenshiftRepoDirPath() + BOT_RESOURCE_FOLDER;
        allImages = getAllFileNames(imageFolderPath);
        BotLogger.info(TAG, "All image files a loaded. Size:" + allImages.length);
    }

    public String getRandomImagePath() {
        return imageFolderPath + allImages[random.nextInt(allImages.length - 1)];
    }

    private String[] getAllFileNames(String path) {
        File[] listOfFiles = new File(path).listFiles();
        String[] result = new String[listOfFiles.length];

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                result[i] = listOfFiles[i].getName();
            }
        }
        return result;
    }

    private String getOpenshiftRepoDirPath() {
        String envVar = System.getenv(OPENSHIFT_REPO_DIR_VAR);
        return envVar != null ? envVar + OPENSHIFT_REPO_DIY_FOLDER : "";
    }

}

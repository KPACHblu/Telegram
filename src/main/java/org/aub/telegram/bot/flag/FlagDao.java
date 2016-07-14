package org.aub.telegram.bot.flag;

import org.aub.telegram.bot.flag.model.Country;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class FlagDao {
    private static final String TAG = "FlagDao";
    //Relative path of resource folder
    private static final String BOT_RESOURCE_FOLDER = "resources/flag-bot/";
    private static final String OPENSHIFT_REPO_DIR_VAR = "OPENSHIFT_REPO_DIR";
    private static final String OPENSHIFT_REPO_DIY_FOLDER = "diy/";
    private Random random = new Random();
    private String imageFolderPath;
    private Country[] allCountries;

    public FlagDao() {
        //Get absolute path to image folder
        imageFolderPath = getOpenshiftRepoDirPath() + BOT_RESOURCE_FOLDER;

        String file = getFile("flag-bot/flags_ru.csv");
        String[] countries = file.split("\n");
        allCountries = new Country[countries.length];
        for (int i=0; i<countries.length; i++) {
            String[] countryParam = countries[i].split(",");
            allCountries[i] = new Country(countryParam[1], countryParam[2], countryParam[3], imageFolderPath + countryParam[0]);
        }

        BotLogger.info(TAG, "All image files a loaded. Size:" + allCountries.length);
    }

    public Country getRandomCountry() {
        return allCountries[random.nextInt(allCountries.length - 1)];
    }

    public List<String> getAnswers(String name) {
        List<String> answers = new ArrayList<>();
        answers.add(name);
        answers.add(allCountries[random.nextInt(allCountries.length - 1)].getName());
        answers.add(allCountries[random.nextInt(allCountries.length - 1)].getName());
        answers.add(allCountries[random.nextInt(allCountries.length - 1)].getName());
        Collections.shuffle(answers);
        return answers;
    }


    private String getOpenshiftRepoDirPath() {
        String envVar = System.getenv(OPENSHIFT_REPO_DIR_VAR);
        return envVar != null ? envVar + OPENSHIFT_REPO_DIY_FOLDER : "";
    }

    private String getFile(String fileName) {
        StringBuilder result = new StringBuilder("");
        ClassLoader classLoader = getClass().getClassLoader();
        try (Scanner scanner = new Scanner(classLoader.getResourceAsStream(fileName), StandardCharsets.UTF_8.displayName())) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        new FlagDao();
    }
}

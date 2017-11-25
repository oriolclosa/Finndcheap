import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Timer;

public class Main {
    public static void main(String[] args) {
        // Initialize Api Context
        //test purpouses muniategui
        Insta insta = new Insta();
        insta.flyFrom("HEL","STO","2017-12-12","10");
        //end of test  muniategui
        ApiContextInitializer.init();

        // Instantiate Telegram Bots API
        TelegramBotsApi botsApi = new TelegramBotsApi();

        FinndcheapBot bot = new FinndcheapBot();

        // Register our bot
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
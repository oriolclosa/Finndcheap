import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Timer;

import static java.lang.Math.toIntExact;

public class Main {
    public static void main(String[] args) {
        // Initialize Api Context
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

        Timer timer = new Timer();
        timer.schedule(new AlertWeather(bot), 0, 5000);
    }
}
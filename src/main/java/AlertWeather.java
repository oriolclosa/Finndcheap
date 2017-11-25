import java.util.TimerTask;

public class AlertWeather extends TimerTask {
    FinndcheapBot bot;
    int chat_id;

    public AlertWeather(FinndcheapBot pBot, int pChat_id){
        bot = pBot;
        chat_id = pChat_id;
    }
    public void run() {
        bot.alertWeather(chat_id);
    }
}
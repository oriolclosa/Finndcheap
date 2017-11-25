import java.util.TimerTask;

public class AlertWeather extends TimerTask {
    FinndcheapBot bot;

    public AlertWeather(FinndcheapBot pBot){
        bot = pBot;
    }
    public void run() {
        bot.alertWeather();
    }
}
import com.mongodb.DBCursor;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.mongodb.MongoClient; import com.mongodb.MongoClientURI; import com.mongodb.client.MongoCollection; import com.mongodb.client.MongoDatabase; import org.bson.Document; import org.json.JSONObject;

import java.util.logging.Level;

import static java.lang.Math.toIntExact;
import static jdk.nashorn.internal.objects.NativeFunction.function;

public class FinndcheapBot extends TelegramLongPollingBot {

    private void setVariable(int user_id, String variable, String value){
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("users");
        if(variable.equals("weather")){
            if(value.equals("yes")){
                collection.updateOne(Document.parse("{_id: " + user_id + "}"), new Document("$set", new Document("weather", true)));
            }
            else if(value.equals("no")){
                System.out.println("no");
                collection.updateOne(Document.parse("{_id: " + user_id + "}"), new Document("$set", new Document("weather", false)));
            }
        }
    }

    private String getVariable(int user_id, String variable){
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("users");
        long found = collection.count(Document.parse("{_id: " + user_id + "}"));
        if (found == 1) {
            if (variable.equals("weather")) {
                boolean valor = collection.find(Document.parse("{_id: " + user_id + "}")).first().getBoolean("weather");
                if (valor) {
                    return "yes";
                } else {
                    return "false";
                }
            } else {
                return "";
            }
        }
        else{
            return "";
        }
    }

    private String check(int user_id, String first_name, String last_name, String username) {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("users");
        System.out.println(user_id);
        long found = collection.count(Document.parse("{_id: " + user_id + "}"));
        System.out.println(found);
        String inicial="";
        if (found == 0) {
            Document doc = new Document("_id", user_id)
                    .append("username", username)
                    .append("first_name", first_name)
                    .append("last_name", last_name)
                    .append("weather", true);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            inicial =  "Welcome " + first_name + "! I'm Finndcheap, your easy-to-use flight assistant! ✈";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            inicial = "Welcome back " + first_name + "! ✈";
        }
        return inicial+"\n\n" +
                "Here is a list of commands!\n" +
                "/settings";
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            long user_id = update.getMessage().getChat().getId();
            String user_first_name = update.getMessage().getChat().getFirstName();
            String user_last_name = update.getMessage().getChat().getLastName();
            String user_username = update.getMessage().getChat().getUserName();

            if(message_text.equals("/start")){
                sendMessage(chat_id, (check(toIntExact(user_id), user_first_name, user_last_name, user_username)));
            }
            else if(message_text.equals("/settings")){
                sendMessage(chat_id, "You can customise the following options... \uD83D\uDD28");
                sendMessage(chat_id, "/settings_weather (yes/no) ⛅\n" +
                        "If you have any flight, it will alert you of any bad weather!");
            }
            else if(message_text.equals("/settings_weather")){
                if(getVariable(toIntExact(user_id), "weather").equals("yes")){
                    sendMessage(chat_id, "You'll get bad weather notifications for your flights! \uD83D\uDE0E\n" +
                            "You can turn this off by typing /settings_weather_no.");
                }
                else{
                    sendMessage(chat_id, "You won't get any bad weather notifications for your flights... \uD83D\uDE14\n" +
                            "You can turn this on by typing /settings_weather_yes.");
                }
            }
            else if(message_text.equals("/settings_weather_yes")) {
                setVariable(toIntExact(user_id),"weather", "yes");
                sendMessage(chat_id, "You'll get bad weather notifications for your flights! \uD83D\uDE0E\n" +
                        "You can turn this off by typing /settings_weather_no.");
            }
            else if(message_text.equals("/settings_weather_no")) {
                setVariable(toIntExact(user_id),"weather", "no");
                sendMessage(chat_id, "You won't get any bad weather notifications for your flights... \uD83D\uDE14\n" +
                        "You can turn this on by typing /settings_weather_yes.");
            }
            else if((message_text.contains(" "))&&((message_text.substring(0, message_text.indexOf(" "))).equals("/settings_weather"))){
                String opcio = message_text.substring(message_text.indexOf(" ")+1, message_text.length());
                if(opcio.equals("yes")){
                    setVariable(toIntExact(user_id),"weather", "yes");
                    sendMessage(chat_id, "You'll get bad weather notifications for your flights! \uD83D\uDE0E\n" +
                            "You can turn this off by typing /settings_weather_no.");
                }
                else if(opcio.equals("no")){
                    setVariable(toIntExact(user_id),"weather", "no");
                    sendMessage(chat_id, "You won't get any bad weather notifications for your flights... \uD83D\uDE14\n" +
                            "You can turn this on by typing /settings_weather_yes.");
                }
            }
            else if((message_text.length()>0)&&(message_text.charAt(0)==('/'))){
                sendMessage(chat_id, "I'm sorry, I don't understand this command... \uD83D\uDE30");
            }
            else{
                System.out.println(message_text);
                sendMessage(chat_id, "I'm sorry, I don't understand what you are saying... \uD83D\uDE33");
            }
        }
    }

    private void sendMessage(long chat_id, String message_text){
        SendMessage message = new SendMessage() // Create a message object object
                .setChatId(chat_id)
                .setText(message_text);
        try {
            execute(message); // Sending our message object to user
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        // Return bot username
        // If bot username is @MyAmazingBot, it must return 'MyAmazingBot'
        return "finndcheap_bot";
    }

    @Override
    public String getBotToken() {
        // Return bot token from BotFather
        return "483875557:AAHc3q_N0G3jLhLR04B_EeVYgpui3ztPF8A";
    }
}
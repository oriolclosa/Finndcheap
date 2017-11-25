import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.mongodb.MongoClient; import com.mongodb.MongoClientURI; import com.mongodb.client.MongoCollection; import com.mongodb.client.MongoDatabase; import org.bson.Document; import org.json.JSONObject;

import java.util.logging.Level;

import static java.lang.Math.toIntExact;

public class FinndcheapBot extends TelegramLongPollingBot {

    private String check(int user_id, String first_name, String last_name, String username) {
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("users");
        System.out.println(user_id);
        long found = collection.count(Document.parse("{_id: " + user_id + "}"));
        System.out.println(found);
        if (found == 0) {
            Document doc = new Document("_id", user_id)
                    .append("username", username)
                    .append("first_name", first_name)
                    .append("last_name", last_name);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            return "Welcome " + first_name + "! I'm Finndcheap, your easy-to-use flight assistant! ✈";
        } else {
            System.out.println("User exists in database.");
            mongoClient.close();
            return "Welcome back " + first_name + "! ✈";
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            // Set variables
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            if(message_text.equals("/start")){
                // Set variables
                long user_id = update.getMessage().getChat().getId();
                String user_first_name = update.getMessage().getChat().getFirstName();
                String user_last_name = update.getMessage().getChat().getLastName();
                String user_username = update.getMessage().getChat().getUserName();
                //long user_id = update.getMessage().getChat().getId();
                sendMessage(chat_id, (check(toIntExact(user_id), user_first_name, user_last_name, user_username)));
            }
            else{
                System.out.println(message_text);
                sendMessage(chat_id, message_text);
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
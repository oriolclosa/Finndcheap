import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import com.mongodb.MongoClient; import com.mongodb.MongoClientURI; import com.mongodb.client.MongoCollection; import com.mongodb.client.MongoDatabase; import org.bson.Document; import org.json.JSONObject;

import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;

import static java.lang.Math.toIntExact;
import static jdk.nashorn.internal.objects.NativeFunction.function;

public class FinndcheapBot extends TelegramLongPollingBot {

    public void alertWeather(){
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("users");
        FindIterable<Document> values = collection.find(new Document());
        MongoCursor<Document> cursor = values.iterator();
        try{
            while(cursor.hasNext()){
                Document actual = cursor.next();
                int chat_id = actual.getInteger("chat_id");
                if(actual.getBoolean("weather")){
                    MongoCollection<Document> collection2 = database.getCollection("flights");
                    FindIterable<Document> values2 = collection2.find(new Document());
                    MongoCursor<Document> cursor2 = values2.iterator();
                    try{
                        while(cursor2.hasNext()){
                            Document actual2 = cursor2.next();
                            if(Objects.equals(actual.getInteger("_id"), actual2.getInteger("user"))) {
                                String data = actual2.getString("time");
                                Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(data);
                                Date d2 = new Date();
                                d1.setTime(d1.getTime()+43200000);  //12 hores més
                                long diff = Math.abs(d1.getTime() - d2.getTime());
                                long diffDays = diff / (24 * 60 * 60 * 1000);
                                if ((diffDays>0)&&(diffDays < 5)) {
                                    if(actual2.getInteger("reminded")!=diffDays){
                                        setVariable(actual2.getObjectId("_id").toString(), "flights","reminded", ""+diffDays);
                                        String plural = "days";
                                        if(diffDays==1){
                                            plural = "day";
                                        }
                                        FredCalor temps = new FredCalor();
                                        System.out.println((temps.Fred(actual2.getString("airA"), (int)diffDays)));
                                        if(temps.Fred(actual2.getString("airA"), (int)diffDays)) {
                                            sendMessage(chat_id, "Warning for your flight to " + getName(actual2.getString("airA")) + " in " + diffDays + " " + plural + "! ☔");
                                            sendMessage(chat_id, temps.FredOCalor(actual2.getString("airA"), (int)diffDays));
                                        }
                                    }
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } finally{
                        cursor2.close();
                    }
                }
            }
        }
        finally{
            cursor.close();
        }
    }

    private String getName(String codi){
        return codi;
    }

    private void setVariable(String user_id, String collect, String variable, String value){
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection(collect);
        if(variable.equals("reminded")){
            collection.updateOne(Document.parse("{_id: ObjectId(\"" + user_id + "\")}"), new Document("$set", new Document("reminded", Integer.parseInt(value))));
        }
        else {
            int user_id2 = Integer.parseInt(user_id);
            if (variable.equals("weather")) {
                if (value.equals("yes")) {
                    collection.updateOne(Document.parse("{_id: " + user_id2 + "}"), new Document("$set", new Document("weather", true)));
                } else if (value.equals("no")) {
                    System.out.println("no");
                    collection.updateOne(Document.parse("{_id: " + user_id2 + "}"), new Document("$set", new Document("weather", false)));
                }
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

    private ArrayList<String> getDestinations(int user_id, boolean first){
        ArrayList<String> ciutats = new ArrayList<>();
        if(!first){
            Weather temps = new Weather();
            ArrayList<ArrayList<ArrayList<String>>> ciutats2 = temps.weatherAll();
            int mida = ciutats2.size();
            int valor = (int) (Math.random() * mida);
            ciutats.add(ciutats2.get(valor).get(0).get(0));
        }
        else {
            java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
            MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
            MongoClient mongoClient = new MongoClient(connectionString);
            MongoDatabase database = mongoClient.getDatabase("finndcheap");
            MongoCollection<Document> collection = database.getCollection("flights");
            FindIterable<Document> values = collection.find(new Document());
            MongoCursor<Document> cursor = values.iterator();
            try{
                while(cursor.hasNext()){
                    Document actual = cursor.next();
                    if(actual.getInteger("user")==user_id){
                        ciutats.add(actual.getString("airA"));
                    }
                }
            }
            finally{
                cursor.close();
            }
            if (ciutats.isEmpty()) {
                Weather temps = new Weather();
                ArrayList<ArrayList<ArrayList<String>>> ciutats2 = temps.weatherAll();
                int mida = ciutats2.size();
                int valor = (int) (Math.random() * mida);
                ciutats.add(ciutats2.get(valor).get(0).get(0));
            }
        }
        return ciutats;
    }

    private String check(int user_id, int chat_id, String first_name, String last_name, String username) {
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
                    .append("chat_id", chat_id)
                    .append("username", username)
                    .append("first_name", first_name)
                    .append("last_name", last_name)
                    .append("weather", true);
            collection.insertOne(doc);
            mongoClient.close();
            System.out.println("User not exists in database. Written.");
            inicial =  "Welcome " + first_name + "! I'm Finndcheap, your easy-to-use flight assistant! ✈";
            listCommands(toIntExact(chat_id));
        } else {
            System.out.println("User exists in database.");
            collection.updateOne(Document.parse("{_id: " + user_id + "}"), new Document("$set", new Document("chat_id", chat_id)));
            mongoClient.close();
            inicial = "Welcome back " + first_name + "! ✈";
        }
        return inicial;
    }

    private void getWeather(int chat_id, String location, int days){
        boolean existeix=false;
        Weather temps = new Weather();
        ArrayList<ArrayList<ArrayList<String>>> ciutats2 = temps.weatherAll();
        int mida = ciutats2.size();
        for(int i=0; i<mida; ++i){
            if(ciutats2.get(i).get(0).get(0).equals(location)){
                existeix = true;
            }
        }
        if(existeix) {
            if (days < 0) {
                for (int i = 0; i < 5; ++i) {
                    FredCalor temps2 = new FredCalor();
                    sendMessage(chat_id, "Day " + i + "\n" +
                            temps2.FredOCalor(location, i));
                }
            } else {
                FredCalor temps2 = new FredCalor();
                sendMessage(chat_id, "Day " + days + "\n" +
                        temps2.FredOCalor(location, days));
            }
        }
        else{
            sendError(chat_id);
        }
    }

    private void addFlight(int user_id, String airD, String airA, String time, float price, String type, String cabin){
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
        MongoClientURI connectionString = new MongoClientURI("mongodb://159.89.13.211:27017");
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("finndcheap");
        MongoCollection<Document> collection = database.getCollection("flights");
        Document doc = new Document("user", user_id)
                .append("airD", airD)
                .append("airA", airA)
                .append("time", time)
                .append("price", price)
                .append("type", type)
                .append("cabin", cabin)
                .append("reminded", -1);
        collection.insertOne(doc);
        mongoClient.close();
    }

    private void listCommands(int chat_id){
        sendMessage(chat_id, "Here is a list of commands!\n" +
                "/recommend\n" +
                "/find (origin) [destination]\n" +
                "/weather (days)\n" +
                "/settings\n" +
                "/help");
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
                sendMessage(chat_id, (check(toIntExact(user_id), toIntExact(chat_id), user_first_name, user_last_name, user_username)));
            }
            else if(message_text.equals("/help")){
                listCommands(toIntExact(chat_id));
            }
            else if(message_text.equals("/add_flight")){
                addFlight(toIntExact(user_id), "BCN", "DUB", "2017-11-30", (float)137.30, "adult", "economy");
                addFlight(toIntExact(user_id), "BCN", "HEL", "2017-11-27", (float)137.30, "adult", "economy");
                addFlight(toIntExact(user_id), "HEL", "BCN", "2017-11-25", (float)124.30, "adult", "economy");
                addFlight(toIntExact(user_id), "HEL", "CDG", "2017-10-10", (float)255.67, "adult", "business");
                addFlight(toIntExact(user_id), "HEL", "ORY", "2017-10-23", (float)75.34, "adult", "economy");
                addFlight(toIntExact(user_id), "HEL", "CDG", "2017-07-10", (float)132.12, "adult", "economy");
                addFlight(toIntExact(user_id), "HEL", "CDG", "2017-02-01", (float)165.70, "adult", "economy");
            }
            else if(message_text.equals("/recommend")){
                Insta insta = new Insta();
                Date date = new Date(); // your date
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String recomanacio = mediana(getDestinations(toIntExact(user_id), true));
                System.out.println(recomanacio);
                ArrayList<ArrayList<String>> valors = new ArrayList<>();
                if(recomanacio!="HEL"){
                    valors = insta.flyFrom("HEL", recomanacio,  year+"-"+(month+1)+"-"+day, "10");
                }
                while(valors.size()<=0){
                    recomanacio = mediana(getDestinations(toIntExact(user_id), false));
                    if(recomanacio!="HEL"){
                        valors = insta.flyFrom("HEL", recomanacio,  year+"-"+(month+1)+"-"+day, "10");
                    }
                }
                sendMessage(chat_id, "Our recomendation is... " + recomanacio + "! \uD83C\uDF1A");
                sendMessage(chat_id, "Here you have the next flights.");
                int mida=valors.size();
                if(mida>5){
                    mida = 5;
                }
                for(int i=0; i<mida; ++i){
                    sendMessage(chat_id, valors.get(i).get(0) + " for " + valors.get(0).get(1) + "€");
                }

            }
            else if(message_text.equals("/find")){

            }
            else if((message_text.contains(" "))&&((message_text.substring(0, message_text.indexOf(" "))).equals("/weather"))){
                String opcio = message_text.substring(message_text.indexOf(" ")+1, message_text.length());
                if(opcio.contains(" ")) {
                    System.out.println(opcio);
                    String opcio2 = opcio.substring(0, opcio.indexOf(" "));
                    String opcio3 = opcio.substring(opcio.indexOf(" ")+1, opcio.length());
                    int valor=-1;
                    if (opcio3.equals("0")) {
                        valor = 0;
                    }
                    else if (opcio3.equals("1")) {
                        valor = 1;
                    } else if (opcio3.equals("2")) {
                        valor = 2;
                    } else if (opcio3.equals("3")) {
                        valor = 3;
                    } else if (opcio3.equals("4")) {
                        valor = 4;
                    }
                    getWeather(toIntExact(chat_id), opcio2, valor);
                }
                else{
                    getWeather(toIntExact(chat_id), opcio, -1);
                }
            }
            else if(message_text.equals("/settings")){
                sendMessage(chat_id, "You can customise the following options... \uD83D\uDD28");
                sendMessage(chat_id, "/settings_weather ⛅\n" +
                        "Turn it on: /settings_weather_yes\n" +
                        "Turn it off: /settings_weather_no\n" +
                        "If you have any flight, it will alert you of any bad weather!\n");
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
                setVariable(""+toIntExact(user_id),"users","weather", "yes");
                sendMessage(chat_id, "You'll get bad weather notifications for your flights! \uD83D\uDE0E\n" +
                        "You can turn this off by typing /settings_weather_no.");
            }
            else if(message_text.equals("/settings_weather_no")) {
                setVariable(""+toIntExact(user_id),"users","weather", "no");
                sendMessage(chat_id, "You won't get any bad weather notifications for your flights... \uD83D\uDE14\n" +
                        "You can turn this on by typing /settings_weather_yes.");
            }
            else if (message_text.equals("Omae wa mou shindeiru")){
                setVariable(""+toIntExact(user_id),"users","weather", "no");
                sendMessage(chat_id, "NANI?");
            }
            else if((message_text.contains(" "))&&((message_text.substring(0, message_text.indexOf(" "))).equals("/settings_weather"))){
                String opcio = message_text.substring(message_text.indexOf(" ")+1, message_text.length());
                if(opcio.equals("yes")){
                    setVariable(""+toIntExact(user_id),"users","weather", "yes");
                    sendMessage(chat_id, "You'll get bad weather notifications for your flights! \uD83D\uDE0E\n" +
                            "You can turn this off by typing /settings_weather_no.");
                }
                else if(opcio.equals("no")){
                    setVariable(""+toIntExact(user_id),"users","weather", "no");
                    sendMessage(chat_id, "You won't get any bad weather notifications for your flights... \uD83D\uDE14\n" +
                            "You can turn this on by typing /settings_weather_yes.");
                }
                else{
                    sendError(toIntExact(chat_id));
                }
            }
            else if((message_text.length()>0)&&(message_text.charAt(0)==('/'))){
                sendMessage(chat_id, "I'm sorry, I don't understand this command... \uD83D\uDE30");
            }
            else{
                sendError(toIntExact(chat_id));
            }

            System.out.println(message_text);
        }
    }

    private void sendError(int chat_id){
        sendMessage(chat_id, "I'm sorry, I don't understand what you are saying... \uD83D\uDE33");
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

    public String mediana(ArrayList<String> ciutats){
        Map<String, Integer> ciutat = new HashMap<>();
        List<String> ci = new ArrayList<>();
        String c;
        for (int i = 0; i < ciutats.size(); ++i){
            c = ciutats.get(i);
            Integer count = ciutat.get(c);
            if (count == null){
                ciutat.put(c, 1);
                ci.add(c);
            }
            else{
                ciutat.put(c, ++count);
            }
        }

        Iterator it = ciutat.entrySet().iterator();

        String ciu = "no hi ha ciutats";
        Integer num = null;

        for (int i = 0; i < ci.size(); ++i){
            String q = ci.get(i);
            Integer w = ciutat.get(q);
            if (ciu.equals("no hi ha ciutats")){
                ciu = q;
                num = w;
            }
            else {
                if (num < w) {
                    ciu = q;
                    num = w;
                }
            }
        }
        return ciu;
    }

    public String intToDate(int merdaseca) {
        switch (merdaseca) {
            case 0:
                return "The day before tomorrow";
            case 1:
                return "The day after today";
            case 2:
                return "The day after tomorrow";
            case 3:
                return "The day after the day after tomorrow";
            default:
                return null;
        }
    }
}
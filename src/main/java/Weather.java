import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Weather {

    public ArrayList<ArrayList<ArrayList<String>>> weatherAll() {
        String JSON = getJSON();
        try{
            JSONObject jObj = new JSONObject(JSON);
            String[] cities = JSONObject.getNames(jObj);
            ArrayList<ArrayList<ArrayList<String>>> all = new ArrayList<>();
            for (String cityAct: cities) {
                if(jObj.has(cityAct)) {
                    ArrayList<ArrayList<String>> days = new ArrayList<>();
                    if (!jObj.isNull(cityAct))  {
                        JSONArray jdays = jObj.getJSONArray(cityAct);
                        ArrayList<String> c = new ArrayList<>();
                        c.add(cityAct);
                        days.add(c);
                        for (int i = 0; i < jdays.length(); ++i) {
                            ArrayList<String> day = new ArrayList<>();
                            if (jdays.getJSONObject(i).has("dt")) {
                                day.add(jdays.getJSONObject(i).getString("dt"));
                            }
                            if (jdays.getJSONObject(i).has("tn")) {
                                day.add(jdays.getJSONObject(i).getString("tn"));
                            }
                            if (jdays.getJSONObject(i).has("tx")) {
                                day.add(jdays.getJSONObject(i).getString("tx"));
                            }
                            if (jdays.getJSONObject(i).has("s")) {
                                day.add(jdays.getJSONObject(i).getString("s"));
                            }
                            if (jdays.getJSONObject(i).has("pr")) {
                                day.add(jdays.getJSONObject(i).getString("pr"));
                            }
                            if (jdays.getJSONObject(i).has("wsx")) {
                                day.add(jdays.getJSONObject(i).getString("wsx"));
                            }
                            if (jdays.getJSONObject(i).has("wn")) {
                                day.add(jdays.getJSONObject(i).getString("wn"));
                            }
                            if (!day.isEmpty()) days.add(day);
                        }
                    }
                    if (!days.isEmpty()) all.add(days);
                }
            }
            return all;
        }catch (Exception e){
        }
        return null;
    }

    public ArrayList<ArrayList<String>> weatherWeek(String city) {
        String JSON = getJSON();
        try{
            JSONObject jObj = new JSONObject(JSON);
            if(jObj.has(city)) {
                ArrayList<ArrayList<String>> days = new ArrayList<>();
                JSONArray jdays = jObj.getJSONArray(city);
                for (int i = 0; i < jdays.length(); ++i) {
                    ArrayList<String> day = new ArrayList<>();
                    if (jdays.getJSONObject(i).has("dt")) {
                        day.add(jdays.getJSONObject(i).getString("dt"));
                    }
                    if (jdays.getJSONObject(i).has("tn")) {
                        day.add(jdays.getJSONObject(i).getString("tn"));
                    }
                    if (jdays.getJSONObject(i).has("tx")) {
                        day.add(jdays.getJSONObject(i).getString("tx"));
                    }
                    if (jdays.getJSONObject(i).has("s")) {
                        day.add(jdays.getJSONObject(i).getString("s"));
                    }
                    if (jdays.getJSONObject(i).has("pr")) {
                        day.add(jdays.getJSONObject(i).getString("pr"));
                    }
                    if (jdays.getJSONObject(i).has("wsx")) {
                        day.add(jdays.getJSONObject(i).getString("wsx"));
                    }
                    if (jdays.getJSONObject(i).has("wn")) {
                        day.add(jdays.getJSONObject(i).getString("wn"));
                    }
                    if (!day.isEmpty()) days.add(day);
                }
                return days;
            }
        }catch (Exception e){
        }
        return null;
    }

    public ArrayList<String> weatherDay(String city, int d) {
        String JSON = getJSON();
        try{
            JSONObject jObj = new JSONObject(JSON);
            if(jObj.has(city)) {
                JSONArray jdays = jObj.getJSONArray(city);
                ArrayList<String> day = new ArrayList<>();
                if (jdays.getJSONObject(d).has("dt")) {
                    day.add(jdays.getJSONObject(d).getString("dt"));
                }
                if (jdays.getJSONObject(d).has("tn")) {
                    day.add(jdays.getJSONObject(d).getString("tn"));
                }
                if (jdays.getJSONObject(d).has("tx")) {
                    day.add(jdays.getJSONObject(d).getString("tx"));
                }
                if (jdays.getJSONObject(d).has("s")) {
                    day.add(jdays.getJSONObject(d).getString("s"));
                }
                if (jdays.getJSONObject(d).has("pr")) {
                    day.add(jdays.getJSONObject(d).getString("pr"));
                }
                if (jdays.getJSONObject(d).has("wsx")) {
                    day.add(jdays.getJSONObject(d).getString("wsx"));
                }
                if (jdays.getJSONObject(d).has("wn")) {
                    day.add(jdays.getJSONObject(d).getString("wn"));
                }
                return day;
            }
        }catch (Exception e){
        }
        return null;
    }

    public String getJSON() {
        HttpURLConnection c = null;
        try {
            URL u = new URL("https://api.finnair.com/media-weather");
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public void main() {
        ArrayList<ArrayList<ArrayList<String>>> a = weatherAll();
        for (int i = 0; i < a.size(); ++i) {
            ArrayList<ArrayList<String>> aa = a.get(i);
            for (int j = 0; j < aa.size(); ++j) {
                ArrayList<String> tresa = aa.get(j);
                for (int k = 0; k < tresa.size() ; ++k) {
                    System.out.println(tresa.get(k));
                }
                System.out.println("-------------------");
            }
            System.out.println("******************");
        }

        System.out.println("//////////////////////////////////");
        System.out.println("//////////////////////////////////");


        ArrayList<ArrayList<String>> w = weatherWeek("HEL");
        for (int i = 0; i < w.size(); ++i) {
            ArrayList<String> ww = w.get(i);
            for (int j = 0; j < ww.size(); ++j) System.out.println(ww.get(j));
            System.out.println("-------------------");
        }

        System.out.println("//////////////////////////////////");
        System.out.println("//////////////////////////////////");


        ArrayList<String> www = weatherDay("ALC", 4);
        for (int j = 0; j < www.size(); ++j) System.out.println(www.get(j));
            System.out.println("-------------------");

        System.out.println(intToDate(0));
        System.out.println(intToDate(1));
        System.out.println(intToDate(2));
        System.out.println(intToDate(3));
        System.out.println(intToDate(4));
    }
}

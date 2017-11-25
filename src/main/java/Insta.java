import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class Insta {
   private ArrayList<ArrayList<String>> flightsOut = new ArrayList<>();
   private ArrayList<String> flight;
   private String fdate;
   private  String fprice;
   private  String fnoFlight;
    //Insta api which will recive querys to search

    public ArrayList<ArrayList<String>> flyFrom(String sour, String dest, String dataexit, String dateto) {
        boolean fixed = false;
        return flyFromCore(sour,dest,dataexit,dateto,fixed);
    }
    public ArrayList<ArrayList<String>> flyFromFixed(String sour, String dest, String dataexit, String dateto) {
        boolean fixed = true;
        return flyFromCore(sour,dest,dataexit,dateto,fixed);
    }
    private ArrayList<ArrayList<String>> flyFromCore(String sour, String dest, String dataexit, String dateto,boolean fixed){


        URIBuilder urireq = new URIBuilder();
        urireq.setScheme("https");
        urireq.setHost("instantsearch-junction.ecom.finnair.com");
        if(fixed) urireq.setPath("/api/instantsearch/pricesforperiod/fixeddeparture");
        else urireq.setPath("/api/instantsearch/pricesforperiod");
        urireq.addParameter("departureLocationCode",sour);
        urireq.addParameter("destinationLocationCode",dest);
        urireq.addParameter("startDate",dataexit);
        urireq.addParameter("numberOfDays",dateto);
        //debug
        System.out.println(urireq.toString());
        //debug-
        StringBuffer content = new StringBuffer();
        try {
            URL url = new URL(urireq.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            String contentType = con.getHeaderField("Content-Type");
            con.setConnectTimeout(3000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(false);
            int status = con.getResponseCode();
            System.out.println(status);
            if (status == 200){
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                con.disconnect();

            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        String travels = content.toString();


        return omaeWaMouShinderu(travels);

    }
   // https://instantsearch-junction.ecom.finnair.com/api/instantsearch/pricesforperiod?departureLocationCode=HEL&destinationLocationCode=STO&startDate=2017-12-12&numberOfDays=10
    private ArrayList<ArrayList<String>> omaeWaMouShinderu(String travels){
        try{
            JSONObject jsonObject = new JSONObject(travels);
            if(jsonObject.has("level")){
                System.out.println("CYKA BLyAT");
                return null;
            }
            if(jsonObject.has("currency")&&jsonObject.has("dep")&&jsonObject.has("dest")&&jsonObject.has("currency")&& jsonObject.has("prices")){

                JSONArray pricesList = jsonObject.getJSONArray("prices");

                for (int i = 0; i < pricesList.length(); i++) {

                    JSONObject data = pricesList.getJSONObject(i);

                    fdate = data.getString("date");
                    fprice = data.getInt("price")+"";
                    fnoFlight = data.getBoolean("noFlight")? "true" : "false";
                    flight = new ArrayList<>(Arrays.asList(fdate,fprice,fnoFlight));

                    flightsOut.add(flight);
                }
            }

        }
        catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
        System.out.println(flightsOut.toString());
        return flightsOut;
    }

}

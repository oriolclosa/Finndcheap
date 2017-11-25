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
   private String fdate="N/A";
   private  String fprice="N/A";
   private  String fnoFlight="N/A";
   private String fflightNum = "N/A";
   private String ffarBas = "N/A";
    private String fflightNumret = "N/A";
    private String ffarBasret = "N/A";
    //Insta api which will recive querys to search

    public ArrayList<ArrayList<String>> flyFrom(String sour, String dest, String dataexit, String dateto, String...more) {
        boolean fixed = false;
        return flyFromCore(sour,dest,dataexit,dateto,fixed,more);
    }
    public ArrayList<ArrayList<String>> flyFromFixed(String sour, String dest, String dataexit, String dateto) {
        boolean fixed = true;
        return flyFromCore(sour,dest,dataexit,dateto,fixed);
    }
    //extraparameters 0: eco or bus (for bussines type or for economical), 1: true or false;
    private ArrayList<ArrayList<String>> flyFromCore(String sour, String dest, String dataexit, String dateto,boolean fixed,String...more){


        URIBuilder urireq = new URIBuilder();
        urireq.setScheme("https");
        urireq.setHost("instantsearch-junction.ecom.finnair.com");
        if(fixed) urireq.setPath("/api/instantsearch/pricesforperiod/fixeddeparture");
        else urireq.setPath("/api/instantsearch/pricesforperiod");
        //extra parameters :3
        System.out.println(more.length);
        if (more.length!=0) {
            if (more.length>=1 && more[0].length()!=0) {
                if (more[0].equals("eco")) {
                    urireq.addParameter("cff", "ECONOMY1");
                } else if (more[0].equals("bus")) {
                    urireq.addParameter("cff", "BUSINESS1");
                }
            }
            if (more.length>=2&&more[1].length()!=0) {
                if (more[1].equals("true")) {
                    urireq.addParameter("oneway", "true");
                } else if (more[1].equals("false")) {
                    urireq.addParameter("oneway", "false");
                }
            }
            if (more.length>=3&&more[2].length()!=0) {
                if (Integer.parseInt(more[2]) >= 1 && Integer.parseInt(more[2]) <= 30)
                    urireq.addParameter("lengthOfStay", more[2]);
            }
            if (more.length>=4&&more[3].length()!=0) {
                if (Integer.parseInt(more[3]) >= 0 && Integer.parseInt(more[3]) <= 30)
                    urireq.addParameter("lengthOfStay", more[3]);
            }
            if (more.length>=5&&more[4].length()!=0) {
                if (more[4].equals("true")) {
                    urireq.addParameter("debug", "true");
                } else if (more[4].equals("false")) {
                    urireq.addParameter("debug", "false");
                }
            }
        }
        urireq.addParameter("departureLocationCode",sour);
        urireq.addParameter("destinationLocationCode",dest);
        if (!fixed)
            urireq.addParameter("startDate",dataexit);
        else
            urireq.addParameter("departureDate",dataexit);
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


        return omaeWaMouShinderu(travels,more);

    }
   // https://instantsearch-junction.ecom.finnair.com/api/instantsearch/pricesforperiod?departureLocationCode=HEL&destinationLocationCode=STO&startDate=2017-12-12&numberOfDays=10
    private ArrayList<ArrayList<String>> omaeWaMouShinderu(String travels,String...more){
        try{
            JSONObject jsonObject = new JSONObject(travels);
            if(jsonObject.has("level")){
                System.out.println("CYKA BLyAT");
                return new ArrayList<ArrayList<String >>();
            }
            if(jsonObject.has("currency")&&jsonObject.has("dep")&&jsonObject.has("dest")&&jsonObject.has("currency")&& jsonObject.has("prices")){

                JSONArray pricesList = jsonObject.getJSONArray("prices");

                for (int i = 0; i < pricesList.length(); i++) {

                    JSONObject data = pricesList.getJSONObject(i);
                    if (!data.isNull("date")) {
                        fdate = data.getString("date");
                    }
                    else fdate = "N/A";

                    if (!data.isNull("price")) {
                        fprice = data.getInt("price") + "";
                    }
                    else fprice = "N/A";


                    fnoFlight = data.getBoolean("noFlight")? "true" : "false";

                    if(data.has("departureDebugInfo")){
                        JSONObject depar = data.getJSONObject("departureDebugInfo");

                        if(depar.has("flightNumbers")){
                            JSONArray fliNum = depar.getJSONArray("flightNumbers");
                            if(fliNum.length()!=0){
                                fflightNum = fliNum.getString(0);
                            }
                        }
                        if(depar.has("fareBasis")){
                            JSONArray farBas = depar.getJSONArray("fareBasis");
                            if(farBas.length()!=0){
                                ffarBas = farBas.getString(0);
                            }
                        }
                    }

                    if(data.has("returnDebugInfo")){
                        JSONObject retur = data.getJSONObject("returnDebugInfo");
                        if(retur.has("flightNumbers")){
                            JSONArray fliNumret = retur.getJSONArray("flightNumbers");
                            if(fliNumret.length()!=0){
                                fflightNumret = fliNumret.getString(0);
                            }
                        }
                        if(retur.has("fareBasis")){
                            JSONArray farBasret = retur.getJSONArray("fareBasis");
                            if(farBasret.length()!=0){
                                ffarBasret = farBasret.getString(0);
                            }
                        }
                    }
                    flight = new ArrayList<>(Arrays.asList(fdate,fprice,fnoFlight,fflightNum,ffarBas,fflightNumret,fflightNumret));

                    flightsOut.add(flight);
                }
            }

        }
        catch (Exception e){

            return new ArrayList<ArrayList<String >>();
        }

        return flightsOut;
    }

}

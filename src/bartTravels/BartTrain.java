package bartTravels;
 
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
 
@SuppressWarnings("deprecation")
public class BartTrain {
 
    public static TreeMap<Integer, ArrayList<String>> haltTimeAndDest = new TreeMap<Integer, ArrayList<String>>();
 
    public static void main(String[] args) {
 
        // running the whole application (fetching input realtime from
        // http://api.bart.gov/api/)
        getResult();
 
        // Testing the application against different inputs
        try {
            // When a train's minutes on the station shows "Leaving"
            String test1 = "{\"?xml\":{\"@version\":\"1.0\",\"@encoding\":\"utf-8\"},\"root\":{\"@id\":\"1\",\"uri\":{\"#cdata-section\":\"http://api.bart.gov/api/etd.aspx?cmd=etd&orig=MONT&json=y\"},\"date\":\"12/07/2018\",\"time\":\"12:17:45 AM PST\",\"station\""
                    + ":[{\"name\":\"Montgomery St.\",\"abbr\":\"MONT\"" + ",\"etd\""
                    + ":[{\"destination\":\"Antioch\",\"abbreviation\":\"ANTC\",\"limited\":\"0\",\"estimate\""
                    + ":[{\"minutes\":\"7\",\"platform\":\"2\",\"direction\":\"North\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"Daly City\",\"abbreviation\":\"DALY\",\"limited\":\"0\",\"estimate\""
                    + ":[{\"minutes\":\"Leaving\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"8\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"0\"}"
                    + ",{\"minutes\":\"24\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"9\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"Dublin/Pleasanton\",\"abbreviation\":\"DUBL\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"1\",\"platform\":\"2\",\"direction\":\"North\",\"length\":\"9\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"165\"}]}"
                    + ",{\"destination\":\"SF Airport\",\"abbreviation\":\"SFIA\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"47\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"SFO/Millbrae\",\"abbreviation\":\"MLBR\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"15\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}]}]"
                    + ",\"message\":\"\"}}";
 
            haltTimeAndDest.clear();
            getTrains(test1);
 
            // No trains from Montgomery station
            String test2 = "{\"?xml\":{\"@version\":\"1.0\",\"@encoding\":\"utf-8\"},\"root\"\n"
                    + ":{\"@id\":\"1\",\"uri\"\n"
                    + ":{\"#cdata-section\":\"http://api.bart.gov/api/etd.aspx?cmd=etd&orig=MONT&json=y\"}\n"
                    + ",\"date\":\"12/07/2018\",\"time\":\"01:18:15 AM PST\",\"station\"\n" + ":\n" + "[\n"
                    + "{\"name\":\"Montgomery St.\",\"abbr\":\"MONT\"}\n" + "]\n"
                    + ",\"message\":{\"warning\":\"No data matched your criteria.\"}}}";
 
            haltTimeAndDest.clear();
            getTrains(test2);
            System.out.println("No trains from Montgomery Station! ");
 
            // When two trains leave at the same time
            String test3 = "{\"?xml\":{\"@version\":\"1.0\",\"@encoding\":\"utf-8\"},\"root\":{\"@id\":\"1\",\"uri\":{\"#cdata-section\":\"http://api.bart.gov/api/etd.aspx?cmd=etd&orig=MONT&json=y\"},\"date\":\"12/07/2018\",\"time\":\"12:17:45 AM PST\",\"station\""
                    + ":[{\"name\":\"Montgomery St.\",\"abbr\":\"MONT\"" + ",\"etd\""
                    + ":[{\"destination\":\"Antioch\",\"abbreviation\":\"ANTC\",\"limited\":\"0\",\"estimate\""
                    + ":[{\"minutes\":\"7\",\"platform\":\"2\",\"direction\":\"North\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"Daly City\",\"abbreviation\":\"DALY\",\"limited\":\"0\",\"estimate\""
                    + ":[{\"minutes\":\"7\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"8\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"0\"}"
                    + ",{\"minutes\":\"24\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"9\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"Dublin/Pleasanton\",\"abbreviation\":\"DUBL\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"1\",\"platform\":\"2\",\"direction\":\"North\",\"length\":\"9\",\"color\":\"BLUE\",\"hexcolor\":\"#0099cc\",\"bikeflag\":\"1\",\"delay\":\"165\"}]}"
                    + ",{\"destination\":\"SF Airport\",\"abbreviation\":\"SFIA\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"47\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}"
                    + ",{\"destination\":\"SFO/Millbrae\",\"abbreviation\":\"MLBR\",\"limited\":\"0\",\"estimate\":[{\"minutes\":\"15\",\"platform\":\"1\",\"direction\":\"South\",\"length\":\"10\",\"color\":\"YELLOW\",\"hexcolor\":\"#ffff33\",\"bikeflag\":\"1\",\"delay\":\"0\"}]}]}]"
                    + ",\"message\":\"\"}}";
 
            haltTimeAndDest.clear();
            getTrains(test3);
 
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
 
    public static void getResult() {
 
        final Logger logger = Logger.getLogger(BartTrain.class.getName());
 
        Scanner ipStream = null;
 
        try {
            final URL url = new URL(
                    "http://api.bart.gov/api/etd.aspx?cmd=etd&orig=MONT&key=MW9S-E7SL-26DU-VV8V&json=y");
            String inputline = "";
            ipStream = new Scanner(url.openStream());
 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
 
            if (responseCode != 200) {
                throw new RuntimeException("Response Code: " + responseCode);
            }
 
            else {
 
                while (ipStream.hasNext()) {
                    inputline = inputline + ipStream.nextLine();
                }
 
                // System.out.println("inputline " + inputline);
 
                getTrains(inputline);
 
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Parser Exception Occured ", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException occures ", e);
        }
 
        finally {
            ipStream.close();
        }
    }
 
    private static void getTrains(String inputline) throws ParseException {
 
        JSONParser parser = new JSONParser();
 
        String destStations = "";
 
        JSONObject jsonobj = (JSONObject) parser.parse(inputline);
 
        JSONObject obj = (JSONObject) jsonobj.get("root");
 
        String time = (String) obj.get("time");
 
        JSONArray stationArr = (JSONArray) obj.get("station");
 
        for (int i = 0; stationArr != null && i < stationArr.size(); i++) {
 
            JSONObject stationObj = (JSONObject) stationArr.get(i);
 
            JSONArray etdArr = (JSONArray) stationObj.get("etd");
 
            for (int j = 0; etdArr != null && j < etdArr.size(); j++) {
 
                JSONObject etdObj = (JSONObject) etdArr.get(j);
 
                List<String> destList = new ArrayList<String>();
 
                destStations = (String) etdObj.get("destination");
                destList.add(destStations);
 
                JSONArray estimateArr = (JSONArray) etdObj.get("estimate");
 
                storeData(estimateArr, destList);
            }
 
        }
 
        System.out.println("--------------------------------------------------\n" + "  Montgomery St.  " + time + "\n"
                + "--------------------------------------------------\n");
 
        printTrains(haltTimeAndDest);
 
    }
 
    // segregating a newsted for loop in a separate method
    private static void storeData(JSONArray estimateArr, List<String> destList) {
 
        int minutes = 0;
        double delay = 0;
 
        for (int k = 0; k < estimateArr.size(); k++) {
            JSONObject estimateObj = (JSONObject) estimateArr.get(k);
 
            if (estimateObj.get("minutes").equals("Leaving"))
                minutes = 0;
 
            else {
                minutes = (Integer.parseInt((String) estimateObj.get("minutes")));
                delay = (Integer.parseInt((String) estimateObj.get("delay")));
                System.out.println("Delay before " + delay);
 
                delay = Math.ceil(delay / 60);
                System.out.println("Delay " + delay);
                minutes += delay;
            }
 
            ArrayList<String> a = haltTimeAndDest.get(minutes);
            ArrayList<String> b = new ArrayList<String>();
 
            if (a == null) {
                haltTimeAndDest.put(minutes, (ArrayList<String>) destList);
            } else {
                a.addAll(destList);
                haltTimeAndDest.put(minutes, a);
            }
 
        }
    }
 
    private static final void printTrains(TreeMap<Integer, ArrayList<String>> dest) {
 
        ArrayList<String> nextStn = new ArrayList<String>();
 
        int count = 1;
 
        for (int val : dest.keySet()) {
            nextStn = haltTimeAndDest.get(val);
 
            for (String stn : nextStn) {
                System.out.println(val + " min   " + stn);
                count++;
                if (count > 10)
                    break;
            }
 
            if (count > 10)
                break;
        }
 
    }
 
}
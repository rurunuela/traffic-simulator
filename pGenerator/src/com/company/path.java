package com.company;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class path{
    public String start;
    public String stop;

    private ArrayList<LatLng> decodePoly(String encoded) {

        // System.err.println("Location " +  "String received: "+encoded);
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),(((double) lng / 1E5)));
            poly.add(p);
        }

  /* for(int i=0;i<poly.size();i++){
        System.err.println("Location " + "Point sent: Latitude: "+poly.get(i).latitude+" Longitude: "+poly.get(i).longitude);
   }*/
        return poly;
    }


    public JSONArray getPolyline () throws Exception{
        ArrayList<LatLng> tmpTab=null;
        String polyline="";
        JSONArray list = new JSONArray();
        String url_select ="https://maps.googleapis.com/maps/api/directions/json?origin="
                +start.replace(" ","%20")+
                "&destination="
                +stop.replace(" ","%20")+
                "&key=AIzaSyD_MQqA-bqdAY-lMrYfEZNuZTWOdKp3LAw";
        //System.err.println(url_select);
        URL url = new URL(url_select );
        StringBuilder postData = new StringBuilder();
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String line = null; (line = reader.readLine()) != null;) {
            builder.append(line).append("\n");
        }
        reader.close();
        conn.disconnect();
        // System.out.println(builder);
        Object obj= JSONValue.parse(builder.toString());
        JSONObject jObject = (JSONObject) obj;
        JSONArray routes=(JSONArray)(jObject.get("routes"));
        Iterator it= routes.iterator();
        while (it.hasNext()){
            JSONObject route= (JSONObject) it.next();
            JSONArray legs=(JSONArray)(route.get("legs"));
            Iterator it2 = legs.iterator();
            while (it2.hasNext()){
                JSONObject leg = (JSONObject)it2.next();
                JSONArray steps=(JSONArray)(leg.get("steps"));
                Iterator it3 = steps.iterator();
                while (it3.hasNext()){
                    JSONObject step = (JSONObject)it3.next();
                    String tmpPolyline=(String)(((JSONObject)step.get("polyline"))

                            .get("points"));
                    tmpTab=decodePoly(tmpPolyline);
                    if (tmpTab!=null){
                        for(int i=0;i<tmpTab.size();i++){
                            list.add(tmpTab.get(i).longitude);
                            list.add(tmpTab.get(i).latitude);
                        }
                    }

                }


            }
            //  System.out.println(((JSONObject)path.get("legs"))
            //                    .get("polyline"));
        }

        /* if (tmpTab!=null){
              for(int i=0;i<tmpTab.size();i++){
               //  System.err.println("Location " + "Point sent: Latitude: "+tmpTab.get(i).latitude+" Longitude: "+tmpTab.get(i).longitude);
                list.add(tmpTab.get(i).longitude);
                list.add(tmpTab.get(i).latitude);
             }
           }*/
        //System.err.println("---"+list);
        return list;


    }
}




import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.net.*;
import java.io.*;
import org.json.simple.*;
import java.util.*;

/*
Cette version ne fonctionne pas car je compare les positions une à une sur les tick
Je dois comparer les routes et non les positions pour définir les points de relais ...


Mauvaise idee de comparer les scenario comparer les routes
*/

public class SearchPathCommun{
    
     static double calcDistance(double dynLat ,double dynLon,double fixLat,double  fixLon){
                  
                  // System.out.println(dynLat+" " + fixLat);
                   
                    double dist = (Math.sqrt(Math.pow(dynLat-fixLat,(double)2) + 
                    Math.pow(dynLon- fixLon,(double)2)) * 100000);
                    //System.err.println(dist);
                    return dist;
    }
    
      public static void main(String[] args) {
        JSONArray points=new JSONArray();
        try {
         JSONParser parser = new JSONParser();

		  Object obj = parser.parse(new FileReader(args[0]));
          int car0 = Integer.parseInt(args[1]);
          int car1 = Integer.parseInt(args[2]);
		  JSONObject jsonObject = (JSONObject) obj;
          JSONArray scenarios = (JSONArray) jsonObject.get("scenario");
          JSONArray paths = (JSONArray) jsonObject.get("paths");
          JSONArray roads = (JSONArray) jsonObject.get("roads");
          
          
          //System.err.println(" ->"  + scenarios.size());
          int scenarioSize=scenarios.size();
          // Recherhche de parcours entre du chemin A vers B 
          // Attetion on cherche ici que de A vers B
          
          int delta=0;//decalage dans le temps entre les vehicule 
          int pos = 0;// position du second vehicule    
          int t=0;
          // 2 boucles ....
          for (delta=0; delta<scenarioSize; delta ++ ){
              t = delta; 
              for (pos=0;t<scenarioSize; pos++,t++){
                  JSONArray scenarioTemp0= (JSONArray)scenarios.get(pos);
                  JSONArray scenarioTemp1= (JSONArray)scenarios.get(t);
                  JSONArray infocarO = (JSONArray)scenarioTemp0.get(car0);      
                  JSONArray infocar1 = (JSONArray)scenarioTemp1.get(car1); 
                  //System.out.print(infocarO+" "+infocar1);
                  long poscar0 =(long) infocarO.get(2);
                  long poscar1 =(long) infocar1.get(2);
                  //get roads and road
                  
                 // long dynLat =(long) ((JSONArray)(paths.get((int)poscar0))).get(0);
                  
                  
                  long roadcar0 =(long) infocarO.get(1);
                  long roadcar1 =(long) infocar1.get(1);
                 // double dynLat = ((JSONArray)((roads.get((int)roadcar0).get("points"))))
                 // .get((int)poscar0);
                 JSONArray roads0Points = (JSONArray)((JSONObject)roads.get((int)roadcar0)).get("points");
                 double dynLat = (double)roads0Points.get(2*(int)poscar0);
                 double dynLon = (double)roads0Points.get((2*(int)poscar0)+1);
                 JSONArray roads1Points = (JSONArray)((JSONObject)roads.get((int)roadcar1)).get("points");
                 double fixLat = (double)roads1Points.get(2*(int)poscar1);
                 double fixLon = (double)roads1Points.get((2*(int)poscar1)+1);
                  
                 // System.out.print( dynLat + " " + fixLat + " |" );
                  /*double dynLat = (double) infocarO.get(3);
                  double dynLon = (double) infocarO.get(4);
                  double fixLat = (double) infocar1.get(3);
                  double fixLon = (double) infocar1.get(4);*/
                  double distance = calcDistance(dynLat,dynLon,fixLat,fixLon);
                 // if (distance < 0.01) {
                      System.out.print(poscar0 + " " + poscar1 + " ");
                 // }
                 // System.out.print(" "+ distance );
              }
              System.out.println("\n");
              
          }
       
          
        }
          catch(Exception e){
              System.err.println(" ERROR " +e);
          }
        
         
         
         
         
        }
    
    
    
    
}
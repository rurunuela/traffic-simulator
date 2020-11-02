package  com.company;

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
example :
http://www.mkyong.com/java/json-simple-example-read-and-write-json/
*/

public class Simulator {
    static ArrayList <SyncPoint> listeContrainte= new <SyncPoint>ArrayList();;
    public static String getRandomColor() {
        String[] letters = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String color = "#";
        for (int i = 0; i < 6; i++ ) {
            color += letters[(int) Math.round(Math.random() * 15)];
        }
        return color;
    }


    static double calcDistance(JSONArray points,car crt){
        //        if(crt.pos==0) return 0;
        double dynLat = (double)points.get((int)crt.pos);
        double     fixLat = (double)points.get((int)crt.pos+2);
        double dynLon=(double)points.get((int)crt.pos+1);
        double fixLon=(double)points.get((int)crt.pos+3);
        // System.out.println(dynLat+" " + fixLat);

        double dist = (Math.sqrt(Math.pow(dynLat-fixLat,(double)2) +
                Math.pow(dynLon- fixLon,(double)2)) * 100000);
        //System.err.println(dist);
        return dist;
    }
    static Coordonnate getVectorDep(JSONArray points,car crt){
        Coordonnate res = new Coordonnate();

        double dynLat = (double)points.get((int)crt.pos);
        double     fixLat = (double)points.get((int)crt.pos+2);
        double dynLon=(double)points.get((int)crt.pos+1);
        double fixLon=(double)points.get((int)crt.pos+3);
        // System.out.println(dynLat+" " + fixLat);

        double dist = (Math.sqrt(Math.pow(dynLat-fixLat,(double)2) +
                Math.pow(dynLon- fixLon,(double)2)) * 100000);



        double ratio =( crt.dep/dist);
        if (ratio>1) ratio =100;
        double lat = ((fixLat-dynLat)*ratio)+dynLat;
        double lon= ((fixLon-dynLon)*ratio)+dynLon;


        res.Lat= lat;
        //res.Lat=dynLat ;
        //res.Lon=dynLon;
        res.Lon=lon;
                   /* System.err.println("------------------------------");
                    System.err.print(" \n " + dynLat + "  " + fixLat);
                    System.err.print(" \n" + dynLon + "  " + fixLon);
                    System.err.print(" \n AA" + lat + " " + lon );
                    System.err.print("\n"+dist + "\n");
                     System.err.println("------------------------------");*/



        return res;
    }
    public static void testDistance( JSONObject jsonObject){

        int car0 = 0;
        int car1 = 1;

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
          //  t = delta;
            //for (pos=0;t<scenarioSize; pos++,t++){
                JSONArray scenarioTemp0= (JSONArray)scenarios.get(delta);
               // JSONArray scenarioTemp1= (JSONArray)scenarios.get(delta);
               // JSONArray infocarO = (JSONArray)scenarioTemp0.get(car0);
               // JSONArray infocar1 = (JSONArray)scenarioTemp1.get(car1);
                //System.out.print(infocarO+" "+infocar1);
               // long poscar0 =(long) infocarO.get(2);
               // long poscar1 =(long) infocar1.get(2);
                //get roads and road

                // long dynLat =(long) ((JSONArray)(paths.get((int)poscar0))).get(0);


                //long roadcar0 =(long) infocarO.get(1);
                //long roadcar1 =(long) infocar1.get(1);
                // double dynLat = ((JSONArray)((roads.get((int)roadcar0).get("points"))))
                // .get((int)poscar0);
               /* JSONArray roads0Points = (JSONArray)((JSONObject)roads.get((int)roadcar0)).get("points");
                double dynLat = (double)roads0Points.get(2*(int)poscar0);
                double dynLon = (double)roads0Points.get((2*(int)poscar0)+1);
                JSONArray roads1Points = (JSONArray)((JSONObject)roads.get((int)roadcar1)).get("points");
                double fixLat = (double)roads1Points.get(2*(int)poscar1);
                double fixLon = (double)roads1Points.get((2*(int)poscar1)+1);
*/
            double dynLat = (double)((JSONArray)scenarioTemp0.get(0)).get(3);
            double dynLon = (double)((JSONArray)scenarioTemp0.get(0)).get(4);
          //  JSONArray roads1Points = (JSONArray)((JSONObject)roads.get((int)roadcar1)).get("points");
            double fixLat = (double)((JSONArray)scenarioTemp0.get(1)).get(3);
            double fixLon = (double)((JSONArray)scenarioTemp0.get(1)).get(4);
           //      System.out.print( dynLat + " " + fixLat + " |" );
           // System.out.print( dynLon + " " + fixLon + " |" );
                  /*double dynLat = (double) infocarO.get(3);
                  double dynLon = (double) infocarO.get(4);
                  double fixLat = (double) infocar1.get(3);
                  double fixLon = (double) infocar1.get(4);*/


                double distance = calcDistance(dynLat, dynLon, fixLat, fixLon);
                // if (distance < 0.01) {
                System.out.println((distance+"").replace(".",","));
                // }
                // System.out.print(" "+ distance );
           // }
            //System.out.println("\n");

        }





}
    public static void main(String[] args) {
        JSONArray points=new JSONArray();
        try {
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(args[0]));
            JSONObject jsonObject = (JSONObject) obj;
            // JSONObject jId= (JSONObject)jsonObject.get("id");
            // JSONObject jCre= (JSONObject)jsonObject.get("creator");
            // JSONArray cars = (JSONArray) jsonObject.get("cars");
            JSONArray pathsJ = (JSONArray) jsonObject.get("paths");
            Iterator<JSONObject> iterator = pathsJ.iterator();
            path paths[]=new path[pathsJ.size()];
            int idPt=0;
            while (iterator.hasNext()){
                JSONObject path = iterator.next();
                path crt = new path();
                crt.start= (String)path.get("start");
                crt.stop= (String)path.get("stop");
                JSONArray Listepoints=crt.getPolyline();
                JSONObject road= new JSONObject();
                road.put("id",idPt);
                road.put("points",Listepoints);
                road.put("color",getRandomColor());
                road.put("name","road"+idPt);
                points.add(road);
                //AJouter la bonne stucture
                // C'est une liste de route
                //"color":"#0099cc","name":"road1"
                idPt++;

                //road.put("id",")
                //System.out.println(crt.getPolyline());
                //System.out.println(points);



            }
            jsonObject.put("roads", points);

            //Calculate point de contact avant les routes




            //Calculate scenario
            //System.out.println(generateRDVPoint(jsonObject));

            // Calculate contrainte
            //generateContraint();

            JSONArray scenar=generateRoute(jsonObject);
            //System.out.println(jsonObject);
            //System.out.println(scenar);


            jsonObject.put("scenario",scenar);
            //test calcul des distnace en dur
          //  testDistance(jsonObject);
            boolean tocvs=false;
            boolean toJson=true;
            for (int i=0; i < args.length ; i++){
                if (args[i].compareToIgnoreCase("-csv")==0) {
                    tocvs = true;
                    toJson = false;
                }
            }
            if (toJson) {
                System.out.println(jsonObject);
            }
            if(tocvs){
                toScenarCVS(scenar);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }



    }

    private static String toScenarCVS(JSONArray scenar) {
        String res="";
        res ="tick,car,long,lat";
        System.out.println(res);
        for (int i=0;i< scenar.size(); i++){
            JSONArray crt = (JSONArray) scenar.get(i);
            for (int j=0;j<crt.size(); j++){
                JSONArray reg = (JSONArray) crt.get(j);
                System.out.println(reg.get(0)+"," +reg.get(1)+"," +reg.get(3)+","+reg.get(4)
                );
            }

        }


        return res;
    }

    /* static double calcDistance2(double dynLat ,double dynLon,double fixLat,double  fixLon){
         double dist =
                 Math.acos(Math.sin(Math.toRadians(dynLat)));

                 ACOS(SIN(RADIANS(B2))*SIN(RADIANS(B3))+COS(RADIANS(B2))*COS(RADIANS(B3))*COS(RADIANS(C2-C3)))*6371.
     }*/
    static double calcDistance(double dynLat ,double dynLon,double fixLat,double  fixLon){

        // System.out.println(dynLat+" " + fixLat);

        double dist = (Math.sqrt(Math.pow(dynLat-fixLat,(double)2) +
                Math.pow(dynLon- fixLon,(double)2)) * 100000);
        //System.err.println(dist);
        return dist;
    }

    //ACtuellement en dur entre les routes 1 et 0;
    public static JSONArray generateRDVPoint   (JSONObject jsonObject) {
        JSONArray list=new JSONArray();




        //Object obj = parser.parse(new FileReader(args[0]));

        int r0 = 1;
        int r1 = 0;

        SynPointRDV syncP = new SynPointRDV();
        syncP.route0= r0;
        syncP.route1= r1;

        JSONArray roads = (JSONArray) jsonObject.get("roads");
        JSONObject comp=new JSONObject();
        comp.put("path1",r0);
        comp.put("path2",r1);
        JSONArray pointsComm= new JSONArray();
        //System.err.println(" ->"  + scenarios.size());
        double distancetotale=0;

        JSONArray  roads0=(JSONArray) ((JSONObject) roads.get(r0)).get("points");
        JSONArray  roads1=(JSONArray) ((JSONObject) roads.get(r1)).get("points");;

        for (int i=0;i < roads0.size()/2;  i++ ){
            int delta = i;
            for (int j=0; j <  roads1.size()/2 ; j++){
                double distance =-1;
                int lasti=-1;
                int lastj=-1;
                JSONArray couple= new JSONArray();
                do {

                    double dynLat = (double)roads0.get(2*i);
                    double dynLon = (double)roads0.get(2*i+1);
                    double fixLat = (double)roads1.get(2*j);
                    double fixLon = (double)roads1.get(2*j+1);

                    distance = calcDistance(dynLat,dynLon,fixLat,fixLon);
                    System.out.println(distance);
                    distancetotale+=distance;
                    couple= new JSONArray();
                    if (distance==0) {
                        //JSONObject couple= new JSONObject();

                        // System.err.println(" --- ");

                        //couple.put("path1",i);
                        //couple.put("path2",j);
                        if(pointsComm.size()==0){
                            couple.add(i);
                            couple.add(j);
                            pointsComm.add(couple);
                            syncP.rdvPT0 = i;
                            syncP.rdvPT1 = j;
                        }
                        //last point ???
                        else {
                            lastj=j;
                            lasti=i;
                        }
                        // System.out.print(i + " " + j + " ");
                        i++;
                        j++;
                        if(j >=  roads1.size()/2) break;
                        if (i>= roads0.size()/2) break;

                    }

                }
                while (distance ==0) ;
                if (lasti>=0) {
                    couple.add(lasti);
                    couple.add(lastj);

                    pointsComm.add(couple);
                    syncP.EndrdvPT0 = i;
                    syncP.EndrdvPT1 = j;
                }

            }

        }
        comp.put("distance",distancetotale);
        comp.put("points",pointsComm);
        list.add(comp);


        //}
        // catch(Exception e){
        //    System.err.println(" ERROR " +e);
        //}
        // Creation de nouvelle contraite

        listeContrainte.add(syncP);





        return list;
    }
    public static void generateContraint(){

    }
    public static JSONArray generateRoute(JSONObject jsonObject) {
        JSONArray list=new JSONArray();

        try {

            //get cars List
            JSONArray carsA = (JSONArray) jsonObject.get("cars");
            //int num = Integer.parse(jsonObject.get("road"));
            JSONArray roads = (JSONArray) jsonObject.get("roads");
            Iterator<JSONObject> iterator = carsA.iterator();
            car cars[]=new car[carsA.size()];
            //Compute car list
            int pt=0;
            // System.out.print("[");
            while(iterator.hasNext()){
                //if(pt>0)System.out.print(",");
                JSONObject carJ = iterator.next();
                //System.out.println(car.get("road"));
                long num = ((long)carJ.get("road"));
                JSONObject road = (JSONObject)roads.get((int)num);
                // System.out.print(car.get("id"));
                car crt= new car();
                //  crt.id = (long)carJ.get("id");
                crt.id = pt;
                crt.road=   (long)carJ.get("road");
                crt.speed= (long)carJ.get("speed");
                crt.delay=(long)carJ.get("delay");
                Coordonnate res =  new Coordonnate();
                cars[pt]=crt;
                pt++;

            }
            boolean flag=true;
            pt =10;
            boolean sep=false;

            //Boucle sur le temps de parcous de Tout les vehicules
            while (flag){
                if (sep){
                    // System.out.print(",[");
                }
                else {
                    sep =true;
                    //System.out.print("[");
                }
                flag=false;

                //iterator on car
                JSONArray  lCars=new JSONArray();
                for (int c=0; c < cars.length; c++){


                    JSONArray lCar=new JSONArray();



                    //  if(c>0)System.out.print(",");
                    car crt = cars[c];
                    // Interroge voiture et eventuelle changement etat
                    if (crt.sync != null ){

                        // crt.sync;
                    }


                    JSONArray points=( JSONArray)((JSONObject)(roads.get((int)crt.road))).get("points");

                    if (crt.pos!= (points.size()-2)){
                        flag=true;
                        // car toujours sur la route
                        double dist=calcDistance( points,  crt);

                        if (crt.delay ==0 ){
                            crt.dep = crt.dep+crt.speed;
                        }
                        else {
                            crt.delay --;
                            crt.dep=crt.dep+0; // Not move
                        }
                        if(crt.dep<dist){

                        }

                        else{
                            do{

                                crt.dep= crt.dep-dist;
                                crt.pos= crt.pos+2;
                                if(crt.pos!= (points.size()-2)){
                                    dist=calcDistance( points,  crt);
                                }

                                else{
                                    dist=crt.dep+1;
                                }

                            }
                            while(crt.dep>=dist);





                        }


                    }
                    lCar = crt.getJson();
                    //System.out.print(crt);
                    Coordonnate res =  new Coordonnate();


                    //calculate coordonnée decalage
                    if (crt.pos!= (points.size()-2)){
                        res =  getVectorDep(points,  crt);
                    }
                    else {
                        res.Lat= (double)points.get((int)crt.pos);
                        res.Lon= (double)points.get((int)crt.pos+1);
                    }
                    //System.out.print(","+res.Lat+","+res.Lon + "]");
                    lCar.add(res.Lat);
                    lCar.add(res.Lon);

                    lCars.add(lCar);

                }
                list.add(lCars);
                //System.out.print("]\n");


            }







        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }

}

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

/*
example :
http://www.mkyong.com/java/json-simple-example-read-and-write-json/
*/
class car {
    long id;
    double dep=0;
    long pos=0;
    long road;
    long speed=5;//m.s-1 equ 60 km.h
    long delay=0;
    public String toString(){
        return ("["+id+","+road+","+(pos/2));//+"]");
    }
    
}

class Coordonnate{
    public double Lat=0;
    public double Lon=0;
}
public class Simulator {
    
    
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
     public static void main(String[] args) {

	/*JSONObject obj = new JSONObject();
	obj.put("name", "mkyong.com");
	obj.put("age", new Integer(100));

	JSONArray list = new JSONArray();
	list.add("msg 1");
	list.add("msg 2");
	list.add("msg 3");

	obj.put("messages", list);

	try {

		FileWriter file = new FileWriter("resultat.json");
		file.write(obj.toJSONString());
		file.flush();
		file.close();

	} catch (IOException e) {
		e.printStackTrace();
	}

	System.out.print(obj);*/
    
    //Read file Json in input 
    JSONParser parser = new JSONParser();

	try {

		Object obj = parser.parse(new FileReader(args[0]));

		JSONObject jsonObject = (JSONObject) obj;

		/*String name = (String) jsonObject.get("name");
		System.out.println(name);

		long age = (Long) jsonObject.get("age");
		System.out.println(age);

		// loop array
		JSONArray msg = (JSONArray) jsonObject.get("messages");
		Iterator<String> iterator = msg.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}*/
        
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
           // System.out.print(crt);
              Coordonnate res =  new Coordonnate();
              
               
          //    System.out.print(","+res.Lat+","+res.Lon + "]");
            
           cars[pt]=crt;
           pt++; 
            
        }
        //System.out.print("]");
        //System.out.print("\n");
        
        // compute move on list road each car after one
        //flag for compute calculation 
        boolean flag=true;
        pt =10;
        boolean sep=false;
        while (flag){
            if (sep){
                System.out.print(",[");
            }
            else {
                sep =true;
                System.out.print("[");
            }
            flag=false;
           /* pt --;
            if (pt>0){
               flag=true;
                
            }*/
            //iterator on car 
            for (int c=0; c < cars.length; c++){
                if(c>0)System.out.print(",");
                car crt = cars[c];
                //System.out.print(crt);
                //calcul distance beetwen two point on road
                JSONArray points=( JSONArray)((JSONObject)(roads.get((int)crt.road))).get("points");
                
                if (crt.pos!= (points.size()-2)){
                    flag=true;
                  // car toujours sur la route
                   double dist=calcDistance( points,  crt);
                   // System.out.println("\n"+dist + "  " + crt.dep);
                    if (crt.delay ==0 ){
                    crt.dep = crt.dep+crt.speed;
                    }
                    else {
                        crt.delay --;
                        crt.dep=crt.dep+0; // Not move 
                    }
                    if(crt.dep<dist){
                        
                    } 
                    //Pas assez deplacement pour passer à autre point 
                    else{
                            do{
                                
                                crt.dep= crt.dep-dist;
                                crt.pos= crt.pos+2;
                                // System.out.println("\n"+crt.dep + "  " + dist +" " +
                                // crt.pos + " " + points.size()) ;
                                if(crt.pos!= (points.size()-2)){
                                    dist=calcDistance( points,  crt);
                                }
                                
                                else{
                                    dist=crt.dep+1;
                                }
                               
                            }
                            while(crt.dep>=dist);
                            
                            
                            
                            
                        
                    }
                  
                  
                   //while ((crt.dep>dist)&&(crt.pos!= (points.size()-4)) ) {
                   
                    //if (crt.dep>dist){
                     //   crt.dep = crt.dep-dist;
                     //   crt.pos= crt.pos+2;
                        
                        
                     /*   dynLat = (double)points.get((int)crt.pos);
                        fixLat = (double)points.get((int)crt.pos+2);
                        dynLon=(double)points.get((int)crt.pos+1);
                        fixLon=(double)points.get((int)crt.pos+3);
                        dist = Math.sqrt(Math.pow(dynLat-fixLat,(double)2) + 
                        Math.pow(dynLon- fixLon,(double)2)) * 100000;
                    }*/
                    
                     
                    
                
                
                }
                
                System.out.print(crt);
                Coordonnate res =  new Coordonnate();
                
                
                //calculate coordonnée decalage
                 if (crt.pos!= (points.size()-2)){
                    res =  getVectorDep(points,  crt);
                 }
                 else {
                     res.Lat= (double)points.get((int)crt.pos);
                     res.Lon= (double)points.get((int)crt.pos+1);
                 }
                System.out.print(","+res.Lat+","+res.Lon + "]");
                
            }
            System.out.print("]\n");
            
            
        }
        
        
        
        
        
        

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}

     

     }

}
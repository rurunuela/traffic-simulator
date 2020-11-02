package com.company;

import org.json.simple.JSONArray;

public class car {
    enum STATE{
        wait,
        run

    }
    public long id;
    public double dep=0;
    public long pos=0;
    public long road;
    public long ref_speed=5;//m.s-1 equ 60 km.h pour 15
    public long speed=5;//m.s-1 equ 60 km.h pour 15
    public long delay=0;
    public String toString(){
        return ("["+id+","+road+","+(pos/2));//+"]");
    }
    public JSONArray getJson(){
        JSONArray res = new JSONArray();
        res.add(id);
        res.add(road);
        res.add(pos/2);
        return res;
    }
    public STATE state= STATE.run;
    public Synchro sync; // en cas de pb de sync definit le type par exemple attente d'un vehicule



}

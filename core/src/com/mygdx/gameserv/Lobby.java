package com.mygdx.gameserv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CooL on 15.03.2018.
 */

public class Lobby {
    public int idcreator;
    public List<Integer> otherid = new ArrayList<Integer>();
    public HashMap<Integer,Integer> characters= new HashMap<Integer,Integer>(); // key-c.getid value-idnamecharacter(Lance)
    public Lobby(int id) {
        idcreator = id;
        characters.put(idcreator,0);
    }

    public void join(Integer id){
        otherid.add(id);
        characters.put(id,0);
    }
    public void disconnect(Integer id){
        characters.remove(id);
    }


    public int getIdcreator() {
        return idcreator;
    }

    public List<Integer> getOtherid() {
        return otherid;
    }
}

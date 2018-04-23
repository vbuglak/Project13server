package com.mygdx.gameserv.GameMech.Gameobjects;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/**
 * Created by CooL on 20.03.2018.
 */

public class Playerattack {
    public int id;
    public ArrayList<Integer> attackid= new ArrayList<Integer>();
    public int bodytype=50;
    public Rectangle body;
    public float z=0;
    public boolean active=false;
    public boolean rview=false;
    public float depth=5;
    public float dvx=0;
    public float dvy=0;
    public float dvz=0;
    public float damage=50;



    public Playerattack(int cid,float w,float h) {
        id=cid;
        body = new Rectangle(0,0,w,h);
    }


}

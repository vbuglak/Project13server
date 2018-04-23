package com.mygdx.gameserv.GameMech;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.mygdx.gameserv.GameMech.Characters.Characters;
import com.mygdx.gameserv.GameMech.Characters.Testcharacter;
import com.mygdx.gameserv.GameMech.Gameobjects.Playerattack;
import com.mygdx.gameserv.Lobby;
import com.mygdx.gameserv.MyGdxGameServ;
import com.mygdx.gameserv.Frontend.Packets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by CooL on 15.03.2018.
 */

public class GameSession extends ApplicationAdapter implements Runnable{
    private final float MAPSTARTPOINTX=0; //сделать класс gamemap
    private final float MAPENDPOINTX=1460;
    private final float MAPSTARTPOINTY=0;
    private final float MAPENDPOINTY=400;
    public boolean sessionend=false;
    public boolean sessionclose=false;
    private long diff, start = System.currentTimeMillis();
    public  int fps = 60;
    public int deathcounter=0;
    public ArrayList<Characters> character = new ArrayList<Characters>();
    public ArrayList<Playerattack> playerattacklist = new ArrayList<Playerattack>();
    public Packets.Packet13Gamesessionstate packet = new Packets.Packet13Gamesessionstate();
    public Packets.Packet15Gamesessionend pend = new Packets.Packet15Gamesessionend();
    private int creatorid;
    private List<Integer> otherid = new ArrayList<Integer>();

    public int testi=0;

    public GameSession(Lobby lobby) {

        for (Map.Entry<Integer,Integer> entry : lobby.characters.entrySet()) {
            if (entry.getValue() == 0) {
                character.add(new Testcharacter(entry.getKey()));
            }
        }
        for (int i = 0; i < character.size(); i++) {
            playerattacklist.add(character.get(i).getPlayerattack());
        }
        creatorid=lobby.getIdcreator();
        otherid=lobby.getOtherid();
    }


    public void sleep(int fps) {    //перенести на общий случай
        if(fps>0){
            diff = System.currentTimeMillis() - start;
            long targetDelay = 1000/fps;
            if (diff < targetDelay) {
                try{
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {}
            }
            start = System.currentTimeMillis();
        }
    }

    public void collision(){
        for (int i = 0; i < character.size(); i++) {
            if(character.get(i).getPosition().x<MAPSTARTPOINTX){
                character.get(i).setPositionx(MAPSTARTPOINTX);
            }
            if(character.get(i).getPosition().x>MAPSTARTPOINTX+(MAPENDPOINTX-MAPSTARTPOINTX)-character.get(i).getBody().getWidth()){
                character.get(i).setPositionx((MAPENDPOINTX-MAPSTARTPOINTX)-character.get(i).getBody().getWidth());
            }
            if(character.get(i).getPosition().y<MAPSTARTPOINTY){
                character.get(i).setPositiony(MAPSTARTPOINTY);
            }
            if(character.get(i).getPosition().y>MAPENDPOINTY){
                character.get(i).setPositiony(MAPENDPOINTY);
            }
        }
    }

    public void playerattackVSplayerfind(){
        for (int i = 0; i < playerattacklist.size(); i++) {
            for (int j = 0; j < character.size(); j++) {
                if (playerattacklist.get(i).active) {
                    if (((playerattacklist.get(i).body.getY() - playerattacklist.get(i).depth <= character.get(j).getPosition().y + character.get(j).getDepth()) && (playerattacklist.get(i).body.getY() + playerattacklist.get(i).depth >= character.get(j).getPosition().y - character.get(j).getDepth()))) {
                        if ((playerattacklist.get(i).body.getX() <= character.get(j).getPosition().x + character.get(j).getBody().getWidth()) && (playerattacklist.get(i).body.getX() + playerattacklist.get(i).body.getWidth() >= character.get(j).getPosition().x)) {
                            if ((playerattacklist.get(i).z + playerattacklist.get(i).body.getHeight() >= character.get(j).getPosition().z) && (playerattacklist.get(i).z <= character.get(j).getPosition().z + character.get(j).getBody().getHeight())) {
                                playerattackVSplayer(playerattacklist.get(i),character.get(j));
                            }
                        }
                    }
                }
            }
        }
    }

    private void playerattackVSplayer(Playerattack playerattack, Characters character) {
        if (playerattack.bodytype == 50 && character.getBodytype() == 1) { //50bt vs 1bt
            playerattack.attackid.add(playerattack.id);
            if (!playerattack.attackid.contains(character.getCid())) {
                character.HPdecrease(playerattack.damage);
                character.setDamage(true);
                System.out.println("damaga!!! " + Integer.toString(character.getCid()) + " hp- " + Float.toString(playerattack.damage));
                playerattack.attackid.add(character.getCid());
            }
        }


        if (playerattack.bodytype == 51 && character.getBodytype() == 1) {     //51bt vs 1bt
            playerattack.attackid.add(playerattack.id);
            if (!playerattack.attackid.contains(character.getCid())) {
                character.HPdecrease(playerattack.damage);
                if (playerattack.rview==character.isRview()) {
                    character.setAttackview(false);
                } else {
                    character.setAttackview(true);
                }
                if (playerattack.rview) {
                    character.setLaunchspeed(playerattack.dvx, playerattack.dvy, playerattack.dvz);
                } else {
                    character.setLaunchspeed(-playerattack.dvx, playerattack.dvy, playerattack.dvz);
                }
                character.setLaunch(true);
                System.out.println("damaga!!! " + Integer.toString(character.getCid()) + " hp- " + Float.toString(playerattack.damage));
                playerattack.attackid.add(character.getCid());
            }
        }
        if (playerattack.bodytype == 51 && character.getBodytype() == 4) {     //51bt vs 4bt
            playerattack.attackid.add(playerattack.id);
            if (!playerattack.attackid.contains(character.getCid())) {
                character.HPdecrease(playerattack.damage);
                if (playerattack.rview==character.isRview()) {
                    character.setAttackview(false);
                } else {
                    character.setAttackview(true);
                }
                if (playerattack.rview) {
                    character.setLaunchspeed(playerattack.dvx, playerattack.dvy, playerattack.dvz);
                } else {
                    character.setLaunchspeed(-playerattack.dvx, playerattack.dvy, playerattack.dvz);
                }
                character.setLaunch(true);
                System.out.println("damaga!!! " + Integer.toString(character.getCid()) + " hp- " + Float.toString(playerattack.damage));
                playerattack.attackid.add(character.getCid());
            }
        }
    }


    public void update(float dt){
        collision();
        packet.characterinfo = new ArrayList<Player>();  //отправить пакеты по состаянию
        for (int i = 0; i < character.size(); i++) {
            packet.characterinfo.add(new Player(0,character.get(i).getPosition(),character.get(i).isRview(),character.get(i).getState(),character.get(i).getCid(),character.get(i).isAttackview()));
        }
        MyGdxGameServ.server.sendToUDP(creatorid,packet);
        for (Integer element: otherid) {
            MyGdxGameServ.server.sendToUDP(element,packet);
        }
        packet.characterinfo.clear();
       // System.out.println("tik"+Integer.toString(testi++));
        sleep(fps);
    }

    @Override
    public void render() {
       //
        for (int i = 0; i < character.size(); i++) {
            character.get(i).updatech();
        }
        update(Gdx.graphics.getDeltaTime());
        playerattackVSplayerfind();
        for (int i = 0; i < character.size(); i++) {
            if (character.get(i).isDead()){
                deathcounter++;
            }
        }
        if ((deathcounter==character.size()-1)&&character.size()!=1/*||(deathcounter==character.size())*/){
            sessionend=true;
        }
        else {
            deathcounter=0;
        }
    }

    public int getCreatorid() {
        return creatorid;
    }

    public List<Integer> getOtherid() {
        return otherid;
    }

    @Override
    public void run() {
        do
        {
            if(!sessionend)	//Проверка на необходимость завершения
            {
                render();
            }
            else {
                MyGdxGameServ.server.sendToTCP(creatorid,pend);
                for (Integer element: otherid) {
                    MyGdxGameServ.server.sendToTCP(element,pend);
                }
                sessionclose=true;
                return;
            }//Завершение потока

           /* try{
                Thread.sleep(1000);
            }catch(InterruptedException e){}*/
        }
        while(true);
    }
}

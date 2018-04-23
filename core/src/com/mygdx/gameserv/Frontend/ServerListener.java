package com.mygdx.gameserv.Frontend;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.gameserv.GameMech.GameSession;
import com.mygdx.gameserv.Lobby;
import com.mygdx.gameserv.MyGdxGameServ;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.mygdx.gameserv.MyGdxGameServ.servdb;


/**
 * Created by CooL on 13.03.2018.
 */

public class ServerListener extends Listener {
   // static Map<Integer, Player> players = new HashMap<Integer, Player>();//заменить на аккаунт систему
    public List<Lobby> LobbyList = new ArrayList<Lobby>();
    public List<GameSession> GameSessionList = new ArrayList<GameSession>(); //придумать реализацию глобал тайма для отправки пакетов





    public ServerListener() {

    }

    @Override
    public void connected(Connection c) {
       System.out.println("client connected "+ c.getRemoteAddressTCP().getHostString());
        /*Packets.Packet02Autorize packet = new Packets.Packet02Autorize();
        packet.autorize=true;
        packet.id=c.getID();
        MyGdxGameServ.server.sendToTCP(c.getID(),packet);*/
    }

    @Override
    public void disconnected(Connection c) {
        System.out.println("кто-то вышел");
   //     players.remove(c.getID());
        Iterator<Lobby> itr = LobbyList.iterator();
        System.out.println("кто-то вышел");
        while (itr.hasNext()) {
            Lobby temp = itr.next();
            if (temp.idcreator == c.getID()) {
                for (Integer element : temp.otherid) {
                    Packets.Packet08Cancellobby v = new Packets.Packet08Cancellobby();
                    MyGdxGameServ.server.sendToTCP(element, v);
                    System.out.println("отправляю инфо лоби сдохло" + Integer.toString(element));
                }
                itr.remove();
            }
        }
        Iterator<Lobby> itr3 = LobbyList.iterator();
        while (itr3.hasNext()) {
            Lobby temp = itr3.next();
            Iterator<Integer> itr2 = temp.otherid.iterator();
            while (itr2.hasNext()) {
                Integer temp2 = itr2.next();
                if (temp2 == c.getID()) {
                    itr2.remove();
                    Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                    v.idcreator = temp.idcreator;
                    v.otherid = temp.getOtherid();
                    MyGdxGameServ.server.sendToTCP(temp.idcreator, v);
                }
            }
            Iterator<Integer> itr4 = temp.otherid.iterator();
            while (itr4.hasNext()) {
                Integer temp2 = itr4.next();
                Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                v.idcreator = temp.idcreator;
                v.otherid = temp.getOtherid();
                MyGdxGameServ.server.sendToTCP(temp2, v);
                System.out.println("отправляю инфо");
            }
        }


    }

    @Override
    public void received(Connection c, Object o) { //убрать пустые пакеты
        if (o instanceof Packets.Packet01Message) {
            Packets.Packet01Message p = (Packets.Packet01Message) o;
            System.out.println("клиент пишет:" + p.message);
        }
        if (o instanceof Packets.Packet03Createlobby) {
            System.out.println("создать лобби");
            LobbyList.add(new Lobby(c.getID()));
            Packets.Packet09Lobbycreated p = new Packets.Packet09Lobbycreated();
            p.id=c.getID();
            MyGdxGameServ.server.sendToTCP(c.getID(), p);
        }
        if (o instanceof Packets.Packet04Findlobby) {
            System.out.println("искать лобби");
            Packets.Packet06Lobbylist p = new Packets.Packet06Lobbylist();
            if (LobbyList.size() > 0) {
                for (int i = 0; i < LobbyList.size(); i++) {
                    p.id[i] = LobbyList.get(i).idcreator;
                }
                System.out.println("отправляю список");
                MyGdxGameServ.server.sendToTCP(c.getID(), p);
            } else {
                p.id = null;
                MyGdxGameServ.server.sendToTCP(c.getID(), p);
            }
        }
        if (o instanceof Packets.Packet07Joinlobby) {
            Packets.Packet07Joinlobby p = (Packets.Packet07Joinlobby) o;
            for (int i = 0; i < LobbyList.size(); i++) {
                if (LobbyList.get(i).idcreator == p.lobbyid) {
                    LobbyList.get(i).join(c.getID());
                    Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                    v.idcreator=LobbyList.get(i).idcreator;
                    v.otherid=LobbyList.get(i).getOtherid();
                    MyGdxGameServ.server.sendToTCP(LobbyList.get(i).idcreator, v);
                    System.out.println("отправляю инфо" + Integer.toString(LobbyList.get(i).idcreator));
                    for (Integer element : LobbyList.get(i).otherid) {
                        MyGdxGameServ.server.sendToTCP(element, v);
                        System.out.println("отправляю инфо" + Integer.toString(element));
                    }
                }

            }
        }

        if (o instanceof Packets.Packet10Givelinfo) {
            Packets.Packet10Givelinfo p = (Packets.Packet10Givelinfo) o;
            for (int i = 0; i < LobbyList.size(); i++) {
                if (LobbyList.get(i).idcreator==p.id){
                    Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                    v.idcreator=LobbyList.get(i).idcreator;
                    v.otherid=LobbyList.get(i).getOtherid();
                    MyGdxGameServ.server.sendToTCP(c.getID(), v);
                }
            }
        }
        if (o instanceof Packets.Packet11Lobyback) {
            Iterator<Lobby> itr = LobbyList.iterator();
            System.out.println("кто-то вышел");
            while (itr.hasNext()) {
                Lobby temp = itr.next();
                if (temp.idcreator == c.getID()) {
                    for (Integer element : temp.otherid) {
                        Packets.Packet08Cancellobby v = new Packets.Packet08Cancellobby();
                        MyGdxGameServ.server.sendToTCP(element, v);
                        System.out.println("отправляю инфо лоби сдохло" + Integer.toString(element));
                    }
                    temp.disconnect(c.getID());
                    itr.remove();
                }
            }
            Iterator<Lobby> itr3 = LobbyList.iterator();
                while (itr3.hasNext()) {
                    Lobby temp = itr3.next();
                    Iterator<Integer> itr2 = temp.otherid.iterator();
                    while (itr2.hasNext()) {
                        Integer temp2 = itr2.next();
                        if (temp2 == c.getID()) {
                            temp.disconnect(c.getID());
                            itr2.remove();
                            System.out.println("этот додик вышел");
                            Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                            v.idcreator = temp.idcreator;
                            v.otherid = temp.getOtherid();
                            MyGdxGameServ.server.sendToTCP(temp.idcreator, v);
                        }
                    }
                    Iterator<Integer> itr4 = temp.otherid.iterator();
                    while (itr4.hasNext()) {
                        Integer temp2 = itr4.next();
                        Packets.Packet05Lobbyinfo v = new Packets.Packet05Lobbyinfo();
                        v.idcreator = temp.idcreator;
                        v.otherid = temp.getOtherid();
                        MyGdxGameServ.server.sendToTCP(temp2, v);
                        System.out.println("отправляю инфо");
                        }
                }

            }
        if (o instanceof Packets.Packet12Gamesessionstart) {
            System.out.println("start sess");
            for (int i = 0; i < LobbyList.size(); i++) {
                if (LobbyList.get(i).idcreator == c.getID()) {
                    GameSessionList.add(new GameSession(LobbyList.get(i)));
                    Thread Sessiontread = new Thread(GameSessionList.get(GameSessionList.size()-1));
                    Sessiontread.start();
                    System.out.println("Treadstart "+ Integer.toString(i));
                }
            }
        }
        if (o instanceof Packets.Packet14Playerbuttons) {
            Packets.Packet14Playerbuttons p = (Packets.Packet14Playerbuttons) o;
            for (int i = 0; i < GameSessionList.size(); i++) {
                if (c.getID()==GameSessionList.get(i).getCreatorid()){
                    for (int j = 0; j < GameSessionList.get(i).character.size(); j++) {
                        if (c.getID()==GameSessionList.get(i).character.get(j).getCid()){
                            GameSessionList.get(i).character.get(j).setKeyup(p.Keyup);
                            GameSessionList.get(i).character.get(j).setKeydown(p.Keydown);
                            GameSessionList.get(i).character.get(j).setKeyleft(p.Keyleft);
                            GameSessionList.get(i).character.get(j).setKeyright(p.Keyright);
                            GameSessionList.get(i).character.get(j).setKeyA(p.KeyA);
                            GameSessionList.get(i).character.get(j).setKeyJ(p.KeyJ);
                            GameSessionList.get(i).character.get(j).setKeyD(p.KeyD);
                        }
                    }
                }
                for (int k = 0; k < GameSessionList.get(i).getOtherid().size(); k++){
                    if (c.getID()==GameSessionList.get(i).getOtherid().get(k)){
                        for (int l = 0; l < GameSessionList.get(i).character.size(); l++) {
                            if (c.getID()==GameSessionList.get(i).character.get(l).getCid()){
                                GameSessionList.get(i).character.get(l).setKeyup(p.Keyup);
                                GameSessionList.get(i).character.get(l).setKeydown(p.Keydown);
                                GameSessionList.get(i).character.get(l).setKeyleft(p.Keyleft);
                                GameSessionList.get(i).character.get(l).setKeyright(p.Keyright);
                                GameSessionList.get(i).character.get(l).setKeyA(p.KeyA);
                                GameSessionList.get(i).character.get(l).setKeyJ(p.KeyJ);
                                GameSessionList.get(i).character.get(l).setKeyD(p.KeyD);
                            }
                        }
                    }
                }
            }
        }
        if (o instanceof Packets.Packet16Login) {
            Packets.Packet16Login p = (Packets.Packet16Login) o;
            System.out.println("login:"+p.login);
            System.out.println("password:"+p.password);
            Packets.Packet16Login v = new Packets.Packet16Login();
            if (servdb.login(p.login,p.password)==0){
                System.out.println("логина нет");
                v.statusinfo=0;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);

            }
            if (servdb.login(p.login,p.password)==1){
                System.out.println("пароль херня");
                v.statusinfo=1;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);
            }
            if (servdb.login(p.login,p.password)==2){
                v.statusinfo=2;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);
                Packets.Packet02Autorize packet = new Packets.Packet02Autorize();
                packet.autorize=true;
                packet.id=c.getID();
                MyGdxGameServ.server.sendToTCP(c.getID(),packet);
            }
        }
        if (o instanceof Packets.Packet17Register) {
            Packets.Packet17Register p = (Packets.Packet17Register) o;
            System.out.println("login:" + p.login);
            System.out.println("password:" + p.password);
            Packets.Packet17Register v = new Packets.Packet17Register();
            if (servdb.register(p.login, p.password) == 0) {
                System.out.println("alllo");
                v.statusinfo = 2;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);
            }
            else if (servdb.register(p.login, p.password) == 2) {
                System.out.println("такой логин уже есть");
                v.statusinfo = 0;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);
            }
            else if (servdb.register(p.login, p.password) == 1) {
                System.out.println("пароль неверен");
                v.statusinfo = 1;
                MyGdxGameServ.server.sendToTCP(c.getID(), v);
            }

        }

    }



    @Override
    public void idle(Connection c) {

    }
}

package com.mygdx.gameserv.Frontend;

import com.mygdx.gameserv.GameMech.Player;

import java.util.List;

/**
 * Created by CooL on 13.03.2018.
 */

public class Packets {

    public static class  Packet01Message{
      public String message;
    }
    public static class  Packet02Autorize{
        public boolean autorize;
        public int id;
    }
    public static class  Packet03Createlobby{
    }
    public static class  Packet04Findlobby{
    }
    public static class  Packet05Lobbyinfo{
        public int idcreator;
        public List<Integer> otherid;
    }
    public static class  Packet06Lobbylist{
        public int id[]= new int[20];
    }
    public static class  Packet07Joinlobby{
        public Integer lobbyid;
    }
    public static class  Packet08Cancellobby{
    }
    public static class  Packet09Lobbycreated{
        public int id;
    }
    public static class  Packet10Givelinfo{
        public int id;
    }
    public static class  Packet11Lobyback{
    }
    public static class  Packet12Gamesessionstart{
    }
    public static class  Packet13Gamesessionstate{
        public List<Player> characterinfo;
    }
    public static class  Packet14Playerbuttons{
        public boolean Keyup;
        public boolean Keydown;
        public boolean Keyleft;
        public boolean Keyright;
        public boolean KeyA;
        public boolean KeyJ;
        public boolean KeyD;
    }
    public static class  Packet15Gamesessionend{
    }
    public static class  Packet16Login{
        public String login;
        public String password;
        public short statusinfo;
    }
    public static class  Packet17Register{
        public String login;
        public String password;
        public short statusinfo;
    }


}

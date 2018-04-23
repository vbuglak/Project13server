package com.mygdx.gameserv.GameMech.Characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.gameserv.GameMech.Gameobjects.Playerattack;

import java.util.ArrayList;

/**
 * Created by CooL on 18.03.2018.
 */

public class Testcharacter implements Characters{

    private Vector3 position;
    private Vector3 speed;
    private Rectangle body;
    private int cid;
    private int bodytype;
    private int depth;
    private int id=999;
    private int state;      // 1-стоим(stay) 2-идем(walk) 3-прыгаем(jump) 4- бежим(run) 5-атакуем 6-defend 7-crouch 8-injured
    private static final float YSPEED = 1;
    private final static float XSPEED= 2;
    private final static float RUNSPEED= 6;
    private final static float JUMPSPEED= 7;
    private final static float GRAVITY= 0.3f;
    private float hp;
    private boolean dead;
    private boolean rview;
    public boolean attackview;
    public boolean rviewblock=false;
    public boolean cancelstate=false;
    public boolean move;
    public boolean jump;
    public boolean run;
    public boolean runjump;
    public boolean jumpattack=false;
    public boolean runattack=false;
    public boolean runjumpattack=false;
    public boolean attack;
    public boolean defend=false;
    public boolean crouch;
    public boolean damage;
    public boolean fall;
    public boolean launch=false;

    public boolean Keyup;
    public boolean Keydown;
    public boolean Keyleft;
    public boolean Keyright;
    public boolean KeyA;
    public boolean KeyJ;
    public boolean KeyD;
    public boolean actiondidA=false;
    public boolean actiondidJ=false;
    public boolean actiondidD=false;

    public float dx;
    public float dy;
    private float dz=0;
//////////////////////////////////////
    public boolean startl=false;
    public boolean startr=false;
    public boolean runinterrupt=false;
    public boolean runready=false;
    public boolean startstay=false;            ///isrun
    public boolean lastkeyleft=false;
    public boolean runrestart=false;
    public boolean runr=false;
    public boolean runl=false;
    public float klt;
    public float krt;
    public float stayt;
//////////////////////////////////////
    public boolean attackst=false;            ////attack
    public boolean combo=false;
    public boolean resetattack=false;
    public float startattime=0;
    public int numberhit=0;
    public boolean resetstartattack=false;
    /////////////////////////////////
    public boolean defendst=false;              //defend
    public float defendstarttime=0;
    ////////////////////////////////
    public boolean crouchst=false;              //crouch
    public float startcrtime=0;
    ///////////////////////////////
    public boolean startinj=false;              //injured
    public float injstarttime=0;
    public int injhp;
    ////////////////////////////
    public boolean juglest=false;              //jugle
    ////////////////////////////////
    public boolean fallst=false;                //fall+wake
    public float fallstarttime=0;
    public boolean wakeinvul=false;
    public float wakestarttime=0;
    ////////////////////////////
    public boolean skill1;                      //skill
    public float keytime1;
    public float keytime2;
    public float startskill1time;
    public boolean skill1st;
    /////////////////////////




    public Playerattack playerattack;



    public Testcharacter(int id) {
        position = new Vector3(100, 100, 0);
        speed = new Vector3(0, 0, 0);
        state = 1;  // 1-стоим(stay) 2-идем(walk) 3-прыгаем(jump) 4- бежим(run) 5-атакуем 6-defend 7-crouch 8-injuredм
        body = new Rectangle(0,0,80,80);
        bodytype = 1;
        depth = 5;
        hp=5000;
        dead=false;
        rview=true;
        cid=id;
        playerattack = new Playerattack(cid,15,80);
    }


    public void updatech() {
        if (hp<=0){
            dead=true;
            state=99;
        }
        if (actiondidA&&!KeyA){
            actiondidA=false;
        }
        if (actiondidJ&&!KeyJ){
            actiondidJ=false;
        }
        if (actiondidD&&!KeyD){
            actiondidD=false;
        }
        playerattack.rview=rview;
        body.x = position.x;
        body.y = position.y;
        statecontrol();
        handleInput();
    }

    public void HPdecrease(float dmg){
        hp-=dmg;
    }

    private void statecontrol() {  // 1-стоим(stay) 2-идем(walk) 3-прыгаем(jump) 4- бежим(run) 5-атакуем 6-defend 7-crouch 8-injured

        if (state == 1) {     //////////STAY
            if (cancelstate) {
                cancelstate();
            }
            bodytype=1;
            stay();
            if (move) {
                state = 2;
            }
            if (jump) {
                state = 3;
                dz = JUMPSPEED;
            }
            if (run) {
                state = 4;
            }
            if (attack) {
                state = 5;
            }
            if (defend) {
                state = 10;
            }
            if (damage) {
                state = 12;
                cancelstate();
            }
            if (launch) {
                state = 13;
                cancelstate();
            }
            if (fall) {
                state = 14;
                cancelstate();
            }
            if (skill1) {
                state = 15;
            }

        }
        if (state == 2) {///////////WALK
            bodytype = 1;
            walk();
            if (!move) {
                state = 1;
                cancelstate=true;
            }
            if (jump) {
                state = 3;
                dz = JUMPSPEED;
            }
            if (run) {
                state = 4;
            }
            if (attack) {
                state = 5;
            }
            if (defend) {
                state = 10;
            }
            if (damage) {
                state = 12;
                cancelstate();
            }
            if (launch) {
                state = 13;
                cancelstate();
            }
            if (fall) {
                state = 14;
                cancelstate();
            }
            if (skill1) {
                state = 15;
            }

        }
        if (state==3) { //////////////JUMP
            bodytype = 1;
            jump();
            if (!jump) {
                state = 1;
                cancelstate=true;
            }
            if (attack){
                state=7;
                jumpattack=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
        }
            if (state==4){         //////////RUN
                bodytype=1;
                run();
                if (!run){
                    state = 1;
                    cancelstate=true;
                }
                if (!move){
                    run=false;
                }
                if (jump){
                    runjump=true;
                    state = 6;
                    dz=JUMPSPEED;
                }
                if (attack){
                    runattack = true;
                    state=8;
                }
                if (crouch){
                    state=11;
                }
                if (damage){
                    state = 12;
                    cancelstate();
                }
                if (launch){
                    state = 13;
                    cancelstate();
                }
                if (fall){
                    state = 14;
                    cancelstate();
                }
                if (skill1){
                    state = 15;
                }
            }
        if (state==5){      //////////////ATTACK
            attack();
            if (!attack){
                state = 1;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
            if (skill1){
                state = 15;
            }
        }
        if (state==6) {      //////////////runjump
            runjump();
            if (!runjump) {
                state = 1;
                cancelstate=true;
            }
            if (attack){
                state=9;
                runjumpattack=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
        if (state==7) {      //////////////JUMPATTACK
            jumpattack();
            if (!jumpattack) {
                state = 1;
                cancelstate=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
        if (state==8) {      //////////////runATTACK
            runattack();
            if (!runattack) {
                state = 1;
                cancelstate=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
        if (state==9) {      //////////////runjumpattack
            runjumpattack();
            if (!runjumpattack) {
                state = 1;
                cancelstate=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
           if (state==10){      //////////////DEfend
            defend();
            if (!defend){
                state = 1;
                cancelstate=true;
            }
               if (skill1){
                   state = 15;
               }
        }
        if (state==11){      //////////////crouch
            crouch();
            if (!crouch){
                state = 1;
                cancelstate=true;
            }
            if (skill1){
                state = 15;
            }
        }
        if (state==12){  ///////////////injured
            injured();
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
        if (state==13){  ///////////////jugle
            jugle();
            if (fall){
                state=14;
                cancelstate();
            }
        }
        if (state==14){  //////////fall
            fall();
            if (!fall){
                state=1;
                cancelstate();
            }
        }
        if (state==15){  //////////skill1
            skill1();
            if (!skill1){
                state=1;
                cancelstate=true;
            }
            if (damage){
                state = 12;
                cancelstate();
            }
            if (launch){
                state = 13;
                cancelstate();
            }
            if (fall){
                state = 14;
                cancelstate();
            }
        }
        if (state==99){
            dead();
        }
    }


    public void handleInput() {
        if (Keyleft) {
            speed.x = -XSPEED;
            move = true;
            if (!rviewblock) {
                rview = false;
            }
        }

        if (Keyright) {
            speed.x = XSPEED;
            move = true;
            if (!rviewblock) {
                rview = true;
            }

        }
        if (Keydown) {
            speed.y = -YSPEED;
            move = true;
        }
        if (Keyup) {
            speed.y = YSPEED;
            move = true;
        }
        if (KeyJ) {
            if(actiondidJ){
                jump=false;
            } else {
                jump = true;
            }
        }

        if (KeyA) {
            if(actiondidA){
                attack=false;
            } else {
                attack = true;
            }
        }
        if (KeyD){
            if(actiondidD){
                defend=false;
                crouch=false;
            } else {
                defend = true;
                crouch = true;
            }
        }

        if (!Keyleft&&!Keydown&&!Keyright&&!Keyup){
            move=false;
        }
        isrun();
        comboinput();
    }

    public void isrun(){
        if (Keyleft) {
            if (runrestart){
                runready=false;
                runrestart=false;
            }
            startr=false;
            startstay = false;
            lastkeyleft = true;
            if (!startl) {
                klt = TimeUtils.nanoTime();
                startl = true;
            }

        }
        if (Keyleft&&(TimeUtils.nanoTime()-klt>300000000)&&!run){
            runinterrupt=true;
        }

        if (!move){
            startl=false;
            startr=false;
            runr=false;
            runl=false;
            run=false;
        }
        if (!move&&!runinterrupt){
            if (!startstay){
                stayt = TimeUtils.nanoTime();
            }
            startstay=true;
            if (TimeUtils.nanoTime()-stayt<100000000) {
                runready = true;
            }
            else {
                runready = false;
            }
        }
        if (!move&&runinterrupt){
            runinterrupt=false;
            runrestart=true;
        }
        if (Keyleft&&runready&&!runrestart&&lastkeyleft){
            run=true;
            runl=true;
            if (runr){
                run=false;
                runr=false;
                runl=false;
                runready=false;
            }
        }
        /////////////////////////////
        if (Keyright){
            if (runrestart){
                runready=false;
                runrestart=false;
            }
            startl=false;
            startstay=false;
            lastkeyleft=false;
            if (!startr){
                krt = TimeUtils.nanoTime();
                startr=true;
            }
            if (Keyright&&(TimeUtils.nanoTime()-krt>300000000)&&!run){
                runinterrupt=true;
            }
        }
        if (Keyright&&runready&&!runrestart&&!lastkeyleft){
            run=true;
            runr=true;
            if (runl){
                run=false;
                runr=false;
                runl=false;
                runready=false;
            }
        }

    }

    public void comboinput() {

        if (KeyD) {
            keytime1 = TimeUtils.nanoTime();
        }

        if (TimeUtils.nanoTime() - keytime1 < 300000000)  {
            if((Keyleft)||(Keyright)){
                keytime2 = TimeUtils.nanoTime();
            }
        }
        if (TimeUtils.nanoTime() - keytime2 < 300000000){
            if (KeyA){
                skill1=true;
            }
        }
    }

    private void cancelstate(){
        move=false;
        jump=false;
        run=false;
        attack=false;
        runjump=false;
        jumpattack=false;
        runattack=false;
        defend=false;
        crouch=false;
        rviewblock=false;
        playerattack.active=false;
        cancelstate = false;
    }

    private void stay() {
        if (wakeinvul){
            wakestarttime = TimeUtils.nanoTime();
            wakeinvul = false;
            bodytype=49;
        }
        if ((TimeUtils.nanoTime() - wakestarttime < 1000000000)){
            bodytype=49;
        }
        else {
            bodytype=1;
        }
        speed.x=0;
        speed.y=0;
    }

    private void walk() {
        position.add(speed.x,speed.y,0);
        speed.x=0;
        speed.y=0;
    }

    private void jump(){
        if  (position.z >= 0) {
            dz-=GRAVITY;
            position.add(speed.x, speed.y, dz);
        } else {
            jump = false;
            position.z = 0;
            actiondidJ=true;
        }
    }

    private void run() {

        if (rview&&runr) {
            position.add(RUNSPEED, speed.y / 2, 0);
        } else if (!rview&&runr){
            run = false;
            runr=false;
            runl=false;
        }
        if (!rview&&runl) {
            position.add(-RUNSPEED, speed.y / 2, 0);
        } else if (rview&&runl){
            run = false;
            runr=false;
            runl=false;
        }

    }

    private void attack() { //нужен нормальный алгоритм
        if (!attackst) {
            startattime = TimeUtils.nanoTime();
            attackst = true;
            numberhit=1;
            playerattack.bodytype=50;
            playerattack.dvz=0;
            playerattack.dvx=0;
            playerattack.dvy=0;
        }

            if (TimeUtils.nanoTime() - startattime < 1000000000 / 3 ) { ////stay attack



                if (KeyA && resetattack) {
                    combo = true;
                }
                if (!KeyA) {
                    resetattack = true;
                }
                if (rview) {
                    position.add(1, 0, 0);
                } else {
                    position.add(-1, 0, 0);
                }
                if ((rview) && (TimeUtils.nanoTime() - startattime > 1000000000 / 6)) {
                    playerattack.body.setPosition(position.x + 10, position.y);
                } else if (TimeUtils.nanoTime() - startattime > 1000000000 / 6) {
                    playerattack.body.setPosition(position.x - 5, position.y);
                }
                playerattack.active = true;

            } else if ((combo) && (numberhit != 3)) {
                resetattack = false;
                combo = false;
                startattime = TimeUtils.nanoTime();
                playerattack.attackid.clear();
                numberhit++;
                if (numberhit == 3) {
                    playerattack.bodytype = 51;
                    playerattack.dvz = 7;
                    playerattack.dvx = 1;
                    playerattack.dvy = 0;
                    playerattack.damage = 100;
                }
            } else if ((!combo) || (numberhit == 3)) {
                resetattack = false;
                resetstartattack=false;
                attack = false;
                attackst = false;
                playerattack.bodytype = 50;
                playerattack.dvx=4;
                playerattack.dvy=0;
                playerattack.dvz=10;
                playerattack.attackid.clear();
                playerattack.active = false;
                actiondidA=true;

            }
    }  //Отрегулировать хитбоксы

    private void runjump(){
        if (position.z >= 0) {
            rviewblock=true;
            dz-=GRAVITY;
            if (rview) {
                position.add(RUNSPEED, speed.y, dz);
            }
            if (!rview) {
                position.add(-RUNSPEED, speed.y, dz);
            }
        } else {
            rviewblock=false;
            runjump = false;
            position.z = 0;
            dz=0;
            actiondidJ=true;
        }
    }

    private void jumpattack(){ //hitbox!!!
        if (position.z >= 0) {
            rviewblock=true;
            dz-=GRAVITY;
            playerattack.active=true;
            playerattack.bodytype = 51;
            playerattack.dvz = -100;
            playerattack.dvx = 3;
            playerattack.dvy = 0;
            if (rview) {
                position.add(XSPEED, speed.y, dz);
                playerattack.body.setPosition(position.x + 10, position.y);
            }
            if (!rview) {
                position.add(-XSPEED, speed.y, dz);
                playerattack.body.setPosition(position.x - 5, position.y);
            }
        } else {
            rviewblock=false;
            jumpattack = false;
            position.z = 0;
            dz=0;
            playerattack.active=false;
            playerattack.attackid.clear();
            actiondidA=true;
        }

    } //hitbox!!!

    private void runattack(){
        playerattack.rview=rview;
        if (!attackst) {
            startattime = TimeUtils.nanoTime();
            attackst = true;
            playerattack.damage=100;
            playerattack.bodytype=51;
            playerattack.dvz=4;
            playerattack.dvx=10;
            playerattack.dvy=0;
        }
        rviewblock=true;
        if (TimeUtils.nanoTime() - startattime < 1000000000 ) { ////stay attack
            if (rview) {
                position.add(RUNSPEED, 0, 0);
                playerattack.body.setPosition(position.x + 10, position.y);
            } else {
                position.add(-RUNSPEED, 0, 0);
                playerattack.body.setPosition(position.x - 5, position.y);
            }
            playerattack.active = true;

        } else {
            playerattack.bodytype = 50;
            playerattack.attackid.clear();
            playerattack.active = false;
            playerattack.damage = 50;
            rviewblock=false;
            runattack=false;
            actiondidA=true;
            attackst = false;
        }
    }

    private void runjumpattack(){
        if (position.z >= 0) {
            rviewblock=true;
            dz-=GRAVITY;
            playerattack.active=true;
            playerattack.bodytype = 51;
            playerattack.dvz = 5;
            playerattack.dvx = 6;
            playerattack.dvy = 0;
            if (rview) {
                position.add(RUNSPEED, speed.y/2, dz);
                playerattack.body.setPosition(position.x + 10, position.y);
            }
            if (!rview) {
                position.add(-RUNSPEED, speed.y/2, dz);
                playerattack.body.setPosition(position.x - 5, position.y);
            }
        } else {
            rviewblock=false;
            runjumpattack = false;
            position.z = 0;
            dz=0;
            playerattack.active=false;
            playerattack.attackid.clear();
            actiondidA=true;
            actiondidJ=true;
        }
    }

    private void defend(){
        if (!defendst) {
            defendstarttime = TimeUtils.nanoTime();
            defendst = true;
        }
        rviewblock=true;
        if ((TimeUtils.nanoTime() - defendstarttime < 1000000000)) {
            bodytype = 2;
        }
        else {
            defend = false;
            defendst=false;
            bodytype = 1;
            rviewblock=false;
            actiondidD=true;
        }
    }

    private void crouch(){
        if (!crouchst) {
            startcrtime = TimeUtils.nanoTime();
            crouchst = true;
        }
        rviewblock=true;
        if (TimeUtils.nanoTime() - startcrtime < 1000000000 ) {
            bodytype=3;
            if (rview) {
                position.add(RUNSPEED, 0, 0);
            } else {
                position.add(-RUNSPEED, 0, 0);
            }
        } else {
            rviewblock=false;
            bodytype=1;
            crouchst=false;
            crouch=false;
            fall=false;
            actiondidD=true;
        }
    }

    private void injured(){
        if (!startinj) {
            injstarttime = TimeUtils.nanoTime();
            startinj = true;
        }
        rviewblock=true;
        if (TimeUtils.nanoTime() - injstarttime < 1000000000) {
            if(position.z>0){
                dz-=GRAVITY;
                position.add(dx,dy,dz);
            }
            if (damage){
                startinj = false;
                injhp++;
            }
            damage=false;
            if (injhp>4){
                damage=false;
                state = 10;
                startinj = false;
                injhp=0;
                fall=true;
            }
        }
        else {
            if (position.z>0){
                state=3;
            } else {
                state=1;
            }
            startinj=false;
            damage=false;
            injhp=0;
            cancelstate=true;
            rviewblock=false;
        }
    }

    private void jugle(){
        bodytype=4;
        injhp=0;
        if (!juglest) {
            position.add(dx, dy, dz);
            juglest=true;
        }
        rviewblock=true;
        if (position.z>0) {
            dz-=GRAVITY;
            position.add(dx,dy,dz);
        }
        else {
            juglest=false;
            bodytype=1;
            launch=false;
            fall=true;
            position.z=0;
            startinj=false;
            rviewblock=false;
        }
    }

    private void fall(){
        bodytype=5;
        injhp=0;
        rviewblock=true;
        if (!fallst) {
            fallstarttime = TimeUtils.nanoTime();
            fallst = true;
        }
        if ((TimeUtils.nanoTime() - fallstarttime < 1000000000)){
            if (((Keyleft)||(Keyright)) && (TimeUtils.nanoTime() - fallstarttime > 800000000 )) {
                if (Keyleft){
                    rview=false;
                }
                if (Keyright){
                    rview=true;
                }
                fallst=false;
                state=11;
                bodytype=3;
                crouch=true;
            }
        }
        else{
            fall=false;
            fallst=false;
            bodytype=49;
            wakeinvul=true;
            rviewblock=false;
            cancelstate=true;
        }
    }

    private void dead(){
        bodytype=49;
        if(position.z>0) {
            dz -= GRAVITY;
            position.add(dx, dy, dz);
        }
        else {
            position.z=0;
        }
    }

    private void skill1(){

        if (!skill1st) {
            startskill1time = TimeUtils.nanoTime();
            skill1st = true;
            numberhit=1;
            playerattack.bodytype=51;
            playerattack.dvz=5;
            playerattack.dvx=1;
            playerattack.dvy=0;
        }

        if (TimeUtils.nanoTime() - startskill1time < 1000000000 / 3 ) {

            if (KeyA && resetattack) {
                combo = true;
            }
            if (!KeyA) {
                resetattack = true;
            }
            if (rview) {
                position.add(1, 0, 0);
            } else {
                position.add(-1, 0, 0);
            }
            if ((rview) && (TimeUtils.nanoTime() - startskill1time > 1000000000 / 6)) {
                playerattack.body.setPosition(position.x + 30, position.y);
            } else if (TimeUtils.nanoTime() - startskill1time > 1000000000 / 6) {
                playerattack.body.setPosition(position.x + 10, position.y);
            }
            playerattack.active = true;

        } else if (combo&&numberhit!=3) {
            resetattack = false;
            combo = false;
            startskill1time = TimeUtils.nanoTime();
            playerattack.attackid.clear();
            numberhit++;
            if (numberhit == 2) {
                playerattack.bodytype = 51;
                playerattack.dvz = 7;
                playerattack.dvx = 1;
                playerattack.dvy = 0;
                playerattack.damage = 100;
            }
        } else if ((!combo) || (numberhit == 3)) {
            resetattack = false;
            resetstartattack=false;
            skill1 = false;
            skill1st = false;
            playerattack.bodytype = 50;
            playerattack.dvx=4;
            playerattack.dvy=0;
            playerattack.dvz=10;
            playerattack.attackid.clear();
            playerattack.active = false;
            actiondidA=true;
        }
    }






    ////////////////////////////////////////////////////////////////////// geter


    public int getDepth() {
        return depth;
    }

    public Rectangle getBody() {
        return body;
    }

    public int getBodytype() {
        return bodytype;
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getState() {
        return state;
    }

    public boolean isRview() {
        return rview;
    }

    public int getCid() {
        return cid;
    }

    public Playerattack getPlayerattack() {
        return playerattack;
    }

    public boolean isAttackview() {
        return attackview;
    }

    public boolean isDead() {
        return dead;
    }

    ////////////////////////////////////////////////////////////////////// seter


    public void setPosition(Vector3 position) {
        this.position = position;
    }
    public void setPositionx(float x) {
        this.position.x = x;
    }
    public void setPositiony(float y) {
        this.position.y = y;
    }
    public void setPositionz(float z) {
        this.position.z = z;
    }

    public void setKeyup(boolean keyup) {
        Keyup = keyup;
    }

    public void setKeydown(boolean keydown) {
        Keydown = keydown;
    }

    public void setKeyleft(boolean keyleft) {
        Keyleft = keyleft;
    }

    public void setKeyright(boolean keyright) {
        Keyright = keyright;
    }

    public void setKeyA(boolean keyA) {
        KeyA = keyA;
    }

    public void setKeyJ(boolean keyJ) {
        KeyJ = keyJ;
    }

    public void setKeyD(boolean keyD) {
        KeyD = keyD;
    }

    public void setDamage(boolean damage) {
        this.damage = damage;
    }

    public void setLaunch(boolean launch) {
        this.launch = launch;
    }

    public void setLaunchspeed(float x,float y,float z) {
        this.dx = x;
        this.dy = y;
        this.dz = z;
    }

    public void setAttackview(boolean attackview) {
        this.attackview = attackview;
    }

}

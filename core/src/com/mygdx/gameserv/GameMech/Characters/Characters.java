package com.mygdx.gameserv.GameMech.Characters;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.gameserv.GameMech.Gameobjects.Playerattack;

/**
 * Created by CooL on 18.03.2018.
 */

public interface Characters {
    void updatech();
    void HPdecrease(float dmg);
    //////////////////////geter
    Rectangle getBody();
    int getBodytype();
    Vector3 getPosition();
    int getState();
    boolean isRview();
    int getCid();
    Playerattack getPlayerattack();
    int getDepth();
    boolean isAttackview();
    boolean isDead();
    /////////////////////seter
    void setPosition(Vector3 position);
    void setPositionx(float x);
    void setPositiony(float y);
    void setPositionz(float z);
    void setKeyup(boolean keyup);
    void setKeydown(boolean keydown);
    void setKeyleft(boolean keyleft);
    void setKeyright(boolean keyright);
    void setKeyA(boolean keyA);
    void setKeyJ(boolean keyJ);
    void setKeyD(boolean keyD);
    void setDamage(boolean damage);
    void setLaunch(boolean launch);
    void setLaunchspeed(float x,float y,float z);
    void setAttackview(boolean attackview);
}

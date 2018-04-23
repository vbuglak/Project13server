package com.mygdx.gameserv.Database;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;

import java.sql.SQLException;


/**
 * Created by CooL on 06.04.2018.
 */

public class Serverdata {

    Database dbacc;
    public static final String TABLE_ACCOUNTS = "Accounts";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_LOGIN = "login";
    public static final String COLUMN_PASSWORD = "password";
    private static final String DATABASE_NAME = "Server.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_ACCOUNTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_LOGIN + " nvarchar not null, " + COLUMN_PASSWORD + " nvarchar not null);";


    public Serverdata() {
        Gdx.app.log("DatabaseAcc", "creation started");
        dbacc = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

        dbacc.setupDatabase();
        try {
            dbacc.openOrCreateDatabase();
            dbacc.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        Gdx.app.log("DatabaseAcc", "created successfully");
       /* try {
            dbacc.execSQL("INSERT INTO Accounts ('login','password') VALUES ('coolstyle2','asddsa11223')");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }*/
    }

    public int login(String login,String password) {
        DatabaseCursor cursor = null;
        try {
            cursor = dbacc.rawQuery("SELECT * FROM Accounts AC WHERE AC.login = '"+login+"';");//select * from Accounts AC where AC.login='coolstyle'
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        if (cursor.next()) {
            if (login.equals(cursor.getString(1))) {
                if (password.equals(cursor.getString(2))) {
                    return 2;//успешная авторизация
                }
                return 1;//неправильный пароль
            }
        }
        return 0;
    }
    public int register(String login,String password) {
        DatabaseCursor cursor = null;
        try {
            cursor = dbacc.rawQuery("SELECT * FROM Accounts AC WHERE AC.login = '"+login+"';");//select * from Accounts AC where AC.login='coolstyle'
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        if (cursor.next()) {
            if (login.equals(cursor.getString(1))) {
                return 2;//логин уже есть
            }
        }
        if (password.isEmpty()){
            return 1; //пароль пустой
        }
        try {
            dbacc.execSQL("INSERT INTO Accounts ('login','password') VALUES ('"+login+"','"+password+"')");
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

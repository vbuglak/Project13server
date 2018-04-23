package com.mygdx.gameserv;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.gameserv.Database.Serverdata;
import com.mygdx.gameserv.Frontend.ServerListener;
import com.mygdx.gameserv.GameMech.GameSession;
import com.mygdx.gameserv.GameMech.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class MyGdxGameServ extends ApplicationAdapter  {
	int ServerPort = 25565;
	public static Server server;
	public static com.mygdx.gameserv.Frontend.ServerListener snl;
	public static Serverdata servdb;
	//////////////////////////////////
	Database dbHandler;
	public static final String TABLE_COMMENTS = "comments";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COMMENT = "comment";

	private static final String DATABASE_NAME = "comments.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table if not exists " + TABLE_COMMENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_COMMENT + " text not null);";
	private Stage stage;
	private TextButton textButton;
	private Label statusLabel;
	private Skin skin;
//////////////////////////////////////////////////////////////////////////////

	public MyGdxGameServ() {
		server = new Server();
		registerPackets();
		snl = new ServerListener();
		server.addListener(snl);
		try {
			server.bind(ServerPort,ServerPort);
		} catch (IOException e) {
			e.printStackTrace();
		}
		server.start();


	}
    private void registerPackets(){
        Kryo kryo = server.getKryo();
		kryo.register(int[].class);
		kryo.register(ArrayList.class);
		kryo.register(Player.class);
		kryo.register(Vector3.class);
        kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet01Message.class);
        kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet02Autorize.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet03Createlobby.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet04Findlobby.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet05Lobbyinfo.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet06Lobbylist.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet07Joinlobby.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet08Cancellobby.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet09Lobbycreated.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet10Givelinfo.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet11Lobyback.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet12Gamesessionstart.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet13Gamesessionstate.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet14Playerbuttons.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet15Gamesessionend.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet16Login.class);
		kryo.register(com.mygdx.gameserv.Frontend.Packets.Packet17Register.class);
	}

	@Override
	public void create () {
		Gdx.app.log("DatabaseTest", "creation started");
		dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

		dbHandler.setupDatabase();
		try {
			dbHandler.openOrCreateDatabase();
			dbHandler.execSQL(DATABASE_CREATE);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}

		Gdx.app.log("DatabaseTest", "created successfully");
		servdb = new Serverdata();

		Texture tmpTex = new Texture(Gdx.files.internal("particle.png"));
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
		Gdx.input.setInputProcessor(stage);

		statusLabel = new Label("", skin);
		statusLabel.setWrap(true);
		statusLabel.setWidth(Gdx.graphics.getWidth() * 0.96f);
		statusLabel.setAlignment(Align.center);
		statusLabel.setPosition(Gdx.graphics.getWidth() * 0.5f - statusLabel.getWidth() * 0.5f, 30f);
		stage.addActor(statusLabel);

		textButton = new TextButton("Insert Data", skin);
		textButton.setPosition(Gdx.graphics.getWidth() * 0.5f - textButton.getWidth() * 0.5f, 60f);
		textButton.addListener(new ClickListener() {
			@Override
			public void clicked (InputEvent event, float x, float y) {
				super.clicked(event, x, y);

				try {
					dbHandler.execSQL("INSERT INTO comments ('comment') VALUES ('This is a test comment')");
				} catch (SQLiteGdxException e) {
					e.printStackTrace();
				}

				DatabaseCursor cursor = null;

				try {
					cursor = dbHandler.rawQuery("SELECT * FROM comments");
				} catch (SQLiteGdxException e) {
					e.printStackTrace();
				}

				while (cursor.next()) {
					statusLabel.setText(String.valueOf(cursor.getInt(0)));

				}

				try {
					cursor = dbHandler.rawQuery(cursor, "SELECT * FROM comments");
				} catch (SQLiteGdxException e) {
					e.printStackTrace();
				}
			}
		});
		stage.addActor(textButton);

	}


	@Override
	public void render () {
		/*for (int i = 0; i < snl.GameSessionList.size(); i++) {
			snl.GameSessionList.get(i).render();
		}*/
		Iterator<GameSession> itr = snl.GameSessionList.iterator();
		while (itr.hasNext()) {
			GameSession temp = itr.next();
			if (temp.sessionclose) {
				itr.remove();
			}
		}
		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose () {
		try {
			dbHandler.closeDatabase();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
		dbHandler = null;
		Gdx.app.log("DatabaseTest", "dispose");
	}
}

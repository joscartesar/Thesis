package com.tejadillas.armov3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBManager {

	public static final String TABLE_LANDFORMS = "landforms";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_ALTITUDE = "altitude";

	private static final String DATABASE_NAME = "landforms.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_LANDFORMS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_NAME
			+ " text not null, " + COLUMN_LATITUDE + " text not null, "
			+ COLUMN_LONGITUDE + " text not null, " + COLUMN_ALTITUDE
			+ " text not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBManager(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(DBManager.class.getName(),
					"Upgrading database from version " + oldVersion + " to "
							+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANDFORMS);
			onCreate(db);
		}
	}
	
	// ---opens the database---
	public DBManager open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert a landform into the database---
	public long insertLandform(String name, String latitudo, String longitude, String altitude) {
		ContentValues initialValues = new ContentValues();
		 initialValues.put(COLUMN_NAME, name);
		 initialValues.put(COLUMN_LATITUDE, latitudo);
		 initialValues.put(COLUMN_LONGITUDE, longitude);
		initialValues.put(COLUMN_ALTITUDE, altitude);
		 return db.insert(TABLE_LANDFORMS, null, initialValues);
	}
	
	// ---retrieves a particular landform---
		public Cursor getTitle(long colId) throws SQLException {
			Cursor mCursor = db.query(true, TABLE_LANDFORMS, new String[] {
					COLUMN_ID, COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_LATITUDE }, COLUMN_ID
					+ "=" + colId, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}

}

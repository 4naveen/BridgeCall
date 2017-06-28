package com.project.lorvent.bridgecall.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BridgeCall";

    // Labels table name
    private static final String TABLE_LABELS = "BridgeDetails";

    // Labels Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String BEFORE_DIAL = "before_dial";
    private static final String AFTER_DIAL = "after_dial";


    private static final String[] COLUMNS = {KEY_ID, KEY_NAME, KEY_NUMBER,BEFORE_DIAL,AFTER_DIAL};

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_LABELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_NUMBER + " TEXT," +BEFORE_DIAL+ " TEXT ,"+AFTER_DIAL+" TEXT)";

        Log.i("table",CREATE_CATEGORIES_TABLE );
        db.execSQL(CREATE_CATEGORIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LABELS);
        Log.i("drop","table droped");

        // Create tables again
        onCreate(db);
    }

    /**
     * Inserting new lable into lables table
     */
    public int insertBridgeDetails(String name, String number,String before_dial,String after_dial) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_NUMBER, number);
        values.put(BEFORE_DIAL, before_dial);
        values.put(AFTER_DIAL, after_dial);

        // Inserting Row
        int row= (int) db.insert(TABLE_LABELS, null, values);

        db.close();    // Closing database connection
        return row;
    }

    /**
     * Getting all labels
     * returns list of labels
     */
    public ArrayList<BridgeListItem> getAllBridgeDetails() {
        ArrayList<BridgeListItem> labels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BridgeListItem bridgeListItem = new BridgeListItem();
                bridgeListItem.setId(Integer.parseInt(cursor.getString(0)));
                bridgeListItem.setName(cursor.getString(1));
                bridgeListItem.setNumber(cursor.getString(2));
                labels.add(bridgeListItem);
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }

    public ArrayList<String> getNumber() {
        ArrayList<String> labels = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT number FROM " + TABLE_LABELS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // SpinnerItem bridgeListItem=new SpinnerItem();

                labels.add(cursor.getString(cursor.getColumnIndex("number")));
            } while (cursor.moveToNext());
        }


        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    }
    public String getName(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;

        Cursor cursor = db.query(TABLE_LABELS,
                COLUMNS, KEY_NUMBER + " = ?", new String[]{number}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
              name= cursor.getString(cursor.getColumnIndex("name"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return name;
    }
    public int updateBridgeItem(BridgeListItem item, String name, String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues args = new ContentValues();
       // args.put(KEY_ID, String.valueOf(pos));
        args.put(KEY_NAME, name);
        args.put(KEY_NUMBER, number);
        int row=db.update(TABLE_LABELS, args,KEY_ID +" = ?",new String[]{String.valueOf(item.getId())});
        return row;
    }

    public BridgeListItem readBridgeItem(BridgeListItem item) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LABELS,
                COLUMNS, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())}, null, null, null, null);

        BridgeListItem bridgeListItem = null;
        // if results !=null, parse the first one
        if (cursor != null) {
            cursor.moveToFirst();
            bridgeListItem = new BridgeListItem();
            bridgeListItem.setId(Integer.parseInt(cursor.getString(0)));
            //Log.i("id",cursor.getString(0));
            bridgeListItem.setName(cursor.getString(1));
            Log.i("column2", String.valueOf(cursor.getColumnNames()));

            //Log.i("name",cursor.getString(1));
            bridgeListItem.setNumber(cursor.getString(2));
            bridgeListItem.setBefore_dial(cursor.getString(3));
            bridgeListItem.setAfter_dial(cursor.getString(4));

            // Log.i("number",cursor.getString(2));
            cursor.close();
        }

        return bridgeListItem;
    }



    public int deleteBridgeListItem(BridgeListItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        Log.i("delete pos",String.valueOf(item.getId()));
        int row=db.delete(TABLE_LABELS, KEY_ID + " = ?", new String[]{String.valueOf(item.getId())});

        db.close();
        return row;
    }





}

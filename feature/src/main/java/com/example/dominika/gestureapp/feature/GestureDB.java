package com.example.dominika.gestureapp.feature;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dominika on 31.10.17.
 */

public final class GestureDB {

    private static String DEBUG_TAG = "GestureDB";

    private GestureDB() {}

    public static class GestureEntry implements BaseColumns {
        public static final String TABLE_NAME = "records";
        public static final String FIRSTNAME = "firstName";
        public static final String LASTNAME = "lastName";
        public static final String GESTURE = "gesture";
        public static final String WHITES = "whitePixels";
        public static final String BLACKS = "blackPixels";
        public static final String ID = "id";
        private static final String[] COLUMNS = {ID,FIRSTNAME,LASTNAME,GESTURE,WHITES,BLACKS};
    }

    public static class Gesture {
        public String firstname;
        public String lastname;
        public String gesture;
        public int whitePixels;
        public int blackPixels;
        public String id;
        public void setId(String id) {
            this.id = id;
        }
        public void setFirstname (String firstname) {
            this.firstname = firstname;
        }
        public void setLastname(String lastname) {
            this.lastname = lastname;
        }
        public void setGesture(String gesture) {
            this.gesture = gesture;
        }
        public void setWhitePixels(int whitePixels) {this.whitePixels = whitePixels;}
        public void setBlacksPixels(int blackPixels) {this.blackPixels = blackPixels;}
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public static final String DB_CREATE_GESTURE_TABLE = "CREATE TABLE " + GestureEntry.TABLE_NAME +
                " ( "+ GestureEntry.ID + " text, " + GestureEntry.FIRSTNAME + " text, " +
                GestureEntry.LASTNAME + " text, " + GestureEntry.GESTURE + " text, " +
                GestureEntry.WHITES + " text, " + GestureEntry.BLACKS + " text )" ;
        public static final String DB_DROP_GESTURE_TABLE = "DROP TABLE IF EXISTS " + GestureEntry.TABLE_NAME;
        public static final int DB_VERSION = 5;

        public DBHelper(Context context, String name,
                        SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_GESTURE_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + GestureEntry.TABLE_NAME + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(DB_DROP_GESTURE_TABLE);
            db.execSQL(DB_CREATE_GESTURE_TABLE);
        }

        public void addGesture(Gesture gesture){
            //for logging
            Log.d(DEBUG_TAG, gesture.toString());

            // get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();

            //create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(GestureEntry.ID, gesture.id);
            values.put(GestureEntry.FIRSTNAME, gesture.firstname); // get firstname
            values.put(GestureEntry.LASTNAME, gesture.lastname); // get lastname
            values.put(GestureEntry.GESTURE, gesture.gesture); // get gesture
            values.put(GestureEntry.WHITES, gesture.whitePixels); // get whitePixels
            values.put(GestureEntry.BLACKS, gesture.blackPixels); // get blackPixels

            //insert
            db.insert(GestureEntry.TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            //close
            db.close();
        }

        public Gesture getGesture(String id){

            //get reference to readable DB
            SQLiteDatabase db = this.getReadableDatabase();

            //build query
            Cursor cursor =
                    db.query(GestureEntry.TABLE_NAME, // a. table
                            GestureEntry.COLUMNS, // b. column names
                            " id = ?", // c. selections
                            new String[] { id }, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            //if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            //build gesture object
            Gesture gesture = new Gesture();
            gesture.setId(cursor.getString(0));
            gesture.setFirstname(cursor.getString(1));
            gesture.setLastname(cursor.getString(2));
            gesture.setGesture(cursor.getString(3));
            gesture.setWhitePixels(cursor.getInt(4));
            gesture.setBlacksPixels(cursor.getInt(5));

            //log
            Log.d(DEBUG_TAG, gesture.toString());

            //return gesture
            return gesture;
        }

        public List<Gesture> getAllGestures() {
            List<Gesture> gestures = new LinkedList<Gesture>();

            //build the query
            String query = "SELECT  * FROM " + GestureEntry.TABLE_NAME;

            //get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            //go over each row, build gesture and add it to list
            Gesture gesture = null;
            if (cursor.moveToFirst()) {
                do {
                    gesture = new Gesture();
                    gesture.setId(cursor.getString(0));
                    gesture.setFirstname(cursor.getString(1));
                    gesture.setLastname(cursor.getString(2));
                    gesture.setGesture(cursor.getString(3));
                    gesture.setWhitePixels(cursor.getInt(4));
                    gesture.setBlacksPixels(cursor.getInt(5));
                    Log.d(DEBUG_TAG, cursor.getString(1));

                    //add gesture to gestures
                    gestures.add(gesture);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            Log.d(DEBUG_TAG, gestures.toString());

            // return books
            return gestures;
        }
        public void deleteAll() {

            //build the query
            String query = "DELETE FROM " + GestureEntry.TABLE_NAME;
            //String query = "DROP DATABASE " + GestureEntry.TABLE_NAME;
            //get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(query);

            db.close();
        }

        public void deleteGesture(String id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GestureEntry.TABLE_NAME, GestureEntry.ID + "='" + id + "'", null);
            db.close();
        }
    }
}

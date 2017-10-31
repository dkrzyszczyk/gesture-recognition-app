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
        public static final String ID = "id";
        private static final String[] COLUMNS = {ID,FIRSTNAME,LASTNAME,GESTURE};
    }

    public static class Gesture {
        public String firstname;
        public String lastname;
        public String gesture;
        public Integer id;
        public void setId(Integer id) {
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
    }

    public static class DBHelper extends SQLiteOpenHelper {

        public static final String DB_CREATE_GESTURE_TABLE = "CREATE TABLE " + GestureEntry.TABLE_NAME +
                " ( "+ GestureEntry.ID + " int, " + GestureEntry.FIRSTNAME + " text, " +
                GestureEntry.LASTNAME + " text, " + GestureEntry.GESTURE + " text )" ;
        public static final int DB_VERSION = 1;

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

        }

        public void addGesture(Gesture gesture){
            //for logging
            Log.d(DEBUG_TAG, gesture.toString());

            // 1. get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();

            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(GestureEntry.FIRSTNAME, gesture.firstname); // get title
            values.put(GestureEntry.LASTNAME, gesture.lastname); // get author
            values.put(GestureEntry.GESTURE, gesture.gesture); // get author

            // 3. insert
            db.insert(GestureEntry.TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values

            // 4. close
            db.close();
        }

        public Gesture getGesture(int id){

            // 1. get reference to readable DB
            SQLiteDatabase db = this.getReadableDatabase();

            // 2. build query
            Cursor cursor =
                    db.query(GestureEntry.TABLE_NAME, // a. table
                            GestureEntry.COLUMNS, // b. column names
                            " id = ?", // c. selections
                            new String[] { String.valueOf(id) }, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            // 3. if we got results get the first one
            if (cursor != null)
                cursor.moveToFirst();

            // 4. build book object
            Gesture gesture = new Gesture();
            gesture.setId(Integer.parseInt(cursor.getString(0)));
            gesture.setFirstname(cursor.getString(1));
            gesture.setLastname(cursor.getString(2));
            gesture.setGesture(cursor.getString(3));

            //log
            Log.d(DEBUG_TAG, gesture.toString());

            // 5. return book
            return gesture;
        }

        public List<Gesture> getAllGestures() {
            List<Gesture> gestures = new LinkedList<Gesture>();

            // 1. build the query
            String query = "SELECT  * FROM " + GestureEntry.TABLE_NAME;

            // 2. get reference to writable DB
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            // 3. go over each row, build book and add it to list
            Gesture gesture = null;
            Log.d(DEBUG_TAG, "Po raz pierwszy111");
            if (cursor.moveToFirst()) {
                Log.d(DEBUG_TAG, "Po raz pierwszy");
                do {
                    gesture = new Gesture();
//                    gesture.setId(Integer.parseInt(cursor.getString(0)));
                    gesture.setId(1);
                    gesture.setFirstname(cursor.getString(1));
                    gesture.setLastname(cursor.getString(2));
                    gesture.setGesture(cursor.getString(3));
                    Log.d(DEBUG_TAG, cursor.getString(1));

                    // Add book to books
                    gestures.add(gesture);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            Log.d(DEBUG_TAG, gestures.toString());

            // return books
            return gestures;
        }

    }
}
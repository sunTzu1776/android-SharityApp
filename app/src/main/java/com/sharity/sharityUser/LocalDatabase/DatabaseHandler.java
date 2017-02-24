package com.sharity.sharityUser.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharity.sharityUser.User;

/**
 * Created by Moi on 21/11/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Contacts table name
    private static final String DATABASE_NAME = "UserManager";

    private static final String User = "UserTable";
    private static final String KEY_OBJECTID = "objectid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_IMAGE = "image";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         String CREATE_TABLE_IMAGE = "CREATE TABLE " + User + "("+KEY_OBJECTID + " TEXT,"+ KEY_USERNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_IMAGE + " BLOB);";
        db.execSQL(CREATE_TABLE_IMAGE);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addUserProfil(com.sharity.sharityUser.User user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_OBJECTID, user.get_id());
        cv.put(KEY_USERNAME, user.get_name());
        cv.put(KEY_EMAIL,user.get_email());
        cv.put(KEY_IMAGE, user.getPictureprofil());
        db.insert(User, null, cv);
        db.close(); // Closing database connection
    }

    public byte[] getPictureProfile() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        byte[] image = cursor.getBlob(3);
        return image;
    }

    public String getUsername() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String image = cursor.getString(1);
        return image;
    }

    // Getting single contact
    public com.sharity.sharityUser.User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User
                , new String[] { KEY_OBJECTID,
                        KEY_USERNAME, KEY_EMAIL,KEY_IMAGE}, KEY_OBJECTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        com.sharity.sharityUser.User contact = new User(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getBlob(3));
        // return contact
        return contact;
    }

    // G

    // Updating single contact
    public int updateUser(com.sharity.sharityUser.User contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OBJECTID, contact.get_id());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_USERNAME, contact.get_name());
        values.put(KEY_IMAGE, contact.getPictureprofil());

        // updating row
        return db.update(User, values, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }

    // Deleting single contact
    public void deleteContact(com.sharity.sharityUser.User contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(User, KEY_OBJECTID + " = ?",new String[] { String.valueOf(contact.get_id()) });
            db.close();
    }



    public int getUserCount() {
        String countQuery = "SELECT  * FROM " + User;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

}
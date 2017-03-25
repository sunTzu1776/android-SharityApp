package com.sharity.sharityUser.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;

import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;

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
    private static final String KEY_CODE = "code";



    private static final String Business = "BusinessTable";
    private static final String KEY_BISOBJECTID = "objectidbus";
    private static final String KEY_BISUSERNAME = "usernamebus";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_RIB = "rib";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_OFFICERNAME = "officername";
    private static final String KEY_BUSINESSNAME = "businessname";
    private static final String KEY_TELEPHONE = "phone";
    private static final String KEY_OWNER = "owner";
    private static final String KEY_LATITUDE="latitude";
    private static final String KEY_LONGITUDE="longitude";
    private static final String KEY_SIRET = "siret";
    private static final String KEY_EMAILVERIFIED="emailverified";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         String CREATE_TABLE_IMAGE = "CREATE TABLE " + User + "("+KEY_OBJECTID + " TEXT,"+ KEY_USERNAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_IMAGE + " BLOB,"+ KEY_CODE + " TEXT);";
        db.execSQL(CREATE_TABLE_IMAGE);
        String CREATE_TABLE_BUSINESS = "CREATE TABLE " + Business + "("+KEY_BISOBJECTID + " TEXT,"+ KEY_BISUSERNAME + " TEXT," + KEY_OWNER +" TEXT,"
                + KEY_OFFICERNAME +" TEXT," + KEY_BUSINESSNAME +" TEXT," + KEY_RIB +" TEXT,"+ KEY_SIRET +" TEXT,"
                + KEY_TELEPHONE +" TEXT," + KEY_ADDRESS +" TEXT," + KEY_LATITUDE +" TEXT," + KEY_LONGITUDE +" TEXT,"+ KEY_MAIL +" TEXT,"+ KEY_EMAILVERIFIED + " TEXT);";
        db.execSQL(CREATE_TABLE_BUSINESS);


    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + User);
        db.execSQL("DROP TABLE IF EXISTS " + Business);

        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
    public void addUserProfil(com.sharity.sharityUser.BO.User user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_OBJECTID, user.get_id());
        cv.put(KEY_USERNAME, user.get_name());
        cv.put(KEY_EMAIL,user.get_email());
        cv.put(KEY_IMAGE, user.getPictureprofil());
        cv.put(KEY_CODE, user.get_code());
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

    public String getCodeUser() {

        String selectQuery = "SELECT * FROM " + User;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String code = cursor.getString(4);
        return code;
    }
    // Getting single contact
    public com.sharity.sharityUser.BO.User getUser(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(User
                , new String[] { KEY_OBJECTID,
                        KEY_USERNAME, KEY_EMAIL,KEY_IMAGE,KEY_CODE}, KEY_OBJECTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        com.sharity.sharityUser.BO.User contact = new User(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getBlob(3),cursor.getString(4));
        // return contact
        return contact;
    }

    // G

    // Updating single contact
    public int updateUser(com.sharity.sharityUser.BO.User contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OBJECTID, contact.get_id());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_USERNAME, contact.get_name());
        values.put(KEY_IMAGE, contact.getPictureprofil());
        values.put(KEY_CODE, contact.get_code());


        // updating row
        return db.update(User, values, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(contact.get_id()) });
    }


    public void deleteAllUser() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(User, null, null);
        db.close();
    }


    // Deleting single contact
    public void deleteContact(com.sharity.sharityUser.BO.User contact) {
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

    /*/*
    BUSINESS
     */

    public void addProProfil(Business user) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_BISUSERNAME, user.get_username());
        cv.put(KEY_MAIL,user.get_email());
        cv.put(KEY_SIRET,user.get_Siret());
        cv.put(KEY_OFFICERNAME,user.get_officerName());
        cv.put(KEY_BUSINESSNAME,user.get_businessName());
        cv.put(KEY_TELEPHONE,user.get_telephoneNumber());
        cv.put(KEY_OWNER,user.get_id());
        cv.put(KEY_RIB,user.get_RIB());
        cv.put(KEY_ADDRESS, user.get_address());
        cv.put(KEY_LATITUDE,String.valueOf(user.getLatitude()));
        cv.put(KEY_LONGITUDE,String.valueOf(user.get_longitude()));
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());
        db.insert(Business, null, cv);
        db.close(); // Closing database connection
    }

    // Updating single contact
    public int UpdateProProfil(Business user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_BISUSERNAME, user.get_username());
        cv.put(KEY_MAIL,user.get_email());
        cv.put(KEY_SIRET,user.get_Siret());
        cv.put(KEY_OFFICERNAME,user.get_officerName());
        cv.put(KEY_BUSINESSNAME,user.get_businessName());
        cv.put(KEY_TELEPHONE,user.get_telephoneNumber());
        cv.put(KEY_OWNER,user.get_id());
        cv.put(KEY_RIB,user.get_RIB());
        cv.put(KEY_ADDRESS, user.get_address());
        cv.put(KEY_LATITUDE,String.valueOf(user.getLatitude()));
        cv.put(KEY_LONGITUDE,String.valueOf(user.get_longitude()));
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());

        // updating row
        return db.update(Business, cv, KEY_OBJECTID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }



    public Business getBusiness(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Business
                , new String[] { KEY_BISOBJECTID,
                        KEY_BISUSERNAME,KEY_OWNER,KEY_OFFICERNAME,KEY_BUSINESSNAME,KEY_RIB,KEY_SIRET,KEY_TELEPHONE,KEY_ADDRESS,KEY_LATITUDE,KEY_LONGITUDE,KEY_MAIL,KEY_EMAILVERIFIED}, KEY_BISOBJECTID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
            cursor.moveToFirst();

        Business contact = new Business(cursor.getString(0),
                cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12));
        // return contact
        return contact;
    }


    public void deleteAllBusiness() {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(Business, null, null);
        db.close();
    }

    public String getBusinessId() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(0);
        return objectid;
    }

    public String getBusinessName() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(4);
        return objectid;
    }

    public String getEmailverified() {
        String selectQuery = "SELECT * FROM " + Business;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        String objectid = cursor.getString(12);
        return objectid;
    }

    public int UpdateEmailVerified(Business user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_BISOBJECTID, user.get_id());
        cv.put(KEY_EMAILVERIFIED,user.getEmailveried());
        // updating row
        return db.update(Business, cv, KEY_BISOBJECTID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
    }


    public int getBusinessCount() {
        String countQuery = "SELECT  * FROM " + Business;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }




}
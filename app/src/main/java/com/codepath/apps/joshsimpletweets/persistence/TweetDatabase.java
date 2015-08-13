package com.codepath.apps.joshsimpletweets.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codepath.apps.joshsimpletweets.models.Tweet;

import java.util.List;

/**
 * Created by Prajakta on 6/1/2015.
 */
public class TweetDatabase extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tweetDatabase";

    //  table name
    private static final String TABLE_HOME = "tweets_home";
    private static final String TABLE_MENTIONS = "tweets_mentions";

    //  Table Columns names
    private static final String KEY_UID = "uid";
    private static final String KEY_BODY = "body";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_RETWEETCOUNT = "retweetCount";
    private static final String KEY_FAVCOUNT = "favCount";

    public TweetDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating our initial tables
    // These is where we need to write create table statements.
    // This is called when database is created.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Construct a table for todo items
        String CREATE_HOME_TABLE = "CREATE TABLE " + TABLE_HOME + "("
                + KEY_UID + " LONG PRIMARY KEY,"
                + KEY_BODY + " TEXT,"
                + KEY_CREATED_AT + " TEXT,"
                + KEY_FAVCOUNT + "INTEGER,"
                + KEY_RETWEETCOUNT + " INTEGER" +
                ")";
        db.execSQL(CREATE_HOME_TABLE);
    }

    // Upgrading the database between versions
    // This method is called when database is upgraded like modifying the table structure,
    // adding constraints to database, etc
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOME);
            // Create tables again
            onCreate(db);
        }
    }

    // Insert record into the database
    public void addTweet(Tweet item) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();
        values.put(KEY_UID, item.getUid());
        values.put(KEY_BODY, item.getBody());

        values.put(KEY_CREATED_AT,item.getCreatedAt());
        values.put(KEY_FAVCOUNT,item.getFavCount());
        values.put(KEY_RETWEETCOUNT,item.getRetweetCount());
        // Insert Row and get generated id
        long rowId = db.insertOrThrow(TABLE_HOME, null, values);
        // Closing database connection
        db.close();
    }

    public void addTweets(List<Tweet> list){

        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        for(Tweet item :list) {
            ContentValues values = new ContentValues();
            values.put(KEY_UID, item.getUid());
            values.put(KEY_BODY, item.getBody());

            values.put(KEY_CREATED_AT, item.getCreatedAt());
            values.put(KEY_FAVCOUNT, item.getFavCount());
            values.put(KEY_RETWEETCOUNT, item.getRetweetCount());
            // Insert Row and get generated id
            long rowId = db.insertOrThrow(TABLE_HOME, null, values);
        }// Closing database connection
        db.close();
    }
    // Returns a single todo item by id
    public Tweet getTweets() {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct and execute query
        Cursor cursor = db.query(TABLE_HOME,  // TABLE
                new String[] { KEY_UID, KEY_BODY, KEY_CREATED_AT, KEY_FAVCOUNT, KEY_RETWEETCOUNT }, // SELECT
                null,  // WHERE, ARGS
                null, null, "id ASC", "100"); // GROUP BY, HAVING, ORDER BY, LIMIT
        if (cursor != null)
            cursor.moveToFirst();
        // Load result into model object
        Tweet item = new Tweet();
        item.setUid(cursor.getLong(1));
        item.setBody(cursor.getString(2));
        item.setCreatedAt(cursor.getString(3));
        item.setFavCount(cursor.getInt(4));
        item.setRetweetCount(cursor.getInt(5));
        //item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
        // Close the cursor
        if (cursor != null)
            cursor.close();
        // return todo item
        return item;
    }
}
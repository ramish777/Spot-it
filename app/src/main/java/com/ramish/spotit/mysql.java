package com.ramish.spotit;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class mysql extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "spot_it.db";
    public static String TABLE_NAME = "contacts";
    public static int DATABASE_VERSION = 1;

    public static class contacts implements BaseColumns {
        public static String TABLE_NAME = "contacts";
        public static String _NAME = "name";
        public static String _PHONE = "phone";
        public static String _EMAIL = "email";
    }

    public static class userchats implements BaseColumns {
        public static String TABLE_NAME = "userchats";
        public static String _ID = "id";
        public static String _CHAT_ID = "chatID";
        public static String _USER_ID = "userID";
    }

    public static class chats implements BaseColumns {
        public static String TABLE_NAME = "chats";
        public static String _ID = "id";
        public static String _FIRST_MEMBER_ID = "firstMemberID";
        public static String _SECOND_MEMBER_ID = "secondMemberID";
        public static String _LAST_MESSAGE = "lastMessage";
    }

    public static class messages implements BaseColumns {
        public static String TABLE_NAME = "messages";
        public static String _ID = "id";
        public static String _CHAT_ID = "chatID";
        public static String _TEXT = "text";
        public static String _PICTURE_URL = "pictureURL";
        public static String _VIDEO_URL = "videoURL";
        public static String _TIME = "time";
        public static String _SENTBY_ID = "sentby_ID";
    }

    public static String CREATE_TABLE_CONTACTS = "CREATE TABLE " + contacts.TABLE_NAME + " (" +
            contacts._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            contacts._NAME + " TEXT NOT NULL, " +
            contacts._EMAIL + " TEXT, " +
            contacts._PHONE + " TEXT)";

    public static String CREATE_TABLE_USERCHATS = "CREATE TABLE " + userchats.TABLE_NAME + " (" +
            userchats._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            userchats._CHAT_ID + " INTEGER NOT NULL, " +
            userchats._USER_ID + " INTEGER NOT NULL)";

    public static String CREATE_TABLE_CHATS = "CREATE TABLE " + chats.TABLE_NAME + " (" +
            chats._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            chats._FIRST_MEMBER_ID + " INTEGER NOT NULL, " +
            chats._SECOND_MEMBER_ID + " INTEGER NOT NULL, " +
            chats._LAST_MESSAGE + " TEXT)";

    public static String CREATE_TABLE_MESSAGES = "CREATE TABLE " + messages.TABLE_NAME + " (" +
            messages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            messages._CHAT_ID + " INTEGER NOT NULL, " +
            messages._TEXT + " TEXT, " +
            messages._PICTURE_URL + " TEXT, " +
            messages._VIDEO_URL + " TEXT, " +
            messages._TIME + " TEXT, " +
            messages._SENTBY_ID + " INTEGER)";


    public static String DROP_TABLE_CONTACTS = " DROP TABLE IF EXISTS " + contacts.TABLE_NAME;
    public static String DROP_TABLE_USERCHATS = " DROP TABLE IF EXISTS " + userchats.TABLE_NAME;
    public static String DROP_TABLE_CHATS = " DROP TABLE IF EXISTS " + chats.TABLE_NAME;
    public static String DROP_TABLE_MESSAGES = " DROP TABLE IF EXISTS " + messages.TABLE_NAME;
    public mysql(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_USERCHATS);
        db.execSQL(CREATE_TABLE_CHATS);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_CONTACTS);
        db.execSQL(DROP_TABLE_USERCHATS);
        db.execSQL(DROP_TABLE_CHATS);
        db.execSQL(DROP_TABLE_MESSAGES);  // Add this line to drop the messages table
        onCreate(db);
    }


}

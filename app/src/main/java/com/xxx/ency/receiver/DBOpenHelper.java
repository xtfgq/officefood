package com.xxx.ency.receiver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBOpenHelper";

    public DBOpenHelper(Context context) {
        super(context, "SALE_XGExample.db", null, 4);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notification (id integer primary key autoincrement,msg_id varchar(64),title varchar(128),status varchar(64),activity varchar(256),notificationActionType varchar(512),content text,update_time varchar(16))");
        db.execSQL("CREATE TABLE entypreorder (id integer primary key autoincrement,wareId varchar(100),wareName varchar(128),price varchar(64),num varchar(64),buyOrSale varchar(64),state varchar(2),addTime varchar(16))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "######@@@@@@@@######oldVersion:"+oldVersion+";newVersion:"+newVersion);
    }
}

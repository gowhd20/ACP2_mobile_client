package com.example.dhaejong.acp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dhaejong on 5.2.2016.
 */
public class LocalDB extends SQLiteOpenHelper{

    private String TAG = "LocalDB";

    private SQLiteDatabase myDatabase;
    private static final String DATABASE_NAME = "localDb.sqlite";
    private static final String DATABASE_TABLE_NAME = "Tags";
    private static final int DATABASE_VERSION = 4;

    private static final String COLUMN_NAME_TAG_NAME = "name";
    private static final String COLUMN_NAME_ID = "id";

    private static final String DATABASE_CREATE_PLAYER = "create table if not exists Tags " +
            "("+COLUMN_NAME_ID+" integer primary key, "+COLUMN_NAME_TAG_NAME+" text not null)";

    private Context m_context;
    public SQLiteDatabase m_db;

    LocalDB(Context context){
        super(context, DATABASE_TABLE_NAME, null, DATABASE_VERSION);
        m_context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_PLAYER);
        m_db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
        onCreate(db);
    }


    public boolean insertTag(String id, String name) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("id", id);
            db.insert("Tags", null, contentValues);
            Log.i(TAG, "tag is stored");
            return true;
        }catch(Exception e){
            e.printStackTrace();
            Log.i(TAG, "something went wrong with inserting data");
            return false;
        }
    }

    public ArrayList<String> getData(int id){
        ArrayList<String> array_list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from " + DATABASE_TABLE_NAME + " where id =" + id + "", null);
            res.moveToFirst();
            int count = res.getColumnCount();
            Log.i(TAG, count+" let's see");
            while (count > 0) {
                array_list.add(res.getString(--count));
                Log.i(TAG, count + " value of count");
            }
            res.close();
            db.close();
        }catch(Exception e){
            Log.i(TAG, "not exist info what you are looking for");
        }
        return array_list;

    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DATABASE_TABLE_NAME);
        return numRows;
    }

    public Integer deleteTag (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME,
                "id = ? ",
                new String[] {
                        Integer.toString(id)
                });
    }



    public ArrayList<String> getAllTagNames(){
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+DATABASE_TABLE_NAME+"", null );
        res.moveToFirst();


        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME_TAG_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public boolean deleteTable(String tableName) {
        try {
            m_context.deleteDatabase(tableName);
            return true;
        }catch(Exception e){
            Log.i(TAG, "Something went wrong with deleting");
            return false;
        }
    }





}

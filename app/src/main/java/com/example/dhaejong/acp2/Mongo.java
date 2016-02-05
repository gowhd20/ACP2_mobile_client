package com.example.dhaejong.acp2;

import android.util.Log;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

import java.util.List;

/**
 * Created by dhaejong on 5.2.2016.
 */
public class Mongo {


    private String TAG = "Mongo";
    MongoClient mongoClient;
    MongoDatabase db;

    public void MongoInit(){
        mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("local_data");

        MongoIterable dbs = mongoClient.listDatabaseNames();
        Log.i(TAG, dbs.toString());



    }
}

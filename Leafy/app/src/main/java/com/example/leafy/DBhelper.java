package com.example.leafy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    final static String DBname = "Login.db";

    public DBhelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase mydb) {
        mydb.execSQL("create table users(username TEXT primary key,password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase mydb, int oldVersion, int newVersion) {
        mydb.execSQL("drop Table if exists users");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase mydb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = mydb.insert("users", null, contentValues);
        if (result == -1) return false;
        else {
            return true;
        }
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase mydb = this.getWritableDatabase();
        Cursor cursor = mydb.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkusernamepassword(String username, String password) {
    SQLiteDatabase mydb= this.getWritableDatabase();
    Cursor cursor = mydb.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
    if (cursor.getCount()>0) {
        return true;
    } else {
        return false;
    }
    }
}

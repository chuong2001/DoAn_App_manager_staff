package com.example.managerstaff.supports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "managestaff.db";
    private final static int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void insertAuthorization(String Authorization) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO tblSession VALUES(null,?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, Authorization);
        statement.executeInsert();
    }

    public void deleteSession(String Authorization) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM tblSession WHERE Authorization= '" + Authorization+"'";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.executeInsert();
    }

    public String getSessionAuthorization() {
        String sql = "SELECT * FROM tblSession";
        Cursor data = GetData(sql);
        String Authorization="";
        while (data.moveToNext()) {
            Authorization = data.getString(1);
        }

        return Authorization;
    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS tblSession(id INTEGER PRIMARY KEY AUTOINCREMENT,Authorization TEXT)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

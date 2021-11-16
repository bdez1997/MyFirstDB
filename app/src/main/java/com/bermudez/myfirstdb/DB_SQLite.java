package com.bermudez.myfirstdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB_SQLite extends SQLiteOpenHelper {

    private static final String DB_NAME="tienda.db";
    private static final int DB_VERSION=1;

    private static final String SQL_CREATE_ENTRIES="CREATE TABLE PRODUCTO(id_Producto INTEGER PRIMARY KEY AUTOINCREMENT,nombre TEXT,cantidad INTEGER,seccion TEXT)";
    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS PRODUCTO";




    public DB_SQLite(@Nullable Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRIES);
    }
}

package com.m520it.jdmail.db;

import com.m520it.jdmail.constant.DbConst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public DbOpenHelper(Context context) {
		super(context,DbConst.DB_NAME,null,DbConst.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + 
					DbConst.USER_TABLE + "(" + 
					DbConst._ID + " integer primary key autoincrement," + 
					DbConst._NAME + " text," + 
					DbConst._PWD + " text);");
		db.execSQL("create table " + 
					DbConst.SHOPCAR_TABLE + "(" + 
					DbConst._ID+" integer primary key autoincrement," + 
					DbConst._PRODUCTID + " integer," + 
					DbConst._BUYCOUNT + " integer," + 
					DbConst._PVERSION + " text);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}

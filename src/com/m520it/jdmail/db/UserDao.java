package com.m520it.jdmail.db;

import com.m520it.jdmail.constant.DbConst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//业务驱动代码
//数据库表里面只能有一个用户
public class UserDao {
	private DbOpenHelper mHelper;
	public UserDao(Context c){
		mHelper = new DbOpenHelper(c);
	}
	//保存操作
	public boolean saveUser(String name,String pwd){
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConst._NAME,name);
		values.put(DbConst._PWD,pwd);
		long insertId = db.insert(DbConst.USER_TABLE,null,values);
		return insertId!=-1;
	}
	//清空数据库
	public void clearUsers(){
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.delete(DbConst.USER_TABLE,null,null);
	}
	public UserInfo acquireLastestUser(){
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(DbConst.USER_TABLE,
				new String[]{DbConst._NAME,DbConst._PWD},null,
				null,null,null,null);
		if(cursor.moveToFirst()){
			String name = cursor.getString(0);
			String pwd = cursor.getString(1);
			return new UserInfo(name,pwd);
		}
		return null;
	}
	public class UserInfo{
		public String name;
		public String pwd;
		public UserInfo(String name, String pwd) {
			super();
			this.name = name;
			this.pwd = pwd;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}

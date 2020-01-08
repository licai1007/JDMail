package com.m520it.jdmail.db;

import java.util.ArrayList;
import java.util.List;

import com.m520it.jdmail.bean.Cart;
import com.m520it.jdmail.constant.DbConst;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShopcarDao {
	private DbOpenHelper mHelper;
	public ShopcarDao(Context c) {
		mHelper = new DbOpenHelper(c);
	}
	public boolean add(long pid,int buyCount,String pversion){
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DbConst._PRODUCTID,pid);
		values.put(DbConst._BUYCOUNT,buyCount);
		values.put(DbConst._PVERSION,pversion);
		long insertId = db.insert(DbConst.SHOPCAR_TABLE,null,values);
		return insertId!=-1;
	}
	public List<Cart> getList(){
		List<Cart> carts = new ArrayList<Cart>();
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.query(DbConst.SHOPCAR_TABLE,
				new String[]{DbConst._ID,DbConst._PRODUCTID,DbConst._BUYCOUNT,DbConst._PVERSION},
				null,null,null,null,null);
		while(cursor.moveToNext()){
			Cart cart = new Cart();
			cart.setId(cursor.getLong(0));
			cart.setProductId(cursor.getLong(1));
			cart.setBuyCount(cursor.getInt(2));
			cart.setPversion(cursor.getString(3));
			carts.add(cart);
		}
		return carts;
	}
	public boolean delete(long id){
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int delete = db.delete(DbConst.SHOPCAR_TABLE,DbConst._ID+"=?",new String[]{id+""});
		return delete!=0;
	}

}

package com.m520it.jdmail.controller;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.JDApplication;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.db.ShopcarDao;
import com.m520it.jdmail.db.UserDao;
import com.m520it.jdmail.db.UserDao.UserInfo;
import com.m520it.jdmail.util.AESUtils;
import com.m520it.jdmail.util.NetworkUtil;

public class UserController extends BaseController{
	private UserDao mUserDao;
	protected ShopcarDao mShopcarDao;
	protected long mUserId;
	public UserController(Context c) {
		super(c);
		mShopcarDao = new ShopcarDao(c);
		mUserDao = new UserDao(mContext);
		initUserId(c);
	}
	private void initUserId(Context c) {
		Activity activity = (Activity)c;
		JDApplication jdApplication = (JDApplication)activity.getApplication();
		if(jdApplication.mRLoginResult!=null){
			mUserId = jdApplication.mRLoginResult.getId();
		}
	}
	@Override
	protected void handleMessage(int action, Object... values) {
		switch(action){
			case IdiyMessage.LOGIN_ACTION:
				//登录的请求
				RResult rResult = loginOrRegist(NetworkConst.LOGIN_URL,(String)values[0],(String)values[1]);
				//跟Activity说 数据加载完毕了
				mListener.onModeChanged(IdiyMessage.LOGIN_ACTION_RESULT,rResult);
				break;
			case IdiyMessage.REGIST_ACTION:
				RResult rResult2 = loginOrRegist(NetworkConst.REGIST_URL,(String)values[0],(String)values[1]);
				mListener.onModeChanged(IdiyMessage.REGIST_ACTION_RESULT,rResult2);
				break;
			case IdiyMessage.SAVE_USERTODB:
				boolean saveUser2Db = saveUser2Db((String)values[0],(String)values[1]);
				mListener.onModeChanged(IdiyMessage.SAVE_USERTODB_RESULT,saveUser2Db);
				break;
			case IdiyMessage.GET_USER_ACTION:
				UserInfo userInfo = acquireUser();
				mListener.onModeChanged(IdiyMessage.GET_USER_ACTION_RESULT,userInfo);
				break;
			case IdiyMessage.CLEAR_USER_ACTION:
				clearUser();
				mListener.onModeChanged(IdiyMessage.CLEAR_USER_ACTION_RESULT,0);
				break;
		}
	}
	private void clearUser(){
		mUserDao.clearUsers();
	}
	private UserInfo acquireUser(){
		UserInfo userInfo = mUserDao.acquireLastestUser();
		if(userInfo!=null){
			try {
				userInfo.name = AESUtils.decrypt(userInfo.name);
				userInfo.pwd = AESUtils.decrypt(userInfo.pwd);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return userInfo;
		}
		return null;
	}
	private boolean saveUser2Db(String name,String pwd){
		mUserDao.clearUsers();
		//可逆性加密
		try {
			name = AESUtils.encrypt(name);
			pwd = AESUtils.encrypt(pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mUserDao.saveUser(name,pwd);
	}
	private RResult loginOrRegist(String url,String name,String pwd){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("uname",name);
		params.put("upwd",pwd);
		String jsonStr = NetworkUtil.doPost(url,params);
		Log.d("520it","jsonStr:"+jsonStr);
		//处理json GSON FastJson
		//FastJson JSON不能解析嵌套的对象，它可以将一个嵌套的对象转换成JSON
		return JSON.parseObject(jsonStr,RResult.class);
	}
}

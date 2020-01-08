package com.m520it.jdmail.activity;

import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.JDApplication;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RLoginResult;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.UserController;
import com.m520it.jdmail.db.UserDao.UserInfo;
import com.m520it.jdmail.util.ActivityUtil;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends BaseActivity{
	private EditText mNameEt;
	private EditText mPwdEt;
	@Override
	protected void handlerMessage(Message msg) {
		switch(msg.what){
			case IdiyMessage.LOGIN_ACTION_RESULT:
				handleLoginResult(msg);
				break;
			case IdiyMessage.SAVE_USERTODB_RESULT:
				handleSaveUser2Db((Boolean)msg.obj);
				break;
			case IdiyMessage.GET_USER_ACTION_RESULT:
				handleGetUser(msg.obj);
				break;
		}
	}
	private void handleGetUser(Object obj){
		if(obj!=null){
			UserInfo userInfo = (UserInfo)obj;
			mNameEt.setText(userInfo.name);
			mPwdEt.setText(userInfo.pwd);
		}
	}
	private void handleSaveUser2Db(boolean ifSuccess){
		if(ifSuccess){
			ActivityUtil.start(this,MainActivity.class,true);
		}else{
			tip("登录异常");
		}
	}
	private void handleLoginResult(Message msg) {
		RResult rResult = (RResult)msg.obj;
		if(rResult.isSuccess()){
			//登录成功
			//将用户的信息保存到Application里面
			RLoginResult bean = JSON.parseObject(rResult.getResult(),
					RLoginResult.class);
			((JDApplication)getApplication()).setRLoginResult(bean);
			//将账号密码保存到数据库  传递一个帐号密码
			String name = mNameEt.getText().toString();
			String pwd = mPwdEt.getText().toString();
			mController.sendAsyncMessage(IdiyMessage.SAVE_USERTODB,name,pwd);
		}else{
			tip("登录失败:"+rResult.getErrorMsg());
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initController();
		initUI();
	}
	public void loginClick(View view){
		String name = mNameEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		if(ifValueWasEmpty(name,pwd)){
			tip("请输入帐号密码");
			return;
		}
		//发送一个网络请求，去请求网络数据
		mController.sendAsyncMessage(IdiyMessage.LOGIN_ACTION,name,pwd);
	}
	public void registClick(View v){
		ActivityUtil.start(this,RegistActivity.class,false);
	}
	@Override
	protected void initController() {
		mController = new UserController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mNameEt = (EditText)findViewById(R.id.name_et);
		mPwdEt = (EditText)findViewById(R.id.pwd_et);
		//如果数据库表里面有数据，应该读取账号密码出来
		mController.sendAsyncMessage(IdiyMessage.GET_USER_ACTION,0);
	}	
}

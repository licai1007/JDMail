package com.m520it.jdmail.activity;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.UserController;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

public class RegistActivity extends BaseActivity{
	private EditText mUsernameEt;
	private EditText mPwdEt;
	private EditText mSurepwdEt;
	@Override
	protected void handlerMessage(Message msg) {
		switch(msg.what){
			case IdiyMessage.REGIST_ACTION_RESULT:
				handleRegistResult((RResult)msg.obj);
				break;
		}
	}
	private void handleRegistResult(RResult resultBean){
		tip(resultBean.isSuccess()?"注册成功！":resultBean.getErrorMsg());
		if(resultBean.isSuccess()){
			finish();
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		initUI();
		initController();
	}
	@Override
	protected void initController() {
		mController = new UserController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mUsernameEt = (EditText)findViewById(R.id.username_et);
		mPwdEt = (EditText)findViewById(R.id.pwd_et);
		mSurepwdEt = (EditText)findViewById(R.id.surepwd_et);
	}
	public void registClick(View v){
		String name = mUsernameEt.getText().toString();
		String pwd = mPwdEt.getText().toString();
		String surePwd = mSurepwdEt.getText().toString();
		if(ifValueWasEmpty(name,pwd,surePwd)){
			tip("请输入完整的信息！");
			return;
		}
		if(!pwd.equals(surePwd)){
			tip("两次密码不一致！");
			return;
		}
//		注册用户
		mController.sendAsyncMessage(IdiyMessage.REGIST_ACTION,name,pwd);
	}
	
}

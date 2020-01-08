package com.m520it.jdmail.activity;
import com.m520it.jdmail.controller.BaseController;
import com.m520it.jdmail.listener.IModeChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements IModeChangeListener{
	protected BaseController mController;
	protected Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			handlerMessage(msg);
		}
	};
	protected void handlerMessage(Message msg){
		//default Empty implement
	}
	protected void initController(){
		//default Empty implement
	}
	protected void initData(){
		//default Empty implement
	}
	protected abstract void initUI();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	public void tip(String tipStr){
		Toast.makeText(this,tipStr,Toast.LENGTH_SHORT).show();
	}
	//在子线程中运行
	@Override
	public void onModeChanged(int action, Object... values) {
//		mHandler.sendMessage(mHandler.obtainMessage());
		mHandler.obtainMessage(action,values[0]).sendToTarget();
	}
	protected boolean ifValueWasEmpty(String... values) {
		for(String value:values){
			if(TextUtils.isEmpty(value)){
				return true;
			}
		}
		return false;
	}
}

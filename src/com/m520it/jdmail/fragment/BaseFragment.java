package com.m520it.jdmail.fragment;

import com.m520it.jdmail.controller.BaseController;
import com.m520it.jdmail.listener.IModeChangeListener;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment implements IModeChangeListener{
	protected BaseController mController;
	protected Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			handlerMessage(msg);
		}
	};
	public void tip(String tipStr){
		Toast.makeText(getActivity(),tipStr,Toast.LENGTH_SHORT).show();
	}
	protected void handlerMessage(Message msg){
		//default Empty implement
	}
	protected void initController(){
		//default Empty implement
	}
	protected abstract void initUI();
	@Override
	public void onModeChanged(int action, Object... values) {
		mHandler.obtainMessage(action,values[0]).sendToTarget();
	}
}

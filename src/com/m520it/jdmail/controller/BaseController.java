package com.m520it.jdmail.controller;

import android.content.Context;

import com.m520it.jdmail.listener.IModeChangeListener;

public abstract class BaseController {
	protected Context mContext;
	protected IModeChangeListener mListener;
	public BaseController(Context c){
		mContext = c;
	}
	public void setIModeChangeListener(IModeChangeListener listener) {
		mListener = listener;
	}
	/**
	 * @param action 一个页面可能有多个网络请求，用来区别这些请求的
	 * @param values 请求的数据
	 */
	public void sendAsyncMessage(final int action,final Object... values){
		new Thread(){
			public void run(){
				handleMessage(action,values);
			}
		}.start();
	}
	//子类处理具体的需求的业务代码
	protected abstract void handleMessage(int action,Object... values);
}

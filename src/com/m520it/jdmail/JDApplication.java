package com.m520it.jdmail;

import com.m520it.jdmail.bean.RLoginResult;
import com.m520it.jdmail.util.ErrorReport;

import android.app.Application;

public class JDApplication extends Application{
	public RLoginResult mRLoginResult;
	@Override
	public void onCreate() {
		super.onCreate();
		ErrorReport mErrorReport = ErrorReport.getInstance();
		mErrorReport.init(this);
	}

	public void setRLoginResult(RLoginResult bean) {
		mRLoginResult = bean;
	}
}

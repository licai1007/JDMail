package com.m520it.jdmail.listener;

import com.m520it.jdmail.bean.ROrderParam;

public interface IPayOnlineConfirmListener {
	//确定
	public void onSureClick(ROrderParam bean);
	//取消
	public void onCancelClick(long oid);
}

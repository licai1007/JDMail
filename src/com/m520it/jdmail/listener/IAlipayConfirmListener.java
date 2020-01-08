package com.m520it.jdmail.listener;

public interface IAlipayConfirmListener {
	public void onCancelClick();
	public void onSureClick(String name,String pwd,String payPwd);
}

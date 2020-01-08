package com.m520it.jdmail.bean;

public class RResult {
	private boolean success;
	private String errorMsg = "";
	private String result = "";
	
	public RResult() {
		super();
	}
	public RResult(boolean success, String errorMsg, String result) {
		super();
		this.success = success;
		this.errorMsg = errorMsg;
		this.result = result;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}

package com.m520it.jdmail.listener;
/**
 * 只要是容器，都可以实现他
 */
public interface IViewContainer {
	/**
	 * 让容器显示界面
	 */
	public void onShow(Object... values);
}

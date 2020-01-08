package com.m520it.jdmail.listener;

public interface IShopcarCheckChangeListener {
	public void onBuyCountChanged(int count);
	public void onTotalPriceChanged(double newestPrice);
}

package com.m520it.jdmail.listener;
/**
 * 删除购物车接口
 */
public interface IShopcarDeleteListener {
	/**
	 * @param shopcarId 被删除的购物车表id
	 */
	public void onItemDelete(long shopcarId);
}

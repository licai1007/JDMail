package com.m520it.jdmail.listener;

import com.m520it.jdmail.bean.RArea;

public interface IAreaChangeListener {
	public void onAreaChanged(RArea province,RArea city,RArea area);
}

package com.m520it.jdmail.controller;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.util.NetworkUtil;

import android.content.Context;

public class AlipayController extends UserController {
	public AlipayController(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch (action) {
			case IdiyMessage.GET_ALIPAYINFO_ACTION:
				mListener.onModeChanged(IdiyMessage.GET_ALIPAYINFO_ACTION_RESULT,
						loadPayInfo((Long)values[0],(Double)values[1]));
				break;
		}
	}
	private RResult loadPayInfo(long oid,double money){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("oid",oid+"");
		params.put("money",money+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.GETALIPAYINFO_URL,params);
		return JSON.parseObject(jsonStr,RResult.class);
	}
}

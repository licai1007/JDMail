package com.m520it.jdmail.controller;

import com.m520it.jdmail.constant.IdiyMessage;

import android.content.Context;

public class AlipayController1 extends UserController {
	public AlipayController1(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch (action) {
			case IdiyMessage.GET_ALIPAYINFO_ACTION:
				mListener.onModeChanged(IdiyMessage.GET_ALIPAYINFO_ACTION_RESULT,
						loadPayInfo((String)values[0]));
				break;
			case IdiyMessage.MOCK_PAY_ACTION:
				mockPay((String)values[0],(String)values[1],(String)values[2],(String)values[3]);
				break;
		}
	}
	private String mockPay(String name,String pwd,String payPwd,String mTn){
		return null;
	}
	private String loadPayInfo(String tn){
		return null;
	}
}

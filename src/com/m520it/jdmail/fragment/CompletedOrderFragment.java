package com.m520it.jdmail.fragment;
import java.util.List;

import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.CompleteOrderAdapter;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.OrderStatusConst;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 已完成页
 * */
public class CompletedOrderFragment extends OrderBaseFragment {
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.COMPLETE_ORDER_ACTION_RESULT:
				handleLoadLv((List<ROrderListBean>)msg.obj);
				break;
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_completed_order, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.COMPLETE_ORDER_ACTION,
				OrderStatusConst.COMPLETE_ORDER);
	}

	@Override
	protected void initUI() {
		initXListView(R.id.complete_order_lv,CompleteOrderAdapter.class);
	}

	@Override
	public void onRefresh() {
		mController.sendAsyncMessage(IdiyMessage.COMPLETE_ORDER_ACTION,
				OrderStatusConst.COMPLETE_ORDER);
	}
	
}

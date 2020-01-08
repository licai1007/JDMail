package com.m520it.jdmail.fragment;
import java.util.List;

import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.AllOrderAdapter;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.OrderStatusConst;
import com.m520it.jdmail.controller.OrderController;
import com.m520it.jdmail.listener.IConfirmReceiverListener;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 全部订单
 * */
public class AllOrderFragment extends OrderBaseFragment 
							implements IConfirmReceiverListener{
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.ALL_ORDER_ACTION_RESULT:
				handleLoadLv((List<ROrderListBean>)msg.obj);
				break;
			case IdiyMessage.CONFIRM_ORDER_ACTION_RESULT:
				handleConfirmOrder((RResult)msg.obj);
				break;
		}
	}
	private void handleConfirmOrder(RResult resultBean){
		if(resultBean.isSuccess()){
			mController.sendAsyncMessage(IdiyMessage.ALL_ORDER_ACTION,
					OrderStatusConst.ALL_ORDER);
		}else{
			tip("确认失败:"+resultBean.getErrorMsg());
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_all_order, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mController = new OrderController(getActivity());
		mController.setIModeChangeListener(this);
		initUI();
		mController.sendAsyncMessage(IdiyMessage.ALL_ORDER_ACTION,OrderStatusConst.ALL_ORDER);
	}

	@Override
	protected void initUI() {
		initXListView(R.id.all_order_lv,AllOrderAdapter.class);
		((AllOrderAdapter)mAdapter).setIConfirmReceiverListener(this);
	}

	@Override
	public void onRefresh() {
		mController.sendAsyncMessage(IdiyMessage.ALL_ORDER_ACTION,OrderStatusConst.ALL_ORDER);
	}

	@Override
	public void onOrderReceived(long oid) {
		mController.sendAsyncMessage(IdiyMessage.CONFIRM_ORDER_ACTION,oid);
	}
	
}

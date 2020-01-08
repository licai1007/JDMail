package com.m520it.jdmail.fragment;
import java.util.List;

import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.WaitReceiveAdapter;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.OrderStatusConst;
import com.m520it.jdmail.listener.IConfirmReceiverListener;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 待收货页
 * */
public class WaitReceiveFragment extends OrderBaseFragment 
							implements IConfirmReceiverListener{
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.WAIT_RECEIVE_ACTION_RESULT:
				handleLoadLv((List<ROrderListBean>)msg.obj);
				break;
			case IdiyMessage.CONFIRM_ORDER_ACTION_RESULT:
				handleConfirmOrder((RResult)msg.obj);
				break;
		}
	}
	private void handleConfirmOrder(RResult resultBean){
		if(resultBean.isSuccess()){
			mController.sendAsyncMessage(IdiyMessage.WAIT_RECEIVE_ACTION,
					OrderStatusConst.WAIT_RECEIVE_ORDER);
		}else{
			tip("确认失败:"+resultBean.getErrorMsg());
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_wait_receive, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.WAIT_RECEIVE_ACTION,
				OrderStatusConst.WAIT_RECEIVE_ORDER);
	}

	@Override
	protected void initUI() {
		initXListView(R.id.wait_receive_lv,WaitReceiveAdapter.class);
		((WaitReceiveAdapter)mAdapter).setIConfirmReceiverListener(this);
	}

	@Override
	public void onRefresh() {
		mController.sendAsyncMessage(IdiyMessage.WAIT_RECEIVE_ACTION,
				OrderStatusConst.WAIT_RECEIVE_ORDER);
	}
	@Override
	public void onOrderReceived(long oid) {
		mController.sendAsyncMessage(IdiyMessage.CONFIRM_ORDER_ACTION,oid);
	}
	
}

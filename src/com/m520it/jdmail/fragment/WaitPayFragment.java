package com.m520it.jdmail.fragment;
import java.util.List;
import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.WaitPayAdapter;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.OrderStatusConst;
import com.m520it.jdmail.controller.OrderController;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 待支付页
 * */
public class WaitPayFragment extends OrderBaseFragment {
	private OrderController mController;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.WAIT_PAY_ACTION_RESULT:
				handleLoadLv((List<ROrderListBean>)msg.obj);
				break;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_wait_pay, container,false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mController = new OrderController(getActivity());
		mController.setIModeChangeListener(this);
		initUI();
		mController.sendAsyncMessage(IdiyMessage.WAIT_PAY_ACTION,
				OrderStatusConst.WAIT_PAY_ORDER);
	}
	
	@Override
	protected void initUI() {
		initXListView(R.id.wait_pay_lv,WaitPayAdapter.class);
		
	}
	@Override
	public void onRefresh() {
		mController.sendAsyncMessage(IdiyMessage.WAIT_PAY_ACTION,
				OrderStatusConst.WAIT_PAY_ORDER);
	}

}

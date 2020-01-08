package com.m520it.jdmail.activity;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.OrderDetailProductsAdapter;
import com.m520it.jdmail.bean.ROrderDetails;
import com.m520it.jdmail.bean.ROrderDetailsProducts;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.OrderController;
import com.m520it.jdmail.util.FixedViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class OrderDetailsActivity extends BaseActivity {
	private long mOid;
	private TextView mOrderNoTv;
	private TextView mOrderStatusTv;
	private TextView mReceiveNameTv;
	private TextView mReceivePhoneTv;
	private TextView mReceiveAddressTv;
	private ListView mProductsLv;
	private OrderDetailProductsAdapter mAdapter;
	private TextView mTotalPriceTv;
	private TextView mTakePriceTv;
	private TextView mActualPriceTv;
	private TextView mOrderTimeTv;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.GET_ORDER_DETAILS_ACTION_RESULT:
				handleLoadOrderDetails(msg.obj);
				break;
		}
	}
	private void handleLoadOrderDetails(Object obj){
		if(obj!=null){
			ROrderDetails bean = (ROrderDetails)obj;
			//1.订单编号和状态
			mOrderNoTv.setText("订单编号:"+bean.getOrderNum());
			showOrderStatus(mOrderStatusTv,bean.getStatus());
			//2.收货人信息
			mReceiveNameTv.setText(bean.getReceiverName());
			mReceivePhoneTv.setText(bean.getReceiverPhone());
			mReceiveAddressTv.setText(bean.getReceiverAddress());
			//3.加载商品信息
			String productsJson = bean.getItems();
			List<ROrderDetailsProducts> itemDatas = JSON.parseArray(productsJson,ROrderDetailsProducts.class);
			mAdapter.setDatas(itemDatas);
			mAdapter.notifyDataSetChanged();
			FixedViewUtil.setListViewHeightBasedOnChildren(mProductsLv);
			//4.加载订单价格
			mTotalPriceTv.setText("￥ "+(bean.getTotalPrice()-bean.getFreight()));
			mTakePriceTv.setText("￥ "+bean.getFreight());
			mActualPriceTv.setText("￥ "+bean.getTotalPrice());
			mOrderTimeTv.setText("下单时间:"+bean.getBuyTime());
		}else{
			tip("数据异常");
			finish();
		}
	}
	protected void showOrderStatus(TextView tv,int status){
		switch (status) {
			case -1:
				tv.setText("取消订单");
				break;
			case 0:
				tv.setText("待支付");
				break;
			case 1:
				tv.setText("待发货");
				break;
			case 2:
				tv.setText("待收货");
				break;
			case 3:
				tv.setText("完成交易");
				break;
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_details);
		initData();
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.GET_ORDER_DETAILS_ACTION,mOid);
	}
	
	@Override
	protected void initController() {
		mController = new OrderController(this);
		mController.setIModeChangeListener(this);
	}
	
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOid = intent.getLongExtra("OID",0);
		if(mOid==0){
			finish();
		}
	}
	@Override
	protected void initUI() {
		mOrderNoTv = (TextView)findViewById(R.id.order_no_tv);
		mOrderStatusTv = (TextView)findViewById(R.id.order_status_tv);
		mReceiveNameTv = (TextView)findViewById(R.id.receive_name_tv);
		mReceivePhoneTv = (TextView)findViewById(R.id.receive_phone_tv);
		mReceiveAddressTv = (TextView)findViewById(R.id.receive_address_tv);
		
		mProductsLv = (ListView)findViewById(R.id.products_lv);
		mAdapter = new OrderDetailProductsAdapter(this);
		mProductsLv.setAdapter(mAdapter);
		
		mTotalPriceTv = (TextView)findViewById(R.id.total_price_val_tv);
		mTakePriceTv = (TextView)findViewById(R.id.take_price_val_tv);
		mActualPriceTv = (TextView)findViewById(R.id.actual_price_tv);
		mOrderTimeTv = (TextView)findViewById(R.id.order_time_tv);
	}
	public void goBack(View v){
		finish();
	}

}

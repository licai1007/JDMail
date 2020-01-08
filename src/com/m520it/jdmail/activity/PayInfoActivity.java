package com.m520it.jdmail.activity;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderParam;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PayInfoActivity extends BaseActivity{
	private TextView mPayPriceTv;
	private TextView mOrderInfoTv;
	private TextView mDealTimeValTv;
	private ROrderParam mBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alipay);
		initData();
		initUI();
		loadData();
	}
	private void loadData() {
		mPayPriceTv.setText("¥ "+mBean.getTotalPrice());
		mOrderInfoTv.setText(mBean.getPname());
		mDealTimeValTv.setText(mBean.getBuyTime());
	}
	@Override
	protected void initUI() {
		mPayPriceTv = (TextView)findViewById(R.id.pay_price_tv);
		mOrderInfoTv = (TextView)findViewById(R.id.order_desc_val_tv);
		mDealTimeValTv = (TextView)findViewById(R.id.deal_time_val_tv);
	}
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mBean = (ROrderParam)intent.getSerializableExtra("BEAN");
		if(mBean==null){
			tip("获取支付信息异常");
			finish();
		}
	}
	//立即支付
	public void payClick(View v){
		Intent intent = new Intent(this,PaySelectActivity.class);
		intent.putExtra("OID",mBean.getOid());
		intent.putExtra("MONEY",mBean.getTotalPrice());
		startActivity(intent);
		finish();
	}
	//点返回键
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this,OrderDetailsActivity.class);
		intent.putExtra("OID",mBean.getOid());
		startActivity(intent);
		finish();
	}
	
	
	
	
	
	
}

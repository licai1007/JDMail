package com.m520it.jdmail.ui.pop;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderParam;
import com.m520it.jdmail.listener.IAddOrderConfirmListener;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class PayWhenGetDialog extends AlertDialog implements OnClickListener{
	private ROrderParam mBean;
	private IAddOrderConfirmListener mListener;
	public PayWhenGetDialog(Context context,ROrderParam bean) {
		super(context,R.style.CustomDialog);
		mBean = bean;
	}
	
	public void setIAddOrderConfirmListener(IAddOrderConfirmListener listener) {
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.build_order_pop_view);
		//获取所有的子控件
		TextView orderNoTv = (TextView)findViewById(R.id.order_no_tv);
		TextView totalPriceTv = (TextView)findViewById(R.id.total_price_tv);
		TextView freightTv = (TextView)findViewById(R.id.freight_tv);
		TextView actualPriceTv = (TextView)findViewById(R.id.actual_price_tv);
		findViewById(R.id.sure_btn).setOnClickListener(this);
		//为其赋值
		orderNoTv.setText("订单编号:"+mBean.getOrderNum());
		totalPriceTv.setText("总价: ￥"+mBean.getAllPrice());
		freightTv.setText("运费: ￥"+mBean.getFreight());
		actualPriceTv.setText("实付: ￥"+mBean.getTotalPrice());
	}

	@Override
	public void onClick(View v) {
		//确定按钮
		dismiss();
		//1.通过context强转成activity并调用finish
		//2.设置回调接口
		if(null!=mListener){
			mListener.onSureBtnClick(mBean.getOid());
		}
	}

}

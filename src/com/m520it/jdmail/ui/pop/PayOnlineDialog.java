package com.m520it.jdmail.ui.pop;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderParam;
import com.m520it.jdmail.listener.IPayOnlineConfirmListener;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class PayOnlineDialog extends AlertDialog implements OnClickListener{
	private ROrderParam mBean;
	private IPayOnlineConfirmListener mListener;
	public PayOnlineDialog(Context context,ROrderParam bean) {
		super(context,R.style.CustomDialog);
		mBean = bean;
	}
	
	public void setIPayOnlineConfirmListener(IPayOnlineConfirmListener listener) {
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_online_order_pop_view);
		//获取所有的子控件
		TextView orderNoTv = (TextView)findViewById(R.id.order_no_tv);
		TextView totalPriceTv = (TextView)findViewById(R.id.total_price_tv);
		TextView freightTv = (TextView)findViewById(R.id.freight_tv);
		TextView actualPriceTv = (TextView)findViewById(R.id.actual_price_tv);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
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
			if(v.getId()==R.id.sure_btn){
				mListener.onSureClick(mBean);
			}else if(v.getId()==R.id.cancel_btn){
				//取消
				mListener.onCancelClick(mBean.getOid());
			}
		}
	}

}

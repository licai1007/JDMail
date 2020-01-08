package com.m520it.jdmail.adapter;

import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.PayInfoActivity;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.bean.ROrderParam;
import com.m520it.jdmail.listener.IConfirmReceiverListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AllOrderAdapter extends OrderBaseAdapter{
	private IConfirmReceiverListener mListener;

	public AllOrderAdapter(Context context) {
		super(context);
	}
	
	public void setIConfirmReceiverListener(IConfirmReceiverListener listener) {
		mListener = listener;
	}
	
	class ViewHolder{
		TextView orderNoTv;
		TextView orderStateTv;
		LinearLayout pContainerLl;
		TextView priceTv;
		Button doBtn;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.order_list_item,parent,false);
			holder = new ViewHolder();
			holder.orderNoTv = (TextView)convertView.findViewById(R.id.order_no_tv);
			holder.orderStateTv = (TextView)convertView.findViewById(R.id.order_state_tv);
			holder.pContainerLl = (LinearLayout)convertView.findViewById(R.id.p_container_ll);
			holder.priceTv = (TextView)convertView.findViewById(R.id.price_tv);
			holder.doBtn = (Button)convertView.findViewById(R.id.do_btn);
			holder.doBtn.setOnClickListener(this);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ROrderListBean bean = mDatas.get(position);
		holder.orderNoTv.setText("订单编号:"+bean.getOrderNum());
		orderStatus(holder.orderStateTv,holder.doBtn,bean.getStatus());
		initProductContainer(holder.pContainerLl,bean.getItems());
		holder.priceTv.setText("￥"+bean.getTotalPrice());
		holder.doBtn.setTag(bean);
		return convertView;
	}
	
	private void orderStatus(TextView tv,Button btn,int status){
		switch (status) {
			case -1:
				btn.setVisibility(View.INVISIBLE);
				tv.setText("取消订单");
				break;
			case 0:
				btn.setVisibility(View.VISIBLE);
				btn.setText("去支付");
				tv.setText("待支付");
				break;
			case 1:
				btn.setVisibility(View.INVISIBLE);
				tv.setText("待发货");
				break;
			case 2:
				btn.setVisibility(View.VISIBLE);
				btn.setText("确认收货");
				tv.setText("待收货");
				break;
			case 3:
				btn.setVisibility(View.INVISIBLE);
				tv.setText("完成交易");
				break;
		}
	}
	
	@Override
	public void onClick(View v) {
		//不同的订单状态有不同的实现
		ROrderListBean bean = (ROrderListBean)v.getTag();
		//1.拿到一个oid
		//2.拿到订单的状态  区别处理
		switch (bean.getStatus()) {
			case 0:
				Intent intent = new Intent(mContext,PayInfoActivity.class);
				ROrderParam orderParam = new ROrderParam();
				orderParam.setOid(bean.getOid());
				orderParam.setTotalPrice(bean.getTotalPrice());
				orderParam.setPname(bean.getPname());
				orderParam.setBuyTime(bean.getBuyTime());
				intent.putExtra("BEAN",orderParam);
				Activity activity = (Activity)mContext;
				activity.startActivity(intent);
				break;
			case 2:
				if(mListener!=null){
					mListener.onOrderReceived(bean.getOid());
				}
				break;
		}
	}
	
}

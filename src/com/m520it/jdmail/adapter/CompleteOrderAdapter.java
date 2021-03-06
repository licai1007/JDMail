package com.m520it.jdmail.adapter;

import java.util.List;
import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.constant.NetworkConst;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CompleteOrderAdapter extends OrderBaseAdapter{
	public CompleteOrderAdapter(Context context) {
		super(context);
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
			holder.doBtn.setVisibility(View.GONE);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ROrderListBean bean = mDatas.get(position);
		holder.orderNoTv.setText("订单编号:"+bean.getOrderNum());
		showOrderStatus(holder.orderStateTv,bean.getStatus());
		initProductContainer(holder.pContainerLl,bean.getItems());
		holder.priceTv.setText("￥"+bean.getTotalPrice());
		return convertView;
	}
	
	@Override
	public void onClick(View v) {
		//去支付
	}
	
}

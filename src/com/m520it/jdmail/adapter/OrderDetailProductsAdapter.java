package com.m520it.jdmail.adapter;

import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderDetailsProducts;
import com.m520it.jdmail.bean.RProductList;
import com.m520it.jdmail.constant.NetworkConst;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OrderDetailProductsAdapter extends JDBaseAdapter<ROrderDetailsProducts>{
	public OrderDetailProductsAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		SmartImageView piconIv;
		TextView nameTv;
		TextView buycountTv;
		TextView priceTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.order_details_products_item,parent,false);
			holder = new ViewHolder();
			holder.piconIv = (SmartImageView)convertView.findViewById(R.id.p_icon_iv);
			holder.nameTv = (TextView)convertView.findViewById(R.id.p_name_tv);
			holder.buycountTv = (TextView)convertView.findViewById(R.id.buycount_tv);
			holder.priceTv = (TextView)convertView.findViewById(R.id.p_price_iv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		ROrderDetailsProducts bean = mDatas.get(position);
		holder.piconIv.setImageUrl(NetworkConst.BASE_URL+"/res/"+bean.getPiconUrl());
		holder.nameTv.setText(bean.getPname());
		holder.buycountTv.setText("X"+bean.getBuyCount());
		holder.priceTv.setText("￥ "+bean.getAmount());
		return convertView;
	}
	
}

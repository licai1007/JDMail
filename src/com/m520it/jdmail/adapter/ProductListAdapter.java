package com.m520it.jdmail.adapter;

import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RProductList;
import com.m520it.jdmail.constant.NetworkConst;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductListAdapter extends JDBaseAdapter<RProductList>{
	public ProductListAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		SmartImageView smIv;
		TextView nameTv;
		TextView priceTv;
		TextView commrateTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.product_lv_item,parent,false);
			holder = new ViewHolder();
			holder.smIv = (SmartImageView)convertView.findViewById(R.id.product_iv);
			holder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
			holder.priceTv = (TextView)convertView.findViewById(R.id.price_tv);
			holder.commrateTv = (TextView)convertView.findViewById(R.id.commrate_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RProductList bean = mDatas.get(position);
		holder.smIv.setImageUrl(NetworkConst.BASE_URL+"/res/"+bean.getIconUrl());
		holder.nameTv.setText(bean.getName());
		holder.priceTv.setText("￥ "+bean.getPrice());
		holder.commrateTv.setText(bean.getCommentCount()+"条评价  好评"+bean.getFavcomRate()+"%");
		return convertView;
	}
	@Override
	public long getItemId(int position) {
		return mDatas!=null?mDatas.get(position).getId():0;
	}
}

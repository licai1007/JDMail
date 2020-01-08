package com.m520it.jdmail.adapter;

import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RRecommendProduct;
import com.m520it.jdmail.constant.NetworkConst;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecommendAdapter extends JDBaseAdapter<RRecommendProduct>{
	class ViewHolder{
		SmartImageView smIv;
		TextView nameTv;
		TextView priceTv;
	}
	public RecommendAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.recommend_gv_item,parent,false);
			holder = new ViewHolder();
			holder.smIv = (SmartImageView)convertView.findViewById(R.id.image_iv);
			holder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
			holder.priceTv = (TextView)convertView.findViewById(R.id.price_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RRecommendProduct bean = mDatas.get(position);
		holder.smIv.setImageUrl(NetworkConst.BASE_URL+bean.getIconUrl());
		holder.nameTv.setText(bean.getName());
		holder.priceTv.setText("￥"+bean.getPrice());
		return convertView;
	}
	@Override
	public long getItemId(int position) {
		return mDatas!=null?mDatas.get(position).getProductId():0;
	}
}

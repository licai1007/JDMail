package com.m520it.jdmail.adapter;

import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RSecondKill;
import com.m520it.jdmail.constant.NetworkConst;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SecondKillAdapter extends JDBaseAdapter<RSecondKill>{
	public SecondKillAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		SmartImageView smIv;
		TextView pointPriceTv;
		TextView allPriceTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.home_seckill_item,parent,false);
			holder = new ViewHolder();
			holder.smIv = (SmartImageView)convertView.findViewById(R.id.image_iv);
			holder.pointPriceTv = (TextView)convertView.findViewById(R.id.nowprice_tv);
			holder.allPriceTv = (TextView)convertView.findViewById(R.id.normalprice_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RSecondKill bean = mDatas.get(position);
		holder.smIv.setImageUrl(NetworkConst.BASE_URL+bean.getIconUrl());
		holder.pointPriceTv.setText("￥"+bean.getPointPrice());
		holder.allPriceTv.setText("￥"+bean.getAllPrice());
		holder.allPriceTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
		return convertView;
	}

}

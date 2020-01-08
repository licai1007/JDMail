package com.m520it.jdmail.adapter;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RReceiver;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ReceiverListAdapter extends JDBaseAdapter<RReceiver>{
	public ReceiverListAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		ImageView defaultIv;
		TextView nameTv;
		TextView phoneTv;
		TextView addressTv;
		TextView deleteTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.choose_address_item_view,parent,false);
			holder = new ViewHolder();
			holder.defaultIv = (ImageView)convertView.findViewById(R.id.isDefault_iv);
			holder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
			holder.phoneTv = (TextView)convertView.findViewById(R.id.phone_tv);
			holder.addressTv = (TextView)convertView.findViewById(R.id.address_tv);
			holder.deleteTv = (TextView)convertView.findViewById(R.id.delete_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RReceiver bean = mDatas.get(position);
		holder.defaultIv.setVisibility(bean.getIsDefault()?View.VISIBLE:View.INVISIBLE);
		holder.nameTv.setText(bean.getReceiverName());
		holder.phoneTv.setText(bean.getReceiverPhone());
		holder.addressTv.setText(bean.getReceiverAddress());
		return convertView;
	}
	@Override
	public Object getItem(int position) {
		return mDatas!=null?mDatas.get(position):null;
	}
}

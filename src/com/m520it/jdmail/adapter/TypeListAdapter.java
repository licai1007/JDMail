package com.m520it.jdmail.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.m520it.jdmail.R;

public class TypeListAdapter extends JDBaseAdapter<String>{
	public int mCurrentTabPosition = -1;
	public TypeListAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.brand_lv_item_layout,parent,false);
			tv = (TextView)convertView.findViewById(R.id.brand_tv);
			convertView.setTag(tv);
		}else{
			tv = (TextView)convertView.getTag();
		}
		String productType = mDatas.get(position);
		tv.setText(productType);
		//修改样式
		tv.setSelected(mCurrentTabPosition==position);
		return convertView;
	}
	@Override
	public Object getItem(int position) {
		return mDatas!=null?mDatas.get(position):"";
	}
}

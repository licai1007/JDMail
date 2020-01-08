package com.m520it.jdmail.adapter;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RCategory;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CategoryAdapter extends JDBaseAdapter<RCategory>{
	public int mCurrentTabPosition = -1;
	class ViewHolder{
		View dividerView;
		TextView nameTv;
	}
	public CategoryAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.top_category_item,parent,false);
			holder = new ViewHolder();
			holder.dividerView = (View)convertView.findViewById(R.id.divider);
			holder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RCategory bean = mDatas.get(position);
		holder.nameTv.setText(bean.getName());
		//修改样式
		if(position == mCurrentTabPosition){
			holder.nameTv.setSelected(true);
			holder.nameTv.setBackgroundResource(R.drawable.tongcheng_all_bg01);
			holder.dividerView.setVisibility(View.INVISIBLE);
		}else{
			holder.nameTv.setSelected(false);
			holder.nameTv.setBackgroundColor(0xFFFAFAFA);
			holder.dividerView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}
	@Override
	public Object getItem(int position) {
		return mDatas!=null?mDatas.get(position):null;
	}
}

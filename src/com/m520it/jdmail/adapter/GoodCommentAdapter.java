package com.m520it.jdmail.adapter;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RGoodComment;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.ui.RatingBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GoodCommentAdapter extends JDBaseAdapter<RGoodComment>{
	public GoodCommentAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		RatingBar commentLevelRb;
		TextView nameTv;
		TextView contentTv;
		LinearLayout iamgesContainer;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.good_comment_item_view,parent,false);
			holder = new ViewHolder();
			holder.commentLevelRb = (RatingBar)convertView.findViewById(R.id.rating_bar);
			holder.nameTv = (TextView)convertView.findViewById(R.id.name_tv);
			holder.contentTv = (TextView)convertView.findViewById(R.id.content_tv);
			holder.iamgesContainer = (LinearLayout)convertView.findViewById(R.id.iamges_container);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RGoodComment bean = mDatas.get(position);
		holder.commentLevelRb.setRating(bean.getRate());
		holder.nameTv.setText(bean.getUserName());
		holder.contentTv.setText(bean.getComment());
		initImageContainer(holder, bean);
		return convertView;
	}
	private void initImageContainer(ViewHolder holder, RGoodComment bean) {
		//如果放回的数据是3:显示3
		//如果放回的数据是5:显示4
		int childCount = holder.iamgesContainer.getChildCount();
		List<String> imgUrls = new ArrayList<String>();
		if(bean.getImgUrls()!=null){
			imgUrls = JSON.parseArray(bean.getImgUrls(),String.class);
		}
		int dataSize = imgUrls.size();
		int realSize = Math.min(childCount,dataSize);
		//清空老数据
		for(int i=0;i<childCount;i++){
			SmartImageView smIv = (SmartImageView)holder.iamgesContainer.getChildAt(i);
			smIv.setImageDrawable(new BitmapDrawable());
		}
		//设置新的数据
		for(int i=0;i<realSize;i++){
			SmartImageView smIv = (SmartImageView)holder.iamgesContainer.getChildAt(i);
			smIv.setImageUrl(NetworkConst.BASE_URL+imgUrls.get(i));
		}
		holder.iamgesContainer.setVisibility(realSize>0?View.VISIBLE:View.GONE);
	}

}

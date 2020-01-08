package com.m520it.jdmail.adapter;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RProductComment;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.ui.RatingBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentAdapter extends JDBaseAdapter<RProductComment>{
	public CommentAdapter(Context context) {
		super(context);
	}
	class ViewHolder{
		SmartImageView userIconIv;
		TextView userNameTv;
		TextView userTimeTv;
		RatingBar commentLevelRb;
		TextView commentTv;
		TextView buytimeTv;
		TextView buyversionTv;
		LinearLayout iamgesContainer;
		TextView lovecountTv;
		TextView subcommentTv;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.comment_item_view,parent,false);
			holder = new ViewHolder();
			holder.userIconIv = (SmartImageView)convertView.findViewById(R.id.icon_iv);
			holder.userNameTv = (TextView)convertView.findViewById(R.id.name_tv);
			holder.userTimeTv = (TextView)convertView.findViewById(R.id.time_tv);
			holder.commentLevelRb = (RatingBar)convertView.findViewById(R.id.rating_bar);
			holder.commentTv = (TextView)convertView.findViewById(R.id.content_tv);
			holder.buytimeTv = (TextView)convertView.findViewById(R.id.buytime_tv);
			holder.buyversionTv = (TextView)convertView.findViewById(R.id.buyversion_tv);
			holder.iamgesContainer = (LinearLayout)convertView.findViewById(R.id.iamges_container);
			holder.lovecountTv = (TextView)convertView.findViewById(R.id.lovecount_tv);
			holder.subcommentTv = (TextView)convertView.findViewById(R.id.subcomment_tv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		RProductComment bean = mDatas.get(position);
		holder.userIconIv.setImageUrl(NetworkConst.BASE_URL+bean.getUserImg());
		holder.userNameTv.setText(bean.getUserName());
		holder.userTimeTv.setText(bean.getCommentTime());
		holder.commentLevelRb.setRating(bean.getRate());
		holder.commentTv.setText(bean.getComment());
		holder.buytimeTv.setText("购买时间:"+bean.getBuyTime());
		holder.buyversionTv.setText("型号:"+bean.getProductType());
		initImageContainer(holder.iamgesContainer,bean);
		holder.lovecountTv.setText("喜欢("+bean.getLoveCount()+")");
		holder.subcommentTv.setText("回复("+bean.getSubComment()+")");
		return convertView;
	}
	private void initImageContainer(LinearLayout containerLl,RProductComment bean) {
		//如果放回的数据是3:显示3
		//如果放回的数据是5:显示4
		int childCount = containerLl.getChildCount();
		List<String> imgUrls = new ArrayList<String>();
		if(bean.getImgUrls()!=null){
			imgUrls = JSON.parseArray(bean.getImgUrls(),String.class);
		}
		int dataSize = imgUrls.size();
		int realSize = Math.min(childCount,dataSize);
		//清空老数据
		for(int i=0;i<childCount;i++){
			SmartImageView smIv = (SmartImageView)containerLl.getChildAt(i);
			smIv.setImageDrawable(new BitmapDrawable());
		}
		//设置新的数据
		for(int i=0;i<realSize;i++){
			SmartImageView smIv = (SmartImageView)containerLl.getChildAt(i);
			smIv.setImageUrl(NetworkConst.BASE_URL+imgUrls.get(i));
		}
		containerLl.setVisibility(realSize>0?View.VISIBLE:View.GONE);
	}

}

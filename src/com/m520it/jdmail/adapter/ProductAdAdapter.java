package com.m520it.jdmail.adapter;

import java.util.ArrayList;
import java.util.List;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.constant.NetworkConst;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ProductAdAdapter extends PagerAdapter{
	private List<String> mImgUrList;
	private List<SmartImageView> mSmartImageViews;
	public void setDatas(Context c,List<String> imgUrList) {
		mImgUrList = imgUrList;
		//当数据有了之后，就有对应的图片
		mSmartImageViews = new ArrayList<SmartImageView>();
		for(int i=0;i<mImgUrList.size();i++){
			//创建控件  设置宽高  设置数据源  添加到View容器中
			SmartImageView smIv = new SmartImageView(c);
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			smIv.setLayoutParams(params);
			smIv.setImageUrl(NetworkConst.BASE_URL+mImgUrList.get(i));
			mSmartImageViews.add(smIv);
		}
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		SmartImageView siv = mSmartImageViews.get(position);
		container.addView(siv);
		return siv;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		SmartImageView siv = mSmartImageViews.get(position);
		container.removeView(siv);
	}
	@Override
	public int getCount() {
		return mSmartImageViews!=null?mSmartImageViews.size():0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}

}

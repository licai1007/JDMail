package com.m520it.jdmail.ui;

import com.m520it.jdmail.R;
import com.m520it.jdmail.listener.IBottomBarClickListener;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;;

public class BottomBar extends LinearLayout implements OnClickListener {
	private ImageView mHomeIv;
	private TextView mHomeTv;
	private ImageView mCategoryIv;
	private TextView mCategoryTv;
	private ImageView mShopcarIv;
	private TextView mShopcarTv;
	private ImageView mMineIv;
	private TextView mMineTv;
	private IBottomBarClickListener mListener;
	private int mCurrentTabId;
	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public void setIBottomBarClickListener(IBottomBarClickListener listener) {
		mListener = listener;
	}
	//onFinishInflate 当所有的控件测量排布后，会调用该方法
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
//		1.渲染界面
//		2.实现与用户的交互
		findViewById(R.id.frag_main_ll).setOnClickListener(this);
		findViewById(R.id.frag_category_ll).setOnClickListener(this);
		findViewById(R.id.frag_shopcar_ll).setOnClickListener(this);
		findViewById(R.id.frag_mine_ll).setOnClickListener(this);
		
		mHomeIv = (ImageView)findViewById(R.id.frag_main_iv);
		mHomeTv = (TextView)findViewById(R.id.frag_main);
		mCategoryIv = (ImageView)findViewById(R.id.frag_category_iv);
		mCategoryTv = (TextView)findViewById(R.id.frag_category);
		mShopcarIv = (ImageView)findViewById(R.id.frag_shopcar_iv);
		mShopcarTv = (TextView)findViewById(R.id.frag_shopcar);
		mMineIv = (ImageView)findViewById(R.id.frag_mine_iv);
		mMineTv = (TextView)findViewById(R.id.frag_mine);
		
		setFontType(mHomeTv);
		setFontType(mCategoryTv);
		setFontType(mShopcarTv);
		setFontType(mMineTv);
		
		//模拟用户第一次点击了首页
		findViewById(R.id.frag_main_ll).performClick();
	}
	private void setFontType(TextView tv){
		Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font.ttf");
		tv.setTypeface(tf);
	}
	@Override
	public void onClick(View v) {
		if(mCurrentTabId==v.getId()){
			return;
		}
		mHomeIv.setSelected(v.getId()==R.id.frag_main_ll);
		mHomeTv.setSelected(v.getId()==R.id.frag_main_ll);
		mCategoryIv.setSelected(v.getId()==R.id.frag_category_ll);
		mCategoryTv.setSelected(v.getId()==R.id.frag_category_ll);
		mShopcarIv.setSelected(v.getId()==R.id.frag_shopcar_ll);
		mShopcarTv.setSelected(v.getId()==R.id.frag_shopcar_ll);
		mMineIv.setSelected(v.getId()==R.id.frag_mine_ll);
		mMineTv.setSelected(v.getId()==R.id.frag_mine_ll);
		if(mListener!=null){
			mListener.onItemClick(v.getId());
			mCurrentTabId = v.getId();
		}
	}
	
}

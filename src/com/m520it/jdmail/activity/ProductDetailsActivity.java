package com.m520it.jdmail.activity;

import java.util.ArrayList;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ProductDetailsController;
import com.m520it.jdmail.fragment.ProductCommentFragment;
import com.m520it.jdmail.fragment.ProductDetailsFragment;
import com.m520it.jdmail.fragment.ProductIntroduceFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;

public class ProductDetailsActivity extends BaseActivity implements OnClickListener, OnPageChangeListener{
	public long mProductId;
	public int mBuyCount = 1;
	public String mProductVersion = "";
	private View mIntroduceView;
	private View mDetailsView;
	private View mCommentView;
	private ViewPager mContainerVp;
	private ContainerAdapter mContainerAdapter;
	@Override
	protected void handlerMessage(Message msg) {
		RResult bean = (RResult)msg.obj;
		if(bean.isSuccess()){
			tip("添加成功");
			finish();
		}else{
			tip("添加失败:"+bean.getErrorMsg());
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_details);
		initData();
		initController();
		initUI();
	}
	@Override
	protected void initController() {
		mController = new ProductDetailsController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		findViewById(R.id.productdetails_goback_iv).setOnClickListener(this);
		findViewById(R.id.introduce_ll).setOnClickListener(this);
		findViewById(R.id.details_ll).setOnClickListener(this);
		findViewById(R.id.comment_ll).setOnClickListener(this);
		mIntroduceView = findViewById(R.id.introduce_view);
		mDetailsView = findViewById(R.id.details_view);
		mCommentView = findViewById(R.id.comment_view);
		
		mContainerVp = (ViewPager)findViewById(R.id.container_vp);
		mContainerAdapter = new ContainerAdapter(getSupportFragmentManager());
		mContainerVp.setAdapter(mContainerAdapter);
		mContainerVp.setOnPageChangeListener(this);
	}
	public class ContainerAdapter extends FragmentPagerAdapter{
		private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
		public ContainerAdapter(FragmentManager fm) {
			super(fm);
			ProductIntroduceFragment introduceFragment = new ProductIntroduceFragment();
			mFragments.add(introduceFragment);
			mFragments.add(new ProductDetailsFragment());
			mFragments.add(new ProductCommentFragment());
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}
		
	}
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mProductId = intent.getLongExtra(ProductListActivity.TODETAILSKEY,0);
		if(mProductId==0){
			tip("数据异常!");
			finish();
		}
	}
	@Override
	public void onClick(View v) {
		defaultIndicator();
		switch(v.getId()){
			case R.id.productdetails_goback_iv:
				finish();
				break;
			case R.id.introduce_ll://商品简介
				mIntroduceView.setVisibility(View.VISIBLE);
				mContainerVp.setCurrentItem(0);
				break;
			case R.id.details_ll://商品详情
				mDetailsView.setVisibility(View.VISIBLE);
				mContainerVp.setCurrentItem(1);
				break;
			case R.id.comment_ll://商品评论
				mCommentView.setVisibility(View.VISIBLE);
				mContainerVp.setCurrentItem(2);
				break;
		}
	}
	private void defaultIndicator() {
		mIntroduceView.setVisibility(View.INVISIBLE);
		mDetailsView.setVisibility(View.INVISIBLE);
		mCommentView.setVisibility(View.INVISIBLE);
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int position) {
		defaultIndicator();
		switch(position){
			case 0:
				mIntroduceView.setVisibility(View.VISIBLE);
				break;
			case 1:
				mDetailsView.setVisibility(View.VISIBLE);
				break;
			case 2:
				mCommentView.setVisibility(View.VISIBLE);
				break;
		}
	}
	public void add2ShopCar(View v){
		if(mBuyCount==0){
			tip("请设置购买的数量");
			return;
		}
		if(mProductVersion.equals("")){
			tip("请设置购买的型号");
			return;
		}
		mController.sendAsyncMessage(IdiyMessage.ADD2SHOPCAR_ACTION,
				mProductId,mBuyCount,mProductVersion);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

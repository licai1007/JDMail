package com.m520it.jdmail.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.ProductDetailsActivity;
import com.m520it.jdmail.activity.ProductListActivity;
import com.m520it.jdmail.adapter.RecommendAdapter;
import com.m520it.jdmail.adapter.SecondKillAdapter;
import com.m520it.jdmail.bean.Banner;
import com.m520it.jdmail.bean.RRecommendProduct;
import com.m520it.jdmail.bean.RSecondKill;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.controller.HomeController;
import com.m520it.jdmail.ui.HorizontalListView;
import com.m520it.jdmail.util.FixedViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class HomeFragment extends BaseFragment implements OnPageChangeListener, OnItemClickListener{
	private ViewPager mAdVp;
	private LinearLayout mIndicatorLl;
	private ADadapter mAdAdapter;
	private Timer mTimer;
	private HorizontalListView mSecondKillHlv;
	private SecondKillAdapter mSecondKillAdapter;
	private GridView mRecommendGv;
	private RecommendAdapter mRecommendAdapter;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
		case IdiyMessage.GET_BANNER_ACTION_RESULT:
			//处理广告结果
			handleBannerResult((List<Banner>)msg.obj);
			break;
		case IdiyMessage.SECOND_KILL_ACTION_RESULT:
			handleSecondKillResult((List<RSecondKill>) msg.obj);
			break;
		case IdiyMessage.RECOMMEND_PRODUCT_ACTION_RESULT:
			handleRecommendProductResult((List<RRecommendProduct>)msg.obj);
			break;
		}
	}
	private void handleRecommendProductResult(List<RRecommendProduct> datas){
		mRecommendAdapter.setDatas(datas);
		mRecommendAdapter.notifyDataSetChanged();
		//item已经有了，接下来就是计算高度
		FixedViewUtil.setGridViewHeightBasedOnChildren(mRecommendGv,2);
	}
	private void handleSecondKillResult(List<RSecondKill> datas){
		mSecondKillAdapter.setDatas(datas);
		mSecondKillAdapter.notifyDataSetChanged();
	}
	private void handleBannerResult(final List<Banner> datas){
		if(datas.size()!=0){
			mAdAdapter.setDatas(datas);
			mAdAdapter.notifyDataSetChanged();
			//设置指示器
			initBannerIndicator(datas);
			//启动一个定时器
			initBannerTimer(datas);
		}
	}
	private void initBannerTimer(final List<Banner> datas) {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				changeBannerItem(datas);
			}
		},3000,3000);
	}
	private void initBannerIndicator(final List<Banner> datas) {
		for (int i = 0; i < datas.size(); i++) {
			View view = new View(getActivity());
			//720*1280  40px=20dp
			LayoutParams params = new LinearLayout.LayoutParams(15,15);
			params.setMargins(10,0,0,0);
			view.setLayoutParams(params);
			view.setBackgroundResource(R.drawable.ad_indicator_bg);
			view.setEnabled(i==0);
			mIndicatorLl.addView(view);
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mTimer!=null){
			mTimer.cancel();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.GET_BANNER_ACTION,1);
		mController.sendAsyncMessage(IdiyMessage.SECOND_KILL_ACTION,0);
		mController.sendAsyncMessage(IdiyMessage.RECOMMEND_PRODUCT_ACTION,0);
	}
	@Override
	protected void initController() {
		mController = new HomeController(getActivity());
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mAdVp = (ViewPager)getActivity().findViewById(R.id.ad_vp);
		mAdVp.setOnPageChangeListener(this);
		mIndicatorLl = (LinearLayout)getActivity().findViewById(R.id.ad_indicator);
		mAdAdapter = new ADadapter();
		mAdVp.setAdapter(mAdAdapter);
		//初始化横向的ListView
		mSecondKillHlv = (HorizontalListView)getActivity().findViewById(R.id.horizon_listview);
		mSecondKillAdapter = new SecondKillAdapter(getActivity());
		mSecondKillHlv.setAdapter(mSecondKillAdapter);
		//猜你喜欢，推荐商品的模块
		mRecommendGv = (GridView)getActivity().findViewById(R.id.recommend_gv);
		mRecommendAdapter = new RecommendAdapter(getActivity());
		mRecommendGv.setAdapter(mRecommendAdapter);
		mRecommendGv.setOnItemClickListener(this);
	}
	public class ADadapter extends PagerAdapter{
		private List<Banner> mDatas;
		private List<SmartImageView> mChildViews;
		@Override
		public int getCount() {
			return mDatas!=null?mDatas.size():0;
		}

		public void setDatas(List<Banner> datas) {
			mDatas = datas;
			mChildViews = new ArrayList<SmartImageView>(mDatas.size());
			for(Banner banner:mDatas){
				SmartImageView siv = new SmartImageView(getActivity());
				//LayoutParams添加到哪个容器，就应该使用哪个容器的LayoutParams
				siv.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				siv.setScaleType(ScaleType.FIT_CENTER);
				siv.setImageUrl(NetworkConst.BASE_URL+banner.getAdUrl());
				mChildViews.add(siv);
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			SmartImageView siv = mChildViews.get(position);
			container.addView(siv);
			return siv;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			SmartImageView siv = mChildViews.get(position);
			container.removeView(siv);
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
		//修改指示器的样式
		int childCount = mIndicatorLl.getChildCount();
		for(int i=0;i<childCount;i++){
			mIndicatorLl.getChildAt(i).setEnabled(i==arg0);
		}
	}
	private void changeBannerItem(final List<Banner> datas) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				int item = mAdVp.getCurrentItem();
				item++;
				if(item>datas.size()-1){
					item = 0;
				}
				mAdVp.setCurrentItem(item);
			}
		});
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		long pId = mRecommendAdapter.getItemId(position);
		Intent intent = new Intent(getActivity(),ProductDetailsActivity.class);
		intent.putExtra(ProductListActivity.TODETAILSKEY,pId);
		startActivity(intent);
	}	
}

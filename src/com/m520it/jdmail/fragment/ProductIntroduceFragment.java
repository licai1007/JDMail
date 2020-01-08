package com.m520it.jdmail.fragment;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.ProductDetailsActivity;
import com.m520it.jdmail.adapter.GoodCommentAdapter;
import com.m520it.jdmail.adapter.ProductAdAdapter;
import com.m520it.jdmail.adapter.TypeListAdapter;
import com.m520it.jdmail.bean.RGoodComment;
import com.m520it.jdmail.bean.RProductInfo;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ProductDetailsController;
import com.m520it.jdmail.listener.INumberInputListener;
import com.m520it.jdmail.ui.NumberInputView;
import com.m520it.jdmail.util.FixedViewUtil;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ProductIntroduceFragment extends BaseFragment implements OnItemClickListener, INumberInputListener{
	private ViewPager mAdVp;
	private ProductAdAdapter mAdAdapter;
	private ProductDetailsActivity mActivity;
	private TextView mAdIndicator;
	private Timer mTimer;
	private TextView mNameTv;
	private TextView mSelfSaleTv;
	private TextView mRecommendTv;
	private ListView mProductVersionsLv;
	private TypeListAdapter mTypeListAdapter;
	private NumberInputView mNumberInputView;
	private TextView mGoodRateTv;
	private TextView mGoodCommentTv;
	private TextView mPriceTv;
	private ListView mGoodCommentLv;
	private GoodCommentAdapter mGoodCommentAdapter;
	@Override
	protected void handlerMessage(Message msg) {
		switch(msg.what){
			case IdiyMessage.PRODUCT_INFO_ACTION_RESULT:
				handleProductInfo(msg.obj);
				break;
			case IdiyMessage.GOOD_COMMENT_ACTION_RESULT:
				handleGoodComment((List<RGoodComment>)msg.obj);
				break;
		}
	}
	private void handleGoodComment(List<RGoodComment> datas){
		mGoodCommentAdapter.setDatas(datas);
		mGoodCommentAdapter.notifyDataSetChanged();
		FixedViewUtil.setListViewHeightBasedOnChildren(mGoodCommentLv);
	}
	private void handleProductInfo(Object obj){
		if(obj==null){
			tip("数据异常!");
			mActivity.finish();
			return;
		}
		RProductInfo bean = (RProductInfo)obj;
		handleAdBanner(bean.getImgUrls());
		mNameTv.setText(bean.getName());
		mSelfSaleTv.setVisibility(bean.isIfSaleOneself()?View.VISIBLE:View.INVISIBLE);
		mRecommendTv.setText(bean.getRecomProduct());
		mPriceTv.setText("￥ "+bean.getPrice());
		handleTypeListLv(bean.getTypeList());
		mNumberInputView.setMax(bean.getStockCount());
		mGoodRateTv.setText(bean.getFavcomRate()+"%好评");
		mGoodCommentTv.setText(bean.getCommentCount()+"人评论");
	}
	private void handleTypeListLv(String typeListJson) {
		List<String> typeList = JSON.parseArray(typeListJson,String.class);
		mTypeListAdapter.setDatas(typeList);
		mTypeListAdapter.notifyDataSetChanged();
		FixedViewUtil.setListViewHeightBasedOnChildren(mProductVersionsLv);
	}
	/**
	 * 处理广告图片信息
	 */
	private void handleAdBanner(String imgUrls) {
		final List<String> imgUrList = JSON.parseArray(imgUrls,String.class);
		mAdAdapter.setDatas(getActivity(),imgUrList);
		mAdAdapter.notifyDataSetChanged();
		mAdIndicator.setText(1+"/"+imgUrList.size());
		
		initAdBannerTimer(imgUrList);
	}
	private void initAdBannerTimer(final List<String> imgUrList) {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				translateAdBannerItem(imgUrList);
			}
		},3*1000,3*1000);
	}
	/**
	 * 指定广告的item
	 */
	private void translateAdBannerItem(final List<String> imgUrList) {
		if(imgUrList!=null&&imgUrList.size()!=0){
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					int currentItem = mAdVp.getCurrentItem();
					currentItem++;
					if(currentItem>imgUrList.size()-1){
						currentItem = 0;
					}
					mAdVp.setCurrentItem(currentItem);
					mAdIndicator.setText((currentItem+1)+"/"+imgUrList.size());
				}
			});
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mTimer!=null){
			mTimer.cancel();
			mTimer = null;
		}
	}
	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_introduce,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	initData();
    	initController();
    	initUI();
    	mController.sendAsyncMessage(IdiyMessage.PRODUCT_INFO_ACTION,mActivity.mProductId);
    	mController.sendAsyncMessage(IdiyMessage.GOOD_COMMENT_ACTION,mActivity.mProductId);
    }
    private void initData() {
    	mActivity = (ProductDetailsActivity)getActivity();
	}
	@Override
    protected void initController() {
    	mController = new ProductDetailsController(getActivity());
    	mController.setIModeChangeListener(this);
    }
	@Override
	protected void initUI() {
		mAdVp = (ViewPager)getActivity().findViewById(R.id.advp);
		mAdAdapter = new ProductAdAdapter();
		mAdVp.setAdapter(mAdAdapter);
		mAdIndicator = (TextView)getActivity().findViewById(R.id.vp_indic_tv);
		mNameTv = (TextView)getActivity().findViewById(R.id.name_tv);
		mSelfSaleTv = (TextView)getActivity().findViewById(R.id.self_sale_tv);
		mRecommendTv = (TextView)getActivity().findViewById(R.id.recommend_p_tv);
		mProductVersionsLv = (ListView)getActivity().findViewById(R.id.product_versions_lv);
		mTypeListAdapter = new TypeListAdapter(getActivity());
		mProductVersionsLv.setAdapter(mTypeListAdapter);
		mProductVersionsLv.setOnItemClickListener(this);
		mNumberInputView = (NumberInputView)getActivity().findViewById(R.id.number_input_et);
		mNumberInputView.setListener(this);
		mGoodRateTv = (TextView)getActivity().findViewById(R.id.good_rate_tv);
		mGoodCommentTv = (TextView)getActivity().findViewById(R.id.good_comment_tv);
		mGoodCommentLv = (ListView)getActivity().findViewById(R.id.good_comment_lv);
		mGoodCommentAdapter = new GoodCommentAdapter(getActivity());
		mGoodCommentLv.setAdapter(mGoodCommentAdapter);
		mPriceTv = (TextView)getActivity().findViewById(R.id.price_tv);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mTypeListAdapter.mCurrentTabPosition = position;
		mTypeListAdapter.notifyDataSetChanged();
		//获取选中的item数据
		String data = (String)mTypeListAdapter.getItem(position);
		//赋值给Activity.mProductVersion
		mActivity.mProductVersion = data;
	}
	//只要购买的数量改变(包括加减，输入的文本)--->调用该方法
	@Override
	public void onTextChange(int num) {
		//修改Activity里面的数据
		mActivity.mBuyCount = num;
	}

}

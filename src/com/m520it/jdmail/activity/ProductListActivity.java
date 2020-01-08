package com.m520it.jdmail.activity;

import java.util.List;
import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.BrandAdapter;
import com.m520it.jdmail.adapter.ProductListAdapter;
import com.m520it.jdmail.bean.RBrand;
import com.m520it.jdmail.bean.RProductList;
import com.m520it.jdmail.bean.SProductList;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.CategoryController;
import com.m520it.jdmail.listener.IProductSortChanegListener;
import com.m520it.jdmail.ui.FlexiScrollView;
import com.m520it.jdmail.ui.SubCategoryView;
import com.m520it.jdmail.ui.pop.ProductSortPopWindow;
import com.m520it.jdmail.util.FixedViewUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListActivity extends BaseActivity implements OnClickListener,IProductSortChanegListener,OnItemClickListener{
	private long mCategoryId;
	private long cid;
	private GridView mBrandGv;
	private BrandAdapter mBrandAdapter;
	private SProductList mSendArgs;
	private TextView mAllIndicator;
	private TextView mSaleIndicator;
	private TextView mPriceIndicator;
	private ProductSortPopWindow mSortPopWindow;
	private TextView mJdTakeTv;
	private TextView mPayWhenReceiveTv;
	private TextView mJustHasStockTv;
	private TextView mChooseIndicator;
	private DrawerLayout mDrawerLayout;
	private FlexiScrollView mSlideView;
	private EditText mMinPriceEt;
	private EditText mMaxPriceEt;
	private ListView mProductLv;
	private ProductListAdapter mProductListAdapter;
	private ImageView mProductListGobackIv;
	public static final String TODETAILSKEY = "TODETAILSKEY";
	@Override
	protected void handlerMessage(Message msg) {
		switch(msg.what){
			case IdiyMessage.BRAND_ACTION_RESULT:
				handleBrands((List<RBrand>)msg.obj);
				break;
			case IdiyMessage.PRODUCT_LIST_ACTION_RESULT:
				handleProductList((List<RProductList>)msg.obj);
				break;
		}
	}
	private void handleProductList(List<RProductList> datas){
		mProductListAdapter.setDatas(datas);
		mProductListAdapter.notifyDataSetChanged();
	}
	//处理品牌的信息
	private void handleBrands(List<RBrand> brands){
		mBrandAdapter.setDatas(brands);
		mBrandAdapter.notifyDataSetChanged();
		//重置品牌列表的高度
		FixedViewUtil.setGridViewHeightBasedOnChildren(mBrandGv,3);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		initData();
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.BRAND_ACTION,cid);
		mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
	}
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mCategoryId = intent.getLongExtra(SubCategoryView.TOPRODUCTLISTKEY,0);
		cid = intent.getLongExtra(SubCategoryView.TOPCATEGORY_ID,0);
		if(mCategoryId==0||cid==0){
			tip("数据异常!");
			finish();
		}
		mSendArgs = new SProductList();
		mSendArgs.setCategoryId(mCategoryId);
	}
	@Override
	protected void initController() {
		mController = new CategoryController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
		initMainUI();
		initSlideUI();
	}
	private void initSlideUI() {
		mSlideView = (FlexiScrollView)findViewById(R.id.slide_view);
		
		mJdTakeTv = (TextView)findViewById(R.id.jd_take_tv);
		mJdTakeTv.setOnClickListener(this);
		mPayWhenReceiveTv = (TextView)findViewById(R.id.paywhenreceive_tv);
		mPayWhenReceiveTv.setOnClickListener(this);
		mJustHasStockTv = (TextView)findViewById(R.id.justhasstock_tv);
		mJustHasStockTv.setOnClickListener(this);
		
		mMinPriceEt = (EditText)findViewById(R.id.minPrice_et);
		mMaxPriceEt = (EditText)findViewById(R.id.maxPrice_et);
		
		mBrandGv = (GridView)findViewById(R.id.brand_gv);
		mBrandAdapter = new BrandAdapter(this);
		mBrandGv.setAdapter(mBrandAdapter);
		mBrandGv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mBrandAdapter.mCurrentTabPosition = position;
				mBrandAdapter.notifyDataSetChanged();
				//点击记录下品牌的id
			}
		});
	}
	/**
	 * 确定按钮
	 */
	public void chooseSearchClick(View v){
		//确定选择服务
		int deliverChoose = 0;
		if(mJdTakeTv.isSelected()){
			deliverChoose += 1;
		}
		if(mPayWhenReceiveTv.isSelected()){
			deliverChoose += 2;
		}
		if(mJustHasStockTv.isSelected()){
			deliverChoose += 4;
		}
		mSendArgs.setDeliverChoose(deliverChoose);
		//价格区间
		String minPriceStr = mMinPriceEt.getText().toString();
		String maxPriceStr = mMaxPriceEt.getText().toString();
		if(!TextUtils.isEmpty(maxPriceStr)&&!TextUtils.isEmpty(minPriceStr)){
			mSendArgs.setMinPrice(Double.parseDouble(minPriceStr));
			mSendArgs.setMaxPrice(Double.parseDouble(maxPriceStr));
		}
		//选择品牌
		if(mBrandAdapter.mCurrentTabPosition != -1){
			//获取品牌ID
			long brandId = mBrandAdapter.getItemId(mBrandAdapter.mCurrentTabPosition);
			mSendArgs.setBrandId(brandId);
		}
		mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
		mDrawerLayout.closeDrawer(mSlideView);
	}
	/**
	 * 重置按钮
	 */
	public void resetClick(View v){
		mSendArgs = new SProductList();
		mSendArgs.setCategoryId(mCategoryId);
		mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
		mDrawerLayout.closeDrawer(mSlideView);
	}
	private void initMainUI() {
		mProductListGobackIv = (ImageView)findViewById(R.id.productList_goback_iv);
		mProductListGobackIv.setOnClickListener(this);
		mAllIndicator = (TextView)findViewById(R.id.all_indicator);
		mAllIndicator.setOnClickListener(this);
		mSaleIndicator = (TextView)findViewById(R.id.sale_indicator);
		mSaleIndicator.setOnClickListener(this);
		mPriceIndicator = (TextView)findViewById(R.id.price_indicator);
		mPriceIndicator.setOnClickListener(this);
		mChooseIndicator = (TextView)findViewById(R.id.choose_indicator);
		mChooseIndicator.setOnClickListener(this);
		
		mProductLv = (ListView)findViewById(R.id.product_lv);
		mProductListAdapter = new ProductListAdapter(this);
		mProductLv.setAdapter(mProductListAdapter);
		mProductLv.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.productList_goback_iv:
				finish();
				break;
			case R.id.all_indicator:
				if(mSortPopWindow==null){
					mSortPopWindow = new ProductSortPopWindow(this);
					mSortPopWindow.setListener(this);
				}
				mSortPopWindow.onShow(v);
				break;
			case R.id.sale_indicator:
				mSendArgs.setSortType(1);
				mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
				break;
			case R.id.price_indicator:
				int sortType = mSendArgs.getSortType();
				if(sortType==0||sortType==1||sortType==3){
					mSendArgs.setSortType(2);
				}
				if(sortType==0||sortType==1||sortType==2){
					mSendArgs.setSortType(3);
				}
				mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
				break;
			case R.id.choose_indicator:
				mDrawerLayout.openDrawer(mSlideView);
				break;
			case R.id.jd_take_tv:
			case R.id.paywhenreceive_tv:
			case R.id.justhasstock_tv:
				v.setSelected(!v.isSelected());
				break;
		}
	}
	@Override
	public void onSortChanged(int action) {
		switch(action){
			case IProductSortChanegListener.ALLSORT:
				mAllIndicator.setText("综合");
				mSendArgs.setFilterType(1);
				mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
				break;
			case IProductSortChanegListener.NEWSSORT:
				mAllIndicator.setText("新品");
				mSendArgs.setFilterType(2);
				mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
				break;
			case IProductSortChanegListener.COMMENTSORT:
				mAllIndicator.setText("评价");
				mSendArgs.setFilterType(3);
				mController.sendAsyncMessage(IdiyMessage.PRODUCT_LIST_ACTION,mSendArgs);
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		long pId = mProductListAdapter.getItemId(position);
		Intent intent = new Intent(this,ProductDetailsActivity.class);
		intent.putExtra(TODETAILSKEY,pId);
		startActivity(intent);
	}

}

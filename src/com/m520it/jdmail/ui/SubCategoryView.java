package com.m520it.jdmail.ui;

import java.util.List;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.ProductListActivity;
import com.m520it.jdmail.bean.RCategory;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.controller.CategoryController;
import com.m520it.jdmail.listener.IModeChangeListener;
import com.m520it.jdmail.listener.IViewContainer;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SubCategoryView extends FlexiScrollView implements IViewContainer, IModeChangeListener, OnClickListener{
	private RCategory mCategoryBean;
	private LinearLayout mContainerLl;
	private CategoryController mCategoryController;
	private List<RCategory> mTwoCategory;
	private static final int mLinePerSize = 3;
	private int mFlagHelper = 0;
	public static String TOPRODUCTLISTKEY = "TOPRODUCTLISTKEY";//3级分类id
	public static String TOPCATEGORY_ID = "TOPCATEGORY_ID";//1级分类id
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case IdiyMessage.CATEGORY_ACTION_RESULT:
					int level = msg.arg1;//分类级别
					if(level==IdiyMessage.CategoryLevel.SECOND_REQUEST){
						mFlagHelper = 0;
						mTwoCategory = (List<RCategory>)msg.obj;
						handleTwoCategory();
					}else if(level==IdiyMessage.CategoryLevel.THIRD_REQUEST){
						handleThreeCategory((List<RCategory>)msg.obj);
					}
					break;
			}
		};
	};
	private void handleThreeCategory(final List<RCategory> cates){
		//计算行数
		int totalSize = cates.size();
		int lines = totalSize/mLinePerSize;
		if(totalSize%mLinePerSize!=0){
			lines++;
		}
		for(int i=0;i<lines;i++){
			//行的容器
			LinearLayout lineLl = new LinearLayout(getContext());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0,8,0,0);
			lineLl.setLayoutParams(params);
			//颜色值必须为8位才能显示
			//lineLl.setBackgroundColor(0xFFFF0000);
			initColumn(cates, i, lineLl);
			mContainerLl.addView(lineLl);
		}
		mFlagHelper++;
		if(mFlagHelper>mTwoCategory.size()-1){
			return;
		}
		handleTwoCategory();
	}
	private void initColumn(final List<RCategory> cates, int i,
			LinearLayout lineLl) {
		for(int j=0;j<3;j++){
			//计算3级分类的索引
			int index = i*mLinePerSize+j;
			if(index>cates.size()-1){
				break;
			}
			//列的容器
			LinearLayout partLl = new LinearLayout(getContext());
			LinearLayout.LayoutParams partParams = new LinearLayout.LayoutParams(180,220);
			partParams.setMargins(0,0,10,0);
			partLl.setLayoutParams(partParams);
			partLl.setOrientation(LinearLayout.VERTICAL);//纵向布局
			partLl.setGravity(Gravity.CENTER_HORIZONTAL);//里面的内容水平居中
			//添加图片
			RCategory cate = cates.get(index);
			String imgUrl = NetworkConst.BASE_URL + cate.getBannerUrl();
			SmartImageView bannerIv = new SmartImageView(getContext());
			LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(180,180);
			imgParams.setMargins(8,0,4,0);
			bannerIv.setLayoutParams(imgParams);
			bannerIv.setScaleType(ScaleType.FIT_XY);
			bannerIv.setImageUrl(imgUrl);
			partLl.addView(bannerIv);
			//添加商品3级分类名称
			TextView nameTv = new TextView(getContext());
			LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			nameTv.setLayoutParams(nameParams);
			nameTv.setText(cate.getName());
			partLl.addView(nameTv);
			partLl.setOnClickListener(this);
			partLl.setTag(cate);
			lineLl.addView(partLl);
		}
	}
	private void handleTwoCategory(){
		if(mTwoCategory.size()==0){
			return;
		}
		RCategory category = mTwoCategory.get(mFlagHelper);
		initSecondCategoryNameTv(category);
		//添加3级分类
		long id = category.getId();//3级分类父id
		//请求3级分类里面的数据
		mCategoryController.sendAsyncMessage(IdiyMessage.CATEGORY_ACTION,id,IdiyMessage.CategoryLevel.THIRD_REQUEST);
	}
	/**
	 * 添加2级分类名称
	 */
	private void initSecondCategoryNameTv(RCategory bean) {
		TextView secondCategoryNameTv = new TextView(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(8,16,0,4);
		secondCategoryNameTv.setLayoutParams(params);
		secondCategoryNameTv.setText(bean.getName());
		mContainerLl.addView(secondCategoryNameTv);
	}
	public SubCategoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initController();
		initUI();
	}
	private void initController() {
		mCategoryController = new CategoryController(getContext());
		mCategoryController.setIModeChangeListener(this);
	}
	private void initUI() {
		mContainerLl = (LinearLayout)findViewById(R.id.child_container_ll);
	}
	@Override
	public void onShow(Object... values) {
		mCategoryBean = (RCategory)values[0];
		//清空容器
		mContainerLl.removeAllViews();
		//往容器里面添加一张广告图
		initBannerIv();
		//请求2级分类里面的数据
		mCategoryController.sendAsyncMessage(IdiyMessage.CATEGORY_ACTION,mCategoryBean.getId(),IdiyMessage.CategoryLevel.SECOND_REQUEST);
	}
	private void initBannerIv() {
		String imgUrl = NetworkConst.BASE_URL + mCategoryBean.getBannerUrl();
		SmartImageView bannerIv = new SmartImageView(getContext());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(8,8,8,8);
		bannerIv.setLayoutParams(params);
		bannerIv.setScaleType(ScaleType.FIT_XY);
		bannerIv.setImageUrl(imgUrl);
		mContainerLl.addView(bannerIv);
	}
	@Override
	public void onModeChanged(int action, Object... values) {
		Message message = mHandler.obtainMessage(action,values[0]);
		message.arg1 = (Integer)values[1];
		message.sendToTarget();
	}
	@Override
	public void onClick(View view) {
		//当点击3级分类，需要传递3级分类数据给商品列表页
		RCategory cate = (RCategory)view.getTag();
		Intent intent = new Intent(getContext(),ProductListActivity.class);
		intent.putExtra(TOPRODUCTLISTKEY,cate.getId());
		intent.putExtra(TOPCATEGORY_ID,mCategoryBean.getId());
		getContext().startActivity(intent);
	}
	

}

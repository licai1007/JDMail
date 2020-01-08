package com.m520it.jdmail.fragment;
import java.util.List;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.ProductDetailsActivity;
import com.m520it.jdmail.adapter.CommentAdapter;
import com.m520it.jdmail.bean.RCommentCount;
import com.m520it.jdmail.bean.RProductComment;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ProductDetailsController;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class ProductCommentFragment extends BaseFragment implements OnClickListener {
	private ProductDetailsActivity mActivity;
	private TextView mAllCommentTip;
	private TextView mAllCommentTv;
	private TextView mPositiveCommentTip;
	private TextView mPositiveCommentTv;
	private TextView mCenterCommentTip;
	private TextView mCenterCommentTv;
	private TextView mNagetiveCommentTip;
	private TextView mNagetiveCommentTv;
	private TextView mHasImageCommentTip;
	private TextView mHasImageCommentTv;
	private ListView mCommentLv;
	private CommentAdapter mCommentAdapter;
	
	public static final int ALL_COMMENT = 0;//全部评论
	public static final int GOOD_COMMENT = 1;//好评
	public static final int CENTER_COMMENT = 2;//中评
	public static final int BAD_COMMENT = 3;//差评
	public static final int HASIMG_COMMENT = 4;//有图
	
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.GET_COMMENT_COUNT_ACTION_RESULT:
				handleCommentCount(msg.obj);
				break;
			case IdiyMessage.GET_COMMENT_ACTION_RESULT:
				handleComment((List<RProductComment>)msg.obj);
				break;
		}
	}
	private void handleComment(List<RProductComment> datas){
		mCommentAdapter.setDatas(datas);
		mCommentAdapter.notifyDataSetChanged();
	}
	private void handleCommentCount(Object obj){
		if(obj!=null){
			RCommentCount bean = (RCommentCount)obj;
			mAllCommentTv.setText(bean.getAllComment()+"");
			mPositiveCommentTv.setText(bean.getPositiveCom()+"");
			mCenterCommentTv.setText(bean.getModerateCom()+"");
			mNagetiveCommentTv.setText(bean.getNegativeCom()+"");
			mHasImageCommentTv.setText(bean.getHasImgCom()+"");
		}
	}
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_comment,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	mActivity = (ProductDetailsActivity)getActivity();
    	initController();
    	initUI();
    	mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_COUNT_ACTION,mActivity.mProductId);
    }
    @Override
    protected void initController() {
    	mController = new ProductDetailsController(mActivity);
    	mController.setIModeChangeListener(this);
    }
	@Override
	protected void initUI() {
		mAllCommentTip = (TextView)mActivity.findViewById(R.id.all_comment_tip);
		mAllCommentTv = (TextView)mActivity.findViewById(R.id.all_comment_tv);
		mPositiveCommentTip = (TextView)mActivity.findViewById(R.id.positive_comment_tip);
		mPositiveCommentTv = (TextView)mActivity.findViewById(R.id.positive_comment_tv);
		mCenterCommentTip = (TextView)mActivity.findViewById(R.id.center_comment_tip);
		mCenterCommentTv = (TextView)mActivity.findViewById(R.id.center_comment_tv);
		mNagetiveCommentTip = (TextView)mActivity.findViewById(R.id.nagetive_comment_tip);
		mNagetiveCommentTv = (TextView)mActivity.findViewById(R.id.nagetive_comment_tv);
		mHasImageCommentTip = (TextView)mActivity.findViewById(R.id.has_image_comment_tip);
		mHasImageCommentTv = (TextView)mActivity.findViewById(R.id.has_image_comment_tv);
		
		mActivity.findViewById(R.id.all_comment_ll).setOnClickListener(this);
		mActivity.findViewById(R.id.positive_comment_ll).setOnClickListener(this);
		mActivity.findViewById(R.id.center_comment_ll).setOnClickListener(this);
		mActivity.findViewById(R.id.nagetive_comment_ll).setOnClickListener(this);
		mActivity.findViewById(R.id.has_image_comment_ll).setOnClickListener(this);
		mActivity.findViewById(R.id.all_comment_ll).performClick();
		
		mCommentLv = (ListView)mActivity.findViewById(R.id.comment_lv);
		mCommentAdapter = new CommentAdapter(mActivity);
		mCommentLv.setAdapter(mCommentAdapter);
	}
	@Override
	public void onClick(View v) {
		defaultIndicator();
		switch (v.getId()) {
			case R.id.all_comment_ll:
				mAllCommentTip.setSelected(true);
				mAllCommentTv.setSelected(true);
				mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_ACTION,
		    			mActivity.mProductId,ALL_COMMENT);
				break;
			case R.id.positive_comment_ll:
				mPositiveCommentTip.setSelected(true);
				mPositiveCommentTv.setSelected(true);
				mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_ACTION,
		    			mActivity.mProductId,GOOD_COMMENT);
				break;
			case R.id.center_comment_ll:
				mCenterCommentTip.setSelected(true);
				mCenterCommentTv.setSelected(true);
				mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_ACTION,
		    			mActivity.mProductId,CENTER_COMMENT);
				break;
			case R.id.nagetive_comment_ll:
				mNagetiveCommentTip.setSelected(true);
				mNagetiveCommentTv.setSelected(true);
				mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_ACTION,
		    			mActivity.mProductId,BAD_COMMENT);
				break;
			case R.id.has_image_comment_ll:
				mHasImageCommentTip.setSelected(true);
				mHasImageCommentTv.setSelected(true);
				mController.sendAsyncMessage(IdiyMessage.GET_COMMENT_ACTION,
		    			mActivity.mProductId,HASIMG_COMMENT);
				break;
			}
	}
	private void defaultIndicator() {
		mAllCommentTip.setSelected(false);
		mAllCommentTv.setSelected(false);
		mPositiveCommentTip.setSelected(false);
		mPositiveCommentTv.setSelected(false);
		mCenterCommentTip.setSelected(false);
		mCenterCommentTv.setSelected(false);
		mNagetiveCommentTip.setSelected(false);
		mNagetiveCommentTv.setSelected(false);
		mHasImageCommentTip.setSelected(false);
		mHasImageCommentTv.setSelected(false);
	}
}

package com.m520it.jdmail.fragment;

import com.m520it.jdmail.JDApplication;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.LoginActivity;
import com.m520it.jdmail.activity.OrderListActivity;
import com.m520it.jdmail.bean.RLoginResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.UserController;
import com.m520it.jdmail.util.ActivityUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MineFragment extends BaseFragment implements OnClickListener{
	private TextView mUserNameTv;
	private TextView mUserLevelTv;
	private TextView mWaitPayTv;
	private TextView mWaitReceiveTv;
	private LinearLayout mMimeOrderLl;
	@Override
	protected void handlerMessage(Message msg) {
		switch(msg.what){
			case IdiyMessage.CLEAR_USER_ACTION_RESULT:
				ActivityUtil.start(getActivity(),LoginActivity.class,true);
				break;
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mine,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initController();
		initUI();
	}
	@Override
	protected void initController() {
		mController = new UserController(getActivity());
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		getActivity().findViewById(R.id.logout_btn).setOnClickListener(this);
		//找到所有的用户控件
		mUserNameTv = (TextView)getActivity().findViewById(R.id.user_name_tv);
		mUserLevelTv = (TextView)getActivity().findViewById(R.id.user_level_tv);
		mWaitPayTv = (TextView)getActivity().findViewById(R.id.wait_pay_tv);
		mWaitReceiveTv = (TextView)getActivity().findViewById(R.id.wait_receive_tv);
		//执行一个后台请求更新控件
		JDApplication application = (JDApplication)getActivity().getApplication();
		RLoginResult rLoginResult = application.mRLoginResult;
		mUserNameTv.setText(rLoginResult.getUserName());
		initUserLevel(rLoginResult);
		mWaitPayTv.setText(rLoginResult.getWaitPayCount()+"");
		mWaitReceiveTv.setText(rLoginResult.getWaitReceiveCount()+"");
		//初始化控件
		mMimeOrderLl = (LinearLayout)getActivity().findViewById(R.id.mime_order);
		mMimeOrderLl.setOnClickListener(this);
		
		getActivity().findViewById(R.id.wait_pay_ll).setOnClickListener(this);
		getActivity().findViewById(R.id.wait_receive_ll).setOnClickListener(this);
	}
	private void initUserLevel(RLoginResult rLoginResult) {
		String text = "";
		switch(rLoginResult.getUserLevel()){
			case 1:
				text = "注册会员";
				break;
			case 2:
				text = "铜牌会员";
				break;
			case 3:
				text = "银牌会员";
				break;
			case 4:
				text = "金牌会员";
				break;
			case 5:
				text = "钻石会员";
				break;
		}
		mUserLevelTv.setText(text);
	}
	
	public static final String WAIT = "wait";
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.logout_btn:
				mController.sendAsyncMessage(IdiyMessage.CLEAR_USER_ACTION,0);
				break;
			case R.id.mime_order:
				//跳转
				ActivityUtil.start(getActivity(),OrderListActivity.class,false);
				break;
			case R.id.wait_pay_ll:
				Intent intent = new Intent(getActivity(),OrderListActivity.class);
				intent.putExtra(WAIT,-1);
				startActivity(intent);
				break;
			case R.id.wait_receive_ll:
				Intent it = new Intent(getActivity(),OrderListActivity.class);
				it.putExtra(WAIT,-2);
				startActivity(it);
				break;
		}
	}
	
	
	
	
	
	
	
	
}

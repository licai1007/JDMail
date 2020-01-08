package com.m520it.jdmail.fragment;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.SettleActivity;
import com.m520it.jdmail.adapter.ShopCarAdapter;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.bean.RShopCar;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ShopCarController;
import com.m520it.jdmail.listener.IShopcarCheckChangeListener;
import com.m520it.jdmail.listener.IShopcarDeleteListener;
import com.m520it.jdmail.ui.pop.LoadingDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

public class ShopcarFragment extends BaseFragment implements 
						OnItemClickListener,IShopcarCheckChangeListener,
						OnCheckedChangeListener,IShopcarDeleteListener, OnClickListener{
	private ListView mShopCarLv;
	private ShopCarAdapter mShopCarAdapter;
	private CheckBox mAllCbx;
	private TextView mAllMoneyTv;
	private TextView mSettleTv;
	private double mTotalPrice;
	private LoadingDialog mLoadingDialog;
	private View mNullView;
	public static final String CHECKEDDATAS = "checkeddatas";
	public static final String CHECKTOTALPRICE = "checktotalprice";
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.SHOPCAR_LIST_ACTION_RESULT:
				handleLoadShopcar((List<RShopCar>)msg.obj);
				break;
			case IdiyMessage.DELETE_SHOPCAR_ACTION_RESULT:
				handleDelShopcar((RResult)msg.obj);
				break;
		}
	}
	private void handleDelShopcar(RResult result){
		if(result.isSuccess()){
			//两种方法：
			//1.告诉Adapter删除某个item的数据(找到mDatas去删除，对应的sItemChecked也应该删除索引，外部的提示信息也要更新)
			//2.重新请求数据
			mController.sendAsyncMessage(IdiyMessage.SHOPCAR_LIST_ACTION,0);
		}else{
			tip("删除失败:"+result.getErrorMsg());
		}
	}
	private void handleLoadShopcar(List<RShopCar> datas){
		if(datas.size()==0){
			mNullView.setVisibility(View.VISIBLE);
			mShopCarLv.setVisibility(View.GONE);
		}else{
			mNullView.setVisibility(View.GONE);
			mShopCarLv.setVisibility(View.VISIBLE);
			mShopCarAdapter.setDatas(datas);
			mShopCarAdapter.notifyDataSetChanged();
		}
		//界面刷新后，重新修改两个提示
		mAllMoneyTv.setText("总额: ￥ 0");
		mSettleTv.setText("去结算(0)");
		mAllCbx.setChecked(false);
		//隐藏对话框
		mLoadingDialog.hide();
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Subscribe(threadMode=ThreadMode.MAIN)
	public void onAction(Integer action){
		if(action==IdiyMessage.SHOPCAR_LIST_ACTION){
			//重新请求数据
			mController.sendAsyncMessage(IdiyMessage.SHOPCAR_LIST_ACTION,0);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shopcar,container,false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initController();
		initUI();
		//弹出一个加载框
		mLoadingDialog.show();
		mController.sendAsyncMessage(IdiyMessage.SHOPCAR_LIST_ACTION,0);
	}
	@Override
	protected void initController() {
		mController = new ShopCarController(getActivity());
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mLoadingDialog = new LoadingDialog(getActivity());
		mNullView = getActivity().findViewById(R.id.null_view);
		mShopCarLv = (ListView)getActivity().findViewById(R.id.shopcar_lv);
		mShopCarAdapter = new ShopCarAdapter(getActivity());
		mShopCarAdapter.setIShopcarCheckChangeListener(this);
		mShopCarAdapter.setIShopcarDeleteListener(this);
		mShopCarLv.setAdapter(mShopCarAdapter);
		mShopCarLv.setOnItemClickListener(this);
		mAllCbx = (CheckBox)getActivity().findViewById(R.id.all_cbx);
		mAllCbx.setOnCheckedChangeListener(this);
		mAllMoneyTv = (TextView)getActivity().findViewById(R.id.all_money_tv);
		mSettleTv = (TextView)getActivity().findViewById(R.id.settle_tv);
		mSettleTv.setOnClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mShopCarAdapter.setCheck(position);
	}
	@Override
	public void onBuyCountChanged(int count) {
		//修改TextView
		mSettleTv.setText("去结算("+count+")");
	}
	//全选按钮的监听器
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		mShopCarAdapter.checkAll(isChecked);
	}
	@Override
	public void onTotalPriceChanged(double newestPrice) {
		mTotalPrice = newestPrice;
		mAllMoneyTv.setText("总额: ￥ "+newestPrice);
	}
	@Override
	public void onItemDelete(long shopcarId) {
		//发送删除购物车
		mController.sendAsyncMessage(IdiyMessage.DELETE_SHOPCAR_ACTION,shopcarId);
	}
	//结算的按钮
	@Override
	public void onClick(View view) {
		//判断是否选中
		if(!mShopCarAdapter.ifItemChecked()){
			tip("请选择购买的商品!");
			return;
		}
		ArrayList<RShopCar> checkedDatas = mShopCarAdapter.getCheckedItem();
		Intent intent = new Intent(getActivity(),SettleActivity.class);
		intent.putExtra(CHECKEDDATAS,checkedDatas);
		intent.putExtra(CHECKTOTALPRICE,mTotalPrice);
		startActivity(intent);
	}
}

package com.m520it.jdmail.activity;

import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;

import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.ROrderParam;
import com.m520it.jdmail.bean.RReceiver;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.bean.RShopCar;
import com.m520it.jdmail.bean.SAddOrderParam;
import com.m520it.jdmail.bean.SAddOrderProductParam;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.controller.ShopCarController;
import com.m520it.jdmail.fragment.ShopcarFragment;
import com.m520it.jdmail.listener.IAddOrderConfirmListener;
import com.m520it.jdmail.listener.IPayOnlineConfirmListener;
import com.m520it.jdmail.ui.pop.PayOnlineDialog;
import com.m520it.jdmail.ui.pop.PayWhenGetDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettleActivity extends BaseActivity 
						implements OnClickListener,IAddOrderConfirmListener,
						IPayOnlineConfirmListener{
	private RelativeLayout mHasReceiverRl;
	private TextView mReceiverNameTv;
	private TextView mReceiverPhoneTv;
	private TextView mReceiverAddressTv;
	private RelativeLayout mNoReceiverRl;
	private static final int ADD_RECEIVER_REQ = 0x001;
	private static final int CHOOSE_RECEIVER_REQ = 0x002;
	private ArrayList<RShopCar> mCheckedDatas;
	private LinearLayout mProductContainerLl;
	private TextView mTotalSizeTv;
	private double mTotalPrice;
	private TextView mAllPriceTv;
	private TextView mPayMoneyTv;
	private Button mPayOnlineTv;
	private Button mPayWhengetTv;
	private long mAddressId;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.GET_DEFAULT_RECEIVER_ACTION_RESULT:
				handleDefaultReceiver(msg.obj);
				break;
			case IdiyMessage.ADD_ORDER_ACTION_RESULT:
				handleAddOrder((RResult)msg.obj);
				break;
		}
	}
	//处理下单的结果,成功的情况下会弹出一个对话框
	private void handleAddOrder(RResult result){
		if(result.isSuccess()){
			//取出数据
			ROrderParam bean = JSON.parseObject(result.getResult(),ROrderParam.class);
			//判断是哪种支付方式
			if(bean.getPayWay()==0){
				//在线支付
				PayOnlineDialog addOrderDialog = new PayOnlineDialog(this,bean);
				addOrderDialog.setIPayOnlineConfirmListener(this);
				addOrderDialog.show();
			}else if(bean.getPayWay()==1){
				//货到付款
				PayWhenGetDialog addOrderDialog = new PayWhenGetDialog(this,bean);
				addOrderDialog.setIAddOrderConfirmListener(this);
				addOrderDialog.show();
			}
			//删除购物车中已经下单的项
			for(RShopCar sc:mCheckedDatas){
				mController.sendAsyncMessage(IdiyMessage.DELETE_SHOPCAR_ACTION,sc.getId());
			}
			//由其他地方向Fragment post事件的时候，Fragment还未初始化，
			//也即AndroidEventBus在此Fragment中还未注册，所以收不到事件。
			try {
				//等待几十毫秒后再post事件，这样既可解决未初始化问题。
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//告诉购物车刷新数据
			EventBus.getDefault().post(IdiyMessage.SHOPCAR_LIST_ACTION);
		}else{
			tip("订单添加失败:"+result.getErrorMsg());
		}
	}
	/**
	 * 判断有没默认的收货人地址决定是否显示有收货人地址的模块
	 */
	private void handleDefaultReceiver(Object obj){
		mNoReceiverRl.setVisibility(obj!=null?View.GONE:View.VISIBLE);
		mHasReceiverRl.setVisibility(obj!=null?View.VISIBLE:View.GONE);
		if(obj!=null){
			RReceiver bean = (RReceiver)obj;
			mAddressId = bean.getId();
			//为mNoReceiverRl内部控件添加数据
			mReceiverNameTv.setText(bean.getReceiverName());
			mReceiverPhoneTv.setText(bean.getReceiverPhone());
			mReceiverAddressTv.setText(bean.getReceiverAddress());
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settle);
		initData();
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.GET_DEFAULT_RECEIVER_ACTION,true);
	}
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mCheckedDatas = (ArrayList<RShopCar>)intent.
				getSerializableExtra(ShopcarFragment.CHECKEDDATAS);
		mTotalPrice = intent.getDoubleExtra(ShopcarFragment.CHECKTOTALPRICE,0);
		if(mCheckedDatas==null||mCheckedDatas.size()==0||mTotalPrice==0){
			finish();
		}
	}
	@Override
	protected void initController() {
		mController = new ShopCarController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mHasReceiverRl = (RelativeLayout)findViewById(R.id.has_receiver_rl);
		mReceiverNameTv = (TextView)findViewById(R.id.name_tv);
		mReceiverPhoneTv = (TextView)findViewById(R.id.phone_tv);
		mReceiverAddressTv = (TextView)findViewById(R.id.address_tv);
		mNoReceiverRl = (RelativeLayout)findViewById(R.id.no_receiver_rl);
		initBuyProductModel();
		mTotalSizeTv = (TextView)findViewById(R.id.total_psize_tv);
		mTotalSizeTv.setText("共"+mCheckedDatas.size()+"件");
		mAllPriceTv = (TextView)findViewById(R.id.all_price_val_tv);
		mAllPriceTv.setText("￥ "+mTotalPrice);
		mPayMoneyTv = (TextView)findViewById(R.id.pay_money_tv);
		mPayMoneyTv.setText("实付款:￥ "+mTotalPrice);
		
		mPayOnlineTv = (Button)findViewById(R.id.pay_online_tv);
		mPayWhengetTv = (Button)findViewById(R.id.pay_whenget_tv);
		mPayOnlineTv.setOnClickListener(this);
		mPayWhengetTv.setOnClickListener(this);
	}
	private void initBuyProductModel() {
		//展示购买的商品
		mProductContainerLl = (LinearLayout)findViewById(R.id.product_container_ll);
		//容器内部的控件个数
		int childCount = mProductContainerLl.getChildCount();
		//获取数据的个数
		int dataSize = mCheckedDatas.size();
		//取最小
		int showCount = Math.min(childCount,dataSize);
		//填充数据
		for(int i=0;i<showCount;i++){
			RShopCar bean = mCheckedDatas.get(i);
			LinearLayout ll = (LinearLayout)mProductContainerLl.getChildAt(i);
			SmartImageView smIv = (SmartImageView)ll.getChildAt(0);
			smIv.setImageUrl(NetworkConst.BASE_URL+"/res/"+bean.getPimageUrl());
			TextView tv = (TextView)ll.getChildAt(1);
			tv.setText("x"+bean.getBuyCount());
		}
	}
	public void addAddress(View view){
		Intent intent = new Intent(this,AddReceiverActivity.class);
		startActivityForResult(intent,ADD_RECEIVER_REQ);
	}
	public void chooseAddress(View view){
		Intent intent = new Intent(this,ChooseReceiverActivity.class);
		startActivityForResult(intent,CHOOSE_RECEIVER_REQ);
	}
	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {
		if(reqCode==ADD_RECEIVER_REQ){
			//决定要显示什么模块
			if(data!=null){
				RReceiver receiver = (RReceiver)data.getSerializableExtra("receiverAddress");
				//应该往有数据的界面进行数据填充
				handleDefaultReceiver(receiver);
			}	
		}else if(reqCode==CHOOSE_RECEIVER_REQ){
			//决定要显示什么模块
			if(data!=null){
				RReceiver receiver = (RReceiver)data.getSerializableExtra("chooseReceiver");
				//应该往有数据的界面进行数据填充
				handleDefaultReceiver(receiver);
			}	
		}
		super.onActivityResult(reqCode,resCode,data);
	}
	@Override
	public void onClick(View v) {
		mPayOnlineTv.setSelected(v.getId()==R.id.pay_online_tv);
		mPayWhengetTv.setSelected(v.getId()==R.id.pay_whenget_tv);
	}
	public void submitClick(View v){
		//提交订单
		//必须有收货人的信息
		if(mAddressId==0){
			tip("请选择收货人信息");
			return;
		}
		//支付方式必须选一个
		if(!mPayOnlineTv.isSelected()&&!mPayWhengetTv.isSelected()){
			tip("请选择支付方式");
			return;
		}
		//把所有的信息构建成SAddOrderParam对象
		SAddOrderParam params = initAddOrderParam();
		mController.sendAsyncMessage(IdiyMessage.ADD_ORDER_ACTION,params);
	}
	private SAddOrderParam initAddOrderParam() {
		SAddOrderParam paramBean = new SAddOrderParam();
		paramBean.setAddrId(mAddressId);
		paramBean.setPayWay(mPayOnlineTv.isSelected()?0:1);
		ArrayList<SAddOrderProductParam> products = new ArrayList<SAddOrderProductParam>();
		for(RShopCar shopCar:mCheckedDatas){
			SAddOrderProductParam product = new SAddOrderProductParam();
			product.setPid(shopCar.getPid());
			product.setBuyCount(shopCar.getBuyCount());
			product.setType(shopCar.getPversion());
			products.add(product);
		}
		paramBean.setProducts(products);
		return paramBean;
	}
	@Override
	public void onSureBtnClick(long oid) {
		Intent intent = new Intent(this,OrderDetailsActivity.class);
		intent.putExtra("OID",oid);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onSureClick(ROrderParam bean) {
		//1.启动一个新的支付页面
		Intent intent = new Intent(this,PayInfoActivity.class);
		intent.putExtra("BEAN",bean);
		startActivity(intent);
		//2.关闭Activity
		finish();
	}
	@Override
	public void onCancelClick(long oid) {
		Intent intent = new Intent(this,OrderDetailsActivity.class);
		intent.putExtra("OID",oid);
		startActivity(intent);
		finish();
	}
	
	
	
	
	
	
	
	
}

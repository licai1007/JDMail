package com.m520it.jdmail.activity;

import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RArea;
import com.m520it.jdmail.bean.RReceiver;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ShopCarController;
import com.m520it.jdmail.listener.IAreaChangeListener;
import com.m520it.jdmail.ui.pop.ChooseAreaPopWindow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class AddReceiverActivity extends BaseActivity implements IAreaChangeListener{
	private ChooseAreaPopWindow mChooseAreaPopWindow;
	private View mParentView;
	private TextView mChooseProvinceTv;
	private EditText mNameEt;
	private EditText mPhoneEt;
	private String mAddress;
	private EditText mAddressDetailsEt;
	private CheckBox mDefaultCbx;
	private RReceiver mReceiver;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.ADD_RECEIVER_ACTION_RESULT:
				handleAddReceiver((RResult)msg.obj);
				break;
		}
	}
	private void handleAddReceiver(RResult rResult){
		if(rResult.isSuccess()){
			tip("添加收货人成功!");
			Intent intent = new Intent();
			intent.putExtra("receiverAddress",mReceiver);
			setResult(0,intent);
			finish();
		}else{
			tip("添加收货人失败:"+rResult.getErrorMsg());
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_receiver);
		initController();
		initUI();
	}
	@Override
	protected void initController() {
		mController = new ShopCarController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mParentView = findViewById(R.id.parent_view);
		mChooseProvinceTv = (TextView)findViewById(R.id.choose_province_tv);
		mNameEt = (EditText)findViewById(R.id.name_et);
		mPhoneEt = (EditText)findViewById(R.id.phone_et);
		mAddressDetailsEt = (EditText)findViewById(R.id.address_details_et);
		mDefaultCbx = (CheckBox)findViewById(R.id.default_cbx);
	}
	public void reGetAddress(View v){
		//弹出一个选择对话框
		if(mChooseAreaPopWindow==null){
			mChooseAreaPopWindow = new ChooseAreaPopWindow(this);
			mChooseAreaPopWindow.setIAreaChangeListener(this);
		}
		mChooseAreaPopWindow.onShow(mParentView);
	}
	@Override
	public void onAreaChanged(RArea province, RArea city, RArea area) {
		mAddress = province.getName()+city.getName()+area.getName();
		mChooseProvinceTv.setText(mAddress);
	}
	public void saveAddress(View view){
		String name = mNameEt.getText().toString();
		String phone = mPhoneEt.getText().toString();
		String addressDetails = mAddressDetailsEt.getText().toString();
		boolean isDefault = mDefaultCbx.isChecked();
		//数据校验
		if(ifValueWasEmpty(name,phone,addressDetails)){
			tip("请输入完整的收货人信息");
			return;
		}
		if(mAddress==null){
			tip("请选择省市区");
			return;
		}
		mReceiver = new RReceiver();
		mReceiver.setIsDefault(isDefault);
		mReceiver.setReceiverName(name);
		mReceiver.setReceiverAddress(mAddress+addressDetails);
		mReceiver.setReceiverPhone(phone);
		//发送一个请求
		mController.sendAsyncMessage(IdiyMessage.ADD_RECEIVER_ACTION,mReceiver);
	}
	
	
	
	
	
	
	
	
	
	
}

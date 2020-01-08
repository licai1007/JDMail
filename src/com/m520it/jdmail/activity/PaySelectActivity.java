package com.m520it.jdmail.activity;

import java.util.Map;
import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.PayResult;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.AlipayController;
import com.m520it.jdmail.util.ActivityUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PaySelectActivity extends BaseActivity implements OnClickListener{
	private long mOid;
	private double mMoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_select);
		//1.取出其他界面传过来的数据
		initData();
		//2.初始化控制器
		initController();
		//3.初始化UI
		initUI();
	}
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.GET_ALIPAYINFO_ACTION_RESULT:
				handleAlipayInfo((RResult)msg.obj);
				break;
		}
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			PayResult payResult = new PayResult((Map<String, String>) msg.obj);
			/**
			 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
			 */
			String resultInfo = payResult.getResult();// 同步返回需要验证的信息
			String resultStatus = payResult.getResultStatus();
			// 判断resultStatus 为9000则代表支付成功
			if (TextUtils.equals(resultStatus, "9000")) {
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				Toast.makeText(PaySelectActivity.this, "支付可能成功", Toast.LENGTH_SHORT).show();
			} else {
				// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
				Toast.makeText(PaySelectActivity.this, "支付可能失败", Toast.LENGTH_SHORT).show();
			}
			ActivityUtil.start(PaySelectActivity.this,OrderListActivity.class,true);
		};
	};
	private void handleAlipayInfo(final RResult resultBean){
		if(resultBean.isSuccess()){
			Runnable payRunnable = new Runnable() {
				@Override
				public void run() {
					PayTask alipay = new PayTask(PaySelectActivity.this);
					Map<String, String> result = alipay.payV2(resultBean.getResult(),true);
					Log.i("msp", result.toString());
					Message msg = new Message();
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			};
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}else{
			tip("支付异常");
			Intent intent = new Intent(this,OrderDetailsActivity.class);
			intent.putExtra("OID",mOid);
			startActivity(intent);
			finish();
		}
	}
	@Override
	protected void initUI() {
		Button alipayBtn = (Button)findViewById(R.id.alipay_btn);
		alipayBtn.setOnClickListener(this);
	}
	@Override
	protected void initController() {
		mController = new AlipayController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initData() {
		Intent intent = getIntent();
		mOid = intent.getLongExtra("OID",0);
		mMoney = intent.getDoubleExtra("MONEY",0.0);
		if(mOid==0||mMoney==0){
			tip("获取支付信息异常");
			finish();
		}
	}
	//点返回键
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this,OrderDetailsActivity.class);
		intent.putExtra("OID",mOid);
		startActivity(intent);
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.alipay_btn:
				mController.sendAsyncMessage(IdiyMessage.GET_ALIPAYINFO_ACTION,mOid,mMoney);
				break;
		}
	}

}

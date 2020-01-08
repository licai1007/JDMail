package com.m520it.jdmail.activity;

import java.util.List;

import com.m520it.jdmail.R;
import com.m520it.jdmail.adapter.ReceiverListAdapter;
import com.m520it.jdmail.bean.RReceiver;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ShopCarController;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ChooseReceiverActivity extends BaseActivity implements OnItemClickListener{
	private ListView mReceiveLv;
	private ReceiverListAdapter mAdapter;
	@Override
	protected void handlerMessage(Message msg) {
		switch (msg.what) {
			case IdiyMessage.CHOOSE_RECEIVER_ACTION_RESULT:
				handleReceiverList((List<RReceiver>)msg.obj);
				break;
		}
	}
	private void handleReceiverList(List<RReceiver> datas){
		mAdapter.setDatas(datas);
		mAdapter.notifyDataSetChanged();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_receiver);
		initController();
		initUI();
		mController.sendAsyncMessage(IdiyMessage.CHOOSE_RECEIVER_ACTION,0);
	}
	@Override
	protected void initController() {
		mController = new ShopCarController(this);
		mController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		mReceiveLv = (ListView)findViewById(R.id.receive_lv);
		mReceiveLv.setOnItemClickListener(this);
		mAdapter = new ReceiverListAdapter(this);
		mReceiveLv.setAdapter(mAdapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		RReceiver bean = (RReceiver)mAdapter.getItem(position);
		Intent intent = new Intent();
		intent.putExtra("chooseReceiver",bean);
		setResult(0,intent);
		finish();
	}

}

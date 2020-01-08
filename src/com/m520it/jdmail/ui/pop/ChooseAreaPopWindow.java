package com.m520it.jdmail.ui.pop;

import java.util.ArrayList;
import java.util.List;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RArea;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.controller.ShopCarController;
import com.m520it.jdmail.listener.IAreaChangeListener;
import com.m520it.jdmail.listener.IModeChangeListener;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ChooseAreaPopWindow extends IPopupWindowProtocal implements OnClickListener,
							IModeChangeListener{
	private ListView mProvinceLv;
	private ListView mCityLv;
	private ListView mDistLv;
	private ShopCarController mShopCarController;
	private ArrayAdapter<String> mProvinceAdapter;
	private ArrayAdapter<String> mCityAdapter;
	private ArrayAdapter<String> mAreaAdapter;
	private List<RArea> mProvinceDatas;
	private List<RArea> mCityDatas;
	private List<RArea> mAreaDatas;
	private RArea mProvinceData;
	private RArea mCityData;
	private RArea mAreaData;
	private IAreaChangeListener mAreaChangeListener;
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch(msg.what){
				case IdiyMessage.PROVINCE_ACTION_RESULT:
					handleProvince((List<RArea>)msg.obj);
					break;
				case IdiyMessage.CITY_ACTION_RESULT:
					handleCity((List<RArea>)msg.obj);
					break;
				case IdiyMessage.AREA_ACTION_RESULT:
					handleArea((List<RArea>)msg.obj);
					break;
			}
		};
	};
	private void handleArea(List<RArea> datas){
		mAreaDatas = datas;
		mAreaAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1,android.R.id.text1,initShowDatas(datas));
		mDistLv.setAdapter(mAreaAdapter);
	}
	private void handleCity(List<RArea> datas){
		mCityDatas = datas;
		mCityAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1,android.R.id.text1,initShowDatas(datas));
		mCityLv.setAdapter(mCityAdapter);
	}
	private void handleProvince(List<RArea> datas){
		mProvinceDatas = datas;
		mProvinceAdapter = new ArrayAdapter<String>(mContext,
				android.R.layout.simple_list_item_1,android.R.id.text1,initShowDatas(datas));
		mProvinceLv.setAdapter(mProvinceAdapter);
	}
	private ArrayList<String> initShowDatas(List<RArea> datas) {
		ArrayList<String> showDatas = new ArrayList<String>();
		for(int i=0;i<datas.size();i++){
			showDatas.add(datas.get(i).getName());
		}
		return showDatas;
	}
	public ChooseAreaPopWindow(Context c) {
		super(c);
	}
	public void setIAreaChangeListener(IAreaChangeListener listener){
		mAreaChangeListener = listener;
	}
	@Override
	protected void initController() {
		mShopCarController = new ShopCarController(mContext);
		mShopCarController.setIModeChangeListener(this);
	}
	@Override
	protected void initUI() {
		View contentView = LayoutInflater.from(mContext).
				inflate(R.layout.address_pop_view,null,false);
		contentView.findViewById(R.id.left_v).setOnClickListener(this);
		contentView.findViewById(R.id.submit_tv).setOnClickListener(this);
		mProvinceLv = (ListView)contentView.findViewById(R.id.province_lv);
		mProvinceLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mProvinceData = mProvinceDatas.get(position);
				mCityData = null;
				mAreaData = null;
				String fcode = mProvinceData.getCode();
				//1.清空市区列表
				handleCity(new ArrayList<RArea>());
				handleArea(new ArrayList<RArea>());
				//2.重新发送请求
				mShopCarController.sendAsyncMessage(IdiyMessage.CITY_ACTION,fcode);
			}
		});
		mShopCarController.sendAsyncMessage(IdiyMessage.PROVINCE_ACTION,0);
		mCityLv = (ListView)contentView.findViewById(R.id.city_lv);
		mCityLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCityData = mCityDatas.get(position);
				mAreaData = null;
				String fcode = mCityData.getCode();
				mShopCarController.sendAsyncMessage(IdiyMessage.AREA_ACTION,fcode);
			}
		});
		mDistLv = (ListView)contentView.findViewById(R.id.dist_lv);
		mDistLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mAreaData = mAreaDatas.get(position);
			}
		});
		mPopWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
		//PopWindow内部的控件能否点击
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.update();
	}

	@Override
	public void onShow(View anchor) {
		if(mPopWindow!=null){
			mPopWindow.showAtLocation(anchor,Gravity.CENTER,0,0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.left_v:
				onDismiss();
				break;
			case R.id.submit_tv:
				//当点确定的时候，要确保前面都有选择的值
				if(mProvinceData==null||
						mCityData==null||
						mAreaData==null){
					Toast.makeText(mContext,"请选择省市区",Toast.LENGTH_SHORT).show();
					return;
				}
				//返回Activity修改文本
				if(mAreaChangeListener!=null){
					mAreaChangeListener.onAreaChanged(mProvinceData,mCityData,mAreaData);
				}
				onDismiss();
				break;
		}
	}
	@Override
	public void onModeChanged(int action, Object... values) {
		mHandler.obtainMessage(action,values[0]).sendToTarget();
	}
	
}

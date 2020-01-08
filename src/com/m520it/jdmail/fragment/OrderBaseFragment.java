package com.m520it.jdmail.fragment;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.m520it.jdmail.activity.OrderDetailsActivity;
import com.m520it.jdmail.adapter.OrderBaseAdapter;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.controller.OrderController;
import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

public abstract class OrderBaseFragment extends BaseFragment 
						implements IXListViewListener,OnItemClickListener {
	protected XListView mListView;
	protected OrderBaseAdapter mAdapter;
	protected void handleLoadLv(List<ROrderListBean> datas){
		mAdapter.setDatas(datas);
		mAdapter.notifyDataSetChanged();
		//告诉列表刷新的时间
		mListView.setRefreshTime(getCurrentTime());
		//告诉列表停止刷新
		mListView.stopRefresh();
	}
	protected String getCurrentTime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date());
	}
	@Override
	protected void initController() {
		mController = new OrderController(getActivity());
		mController.setIModeChangeListener(this);
	}
	protected void initXListView(int resId,Class<? extends OrderBaseAdapter> clazz){
		mListView = (XListView)getActivity().findViewById(resId);
		mListView.setOnItemClickListener(this);
		//启动下拉刷新
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(false);
		//设置下拉刷新监听器
		mListView.setXListViewListener(this);
		
		//通过子类来回调IOC  通过反射
//		clazz.newInstance();//通过空参构造器创建一个对象
		try {
			Constructor constructor = clazz.getDeclaredConstructor(Context.class);
			mAdapter = (OrderBaseAdapter)constructor.newInstance(getActivity());
		} catch (Exception e) {
			e.printStackTrace();
		}
//		mAdapter = new WaitPayAdapter(getActivity());
		mListView.setAdapter(mAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//1.获取oid
		Object item = parent.getAdapter().getItem(position);
		long oid = ((ROrderListBean)item).getOid();
		//2.启动订单详情的Activity
		Intent intent = new Intent(getActivity(),OrderDetailsActivity.class);
		intent.putExtra("OID",oid);
		startActivity(intent);
	}
	
	@Override
	public void onLoadMore() {
		//default Empty implements
	}
	
	
	
	
	
	
}

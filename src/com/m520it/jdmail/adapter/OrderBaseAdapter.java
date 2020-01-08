package com.m520it.jdmail.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.bean.ROrderListBean;
import com.m520it.jdmail.constant.NetworkConst;

public abstract class OrderBaseAdapter extends JDBaseAdapter<ROrderListBean> 
						implements OnClickListener{
	public OrderBaseAdapter(Context context) {
		super(context);
	}

	protected void initProductContainer(LinearLayout containerLl,String jsonStr){
		List<String> datas = JSON.parseArray(jsonStr,String.class);
		//1.计算数据的长度
		int dataSize = datas.size();
		//2.计算容器子控件的大小
		int childCount = containerLl.getChildCount();
		//3.取最小
		int realSize = Math.min(dataSize,childCount);
		//4.清空之前数据
		for (int i = 0; i < childCount; i++) {
			ImageView iv = (ImageView)containerLl.getChildAt(i);
			iv.setImageDrawable(new BitmapDrawable());
			iv.setVisibility(View.INVISIBLE);
		}
		//5.做一个for循环填充数据
		for (int i = 0; i < realSize; i++) {
			SmartImageView siv = (SmartImageView)containerLl.getChildAt(i);
			siv.setImageUrl(NetworkConst.BASE_URL+"/res/"+datas.get(i));
			siv.setVisibility(View.VISIBLE);
		}
	}
	
	protected void showOrderStatus(TextView tv,int status){
		switch (status) {
			case -1:
				tv.setText("取消订单");
				break;
			case 0:
				tv.setText("待支付");
				break;
			case 1:
				tv.setText("待发货");
				break;
			case 2:
				tv.setText("待收货");
				break;
			case 3:
				tv.setText("完成交易");
				break;
		}
	}
}

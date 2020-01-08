package com.m520it.jdmail.adapter;

import java.util.ArrayList;
import java.util.List;
import com.loopj.android.image.SmartImageView;
import com.m520it.jdmail.R;
import com.m520it.jdmail.bean.RShopCar;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.listener.IShopcarCheckChangeListener;
import com.m520it.jdmail.listener.IShopcarDeleteListener;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ShopCarAdapter extends JDBaseAdapter<RShopCar>{
	private static ArrayList<Boolean> sItemChecked = new ArrayList<Boolean>();
	private IShopcarCheckChangeListener mListener;
	private IShopcarDeleteListener mDeleteListener;
	
	public ShopCarAdapter(Context context) {
		super(context);
	}
	
	public void setIShopcarCheckChangeListener(IShopcarCheckChangeListener listener) {
		mListener = listener;
	}
	
	public void setIShopcarDeleteListener(IShopcarDeleteListener deleteListener){
		mDeleteListener = deleteListener;
	}
	
	@Override
	public void setDatas(List<RShopCar> datas) {
		super.setDatas(datas);
		sItemChecked.clear();
		//初始化sItemChecked
		for (int i = 0; i < datas.size(); i++) {
			sItemChecked.add(false);
		}
	}
	//设置item是否选中
	public void setCheck(int position) {
		//如果选中则取消，如未选中则选中
		sItemChecked.set(position,!sItemChecked.get(position));
		//刷新界面
		notifyDataSetChanged();
		//刷新外部的Fragment
		refreshOuterFragmentTip();
	}
	
	public ArrayList<RShopCar> getCheckedItem(){
		ArrayList<RShopCar> shopCars = new ArrayList<RShopCar>();
		for (int i = 0; i < sItemChecked.size(); i++) {
			if(sItemChecked.get(i)){
				shopCars.add(mDatas.get(i));
			}
		}
		return shopCars;
	}
	
	public void checkAll(boolean flag){
		for (int i = 0; i < sItemChecked.size(); i++) {
			sItemChecked.set(i,flag);
		}
		notifyDataSetChanged();
		refreshOuterFragmentTip();
	}
	
	private void refreshOuterFragmentTip() {
		if(mListener!=null){
			int count = 0;
			double totalPrice = 0;
			for (int i = 0; i < sItemChecked.size(); i++) {
				if(sItemChecked.get(i)){
					count++;
					RShopCar cart = mDatas.get(i);
					totalPrice += cart.getPprice()*cart.getBuyCount();
				}
			}
			mListener.onBuyCountChanged(count);
			mListener.onTotalPriceChanged(totalPrice);
		}
	}
	
	public boolean ifItemChecked(){
		for (int i = 0; i < sItemChecked.size(); i++) {
			if(sItemChecked.get(i)){
				return true;
			}
		}
		return false;
	}
	
	class ViewHolder{
		CheckBox itemCbx;
		SmartImageView smIv;
		TextView pnameTv;
		TextView pversionTv;
		TextView priceTv;
		TextView buyCountTv;
		Button deleteBtn;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.shopcar_lv_item,parent,false);
			holder = new ViewHolder();
			holder.itemCbx = (CheckBox)convertView.findViewById(R.id.cbx);
			holder.smIv = (SmartImageView)convertView.findViewById(R.id.product_iv);
			holder.pnameTv = (TextView)convertView.findViewById(R.id.pname_tv);
			holder.pversionTv = (TextView)convertView.findViewById(R.id.pversion_tv);
			holder.priceTv = (TextView)convertView.findViewById(R.id.price_tv);
			holder.buyCountTv = (TextView)convertView.findViewById(R.id.buyCount_tv);
			holder.deleteBtn = (Button)convertView.findViewById(R.id.delete_product);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		final RShopCar bean = mDatas.get(position);
		holder.smIv.setImageUrl(NetworkConst.BASE_URL+"/res/"+bean.getPimageUrl());
		holder.pnameTv.setText(bean.getPname());
		holder.pversionTv.setText(bean.getPversion());
		holder.priceTv.setText(" ￥ "+bean.getPprice());
		holder.buyCountTv.setText("x"+bean.getBuyCount());
		holder.itemCbx.setChecked(sItemChecked.get(position));
		holder.deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mDeleteListener!=null){
					mDeleteListener.onItemDelete(bean.getId());
				}
			}
		});
		return convertView;
	}
	
}

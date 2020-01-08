package com.m520it.jdmail.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.bean.Cart;
import com.m520it.jdmail.bean.RArea;
import com.m520it.jdmail.bean.RReceiver;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.bean.RShopCar;
import com.m520it.jdmail.bean.SAddOrderParam;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.util.NetworkUtil;
import android.content.Context;

public class ShopCarController extends UserController{
	public ShopCarController(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch (action) {
			case IdiyMessage.SHOPCAR_LIST_ACTION:
				mListener.onModeChanged(IdiyMessage.SHOPCAR_LIST_ACTION_RESULT,loadShopcarList());
				break;
			case IdiyMessage.DELETE_SHOPCAR_ACTION:
				mListener.onModeChanged(IdiyMessage.DELETE_SHOPCAR_ACTION_RESULT,
						deleteShopcar((Long)values[0]));
				break;
			case IdiyMessage.GET_DEFAULT_RECEIVER_ACTION:
				mListener.onModeChanged(IdiyMessage.GET_DEFAULT_RECEIVER_ACTION_RESULT,
						loadDefaultReceiver((Boolean)values[0]));
				break;
			case IdiyMessage.PROVINCE_ACTION:
				mListener.onModeChanged(IdiyMessage.PROVINCE_ACTION_RESULT,
						loadArea(NetworkConst.PROVINCE_URL,null));
				break;
			case IdiyMessage.CITY_ACTION:
				mListener.onModeChanged(IdiyMessage.CITY_ACTION_RESULT,
						loadArea(NetworkConst.CITY_URL,(String)values[0]));
				break;
			case IdiyMessage.AREA_ACTION:
				mListener.onModeChanged(IdiyMessage.AREA_ACTION_RESULT,
						loadArea(NetworkConst.AREA_URL,(String)values[0]));
				break;
			case IdiyMessage.ADD_RECEIVER_ACTION:
				mListener.onModeChanged(IdiyMessage.ADD_RECEIVER_ACTION_RESULT,
						addReceiver((RReceiver)values[0]));
				break;
			case IdiyMessage.CHOOSE_RECEIVER_ACTION:
				mListener.onModeChanged(IdiyMessage.CHOOSE_RECEIVER_ACTION_RESULT,
						loadReceiver());
				break;
			case IdiyMessage.ADD_ORDER_ACTION:
				mListener.onModeChanged(IdiyMessage.ADD_ORDER_ACTION_RESULT,
						addOrder((SAddOrderParam)values[0]));
				break;
		}
	}
	private RResult addOrder(SAddOrderParam paramsBean){
		//补全UserId
		paramsBean.setUserId(mUserId);
		//转换对象为JSON
		String jsonParam = JSON.toJSONString(paramsBean);
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("detail",jsonParam);
		String jsonStr = NetworkUtil.doPost(NetworkConst.ADDORDER_URL,params);
		return JSON.parseObject(jsonStr,RResult.class);
	}
	private List<RReceiver> loadReceiver(){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("userId",mUserId+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.RECEIVEADDRESS_URL,params);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			return JSON.parseArray(resultBean.getResult(),RReceiver.class);
		}
		return new ArrayList<RReceiver>();
	}
	private RResult addReceiver(RReceiver receiver){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("userId",mUserId+"");
		params.put("name",receiver.getReceiverName()+"");
		params.put("phone",receiver.getReceiverPhone()+"");
		params.put("addressDetails",receiver.getReceiverAddress()+"");
		params.put("isDefault",receiver.getIsDefault()+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.ADDADDRESS_URL,params);
		return JSON.parseObject(jsonStr,RResult.class);
	}
	private List<RArea> loadArea(String urlPath,String fcode){
		String realPath = urlPath;
		if(fcode!=null){
			realPath += "?fcode="+fcode;
		}
		String jsonStr = NetworkUtil.doGet(realPath);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			return JSON.parseArray(resultBean.getResult(),RArea.class);
		}
		return new ArrayList<RArea>();
	}
	/**
	 * 获取默认的收货人信息
	 */
	private RReceiver loadDefaultReceiver(boolean flag){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("userId",mUserId+"");
		params.put("isDefault",flag+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.RECEIVEADDRESS_URL,params);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			List<RReceiver> receivers = JSON.parseArray(resultBean.getResult(),RReceiver.class);
			if(receivers.size()>0){
				return receivers.get(0);
			}
		}
		return null;
	}
	private RResult deleteShopcar(long shopcarId){
		boolean delete = mShopcarDao.delete(shopcarId);
		RResult result = new RResult();
		result.setSuccess(delete);
		if(!delete){
			result.setErrorMsg("系统异常");
		}
		return result;
	}
	private List<RShopCar> loadShopcarList(){
		List<RShopCar> shopCars = new ArrayList<RShopCar>();
		List<Cart> list = mShopcarDao.getList();
		HashMap<String,String> params = new HashMap<String, String>();
		for(Cart cart:list){
			params.put("productId",cart.getProductId()+"");
			String jsonStr = NetworkUtil.doPost(NetworkConst.SHOPCAR_URL,params);
			RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
			RShopCar shopCar = null;
			if(resultBean.isSuccess()){
				shopCar = JSON.parseObject(resultBean.getResult(),RShopCar.class);
			}
			shopCar.setId(cart.getId());
			shopCar.setBuyCount(cart.getBuyCount());
			shopCar.setPversion(cart.getPversion());
			shopCars.add(shopCar);
		}
		return shopCars;
	}
	/*private List<RShopCar> loadShopcarList(){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("userId",mUserId+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.SHOPCAR_URL,params);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			return JSON.parseArray(resultBean.getResult(),RShopCar.class);
		}
		return new ArrayList<RShopCar>();
	}*/
}

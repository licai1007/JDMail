package com.m520it.jdmail.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.bean.RBrand;
import com.m520it.jdmail.bean.RProductList;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.bean.RCategory;
import com.m520it.jdmail.bean.SProductList;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.util.NetworkUtil;
import android.content.Context;

public class CategoryController extends BaseController{

	public CategoryController(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch(action){
			case IdiyMessage.CATEGORY_ACTION:
				mListener.onModeChanged(IdiyMessage.CATEGORY_ACTION_RESULT,
						loadCategory((Long)values[0]),values[1]);
				break;
			case IdiyMessage.BRAND_ACTION:
				mListener.onModeChanged(IdiyMessage.BRAND_ACTION_RESULT,
						loadBrand((Long)values[0]));
				break;
			case IdiyMessage.PRODUCT_LIST_ACTION:
				mListener.onModeChanged(IdiyMessage.PRODUCT_LIST_ACTION_RESULT,
						loadProductList((SProductList)values[0]));
				break;
		}
	}
	private List<RProductList> loadProductList(SProductList sendArgs){
		HashMap<String,String> params = initProductListParams(sendArgs);
		String jsonStr = NetworkUtil.doPost(NetworkConst.PRODUCTLIST_URL,params);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			return JSON.parseArray(resultBean.getResult(),RProductList.class);
		}
		return new ArrayList<RProductList>();
	}

	private HashMap<String, String> initProductListParams(SProductList sendArgs) {
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("categoryId",sendArgs.getCategoryId()+"");
		params.put("filterType",sendArgs.getFilterType()+"");
		if(sendArgs.getSortType()!=0){
			params.put("sortType",sendArgs.getSortType()+"");
		}
		params.put("deliverChoose",sendArgs.getDeliverChoose()+"");
		if(sendArgs.getMaxPrice()!=0){
			params.put("minPrice",sendArgs.getMinPrice()+"");
			params.put("maxPrice",sendArgs.getMaxPrice()+"");
		}
		if(sendArgs.getBrandId()!=0){
			params.put("brandId",sendArgs.getBrandId()+"");
		}
		return params;
	}
	private List<RBrand> loadBrand(long cid){
		String urlPath = NetworkConst.BRAND_URL + "?cid=" + cid;
		String jsonStr = NetworkUtil.doGet(urlPath);
		RResult rResult = JSON.parseObject(jsonStr,RResult.class);
		if(rResult.isSuccess()){
			return JSON.parseArray(rResult.getResult(),RBrand.class);
		}
		return new ArrayList<RBrand>();
	}
	private List<RCategory> loadCategory(long parentId){
		String urlPath = NetworkConst.CATEGORY_URL + "?parentId=" + parentId;
		String jsonStr = NetworkUtil.doGet(urlPath);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			return JSON.parseArray(resultBean.getResult(),RCategory.class);
		}
		return new ArrayList<RCategory>();
	}

}

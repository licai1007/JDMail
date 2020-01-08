package com.m520it.jdmail.controller;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.bean.Banner;
import com.m520it.jdmail.bean.RRecommendProduct;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.bean.RSecondKill;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.util.NetworkUtil;

import android.content.Context;

public class HomeController extends BaseController {

	public HomeController(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch (action) {
		case IdiyMessage.GET_BANNER_ACTION:
			mListener.onModeChanged(IdiyMessage.GET_BANNER_ACTION_RESULT,
					loadBanner((Integer)values[0]));
			break;
		case IdiyMessage.SECOND_KILL_ACTION:
			mListener.onModeChanged(IdiyMessage.SECOND_KILL_ACTION_RESULT,
					loadSecondKill());
			break;
		case IdiyMessage.RECOMMEND_PRODUCT_ACTION:
			mListener.onModeChanged(IdiyMessage.RECOMMEND_PRODUCT_ACTION_RESULT,
					loadRecommendProduct());
			break;
		}
	}
	private List<RRecommendProduct> loadRecommendProduct(){
		String jsonStr = NetworkUtil.doGet(NetworkConst.GETYOURFAV_URL);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			 return JSON.parseArray(resultBean.getResult(),RRecommendProduct.class);
		}
		return new ArrayList<RRecommendProduct>();
	}
	private List<RSecondKill> loadSecondKill(){
		String jsonStr = NetworkUtil.doGet(NetworkConst.SECKILL_URL);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			 return JSON.parseArray(resultBean.getResult(),RSecondKill.class);
		}
		return new ArrayList<RSecondKill>();
	}
	private List<Banner> loadBanner(int type){
		List<Banner> result = new ArrayList<Banner>();
		//开始一个网络请求
		String urlPath = NetworkConst.BANNER_URL+"?adKind="+type;
		String jsonStr = NetworkUtil.doGet(urlPath);
		RResult resultBean = JSON.parseObject(jsonStr,RResult.class);
		if(resultBean.isSuccess()){
			List<Banner> array = JSON.parseArray(resultBean.getResult(),Banner.class);
			return array;
		}
		return result;
	}	
}

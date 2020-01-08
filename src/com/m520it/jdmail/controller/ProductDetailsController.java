package com.m520it.jdmail.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.alibaba.fastjson.JSON;
import com.m520it.jdmail.bean.RCommentCount;
import com.m520it.jdmail.bean.RGoodComment;
import com.m520it.jdmail.bean.RProductComment;
import com.m520it.jdmail.bean.RProductInfo;
import com.m520it.jdmail.bean.RResult;
import com.m520it.jdmail.constant.IdiyMessage;
import com.m520it.jdmail.constant.NetworkConst;
import com.m520it.jdmail.db.ShopcarDao;
import com.m520it.jdmail.fragment.ProductCommentFragment;
import com.m520it.jdmail.util.NetworkUtil;
import android.content.Context;

public class ProductDetailsController extends UserController{
	public ProductDetailsController(Context c) {
		super(c);
	}

	@Override
	protected void handleMessage(int action, Object... values) {
		switch(action){
			case IdiyMessage.PRODUCT_INFO_ACTION:
				mListener.onModeChanged(IdiyMessage.PRODUCT_INFO_ACTION_RESULT,
						loadProductInfo((Long)values[0]));
				break;
			case IdiyMessage.GOOD_COMMENT_ACTION:
				mListener.onModeChanged(IdiyMessage.GOOD_COMMENT_ACTION_RESULT,
						loadGoodComment((Long)values[0]));
				break;
			case IdiyMessage.GET_COMMENT_COUNT_ACTION:
				mListener.onModeChanged(IdiyMessage.GET_COMMENT_COUNT_ACTION_RESULT,
						loadCommentCount((Long)values[0]));
				break;
			case IdiyMessage.GET_COMMENT_ACTION:
				List<RProductComment> datas = loadComment((Long)values[0],(Integer)values[1]);
				mListener.onModeChanged(IdiyMessage.GET_COMMENT_ACTION_RESULT,datas);
				break;
			case IdiyMessage.ADD2SHOPCAR_ACTION:
				RResult resultBean = add2shopcar((Long)values[0],(Integer)values[1],(String)values[2]);
				mListener.onModeChanged(IdiyMessage.ADD2SHOPCAR_ACTION_RESULT,resultBean);
				break;
		}
	}
	private RResult add2shopcar(long pid,int buyCount,String pversion){
		boolean isSuccess = mShopcarDao.add(pid,buyCount,pversion);
		RResult rResult = new RResult();
		rResult.setSuccess(isSuccess);
		if(!isSuccess){
			rResult.setErrorMsg("系统异常");
		}
		return rResult;
	}
	/*private RResult add2shopcar(long pid,int buyCount,String pversion){
		HashMap<String,String> params = new HashMap<String,String>();
		params.put("userId",mUserId+"");
		params.put("productId",pid+"");
		params.put("buyCount",buyCount+"");
		params.put("pversion",pversion);
		String jsonStr = NetworkUtil.doPost(NetworkConst.TOSHOPCAR_URL,params);
		return JSON.parseObject(jsonStr,RResult.class);
	}*/
	private List<RProductComment> loadComment(long pid,int type){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("productId",pid+"");
		if(type==4){//希望有图=全部评论+hasImgCom
			params.put("type",ProductCommentFragment.ALL_COMMENT+"");
			params.put("hasImgCom","true");
		}else{
			params.put("type",type+"");
			params.put("hasImgCom","false");
		}
		String jsonStr = NetworkUtil.doPost(NetworkConst.COMMENTDETAIL_URL,params);
		RResult rResultBean = JSON.parseObject(jsonStr,RResult.class);
		if(rResultBean.isSuccess()){
			return JSON.parseArray(rResultBean.getResult(),RProductComment.class);
		}
		return new ArrayList<RProductComment>();
	}
	private RCommentCount loadCommentCount(long pid){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("productId",pid+"");
		String jsonStr = NetworkUtil.doPost(NetworkConst.COMMENTCOUNT_URL,params);
		RResult rResultBean = JSON.parseObject(jsonStr,RResult.class);
		if(rResultBean.isSuccess()){
			return JSON.parseObject(rResultBean.getResult(),RCommentCount.class);
		}
		return null;
	}
	private List<RGoodComment> loadGoodComment(long pid){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("productId",pid+"");
		params.put("type","1");
		String jsonStr = NetworkUtil.doPost(NetworkConst.PRODUCTCOMMENT_URL,params);
		RResult rResultBean = JSON.parseObject(jsonStr,RResult.class);
		if(rResultBean.isSuccess()){
			return JSON.parseArray(rResultBean.getResult(),RGoodComment.class);
		}
		return new ArrayList<RGoodComment>();
	}
	private RProductInfo loadProductInfo(long pid){
		String jsonStr = NetworkUtil.doGet(NetworkConst.PRODUCTINFO_URL+"?id="+pid);
		RResult rResultBean = JSON.parseObject(jsonStr,RResult.class);
		if(rResultBean.isSuccess()){
			return JSON.parseObject(rResultBean.getResult(),RProductInfo.class);
		}
		return null;
	}
}

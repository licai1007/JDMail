package com.m520it.jdmail.fragment;
import com.m520it.jdmail.R;
import com.m520it.jdmail.activity.ProductDetailsActivity;
import com.m520it.jdmail.constant.NetworkConst;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ProductDetailsFragment extends BaseFragment {
	private WebView mWebView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_details,container,false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	//initUI();
    }
	@Override
	protected void initUI() {
		mWebView = (WebView)getActivity().findViewById(R.id.webview);
		ProductDetailsActivity activity = (ProductDetailsActivity)getActivity();
		mWebView.loadUrl(NetworkConst.PRODUCTDETAIL_URL+"?productId="+activity.mProductId);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
    
}

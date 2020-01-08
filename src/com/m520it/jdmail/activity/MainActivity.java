package com.m520it.jdmail.activity;

import com.m520it.jdmail.R;
import com.m520it.jdmail.fragment.CategoryFragment;
import com.m520it.jdmail.fragment.HomeFragment;
import com.m520it.jdmail.fragment.MineFragment;
import com.m520it.jdmail.fragment.ShopcarFragment;
import com.m520it.jdmail.listener.IBottomBarClickListener;
import com.m520it.jdmail.ui.BottomBar;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends BaseActivity implements IBottomBarClickListener{
	private BottomBar mBottomBar;
	private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        initUI();
    }

	@Override
	protected void initUI() {
//		初始化底部栏
		mBottomBar = (BottomBar)findViewById(R.id.bottom_bar);
		mBottomBar.setIBottomBarClickListener(this);
//		初始化顶部的Fragment
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.top_bar,new HomeFragment());
		transaction.commit();
	}
//  切换Fragment
	@Override
	public void onItemClick(int action) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		switch(action){
			case R.id.frag_main_ll:
				transaction.replace(R.id.top_bar,new HomeFragment());
				break;
			case R.id.frag_category_ll:
				transaction.replace(R.id.top_bar,new CategoryFragment());
				break;
			case R.id.frag_shopcar_ll:
				transaction.replace(R.id.top_bar,new ShopcarFragment());
				break;
			case R.id.frag_mine_ll:
				transaction.replace(R.id.top_bar,new MineFragment());
				break;
		}
		transaction.commit();
	}

}

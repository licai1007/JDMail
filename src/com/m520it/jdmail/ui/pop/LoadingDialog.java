package com.m520it.jdmail.ui.pop;

import com.m520it.jdmail.R;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;

public class LoadingDialog extends AlertDialog{

	public LoadingDialog(Context context) {
		super(context,R.style.CustomDialog);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_dialog_view);
		ImageView iv = (ImageView)findViewById(R.id.loading_iv);
		AnimationDrawable animationDrawable = (AnimationDrawable)iv.getDrawable();
		animationDrawable.start();
	}
	public void hide(){
		if(isShowing()){
			dismiss();
		}
	}
}

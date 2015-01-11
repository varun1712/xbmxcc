package com.ratan.vesit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		final ImageView logo = (ImageView) findViewById(R.id.imageView1);
		Animation v = AnimationUtils.loadAnimation(getApplicationContext(), R.layout.fade);
		logo.setAnimation(v);
		new CountDownTimer(3000,100){
			public void onTick(long millisUntilFinished) {}
			public void onFinish() {
				Intent c = new Intent (Splash.this , MainActivity.class);
				startActivity(c);
				overridePendingTransition(R.layout.activity_open_scale,R.layout.activity_close_translate);
				finish();
			}
			
		}.start();
	}

}

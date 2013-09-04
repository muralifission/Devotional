package com.devotionalbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	private static final long SPLASHTIME = 5000;
	SplashScreenActivity _activity;
	private Handler splash_time_handler;
	Animation animRotate;
	Runnable r;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		ImageView rotateIV = (ImageView) findViewById(R.id.imgRotate);
		_activity = this;

		// load the animation
		animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.rotate);

		rotateIV.startAnimation(animRotate);
		// Show Splash screen for 3secs and navigate to login screen
		splash_time_handler = new Handler();
		r = new Runnable() {
			@Override
			public void run() {
				finish();
				startActivity(new Intent(SplashScreenActivity.this,
						HomeActivity.class));
			}
		};
		splash_time_handler.postDelayed(r, SPLASHTIME);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void FinishActivity(View v) {
		splash_time_handler.removeCallbacks(r);
		finish();
		startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
	}

}

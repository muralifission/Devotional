package com.devotionalbox;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends Activity{
	private WebView _wView;
	private String webviewUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		_wView = (WebView) findViewById(R.id.webView);
		_wView.getSettings().setJavaScriptEnabled(true);
		_wView.getSettings().setLoadsImagesAutomatically(true);
		
		if (getIntent().getStringExtra("link") != null)
			webviewUrl =getIntent().getStringExtra("link");
//		_wView.clearView();
		_wView.loadUrl(webviewUrl);
		_wView.setWebViewClient(new HelloWebViewClient());
	}// onCreate()

	class HelloWebViewClient extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    view.loadUrl(url);
	    return true;
	    }
	}
	

}

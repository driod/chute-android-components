package com.chute.android.gcshareview.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class BaseDialog extends Dialog {
	
	 private ProgressBar pb;

	public BaseDialog(Context context) {
		super(context, android.R.style.Theme_Light);
	}

	public static final String TAG = BaseDialog.class.getSimpleName();
	private WebView webView;
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		webView = new WebView(getContext());
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setWebViewClient(new BaseWebViewClient());
		FrameLayout frameLayout = new FrameLayout(getContext());
		frameLayout.setMinimumHeight(150);
		pb = new ProgressBar(getContext());
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(100, 100);
		layoutParams.gravity = Gravity.CENTER;
		pb.setLayoutParams(layoutParams);
		frameLayout.addView(webView);
		frameLayout.addView(pb);
		setContentView(frameLayout);
	    }

	    private class BaseWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    view.loadUrl(url);
		    return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
		    pb.setVisibility(View.VISIBLE);
		    super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
		    pb.setVisibility(View.GONE);
		    super.onPageFinished(view, url);
		}
	    }
	    
	    public void loadUrl(String url) {
	    	webView.loadUrl(url);
	    }
	    
}

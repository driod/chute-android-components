package com.chute.android.imagesharer.dialogs;

import java.net.URLEncoder;

import com.chute.android.imagesharer.util.AppUtil;
import com.chute.android.imagesharer.util.Constants;

import android.content.Context;
import android.os.Bundle;

public class DialogShareTwitter extends BaseDialog {

//	private final String TWITTER_BASE_URL = "http://twitter.com/?status=Check+out+this+photo!+";
	private final String TWITTER_BASE_URL = "http://twitter.com/?status=%s";
	private final String shortcut;

	public DialogShareTwitter(Context context, String shortcut) {
		super(context);
		this.shortcut = shortcut;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadUrl(String.format(TWITTER_BASE_URL, URLEncoder.encode(Constants.TWITTER_STATUS + AppUtil.generateShareURLfromCode(shortcut))));
	}

}

package com.chute.android.imagesharer.dialogs;

import java.net.URLEncoder;

import android.content.Context;
import android.os.Bundle;

import com.chute.android.imagesharer.util.AppUtil;

public class DialogShareFacebook extends BaseDialog {

	private final String shortcut;
	private final String FACEBOOK_BASE_URL = "http://www.facebook.com/sharer.php?u=%s&t=%s";

	public DialogShareFacebook(Context context, String shortcut) {
		super(context);
		this.shortcut = shortcut;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadUrl(String.format(FACEBOOK_BASE_URL,
				URLEncoder.encode(AppUtil.generateShareURLfromCode(shortcut)),
				""));
	}

}

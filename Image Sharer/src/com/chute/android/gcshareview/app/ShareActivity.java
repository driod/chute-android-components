package com.chute.android.gcshareview.app;


import com.chute.android.gcshareview.R;
import com.chute.android.gcshareview.dialogs.DialogShareFacebook;
import com.chute.android.gcshareview.dialogs.DialogShareTwitter;
import com.chute.android.gcshareview.intent.ShareActivityIntentWrapper;
import com.chute.android.gcshareview.util.AppUtil;
import com.chute.android.gcshareview.util.IntentUtil;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class ShareActivity extends Activity {

	public static final String TAG = ShareActivity.class.getSimpleName();
	
	private static final int DIAlOG_FACEBOOK = 2;
    private static final int DIALOG_TWITTER = 1;
    private ShareActivityIntentWrapper wrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.setTheme(R.style.themePhotoMenu);
	setContentView(R.layout.share_activity);
	getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	WindowManager.LayoutParams lp = getWindow().getAttributes();
	lp.dimAmount = 0.2f;
	getWindow().setAttributes(lp);
	getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

	wrapper = new ShareActivityIntentWrapper(getIntent());

	Button email = (Button) findViewById(R.id.buttonEmail);
	email.setOnClickListener(new OnEmailClickListener());
	Button facebook = (Button) findViewById(R.id.buttonFacebook);
	facebook.setOnClickListener(new OnFacebookClickListener());
	Button twitter = (Button) findViewById(R.id.buttonTwitter);
	twitter.setOnClickListener(new OnTwitterClickListener());
    }

    private final class OnTwitterClickListener implements OnClickListener {
	@Override
	public void onClick(View v) {
	    showDialog(DIALOG_TWITTER);
	}
    }

    private class OnEmailClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
	    String body = getString(R.string.share_email_body);
	    body = String.format(body, wrapper.getChuteName(),
		    AppUtil.generateShareURLfromCode(wrapper.getChuteShortcut()));
	    IntentUtil.sendEmail(v.getContext(), null, null, body);
	}
    }

    private class OnFacebookClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
	    showDialog(DIAlOG_FACEBOOK);
	}
    }

    @Override
    protected Dialog onCreateDialog(int id) {
	switch (id) {
	case DIALOG_TWITTER:
	    return new DialogShareTwitter(this, wrapper.getChuteShortcut());
	case DIAlOG_FACEBOOK:
	    return new DialogShareFacebook(this, wrapper.getChuteShortcut());
	}
	return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
    }
}

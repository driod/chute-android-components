package com.chute.android.gcchutelisting.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.chute.android.gcchutelisting.R;
import com.chute.android.gcchutelisting.util.intent.MainActivityIntentWrapper;
import com.chute.sdk.model.GCAccount;

public class MainActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */

    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);

	GCAccount account = GCAccount.getInstance(getApplicationContext());
	account.setPassword("46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");

	Button showList = (Button) findViewById(R.id.btnShowList);
	showList.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
	final MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(this);
	wrapper.startActivity(this);
    }
}
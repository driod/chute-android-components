Introduction
====

GCShareView is a component that enables sharing a Chute or an Asset with Facebook, Twitter or via Email. 
Chute represents a container for assets. Asset represents any photo managed by Chute.
This component includes Chute SDK library and is targeted towards android developers who want to make their applications social. 
 
Setup
==== 

* Create a new Android project or open an existing one.
* Copy the classes and resources into your project.
* Add the required permissions to the manifest:

 ```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 ```
* Register the activities into the manifest:
  
    ```
    <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:name=".app.GCShareViewApp"
        android:screenOrientation="portrait"
        android:theme="@android:style/Theme.Black.NoTitleBar" >
        <service android:name="com.chute.sdk.api.GCHttpService" />

        <activity
            android:label="@string/app_name"
            android:name=".app.GCShareViewActivity" >
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".app.ShareActivity" >
        </activity>
        <activity
            android:name="com.chute.sdk.api.authentication.GCAuthenticationActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
    </application>
    ```
 
* After successfully creating a new Android project or opening an existing one, the next thing that needs to be done
  is adding the Chute SDK as a library project.
* Chute SDK project can be found and downloaded at [https://github.com/chute/Chute-SDK](https://github.com/chute/Chute-SDK). Or visit [http://developer.getchute.com/](http://developer.getchute.com/) for more info.

Usage
====

##GCShareViewActivity.java
This class is an Activity class. It consists of a button. When the button is clicked, ShareActivityIntentWrapper starts ShareActivity. ShareActivityIntentWrapper is a wrapper class that wraps the parameters needed for the intent.

<pre><code>
private final class OnShareClickedListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ShareActivityIntentWrapper wrapper = new ShareActivityIntentWrapper(GCShareViewActivity.this);
			wrapper.setChuteId(Constants.CHUTE_ID);
			wrapper.setChuteName(Constants.CHUTE_NAME);
			wrapper.setChuteShortcut(Constants.CHUTE_SHORTCUT);
			wrapper.startActivity(GCShareViewActivity.this);
		}
    	
    }
</code></pre>

##ShareActivity.java
This Activity class consists of a dialog view containing three buttons. 
"Share via Facebook" and "Share via Twitter" buttons start a WebView for sharing to those services with a present message.

<pre><code>
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
</code></pre>
    
"Share via Email" button displays a screen showing a standard email sheet with a preset message and subject that are editable.
  
<pre><code>
private class OnEmailClickListener implements OnClickListener {

	@Override
	public void onClick(View v) {
	    String body = getString(R.string.share_email_body);
	    body = String.format(body, wrapper.getChuteName(),
		    AppUtil.generateShareURLfromCode(wrapper.getChuteShortcut()));
	    IntentUtil.sendEmail(v.getContext(), null, null, body);
	}
    }
</code></pre>        



 
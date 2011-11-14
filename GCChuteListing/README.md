
Introduction
====

GCChuteListing is an Android application which demonstrates a list of chutes. Chute represents a container for assets. Asset represents any photo managed by Chute. The list is populated with GCChuteCollection which consists of chutes which are created using callback from GCUser. This app includes Chute SDK library and is targeted towards android developers who want to make their applications social.  


Setup
====

- Create a new Android project or open an existing one.
- Copy the classes and resources into your project.
- Add the required permissions to the manifest:

 ```
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  <uses-permission android:name="android.permission.WAKE_LOCK" />
 ```
- Register the activities into the manifest:
  
    ```
    <activity
            android:label="@string/app_name"
            android:name=".app.MainActivity"
            android:screenOrientation="portrait" >
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".app.GCChuteListingActivity"
            android:screenOrientation="portrait" />
        <activity
            android:name="com.chute.sdk.api.authentication.GCAuthenticationActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
    ```
- Register the service into the manifest:

  ```
  <service android:name="com.chute.sdk.api.GCHttpService" />
  ```

Key Concepts
========

## GCChuteCollection
Collection of GCChuteModel.

## GCChuteModel
Model containing all the characteristics for a given chute.

## GCUserModel
Model containing all the characteristics for the user: id, name and url where the avatar image is located.


Usage
========

## Staring the Activity
The first thing that has to be done in the MainActivity is creation of new GCAccount. Random token is set as a password:
<pre><code>
        GCAccount account = GCAccount.getInstance(getApplicationContext());
		account.setPassword("46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");
</code></pre>
The MainActivity consists of "Show GCChuteListing" button. When clicked, the MainActivityIntentWrapper is initialized. 
This wrapper class contains String value which has getter and setter methods and shows whether an item from the GCChuteListingActivity is clicked.
The intent calls the method startActivityForResult and GCChuteListingActivity is started:
<pre><code>
@Override
	public void onClick(View v) {
		MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(this);
		wrapper.startActivityForResult(this, Constants.ACTIVITY_FOR_RESULT_KEY);
	}
</code></pre>
GCChuteListingActivity contains AsyncTask that creates GCChuteCollection of a current user ID. In order to create the collection, GCUser.userChutes() callback is triggered in the doInBackground() method in the AsyncTask:
<pre><code>
GCUser.userChutes(getApplicationContext(),
					GCConstants.CURRENT_USER_ID,
					new GCChuteCollectionCallback(collection)).execute();
</code></pre>
GCUser.userChutes() holds a GCHttpCallback<GCChuteCollection> which returns GCChuteCollection as a result in its onSuccess() method.
When the AsyncTask is finished, the returned GCChuteCollection is passed on to the adapter in the onPostExecute() method of the AsyncTask.

## GCChuteListingAdapter
GCChuteListingAdapter populates the list in the GCChuteListingActvity with GCChuteCollection. Each list item contains roll name, assets counter, members counter, settings button, take-photo button and choose-photo button.
When take-photo or choose-photo button is clicked, boolean value "true" is set on the parameter in the MainActivityIntentWrapper and the MainActivity is called with result that an list item is clicked.
<pre><code>
@Override
				public void onClick(View v) {
					MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(
							new Intent());
					wrapper.setExtraItemClicked(true);
					activity.setResult(Activity.RESULT_OK, wrapper.getIntent());
					activity.finish();
				}					
</code></pre>					
					
## Request execution and callback

 Every request can be either:
-synchronous (it executes in the same thread as the <code>execute()</code> method was called
-asynchronous (it executes in the Background and it is started by calling <code>executeAsync()</code>);

 Every request can accept a custom response parser or use the default parser for each request type and a suitable callback which will return an object or a collection depending on the response type
 The callback has 4 possible outcomes

	<pre>
	// returns the parsed response according to the parsers return type.
	
	<code>public void onSuccess(T responseData); </code>
    
	// it returns an object that will contain the request parameters, the URL, the headers and the Request Type (GET, POST, PUT, DELETE)
	// this happens if there was a timeout and the request didn't reach the server (usually due to connectivity issues)
    
	<code>public void onHttpException(GCHttpRequestParameters params, Throwable exception); </code>
	
	// this happens when the server didn't process the result correctly, it returns a HTTP Status code and an error message
    
	<code>public void onHttpError(int responseCode, String statusMessage);</code>
	
	// This happens when the parser didn't successfully parse the response string, usually this requires adjustments on the client side and it is not recoverable by retries
	
	<code>public void onParserException(int responseCode, Throwable exception);</code>
	</pre>
	



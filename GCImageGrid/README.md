
Introduction
====

ImageGrid is an Android application which demonstrates a gallery of assets. Asset represents any photo managed by Chute. This app takes a random chute ID and displays GCAssetCollection for the chosen chute. Chute represents a container for assets. This app includes Chute SDK library and is targeted towards android developers who want to make their applications social. 


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
            android:label="@string/app_name"
            android:name=".app.ImageGridActivity"
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

## GCAssetCollection
Collection of GCAssetModel.

## GCAssetModel
Model containing all the characteristics for an asset: id, comment counter, url, share url, GCUserModel.

## GCUserModel
Model containing all the characteristics for the user: id, name and url where the avatar image is located.


Usage
========

## Staring the Activity
In the MainActivity first is needed creating a GCAccount. Random token is used as a password:
<pre><code>
        GCAccount account = GCAccount.getInstance(getApplicationContext());
		account.setPassword("46b7c778447e18ee5865a83f4202f42a2f85283c47ef24541366509235d8eccf");
</code></pre>		
ImageGridActivity is started using the ImageGridIntentWrapper which holds a String value representing the chute ID and getter and setter methods for initializing it. 
<pre><code>
@Override
	public void onClick(View v) {
		ImageGridIntentWrapper wrapper = new ImageGridIntentWrapper(this);
		wrapper.startActivityForResult(this, ACTIVITY_FOR_RESULT_KEY);
	}
</code></pre>	
ImageGridActivity is called when "Show assets" button is pressed. It consists of GridView which is filled with assets and an AyncTask that gets the assets for a specific chute ID.
In order to get the assets, first a new GCChuteModel must be created in the doInBackground() method in the AsyncTask. The newly created chute is given a random chute ID and GCChutes.Resources.assets() callback is triggered:
<pre><code>
GCChuteModel chute = new GCChuteModel();
			chute.setId("688");
			GCChutes.Resources.assets(getApplicationContext(), chute.getId(),
					new GCAssetListObjectParser(),
					new AssetCollectionCallback(chute)).execute();
</code></pre>
The GCChutes.Resources.assets() has GCHttpCallback<GCAssetCollection> callback which returns GCAssetCollection as a result in its onSuccess() method.
The GCAssetCollection is passed to the adapter which starts loading the GridView after the AsyncTask id finished. 

 ## AssetCollectionAdapter
 AssetCollectionAdapter fills the GridView with GCAssetCollection. When an item from the adapter is clicked, the chute ID is set in the ImageGridIntentWrapper and activity for result is called which gives an information that an image is clicked:
 <pre><code>
 @Override
		public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
			ImageGridIntentWrapper wrapper = new ImageGridIntentWrapper(
							new Intent());
			wrapper.setChuteID("688");
			setResult(RESULT_OK, wrapper.getIntent());
			finish();
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
	


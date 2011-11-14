
Introduction
====

Comments is an Android application which demonstrates adding comments on an asset and saving the comments. It includes Chute SDK library and is targeted towards android developers who want to make their applications social. 


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
            android:name=".app.MainActivity" >
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
     <activity android:name=".app.CommentsActivity"
               android:label="@string/app_name" 
               android:screenOrientation="portrait" />
    ```


Key Concepts
========

## Asset
Any photo or video managed by Chute


## Chute
A container for assets. Chutes can be nested inside of each other.

## GCCommentCollection
Collection of GCCommentModel

## GCCommentModel
Comment model which consists of: ID, comment text, status, creation time, GCUserModel.

## GCUserModel
Model containing all the characteristics for the user: id, name and URL where the Avatar image is located.


Usage
========

## Staring the Activity
CommentsActivity is called when the button "Show comments" is clicked. It uses an Intent with parameter which stores boolean value that shows whether the comments are saved or not.
<pre><code>
@Override
	public void onClick(View v) {
		MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(this);
		wrapper.startActivityForResult(this, Constants.ACTIVITY_FOR_RESULT_KEY);
	}
</code></pre>

## Comments Activity
Displays an EditText used for writing comments and Save button for saving comments. 
The following callback is used for creating comments:
<pre><code>
 GCComments.add(final Context context, final String chuteId,
	    final String assetId, final String comment, final GCHttpResponseParser<T> parser,
	    final GCHttpCallback<T> callback);
		</code></pre>
The chudeId, assetId and comment are taken from the CommentsActivityIntentWrapper which has getters and setters for these parameters.		
The following callback is used to get all the comments for a specific asset:
<pre><code>
GCComments.get(final Context context, final String chuteId,
	    final String assetId, final GCHttpResponseParser<T> parser,
	    final GCHttpCallback<T> callback)
</code></pre>	
When Save button is clicked, GCComments.get callback is launched, the adapter is called and the MainActivityIntentWrapper is triggered to start the activity for result.
<pre><code>
 MainActivityIntentWrapper wrapper = new MainActivityIntentWrapper(new Intent());
		    wrapper.setExtraComments(true);
		    setResult(RESULT_OK, wrapper.getIntent());
</code></pre>

## Comments Adapter
The adapter is used to fill the list of comments with GCCommentCollection. The GCComments.add callback uses GCHttpCallback<GCCommentModel> and calls the method addComment in the adapter which adds the saved comments:
<pre><code>
 public void addComment(GCCommentModel model) {
	this.collection.add(model);
	notifyDataSetChanged();
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
	
	

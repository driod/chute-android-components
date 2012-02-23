Introduction
====

Social Gallery is a component that enables browsing photos in a gallery, sharing photos, commenting on photos and marking photos as favorite.
It includes SDK library, Image Sharer library, Photo Comments library and Cloud Gallery library, and is targeted towards android developers who want to make their applications social. 


Setup
====

* Follow the ProjectSetup tutorial that can be found and downloaded at  
  [https://github.com/chute/chute-tutorials/tree/master/Android/Project%20Setup](https://github.com/chute/chute-tutorials/tree/master/Android/Project%20Setup) for a complete guide on how to setup the chute SDK.
  
* Add the Cloud Gallery component to your project by either copying all the resources and source code or by adding it as an Android Library project.
  Cloud Gallery component can be found and downloaded at [https://github.com/chute/chute-android-components/tree/master/Cloud%20Gallery](https://github.com/chute/chute-android-components/tree/master/Cloud%20Gallery).

* Add the Image Sharer component to your project by either copying all the resources and source code or by adding it as an Android Library project.
  Image Sharer component can be found and downloaded at [https://github.com/chute/chute-android-components/tree/master/Image%20Sharer](https://github.com/chute/chute-android-components/tree/master/Image%20Sharer).

* Add the Photo Comments component to your project by either copying all the resources and source code or by adding it as an Android Library project.
  Photo Comments component can be found and downloaded at [https://github.com/chute/chute-android-components/tree/master/Photo%20Comments](https://github.com/chute/chute-android-components/tree/master/Photo%20Comments).
    
* The next thing you need to do is register the activities and the application class into the AndroidManifest.xml file:

    ```
          <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:name=".app.SocialGalleryApp"
        android:screenOrientation="portrait"
        android:theme="@android:style/Theme.Black.NoTitleBar" >
        <service android:name="com.chute.sdk.api.GCHttpService" />

        <activity
            android:label="@string/app_name"
            android:name=".app.MainActivity" >
            <intent-filter >
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity android:name=".app.SocialGalleryActivity" />
        <activity android:name="com.chute.android.gcshareview.app.ShareActivity" />
        <activity
            android:name="com.chute.sdk.api.authentication.GCAuthenticationActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity android:name="com.chute.android.comments.app.PhotoCommentsActivity" />
        </application>
    ```
    
Usage
====

##SocialGalleryApp.java 
This class is the extended Application class. It is registered inside the "application" tag in the manifest and is used for initializing the utility classes used in the component.
SocialGalleryApp can extend Application like shown in this component, or can extend one of the Application classes of the components included as library projects, such as GCShareViewApp in Image Sharer or PhotoCommentsApp in Photo Comments. 
<pre><code>
public class SocialGalleryApp extends GCShareViewApp {

}
</code></pre>

SocialGalleryApp can also be neglected by registering GCSahreViewApp or PhotoCommentsApp into the manifest instead of SocialGalleryApp if the developer doesn't have the need for extending the Application class.
 
##MainActivity.java
This class is an Activity class. It consists of a button. When the button is clicked, SocialGalleryActivityIntentWrapper starts SocialGalleryActivity. SocialGalleryActivityIntentWrapper is a wrapper class that wraps the parameters needed for the intent.
<pre><code>
private final class OnStartClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			SocialGalleryActivityIntentWrapper wrapper = new SocialGalleryActivityIntentWrapper(MainActivity.this);
			wrapper.setAssetId(Constants.ASSET_ID);
			wrapper.setChuteId(Constants.CHUTE_ID);
			wrapper.setChuteName(Constants.CHUTE_NAME);
			wrapper.setChuteShortcut(Constants.CHUTE_SHORTCUT);
			wrapper.startActivity(MainActivity.this);
		}
		
	}
</code></pre>

##SocialGalleryActivity.java
This Activity class contains a GalleryViewFlipper view from the Cloud Gallery component, ImageButton "share" for sharing the asset, ImageButton "comment" for commenting on the asset and ImageButton "heart" for hearting the asset.
Using a Chute ID which is retrieved from the SocialGalleryActivityIntentWrapper, the GCChutes.Resources.assets() AsynTask is being executed:
<pre><code>
GCChutes.Resources.assets(getApplicationContext(), wrapper.getChuteId(),
				new AssetCollectionCallback()).executeAsync();   
</code></pre>

GCChutes.Resources.assets() has GCHttpCallback<GCAssetCollection> callback which returns GCAssetCollection as a result in its onSuccess() method.
The GCAssetCollection is passed to the GalleryViewFlipper which starts loading the Asset collection after the AsyncTask is finished.
<pre><code>
private final class AssetCollectionCallback implements
			GCHttpCallback<GCAssetCollection> {

		@Override
		public void onSuccess(GCAssetCollection responseData) {
			gallery.setAssetCollection(responseData);
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {

		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
		}

	}
</code></pre>

When "share" button is clicked, the ShareActivityIntentWrapper starts ShareAvtivity which contains a dialog used for sharing the current asset. 
The asset can be shared with Facebook, Twitter or via Email.
<pre><code>
private final class ShareClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			ShareActivityIntentWrapper wrapper = new ShareActivityIntentWrapper(
					SocialGalleryActivity.this);
			wrapper.setChuteId(socialWrapper.getChuteId());
			wrapper.setChuteName(socialWrapper.getChuteName());
			wrapper.setChuteShortcut(socialWrapper.getChuteShortcut());
			wrapper.startActivity(SocialGalleryActivity.this);
		}

	}
</pre></code>

When "comment" button is clicked, the PhotoCommentsActivityIntentWrapper starts PhotoCommentsActivity which contains ListView for displaying comments, EditText for writting a comment, and "Save" button for submitting a comment.
<pre><code>
 final class CommentsClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			PhotoCommentsActivityIntentWrapper wrapper = new PhotoCommentsActivityIntentWrapper(
					SocialGalleryActivity.this);
			wrapper.setChuteId(socialWrapper.getChuteId());
			wrapper.setAssetId(socialWrapper.getAssetId());
			wrapper.setChuteName(socialWrapper.getChuteName());
			wrapper.startActivityForResult(SocialGalleryActivity.this,
					Constants.ACTIVITY_FOR_RESULT_KEY);
		}

	}
</code></pre>

	    
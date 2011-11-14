
Introduction
====

MultiImagePicker is an Android application which demonstrates how to make GCLocalAssetCollection of assets. Asset represents any photo managed by Chute. This app searches for images on the device, displays the images in a gallery and creates GCLocalAssetCollection from the selected images in the gallery. It includes Chute SDK library and is targeted towards android developers who want to make their applications social. 


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
     <activity android:name=".app.MultiImagePickerActivity"
               android:label="@string/app_name"
               android:screenOrientation="portrait" />
    ```
    
Key Concepts
========

## GCLocalAssetCollection
Collection of GCLocalAssetModel.

## GCLocalAssetModel
Asset model which consists of: name, asset ID, file, asset status and fileMD5.


Usage
========

## Staring the Activity
MultiImagePicker Activity is called when the "Select Photos" button is clicked. ChoosePhotosActivityIntentWrapper is called, which represents a class that wraps the parameters needed for the intent.
This class stores a String value and methods that get and set which photos are selected.
<pre><code>
@Override
	public void onClick(View v) {
		ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(
				this);
		wrapper.startActivityForResult(this, ACTIVITY_FOR_RESULT_KEY);
	}
</code></pre>

## MultiImagePicker Activity
This Activity consists of GridView and "OK" and "Cancel" buttons. The GridView is filled with images that can be selected using the PhotoSelectCursoradapter. 
This Activity counts the selected images using the following method:
<pre><code>
public int getSelectedItemsCount() {
		return gridAdapter.tick.size();
	}
</code></pre>
It sets the selected images using the ChoosePhotosActivityIntentWrapper:
<pre><code>
public void setAssetPathList(ArrayList<String> pathList) {
		intent.putStringArrayListExtra(EXTRA_KEY_PATH_LIST, pathList);
	}	
</code></pre>
When the "OK" button is clicked, the MainActivity is called using the ChoosePhotosActivityIintentWrapper with a results which displays how many images were clicked.
GCLocalAssetCollection is being made out of the selected images:
<pre><code>
ChoosePhotosActivityIntentWrapper wrapper = new ChoosePhotosActivityIntentWrapper(
					new Intent());
			wrapper.setAssetPathList(getSelectedFilePath());
			setResult(Activity.RESULT_OK, wrapper.getIntent());
			makeGCLocalAssetCollection(getSelectedFilePath()); 
</code></pre>

## ChoosePhotosAdapter
The Adapter is called using a Cursor object which searches the images on the device:
<pre><code>
public static Cursor getMediaPhotos(Context context) {
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
		return context.getContentResolver().query(images, projection, query,
				null, null);
	}
</code></pre>





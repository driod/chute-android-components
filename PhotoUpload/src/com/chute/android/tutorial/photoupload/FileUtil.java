package com.chute.android.tutorial.photoupload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class FileUtil {
    @SuppressWarnings("unused")
    private static final String TAG = FileUtil.class.getSimpleName();

    public static File copyAsset(final Context context, final String filename) throws IOException {
	final AssetManager assetManager = context.getAssets();
	InputStream in = null;
	OutputStream out = null;
	in = assetManager.open(filename);
	final File outFile = new File(Environment.getExternalStorageDirectory(), filename);
	out = new FileOutputStream(outFile);
	copyFile(in, out);
	in.close();
	in = null;
	out.flush();
	out.close();
	return outFile;

    }

    private static void copyFile(final InputStream in, final OutputStream out) throws IOException {
	final byte[] buffer = new byte[1024];
	int read;
	while ((read = in.read(buffer)) != -1) {
	    out.write(buffer, 0, read);
	}
    }
}

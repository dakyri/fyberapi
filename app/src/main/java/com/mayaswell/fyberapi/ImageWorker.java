package com.mayaswell.fyberapi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by dak on  5/15/2016.
 */
public class ImageWorker extends AsyncTask<String, Integer, Bitmap> {
	@Override
	protected Bitmap doInBackground(String ... src) {
		if (src.length <= 0) {
			return null;
		}
		try {
			URL url = new URL(src[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}
}

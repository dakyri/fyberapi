package com.mayaswell.fyberapi;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by dak on 5/15/2016.
 */
public class CachedImage {
	private String uri = null;
	private Bitmap image = null;

	public void setImageBitmap(final ImageView iv) {
		if (uri == null || uri.equals(""))
			return;
		if (iv != null) {
			if (image != null) {
				iv.setImageBitmap(image);
				return;
			}
			iv.setImageResource(android.R.color.transparent);
		}
		new ImageWorker() {
			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					image = result;
					if (iv != null)
						iv.setImageBitmap(image);
				} else {
					if (iv != null)
						iv.setImageResource(android.R.color.transparent); // clears the image.
				}
			}
		}.execute(uri);
	}

	public void setUri(String uri) {
		this.uri = uri;
		this.image = null;
	}

	public String getUri() {
		return uri;
	}
}

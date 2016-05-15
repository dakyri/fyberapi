package com.mayaswell.fyberapi;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JSONReader extends AsyncTask<String, Integer, JSONObject> {

	protected String lastErrorMsg = "No error";
	protected int responseCode;
	protected String responseBody = null;
	Response response = null;
	OkHttpClient okclient = null;


	@Override
	protected JSONObject doInBackground(String... urls)
	{
		BufferedReader reader = null;
		responseBody = null;
		response = null;

		lastErrorMsg = "No error";
		responseCode = 0;
		if (urls.length < 1) {
			lastErrorMsg = "No request url passed";
			return null;
		}
		String url = urls[0];
		try {
			okclient  = new OkHttpClient();
			Request okrequest = new Request.Builder().url(url).build();
			response = okclient.newCall(okrequest).execute();
			responseBody = response.body().string();
			responseCode = response.code();

/*
			URL httpurl = new URL(url);
			connection = (HttpURLConnection) httpurl.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			if ((responseCode= connection.getResponseCode()) != HttpURLConnection.HTTP_OK) {
				lastErrorMsg = "Error code "+Integer.toString(responseCode)+" , "+url;
// keep going: and try to grab a response which may have more informative error message
			}

			// Read the input stream into a String

			InputStream inputStream = new BufferedInputStream(connection.getInputStream());
			StringBuffer buffer = new StringBuffer();
			if (inputStream == null) {
				return null;
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			if (buffer.length() == 0) {
				return null;
			}
			responseBody = buffer.toString();*/

			JSONObject jsono = new JSONObject(responseBody);
			return jsono;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			lastErrorMsg = "File not found exception for request to url '"+ url+"'";;
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			lastErrorMsg = "IO exception for request to url '"+ url+"'";
			return null;
		} catch (JSONException e) {
			String msg = e.getMessage();
			if (msg == null) {
				msg = "JSON processing exception for request to url '"+ url+"'";
			}
			lastErrorMsg = msg;
			return null;
		} finally {
			try {
				if (response != null) {
					response.body().close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			if (connection != null) {
				connection.disconnect();
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
				}
			}
			*/
		}
	}
}
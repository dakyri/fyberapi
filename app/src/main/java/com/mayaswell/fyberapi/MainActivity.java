package com.mayaswell.fyberapi;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


	private FyberAPI fyberAPI;
	private String baseURL;
	private TextView uidText;
	private TextView appidText;
	private TextView pub0Text;
	private TextView apikeyText;
	private TextView errorView;
	private Button goButton;
	private String defaultLocale;
	private String defaultIp;
	private String defaultOfferTypes;
	private RelativeLayout mainForm;

	private String deviceID;
	private OfferDisplayFragment offerDisplayFragment;
	private RelativeLayout mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uidText = (TextView) findViewById(R.id.textin1);
		apikeyText = (TextView) findViewById(R.id.textin2);
		appidText = (TextView) findViewById(R.id.textin3);
		pub0Text = (TextView) findViewById(R.id.textin4);		 // todo isn't specified with a value in spec?
		errorView = (TextView) findViewById(R.id.errorView);
		goButton = (Button) findViewById(R.id.goButton);

		uidText.setText(getString(R.string.given_uid));
		apikeyText.setText(getString(R.string.given_api_key));
		appidText.setText(getString(R.string.given_appid));
		pub0Text.setText(getString(R.string.given_pub0));

		mainView = (RelativeLayout) findViewById(R.id.mainView);
		mainForm = (RelativeLayout) findViewById(R.id.mainForm);

		offerDisplayFragment = new OfferDisplayFragment();

		defaultLocale = getString(R.string.given_locale);
		defaultIp = getString(R.string.given_ip);
		defaultOfferTypes = getString(R.string.given_offer_types);
		deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

		goButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!validUidText()) {
					showError(getString(R.string.error_text_uid));
				}
				if (!validApikeyText()) {
					showError(getString(R.string.error_text_apikey));
				}
				if (!validAppidText()) {
					showError(getString(R.string.error_text_appid));
				}
				String pub0 = pub0Text.getText().toString();
				if (pub0.equals("")) pub0 = null; /* pub0 optional */
				fyberAPI.request(
						appidText.getText().toString(),
						uidText.getText().toString(),
						deviceID,
						defaultLocale,
						defaultIp,
						defaultOfferTypes,
						apikeyText.getText().toString(),
						pub0);
			}
		});

		hideError();

		baseURL = getString(R.string.base_url);
		fyberAPI = new FyberAPI(this, baseURL, true);
		fyberAPI.setListener(new FyberAPI.Listener() {
			@Override
			public void error(String msg) {
				showError(msg);
			}

			@Override
			public void offersReceived(ArrayList<Offer> offers) {
				showOfferDisplay(offers);
			}
		});
		fyberAPI.setMockedResponse(true);
	}

	private boolean validUidText() {
		return !uidText.getText().equals("");
	}
	private boolean validApikeyText() {
		return !apikeyText.getText().equals("");
	}
	private boolean validAppidText() {
		return !appidText.getText().equals("");
	}

	private void hideError() {
		if (errorView != null) {
			errorView.setText("");
			errorView.setVisibility(View.GONE);
		}
	}

	private void showError(String msg) {
		if (msg == null) msg = "";
		if (errorView != null) {
			errorView.setText(msg);
			errorView.setVisibility(View.VISIBLE);
		}
		Log.e("MainActivity", "Error: "+msg);
	}

	private void showOfferDisplay(ArrayList<Offer>  ol) {
		View detailView = offerDisplayFragment.getView();
		FragmentManager fm = getFragmentManager();
		if (detailView == null) {
			fm.beginTransaction().add(offerDisplayFragment, "details").commit();
			fm.executePendingTransactions();
			detailView = offerDisplayFragment.getView();
		}
		if (detailView != null && detailView.getParent() == null) {
			mainView.addView(detailView);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, R.id.mainForm);
			detailView.setLayoutParams(lp);
		}
		offerDisplayFragment.setOffers(ol);
		detailView.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.d("Main", "new intent " + intent.toString());
		super.onNewIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (item.getItemId()) {
			case android.R.id.home:
				//Write your logic here
				return true;
			default:
				;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void setFyberAPIMocked(boolean b) {
		if (fyberAPI != null) {
			fyberAPI.setMockedResponse(b);
		}
	}
}

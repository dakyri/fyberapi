package com.mayaswell.fyberapi;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by dak on 5/14/2016.
 */
public class FyberAPI {
	private final Context context;
	private final String baseURL;
	private Listener listener;
	private String googleId;
	private String gaoie;

	private final String mockedResponseData = "{\n"+
			" \"code\": \" OK\" ,\n"+
			" \"message\": \"OK\",\n"+
			" \"count\": 1,\n"+
			" \"pages\": 1,\n"+
			" \"information\" : {\n"+
			"  \"app_name\": \"SP Test App\" ,\n"+
			"  \"appid\": 157,\n"+
			"  \"virtual_currency\": \"Coins\",\n"+
			"  \"country\": \" US\" ,\n"+
			"  \"language\": \" EN\" ,\n"+
			"  \"support_url\": \"http://iframe.fyber.com/mobile/DE/157/my_offers\"\n"+
			" },\n"+
			" \"offers\": [\n"+
			"  {\n"+
			"    \"title\": \"Tap  Fish\",\n"+
			"    \"offer_id\": 13554,\n"+
			"    \"teaser\": \"Download and START\" ,\n"+
			"    \"required_actions\": \"Download and START\",\n"+
			"    \"link\": \"http://iframe.fyber.com/mbrowser?appid=157&lpid=11387&uid=player1\",\n"+
			"    \"offer_types\" : [\n"+
			"     {\n"+
			"      \"offer_type_id\": 101,\n"+
			"      \"readable\": \"Download\"\n"+
			"     },\n"+
			"     {\n"+
			"      \"offer_type_id\": 112,\n"+
			"      \"readable\": \"Free\"\n"+
			"     }\n"+
			"    ] ,\n"+
			"    \"thumbnail\" : {\n"+
			"     \"lowres\": \"http://i458.photobucket.com/albums/qq302/North_Nitro/nonransparent.gif\",\n"+
			"     \"hires\": \"https://geekgirlsrule.files.wordpress.com/2012/05/512px-facepalm_yellow_svg.png\"\n"+
			"    },\n"+
			"    \"payout\": 90,\n"+
			"    \"time_to_payout\" : {\n"+
			"     \"amount\": 1800 ,\n"+
			"     \"readable\": \"30 minutes\"\n"+
			"    }\n"+
			"  }\n"+
			" ]\n"+
			"}";

	private final boolean mockedResponse = true;

	private class GetGoogleAdsInfo extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... strings) {
			AdvertisingIdClient.Info adInfo;
			adInfo = null;
			try {
				adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
				setGooApis(adInfo.getId(), adInfo.isLimitAdTrackingEnabled());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (GooglePlayServicesNotAvailableException e) {
				e.printStackTrace();
			} catch (GooglePlayServicesRepairableException e) {
				e.printStackTrace();
			}
			return adInfo.getId();
		}

		@Override
		protected void onPostExecute(String s) {
			Log.d("fyberAPI", "got ads id "+s);
		}
	}

	public interface Listener {
		void error(String msg);
		void offersReceived(ArrayList<Offer> offers);
	}

	FyberAPI(Context c, String baseURL, boolean fetchGooApis) {
		this.baseURL = baseURL;
		this.context = c;
		googleId = null;
		gaoie = null;
		if (fetchGooApis) {
			new GetGoogleAdsInfo().execute();
		}
	}

	public void setGooApis(String googleId, boolean gaoie) {
		this.googleId = googleId;
		this.gaoie = gaoie? "true":"false";
	}

	public void request(
			final String appId,
			final String userId,
			final String deviceID,
			final String locale,
			final String ip,
			final String offerTypes,
			final String apiKey,
			final String pub0)  {

		if (mockedResponse) {
			try {
				processJSONResult(new JSONObject(mockedResponseData));
			} catch (JSONException e) {
				notifyError("mocking response fails");
			}
			return;
		}

		final JSONReader loader = new JSONReader() {
			@Override
			protected void onPostExecute(JSONObject result) {
				if (result == null) {
					notifyError(lastErrorMsg);
					return;
				}
				if (responseCode != HttpURLConnection.HTTP_OK) {
					final String em = getErrorMessage(result, responseCode);
					notifyError(em != null? em: lastErrorMsg);
					return;
				}
				String responseHash = response.header("X-Sponsorpay-Response-Signature");
				if (responseHash == null) {
					notifyError(context.getString(R.string.http_error_security_hash_not_found));
					return;
				}
				if (!verifyResponsHash(responseBody, responseHash, apiKey)) {
					notifyError(context.getString(R.string.http_error_security_hash_failed));
					return;
				}
				processJSONResult(result);
			}

			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Integer ... progressValues)
			{
				super.onProgressUpdate(progressValues);
			}
		};
		Uri targetURI = makeURL(appId, userId, deviceID, locale, ip, offerTypes, apiKey, pub0, System.currentTimeMillis());
		loader.execute(targetURI.toString());
	}

	private void processJSONResult(JSONObject result) {
		ArrayList<Offer> offers = new ArrayList<Offer>();
		try {
			String currency = null;
			try {
				JSONObject resi = result.getJSONObject("information");
				currency = resi.getString("virtual_currency");
			} catch (JSONException e) {

			}
			JSONArray resa = result.getJSONArray("offers");
			for (int i=0; i<resa.length(); i++) {
				JSONObject reso = resa.getJSONObject(i);
				Offer o;
				if ((o=getOffer(reso)) == null) {
					return; // will already have notified precise error
				}
				o.currency = currency;
				offers.add(o);
			}
		} catch (JSONException e) {
			notifyError(e.getMessage());
			return;
		}
		if (listener != null) {
			listener.offersReceived(offers);
		}
	}

	protected boolean verifyResponsHash(String responseBody, String apiKey, String responseHash) {
		return DigestUtils.sha1Hex(responseBody + apiKey).equals(responseHash);
	}

	protected Offer getOffer(final JSONObject r) {
		if (r == null) {
			return null;
		}
		Offer o = new Offer();
		try {
			o.link = r.getString("link");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject ro;
		try {
			o.title = r.getString("title");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			o.offerId = r.getInt("offer_id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			o.teaser = r.getString("teaser");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			o.requiredActions = r.getString("required_actions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject rt;
		try {
			rt = r.getJSONObject("thumbnail");
			o.thumbnailHigh.setUri(rt.getString("hires"));
			o.thumbnailLow.setUri(rt.getString("lowres"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			o.payout = r.getInt("payout");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONObject rp;
		try {
			rp = r.getJSONObject("time_to_payout");
			o.secsToPayout = rp.getInt("amount");
			o.timeToPayout = rp.getString("readable");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray ra;

		try {
			ra = r.getJSONArray("offer_types");
			for (int i=0; i<ra.length(); i++) {
				JSONObject roo = ra.getJSONObject(i);
				Offer.OfferType oo = o.addOffer(roo.getInt("offer_type_id"), roo.getString("readable"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;

	}

	protected String getErrorMessage(JSONObject result, final int responseCode) {
		if (result != null) {
			String msg;
			try {
				msg = result.getString("message");
				return msg;
			} catch (JSONException e) {
			}
			try {
				msg = result.getString("code");
				return msg;
			} catch (JSONException e) {
			}
		}
		return context.getString(R.string.http_error_unknown);
	}

	protected Uri makeURL(String appId, String userId, String deviceID, String locale, String ip, String offerTypes, String apiKey, String pub0, long ts) {
		Uri uri = Uri.parse(baseURL);
		Uri.Builder builder = uri.buildUpon();

		String hash = "";
		String sep = "";
		if (appId != null) {
			hash += appendQueryKV(builder, "appid", appId, sep);
			sep = "&";
		}
		if (deviceID != null) {
			hash += appendQueryKV(builder, "device_id", deviceID, sep);
			sep = "&";
		}
		hash += appendQueryKV(builder, "format", "json", sep);
		sep = "&";


		if (googleId != null) {
			hash += appendQueryKV(builder, "google_ad_id", googleId, sep);
		}
		if (gaoie != null) {
			hash += appendQueryKV(builder, "google_ad_id_limited_tracking_enabled", gaoie, sep);
		}
		if (ip != null) {
			hash += appendQueryKV(builder, "ip", ip, sep);
		}

		if (locale != null) {
			hash += appendQueryKV(builder, "locale", locale, sep);
		}
		hash += appendQueryKV(builder, "os_version", android.os.Build.VERSION.RELEASE, sep);

		if (offerTypes != null) {
			hash += appendQueryKV(builder, "offer_types", offerTypes, sep);
		}
		hash += appendQueryKV(builder, "timestamp" , Long.toString(ts), sep);
		if (pub0 != null) {
			hash += appendQueryKV(builder, "pub0", pub0, sep);
		}
		if (userId != null) {
			hash += appendQueryKV(builder, "uid", userId, sep);
		}
		hash += sep+apiKey;
		String encodedSha1 = new String(Hex.encodeHex(DigestUtils.sha1(hash)));
		builder.appendQueryParameter("hashkey", encodedSha1);

//		&ps_time=[TIMESTAMP]
//		&pub0=[CUSTOM]
//		&google_ad_id=[GAID]
//		&google_ad_id_limited_tracking_enabled=[GAID ENABLED]

		Log.d("test", appId+","+userId+","+deviceID+","+locale+","+ip+","+offerTypes+","+apiKey+","+pub0+","+ts);
		Log.d("test", builder.build().toString());
		return builder.build();
	}

	private String appendQueryKV(Uri.Builder builder, String appIdK, String appIdV, String sep) {
		builder.appendQueryParameter(appIdK, appIdV);
		return sep+appIdK+"="+appIdV;
	}

	/**
	 * calls our listener if there's an error
	 * @param msg
	 */
	protected void notifyError(String msg) {
		if (listener != null) {
			listener.error(msg);
		}
	}
	/**
	 * sets up our listener
	 * @param l listener
	 */
	public void setListener(Listener l )
	{
		listener  = l;
	}


}

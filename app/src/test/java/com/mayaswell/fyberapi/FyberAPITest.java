package com.mayaswell.fyberapi;

import android.net.Uri;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by dak on 5/14/2016.
 */
public class FyberAPITest extends TestCase {

	private FyberAPI fyberAPI;
	private final String testResponse = "{\n"+
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
			"     \"lowres\": \"http://cdn.fyber.com/assets/1808/icon175x175-2_square_60.png\",\n"+
			"     \"hires\": \"http://cdn.fyber.com/assets/1808/icon175x175-2_square_175.png\"\n"+
			"    },\n"+
			"    \"payout\": 90,\n"+
			"    \"time_to_payout\" : {\n"+
			"     \"amount\": 1800 ,\n"+
			"     \"readable\": \"30 minutes\"\n"+
			"    }\n"+
			"  }\n"+
			" ]\n"+
			"}";

	@Before
	public void setUp() throws Exception {
		fyberAPI = new FyberAPI(null, "http://api.fyber.com/feed/v1/offers.json", false);
	}

	@Test
	public void testGetOffer() throws Exception {
		JSONObject jsonpool = new JSONObject(testResponse);
		JSONArray jsonargh = jsonpool.getJSONArray("offers");
		assertEquals(jsonargh.length(), 1);
		Offer o = fyberAPI.getOffer(jsonargh.getJSONObject(0));
		assertNotNull(o);
		assertEquals("Tap  Fish", o.title);
		assertEquals(13554, o.offerId);
		assertEquals("Download and START", o.teaser);
		assertEquals("Download and START", o.requiredActions);
		assertEquals("http://iframe.fyber.com/mbrowser?appid=157&lpid=11387&uid=player1", o.link);
		assertEquals("http://cdn.fyber.com/assets/1808/icon175x175-2_square_60.png", o.thumbnailLow.getUri());
		assertEquals("http://cdn.fyber.com/assets/1808/icon175x175-2_square_175.png", o.thumbnailHigh.getUri());
		assertEquals(90, o.payout);
		assertEquals(1800, o.secsToPayout);
		assertEquals("30 minutes", o.timeToPayout);
		assertEquals(2, o.types.size());
		Offer.OfferType ot = o.types.get(0);
		assertNotNull(ot);
		assertEquals(101, ot.id);
		assertEquals("Download", ot.description);
		ot = o.types.get(1);
		assertEquals(112, ot.id);
		assertEquals("Free", ot.description);
	}

	@Test
	public void testGetErrorMessage() throws Exception {
		JSONObject jsonpool = new JSONObject(testResponse);
		String msg = fyberAPI.getErrorMessage(jsonpool, 400);
		assertEquals(msg, "OK");
	}
}
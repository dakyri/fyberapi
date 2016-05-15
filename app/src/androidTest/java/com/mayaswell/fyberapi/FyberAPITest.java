package com.mayaswell.fyberapi;

import android.content.Context;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by dak on 5/15/2016.
 *
 * unit test for things not mockable
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class FyberAPITest extends InstrumentationTestCase {

	private FyberAPI fyberAPI;

	@Before
	public void setUp() throws Exception {
		Context c = getInstrumentation().getContext();
		fyberAPI = new FyberAPI(c, c.getString(R.string.base_url), false);
		fyberAPI.setGooApis("a1e5b437-0259-4f20-a16c-b6cc838a37c5", false);
	}

	@Test
	public void testMakeURL() throws Exception {
		Uri u = fyberAPI.makeURL(
				"2070",
				"spiderman",
				"e13cf2fd906ccb65",
				"DE",
				"109.235.143.113",
				"112",
				"1c915e3b5d42d05136185030892fbb846c278927",
				"campaign2",
				1463349325527L);
		assertEquals(u.toString(), "http://api.fyber.com/feed/v1/offers.json?appid=2070&device_id=e13cf2fd906ccb65&format=json&google_ad_id=a1e5b437-0259-4f20-a16c-b6cc838a37c5&google_ad_id_limited_tracking_enabled=false&ip=109.235.143.113&locale=DE&os_version=6.0.1&offer_types=112&timestamp=1463349325527&pub0=campaign2&uid=spiderman&hashkey=5ad12c4561666167b21bb1a8a4516fc9b223d3e3");
	}
}
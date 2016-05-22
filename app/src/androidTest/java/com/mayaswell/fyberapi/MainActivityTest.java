package com.mayaswell.fyberapi;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by dak on 5/15/2016.
 * framework for interface test
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	/**
	 * basically equivalent to withText
	 * @param expected
	 * @return
	 */
	private static Matcher<View> withTextValue(final String expected) {
		return new TypeSafeMatcher<View>() {

			@Override
			public boolean matchesSafely(View view) {
				if (!(view instanceof TextView)) {
					return false;
				}
				TextView tv = (TextView) view;
				return tv.getText().toString().equals(expected);
			}

			@Override
			public void describeTo(Description description) {
			}
		};
	}
	public MainActivityTest() {
		super(MainActivity.class);
	}

	MainActivity mainActivity = null;
	@Before
	public void setUp() throws Exception {
		super.setUp();

		// Injecting the Instrumentation instance is required
		// for your test to run with AndroidJUnitRunner.
		injectInstrumentation(InstrumentationRegistry.getInstrumentation());
		mainActivity = getActivity();
		assertNotNull(mainActivity);
	}

	@Test
	public void testClickUnmocked() {
		mainActivity.setFyberAPIMocked(false);
		onView(withId(R.id.goButton)).perform(click());
		onView(withId(R.id.errorView)).check(matches(
				withText("An invalid or expired timestamp was given as a parameter in the request.")));

	}

	@Test
	public void testClickMocked() {
		mainActivity.setFyberAPIMocked(true);
		onView(withId(R.id.goButton)).perform(click());

		DataInteraction di = onData(instanceOf(Offer.class))
				.inAdapterView(withId(R.id.offerListView))
				.atPosition(0);
		di.onChildView(withId(R.id.titleText)).check(matches(withTextValue("Tap  Fish")));
		di.onChildView(withId(R.id.detailText)).check(matches(withTextValue("Download and START")));
		di.onChildView(withId(R.id.payoutText)).check(matches(withTextValue("Payout 90 Coins")));
	}

}

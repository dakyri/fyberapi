package com.mayaswell.fyberapi;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by dak on 5/15/2016.
 * framework for interface test
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
	@Rule
	public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

	@Test
	public void sayHello() {

	}
}

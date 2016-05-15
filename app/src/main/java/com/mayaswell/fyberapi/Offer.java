package com.mayaswell.fyberapi;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by dak on 5/14/2016.
 */
public class Offer {
	public OfferType addOffer(int amount, String readable) {
		types.add(new OfferType(amount, readable));
		return null;
	}

	public class OfferType {
		public OfferType() {
			this(0, "");
		}
		public OfferType(int i, String d) {
			id = i;
			description = d;
		}

		protected int id;
		protected String description;
	}

	public Offer() {
		link = "";
		title = "";
		offerId = 0;
		teaser = "";
		requiredActions = "";
		thumbnailHigh = new CachedImage();
		thumbnailLow = new CachedImage();
		types = new ArrayList<OfferType>();
		payout = 0;
		secsToPayout = 0;
		timeToPayout = "";
	}

	protected String link;
	protected String title;
	protected int offerId;
	protected String teaser;
	protected String requiredActions;
	protected CachedImage thumbnailLow;
	protected CachedImage thumbnailHigh;
	protected ArrayList<OfferType> types;
	protected int payout;
	protected String timeToPayout;
	protected int secsToPayout;


}

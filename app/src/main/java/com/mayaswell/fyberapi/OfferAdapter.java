package com.mayaswell.fyberapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dak on 5/15/2016.
 */
public class OfferAdapter extends ArrayAdapter<Offer> {
	public OfferAdapter(Context context, int resource) {
		super(context, resource);
	}

	public OfferAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId);
	}

	public OfferAdapter(Context context, int resource, Offer[] objects) {
		super(context, resource, objects);
	}

	public OfferAdapter(Context context, int resource, int textViewResourceId, Offer[] objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public OfferAdapter(Context context, int resource, List<Offer> objects) {
		super(context, resource, objects);
	}

	public OfferAdapter(Context context, int resource, int textViewResourceId, List<Offer> objects) {
		super(context, resource, textViewResourceId, objects);
	}

	public View getView(int position, View v, ViewGroup parent) {
		if (v == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.offer_list_item, parent, false);
		}
		final Offer o = getItem(position);
		ImageView imageView = (ImageView) v.findViewById(R.id.imageView);
		TextView titleText = (TextView) v.findViewById(R.id.titleText);
		TextView detailText = (TextView) v.findViewById(R.id.detailText);
		TextView payoutText = (TextView) v.findViewById(R.id.payoutText);

		if (titleText != null) {
			titleText.setText(o.title);
		}
		if (detailText != null) {
			detailText.setText(o.teaser);
		}
		if (payoutText != null) {
			payoutText.setText("Payout "+Integer.toString(o.payout)+(o.currency!=null? " "+o.currency:""));
		}
		if (imageView != null) {
			imageView.setVisibility(View.VISIBLE);
			o.thumbnailHigh.setImageBitmap(imageView);
		}

		return v;
	}
}

package com.mayaswell.fyberapi;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dak on 5/15/2016.
 */
public class OfferDisplayFragment extends Fragment {
	private Offer currentOffer = null;
	private ListView offerListView;
	private OfferAdapter offerAdapter;
	private ArrayList<Offer> offers = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.offer_detail, container, true);

		offerAdapter = new OfferAdapter(getActivity(), R.layout.offer_list_item);
		offerListView = (ListView) v.findViewById(R.id.offerListView);
		offerListView.setAdapter(offerAdapter);
		offerListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		if (offers != null) {
			setOffers(offers);
		}
		return v;
	}

	public void setOffers(final ArrayList<Offer> ol) {
		offers = ol;
		offerAdapter.clear();
		if (ol != null) {
			for (Offer o: ol) {
				offerAdapter.add(o);
			}
		}
	}

	public void setCurrentOffer(Offer o) {
		currentOffer = o;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}

}

package it.pablo.cards.util;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;

public class CardFragment extends Fragment {

	private Bitmap pastImage;
	private Bitmap presImage;
	private Bitmap futuImage;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public Bitmap getPastImage() {
		return pastImage;
	}

	public Bitmap getPresImage() {
		return presImage;
	}

	public Bitmap getFutuImage() {
		return futuImage;
	}

	public void setPastImage(Bitmap pastImage) {
		this.pastImage = pastImage;
	}

	public void setPresImage(Bitmap presImage) {
		this.presImage = presImage;
	}

	public void setFutuImage(Bitmap futuImage) {
		this.futuImage = futuImage;
	}

	
}

package com.b50.savvywords;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {
	
	protected WordEngineFacade wordEngineFacade;
	protected static Typeface handWritingFont;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.wordEngineFacade = new WordEngineFacade();
	}
	
	protected TextView textViewFor(int id) {
		TextView thing = (TextView) findViewById(id);
		thing.setTypeface(getFont());
		return thing;
	}
	
	protected RadioButton radioButtonFor(int id) {
		RadioButton rButton = (RadioButton) findViewById(id);
		rButton.setTypeface(getFont());
		return rButton;
	}

	protected Typeface getFont() {
		if (handWritingFont == null) {
			handWritingFont = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");
		}
		return handWritingFont;
	}
}

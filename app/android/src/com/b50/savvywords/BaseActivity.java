package com.b50.savvywords;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	
	protected WordEngineFacade wordEngineFacade;
	protected static Typeface handWritingFont;

	abstract protected int menuResource();
	
	protected List<Word> manufactureWordList(int wordFile){
		return this.wordEngineFacade.
				buildWordsFromResource(getApplicationContext().getResources().openRawResource(wordFile));
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.wordEngineFacade = new WordEngineFacade();
	}
	
	protected TextView textViewFor(final int id) {
		TextView thing = (TextView) findViewById(id);
		thing.setTypeface(getFont());
		return thing;
	}
	
	protected Button buttonFor(final int id) {
		Button thing = (Button) findViewById(id);
		return thing;
	}
	
	protected RadioButton radioButtonFor(final int id) {
		RadioButton rButton = (RadioButton) findViewById(id);
		rButton.setTypeface(getFont());
		return rButton;
	}
	
	protected RadioGroup radioGrpFor(final int id) {
		RadioGroup grp = (RadioGroup) findViewById(id);
		return grp;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(menuResource(), menu);
		return true;
	}
	
	public void showToast(String phrase) {
		Toast.makeText(getApplicationContext(), phrase, Toast.LENGTH_SHORT).show();
	}
	
	protected Typeface getFont() {
		if (handWritingFont == null) {
			handWritingFont = Typeface.createFromAsset(getAssets(), "ShortStack-Regular.otf");
		}
		return handWritingFont;
	}
}

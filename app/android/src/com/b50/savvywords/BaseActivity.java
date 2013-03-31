package com.b50.savvywords;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {

	protected WordEngineFacade wordEngineFacade;
	protected static Typeface handWritingFont;
	protected static WordStudyEngine studyEngine;
	protected static WordTestEngine testingEngine;
	protected static int defaultWordList = R.raw.words;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.wordEngineFacade = new WordEngineFacade();

		if (studyEngine == null) {
			initializeStudyEngineInstance(defaultWordList);
		}
		
		if (testingEngine == null) {
			initializeTestingEngineInstance(defaultWordList);
		}
	}

	protected void initializeStudyEngineInstance(int rawResource) {
		studyEngine = initalizeStudyEngine(rawResource);
	}

	protected void initializeTestingEngineInstance(int rawResource) {
		testingEngine = initalizeTestingEngine(rawResource);
	}

	abstract protected int menuResource();

	protected List<Word> manufactureWordList(int wordFile) {
		return this.wordEngineFacade.buildWordsFromResource(
				getApplicationContext().getResources().openRawResource(wordFile));
	}

	protected TextView textViewFor(final int id) {
		TextView thing = (TextView) findViewById(id);
		thing.setTypeface(getFont());
		return thing;
	}

	protected ListView listViewFor(final int id) {
		ListView thing = (ListView) findViewById(id);
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

	protected WordStudyEngine initalizeStudyEngine(int wordResource) {
		final List<Word> words = this.manufactureWordList(wordResource);
		return WordStudyEngine.getInstance(words);
	}

	private WordTestEngine initalizeTestingEngine(int wordResource) {
		final List<Word> words = this.manufactureWordList(wordResource);
		return WordTestEngine.getInstance(words);
	}
}

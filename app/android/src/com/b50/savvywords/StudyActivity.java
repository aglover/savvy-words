package com.b50.savvywords;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.b50.gesticulate.SwipeDetector;

public class StudyActivity extends BaseActivity {

	private static WordStudyEngine engine;
	private GestureDetector gestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study);

		if (engine == null) {
			Log.d("SavvyWords", "study engine was null");
			engine = initalizeEngine();
		}

		engine.randomizeStudy();

		displayWordDetails(engine.getWord());

		gestureDetector = initGestureDetector();

		View view = findViewById(R.id.widget35);

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		view.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
	}

	private GestureDetector initGestureDetector() {
		return new GestureDetector(new SimpleOnGestureListener() {

			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				try {
					final SwipeDetector detector = new SwipeDetector(e1, e2,
							velocityX, velocityY);
					if (detector.isDownSwipe()) {
						return false;
					} else if (detector.isUpSwipe()) {
						startActivity(new Intent(getApplicationContext(),
								QuizActivity.class));
					} else if (detector.isLeftSwipe()) {
						displayWordDetails(engine.getWord());
					} else if (detector.isRightSwipe()) {
						showToast("Right Swipe");
//						displayWordDetails(engine.getWord());
					}
				} catch (Exception e) {
					// nothing
				}
				return false;
			}

			private void showToast(String phrase) {
				Toast.makeText(getApplicationContext(), phrase,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private WordStudyEngine initalizeEngine() {
		final List<Word> words = this.manufactureWordList(R.raw.words_2);
		return WordStudyEngine.getInstance(words);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.quit:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected int menuResource() {
		return R.menu.activity_main;
	}

	private void displayWordDetails(final Word aWord) {

		final TextView wordView = textViewFor(R.id.word_study_word);
		wordView.setText(aWord.getSpelling());

		final Definition firstDef = aWord.getDefinitions().get(0);
		final TextView wordPartOfSpeechView = textViewFor(R.id.word_study_part_of_speech);
		wordPartOfSpeechView.setText(firstDef.getPartOfSpeech());

		final TextView defView = textViewFor(R.id.word_study_definition);
		defView.setText(wordEngineFacade.formatDefinition(aWord));
	}

}

package com.b50.savvywords;

import java.util.Stack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.b50.gesticulate.SwipeDetector;

public class StudyActivity extends BaseActivity {
	
	private GestureDetector gestureDetector;
	private static Stack<Word> wordStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study);
		
		if (wordStack == null) {
			Log.d("SavvyWords", "Word was null");
			wordStack = new Stack<Word>();
		}

		studyEngine.randomizeStudy();

		// display FIRST word
		displayWordDetails(studyEngine.getWord());

		gestureDetector = initGestureDetector();

		final View view = findViewById(R.id.widget35);

		view.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
			}
		});

		view.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});
		this.scheduleHintToast();
	}

	private void scheduleHintToast() {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				// now quickly show a how to
				final Toast toast = Toast.makeText(getApplicationContext(),
						"Swipe left & right to study words. Swipe up to take a quiz!", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				final LinearLayout toastView = (LinearLayout) toast.getView();
				final ImageView imageCodeProject = new ImageView(getApplicationContext());
				imageCodeProject.setImageResource(R.drawable.swipe_left_right);
				toastView.addView(imageCodeProject, 0);
				toast.show();
			}
		}, 1500);
	}

	private GestureDetector initGestureDetector() {
		return new GestureDetector(new SimpleOnGestureListener() {

			public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
				try {
					final SwipeDetector detector = new SwipeDetector(e1, e2, velocityX, velocityY);
					if (detector.isDownSwipe()) {
						return false;
					} else if (detector.isUpSwipe()) {
						startActivity(new Intent(getApplicationContext(), QuizActivity.class));
					} else if (detector.isLeftSwipe()) {
						displayWordDetails(studyEngine.getWord());
					} else if (detector.isRightSwipe()) {
						if (wordStack.size() > 1) {
							wordStack.pop(); // throw off top element
							displayWordDetails(wordStack.pop());
						} else {
							Log.d("SavvyWords", "isRightSwipe returning false");
							return false; // can't go backwards as there isn't
											// anything on the stack
						}
					}
				} catch (Exception e) {
					// nothing
				}
				return false;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.quit:
			this.finish();
			return true;
		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected int menuResource() {
		return R.menu.activity_main;
	}

	private void displayWordDetails(final Word aWord) {
		wordStack.push(aWord);
		Log.d("SavvyWords", "displayWordDetails invoked with word " + aWord);
		final TextView wordView = textViewFor(R.id.word_study_word);
		wordView.setText(aWord.getSpelling());

		final Definition firstDef = aWord.getDefinitions().get(0);
		final TextView wordPartOfSpeechView = textViewFor(R.id.word_study_part_of_speech);
		wordPartOfSpeechView.setText(firstDef.getPartOfSpeech());

		final TextView defView = textViewFor(R.id.word_study_definition);
		defView.setText(wordEngineFacade.formatDefinition(aWord));
	}

}

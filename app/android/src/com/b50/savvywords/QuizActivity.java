package com.b50.savvywords;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class QuizActivity extends BaseActivity {

	private static WordTestEngine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_quiz);
		
		if (engine == null) {
			Log.d("SavvyWords", "engine was null");
			engine = initalizeEngine();
		}

		final TestableWord firstWord = engine.getTestableWord();
		final TextView testDefinition = textViewFor(R.id.quiz_definition);
		testDefinition.setText(this.wordEngineFacade.formatDefinition(firstWord.getValidDefinition()));

		final List<String> possibleAnswers = this.wordEngineFacade.possibleAnswersFrom(firstWord);

		final int[] radios = { R.id.quiz_answer_1, R.id.quiz_answer_2, R.id.quiz_answer_3 };
		for (int x = 0; x < radios.length; x++) {
			final RadioButton rButton = radioButtonFor(radios[x]);
			rButton.setText(possibleAnswers.get(x));
		}

		final RadioGroup group = radioGrpFor(R.id.quiz_answers);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				final RadioButton selected = radioButtonFor(checkedId);
				final String answer = (String) selected.getText();
				Log.d("SavvyWords", "value obtained is " + answer);
				if (answer.equals(firstWord.getSpelling())) {
					final TextView result = textViewFor(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#228b22"));
					result.setText("Correct!");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							startActivity(new Intent(getApplicationContext(), QuizActivity.class));
							result.setText("");
							finish();
						}
					}, 2500);
				} else {
					final TextView result = textViewFor(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#ff0000"));
					result.setText("Nope, that's not it! Try again.");
					final Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							selected.setChecked(false);
							result.setText("");
						}
					}, 2000);
				}
			}
		});
	}

	private WordTestEngine initalizeEngine() {
		List<Word> words = this.wordEngineFacade.
				buildWordsFromResource(getApplicationContext().getResources().openRawResource(R.raw.words_2));
		return WordTestEngine.getInstance(words);
	}
	
	protected int menuResource(){
		return R.menu.activity_quiz;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.back:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

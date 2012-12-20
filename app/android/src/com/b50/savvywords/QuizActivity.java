package com.b50.savvywords;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class QuizActivity extends Activity {

	private static WordTestEngine engine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_quiz);

		if (QuizActivity.engine == null) {
			Log.d("SavvyWords", "engine was null");
			QuizActivity.engine = initalizeEngine();
		}

		final TestableWord firstWord = QuizActivity.engine.getTestableWord();
		TextView testDefinition = (TextView) findViewById(R.id.quiz_definition);
		testDefinition.setText(firstWord.getValidDefinition());

		List<String> possibleAnswers = Arrays.asList(firstWord
				.getInvalidWordAnswers().get(0), firstWord
				.getInvalidWordAnswers().get(1), firstWord.getSpelling());

		Collections.shuffle(possibleAnswers);

		int[] radios = { R.id.quiz_answer_1, R.id.quiz_answer_2,
				R.id.quiz_answer_3 };
		for (int x = 0; x < radios.length; x++) {
			RadioButton rButton = (RadioButton) findViewById(radios[x]);
			rButton.setText(possibleAnswers.get(x));
		}

		RadioGroup group = (RadioGroup) findViewById(R.id.quiz_answers);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Log.d("Savvy Words", "new listener invoked");
				final RadioButton selected = (RadioButton) findViewById(checkedId);
				String answer = (String) selected.getText();
				Log.d("SavvyWords", "value obtained is " + answer);
				if (answer.equals(firstWord.getSpelling())) {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							Intent intent = new Intent(getApplicationContext(),
									QuizActivity.class);
							startActivity(intent);
							finish();
						}
					}, 2000);
				} else {
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							selected.setChecked(false);
						}
					}, 1000);

					Log.d("SavvyWords", "answer was wrong?");
				}
			}
		});
	}

	private WordTestEngine initalizeEngine() {
		String fileContents = null;
		List<Word> words = null;
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getApplicationContext().getResources().openRawResource(
							R.raw.words)));
			String read = br.readLine();

			while (read != null) {
				sb.append(read);
				read = br.readLine();
			}
			fileContents = sb.toString();
			JSONObject document = new JSONObject(fileContents);
			JSONArray allWords = document.getJSONArray("words");
			words = new ArrayList<Word>();
			for (int i = 0; i < allWords.length(); i++) {
				Word word = Word.manufacture(allWords.getJSONObject(i));
				words.add(word);
			}

		} catch (Exception e) {
			Log.e("SavvyWords",
					"Exception in getInstance for WordEngine: "
							+ e.getLocalizedMessage());
		}
		return WordTestEngine.getInstance(words);
	}
}

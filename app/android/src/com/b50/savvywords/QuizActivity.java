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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
		testDefinition.setText(formatDefinition(firstWord.getValidDefinition()));

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
				final RadioButton selected = (RadioButton) findViewById(checkedId);
				final String answer = (String) selected.getText();
				Log.d("SavvyWords", "value obtained is " + answer);
				if (answer.equals(firstWord.getSpelling())) {
					final TextView result = (TextView) findViewById(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#228b22"));
					result.setText("Correct!");
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						public void run() {
							Intent intent = new Intent(getApplicationContext(),
									QuizActivity.class);
							startActivity(intent);
							result.setText("");
							finish();
						}
					}, 2500);
				} else {
					final TextView result = (TextView) findViewById(R.id.quiz_result);
					result.setTextColor(Color.parseColor("#ff0000"));
					result.setText("Nope, that's not it! Try again.");
					Handler handler = new Handler();
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
	
	private String formatDefinition(String definition) {
		String firstChar = definition.substring(0, 1).toUpperCase();
		
		StringBuffer buff = new StringBuffer(firstChar);
		buff.append(definition.substring(1, (definition.length() +0)));		
		if(!definition.endsWith(".")){
			buff.append(".");
		}
		return buff.toString();
	}

	private WordTestEngine initalizeEngine() {
		String fileContents = null;
		List<Word> words = null;
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					getApplicationContext().getResources().openRawResource(R.raw.words_2)));
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
			Log.e("SavvyWords", "Exception in getInstance for WordEngine: " + e.getLocalizedMessage());
		}
		return WordTestEngine.getInstance(words);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_quiz, menu);
		return true;
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

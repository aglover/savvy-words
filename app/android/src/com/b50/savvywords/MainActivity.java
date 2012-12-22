package com.b50.savvywords;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static WordStudyEngine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study);

		MainActivity.engine = initalizeEngine();
		MainActivity.engine.randomizeStudy();
		
		Word startingWord = MainActivity.engine.getWord();
		
		TextView wordSpelling = (TextView)findViewById(R.id.word_study_word);
		wordSpelling.setText(startingWord.getSpelling());
		TextView wordDefinition = (TextView)findViewById(R.id.word_study_definition);
		wordDefinition.setText(formatDefinition(startingWord));

		final Button nextButton = (Button) findViewById(R.id.next_word);
		nextButton.setOnClickListener(nextButtonAction());

		final Button quizButton = (Button) findViewById(R.id.take_quiz);
		quizButton.setOnClickListener(quizButtonAction());

	}

	private OnClickListener quizButtonAction() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
				startActivity(intent);
			}
		};
	}

	private OnClickListener nextButtonAction() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Word nextWord = engine.getWord();
				TextView word = (TextView) findViewById(R.id.word_study_word);
				word.setText(nextWord.getSpelling());
				TextView def = (TextView) findViewById(R.id.word_study_definition);							
				def.setText(formatDefinition(nextWord));
			}
		};
	}

	private String formatDefinition(Word startingWord) {
		List<Definition> definitions = startingWord.getDefinitions();
		StringBuffer buff = new StringBuffer();
		int count = 1;
		for(Definition definition: definitions){
			buff.append(count++);
			buff.append(". ");
			buff.append(definition.getPartOfSpeech());
			buff.append(" - ");
			buff.append(definition.getDefinition());
			buff.append("\n\n");
		}
		return buff.toString();
	}

	private WordStudyEngine initalizeEngine() {
		String fileContents = null;
		List<Word> words = null;
		try {

			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(getApplicationContext().getResources().openRawResource(R.raw.words_2)));
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
		return WordStudyEngine.getInstance(words);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

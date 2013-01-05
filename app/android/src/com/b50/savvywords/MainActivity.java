package com.b50.savvywords;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private static WordStudyEngine engine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study);
		
		MainActivity.engine = initalizeEngine();
		MainActivity.engine.randomizeStudy();
		
		Word startingWord = MainActivity.engine.getWord();
		
		TextView wordSpelling = textViewFor(R.id.word_study_word);
		wordSpelling.setText(startingWord.getSpelling());
		
		Definition firstDef = startingWord.getDefinitions().get(0);
		
		TextView wordPartOfSpeech = textViewFor(R.id.word_study_part_of_speech);
		wordPartOfSpeech.setText(firstDef.getPartOfSpeech());
		
		TextView wordDefinition = textViewFor(R.id.word_study_definition);
		wordDefinition.setText(this.wordEngineFacade.formatDefinition(startingWord));

		final Button nextButton = (Button) findViewById(R.id.next_word);
		nextButton.setOnClickListener(nextButtonAction());

		final Button quizButton = (Button) findViewById(R.id.take_quiz);
		quizButton.setOnClickListener(quizButtonAction());

	}
			
	private OnClickListener quizButtonAction() {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), QuizActivity.class));
			}
		};
	}

	private OnClickListener nextButtonAction() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Word nextWord = engine.getWord();
				TextView word = textViewFor(R.id.word_study_word);
				word.setText(nextWord.getSpelling());
				Definition firstDef = nextWord.getDefinitions().get(0);				
				TextView wordPartOfSpeech = textViewFor(R.id.word_study_part_of_speech);
				wordPartOfSpeech.setText(firstDef.getPartOfSpeech());
				TextView def = (TextView) findViewById(R.id.word_study_definition);							
				def.setText(wordEngineFacade.formatDefinition(nextWord));
			}
		};
	}

	private WordStudyEngine initalizeEngine() {
		List<Word> words = this.wordEngineFacade.
				buildWordsFromResource(getApplicationContext().getResources().openRawResource(R.raw.words_2));
		return WordStudyEngine.getInstance(words);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
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

}

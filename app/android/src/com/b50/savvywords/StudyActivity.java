package com.b50.savvywords;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StudyActivity extends BaseActivity {

	private static WordStudyEngine engine;
	
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

		final Button nextButton = buttonFor(R.id.next_word);
		nextButton.setOnClickListener(nextButtonAction());

		final Button quizButton = buttonFor(R.id.take_quiz);
		quizButton.setOnClickListener(quizButtonAction());

	}
			
	private OnClickListener quizButtonAction() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), QuizActivity.class));
			}
		};
	}

	private OnClickListener nextButtonAction() {
		return new View.OnClickListener() {
			public void onClick(View v) {
				displayWordDetails(engine.getWord());
			}
		};
	}

	private WordStudyEngine initalizeEngine() {
		final List<Word> words = this.wordEngineFacade.
				buildWordsFromResource(getApplicationContext().getResources().openRawResource(R.raw.words));
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
	
	protected int menuResource(){
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

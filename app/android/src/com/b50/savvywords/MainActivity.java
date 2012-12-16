package com.b50.savvywords;

import com.b50.savvywords.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_study);

		final Button nextButton = (Button) findViewById(R.id.next_word);
		nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TextView word = (TextView) findViewById(R.id.word_study_word);
				word.setText("Test");
				TextView def = (TextView) findViewById(R.id.word_study_definition);
				def.setText("TestDef");
			}
		});

		final Button quizButton = (Button) findViewById(R.id.take_quiz);
		quizButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						QuizActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}

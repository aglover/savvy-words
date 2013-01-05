package com.b50.savvywords;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class WordEngineFacade {
	
	public List<Word> buildWordsFromResource(final InputStream resource){
		String fileContents = null;
		List<Word> words = null;
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(resource));
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
				words.add(Word.manufacture(allWords.getJSONObject(i)));
			}

		} catch (Exception e) {
			Log.e("SavvyWords", "Exception in getInstance for WordEngine: " + e.getLocalizedMessage());
		}
		return words;
	}
	
	public List<String> possibleAnswersFrom(final TestableWord word){
		List<String> possibleAnswers = Arrays.asList(
				word.getInvalidWordAnswers().get(0), 
				word.getInvalidWordAnswers().get(1), 
				word.getSpelling());

		Collections.shuffle(possibleAnswers);
		return possibleAnswers;
	}
	
	public String formatDefinition(final Word startingWord) {
		return formatDefinition(startingWord.getDefinitions().get(0).getDefinition());
	}
	
	public String formatDefinition(final String definition) {
		String firstChar = definition.substring(0, 1).toUpperCase(Locale.ENGLISH);
		StringBuffer buff = new StringBuffer(firstChar);
		buff.append(definition.substring(1, (definition.length() +0)));		
		if(!definition.endsWith(".")){
			buff.append(".");
		}
		return buff.toString();
	}
}

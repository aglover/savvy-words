package com.b50.savvywords.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import com.b50.savvywords.Word;
import com.b50.savvywords.WordEngine;

public class WordsEngineTest {
	@Test
	public void testEngine() throws Exception {
		List<Word> words = this.getABunchOfWords();
		WordEngine instance = WordEngine.getInstance(words);
		assertNotNull(instance);
		
		instance.startStudy();
		Word firstWord = instance.getRandomWord();
		assertNotNull(firstWord);
		Word secondWord = instance.getRandomWord();
		assertNotNull(secondWord);
		Word thirdWord = instance.getRandomWord();
		assertNotNull(thirdWord);
		
		assertEquals(false, firstWord.getSpelling().equals(secondWord.getSpelling()));
		assertEquals(false, firstWord.getSpelling().equals(thirdWord.getSpelling()));
		assertEquals(false, thirdWord.getSpelling().equals(secondWord.getSpelling()));
		
		for(int x = 0; x < 20; x++){
			assertNotNull(instance.getRandomWord());
		}
		
		assertEquals(5,  instance.wordsInTest());
		
	}

	private List<Word> getABunchOfWords() throws Exception {
		String fileContents = getJSONFromFile();
		JSONObject document = new JSONObject(fileContents);
		JSONArray allWords = document.getJSONArray("words");
		List<Word> words = new ArrayList<Word>();
		for (int i = 0; i < allWords.length(); i++) {
			Word word = Word.manufacture(allWords.getJSONObject(i));
			words.add(word);
		}
		return words;
	}

	private String getJSONFromFile() throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(
				"./etc/words.txt"));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		String fileContents = stringBuilder.toString();
		reader.close();
		return fileContents;
	}
}

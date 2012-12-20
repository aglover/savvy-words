package com.b50.savvywords;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordTestEngine {
	private LinkedList<Word> words;
	
	public static WordTestEngine getInstance(List<Word> words){
		return new WordTestEngine(words);
	}
	
	private WordTestEngine(List<Word> words) {
		this.words = new LinkedList<Word>(words);
	}
	
	public void randomizeWords(){
		Collections.shuffle(this.words, new Random(System.currentTimeMillis()));
	}
	
	public TestableWord getTestableWord(){
		Word word = this.words.removeFirst();
		this.words.addLast(word);
		List<Word> throwOffs = new LinkedList<Word>();
		for(int x = 0; x <= 3; x++){
			throwOffs.add(this.words.get(x));
		}
		return TestableWord.manufacture(word, throwOffs);
	}
}

package com.b50.savvywords;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordEngine {
	private LinkedList<Word> words;

	public static WordEngine getInstance(List<Word> words) {
		return new WordEngine(words);
	}

	private WordEngine(List<Word> words) {
		this.words = new LinkedList<Word>(words);
	}

	public void startStudy() {
		Collections.shuffle(this.words, new Random(System.currentTimeMillis()));
	}

	public Word getRandomWord() {
		Word word = this.words.removeFirst();
		this.words.addLast(word);
		return word;
	}

	public int wordsInTest() {
		return this.words.size();
	}
}

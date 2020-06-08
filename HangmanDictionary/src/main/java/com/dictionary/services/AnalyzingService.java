package com.dictionary.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dictionary.model.LetterStatistics;
import com.dictionary.model.Word;

@Service
public class AnalyzingService {

private Map<Integer, List<Word>> words = new HashMap<Integer, List<Word>>();
	
	//Adding a word into the Map
	public void analizeWord(String str) {
		int size = str.length();
		List<Word> listOfWords = words.get(size);
		if (listOfWords == null) {
			listOfWords = new ArrayList<Word>(); 
		}
		listOfWords.add(getWordData(str));	
		
		words.put(size, listOfWords);
	}

	//getting the statistics of the word
	private Word getWordData(String str) {
		Word word = new Word();
		Map<Character, Integer> counts = new HashMap<Character, Integer>();
		
		word.setText(str);
		
		Map<Character, LetterStatistics> letterStats = new HashMap<Character, LetterStatistics>();

		//getting the number of occurences of each letter and finding the positions of the letter inside the word
		for (int i = 0; i < str.length(); i++) {
			counts.put(str.charAt(i), counts.getOrDefault(str.charAt(i), 0)+1);
			letterStats.put(str.charAt(i), addToStats(letterStats.get(str.charAt(i)),str.charAt(i), i));
		}
		word.setWordStatistics(letterStats);
		return word;
	}

	//getting statistics of each letter - the letter and it's positions in the word
	private LetterStatistics addToStats(LetterStatistics letterStat, char c, int  index) {
		
		if (letterStat == null) {
			letterStat = new LetterStatistics();
		}
		letterStat.getPositions().add(index);
		letterStat.setCount(letterStat.getCount()+1);
		return letterStat;
	}

	public Map<Integer, List<Word>> getWords() {
		return words;
	}
}

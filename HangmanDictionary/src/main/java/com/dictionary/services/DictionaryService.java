package com.dictionary.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dictionary.model.HangmanResult;
import com.dictionary.model.LetterStatistics;
import com.dictionary.model.Word;

@Service
public class DictionaryService {
	@Autowired
	private AnalyzingService analService;
	
	private List<Word> listOfWords = new ArrayList<Word>();
	private Map<Character, List<Integer>> result = new HashMap<Character, List<Integer>>();
	
	public void createMapOfWords(List<String> list) {
		list.forEach(s-> analService.analizeWord(s));
	}
	
	public HangmanResult startGame(String newWord) {
		int wordSize = newWord.length();
		Map<Integer, List<Word>> words = analService.getWords();
		// filtering all the words that does not match the size of word we want to guess
		listOfWords = words.get(wordSize);
		
		//looking for the most common character
		HangmanResult hangmanResult = findMostCommonChars(listOfWords);
		return hangmanResult;	
	}

	public HangmanResult findMostCommonChars(List<Word> listOfWords, String word, boolean exists, char prevChar) {
		// if character found putting all the characters in a list else 
		// putting the wrong character in other list
		if (exists) {
			for(int i = 0; i < word.length(); i++) {
				if (word.charAt(i) == prevChar || word.charAt(i) == '-') {
					List<Integer> res =  result.get(word.charAt(i));
					if(res == null) {
						res = new ArrayList<Integer>();
					}
					res.add(i);
					result.put(word.charAt(i), res);
				}
			}	
			
		}
		else {
			result.put(prevChar, new ArrayList<Integer>());
		}
		//filtering words with new results
		listOfWords = filterWords(listOfWords);
	
		//If we have more than 1 word in list of words looking for the most common character again 
		return findMostCommonChars(listOfWords);
		
	}

	private List<Word> filterWords(List<Word> listOfWords) {
		List<Word> newList = new ArrayList<Word>();
		boolean remove = false;
		for (Word word: listOfWords) {
			for (Map.Entry<Character , LetterStatistics> item : word.getWordStatistics().entrySet()) {
				//filter all words that which chars not matching the result's position or have an except character  
				if(filterCorrectResults(item) ) {
					remove = true;
					break;
				}
			}
			if (remove) {
				remove = false;
			}
			else {
				newList.add(word);
			}
		}	
		return newList;
	}

	//finding the most common char amongst all words
	private HangmanResult findMostCommonChars(List<Word> listOfWords) {
		
		Map<Character , Integer> mapOfCounts = new HashMap<Character , Integer>();
		
		for (Word word: listOfWords) {
			for (Map.Entry<Character , LetterStatistics> item : word.getWordStatistics().entrySet()) {
				mapOfCounts.put(item.getKey(), mapOfCounts.getOrDefault(item.getKey(), 0)+item.getValue().getCount());	
			}
		}
		
		for (Map.Entry<Character , List<Integer>> chars : result.entrySet()) {
			if (mapOfCounts.containsKey(chars.getKey())) {
				mapOfCounts.remove(chars.getKey());
			}
		} 
		
		Map<Character, Integer> sortedByValue = mapOfCounts.entrySet().stream()
        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
        .collect(Collectors.toMap(
                Map.Entry::getKey, 
                Map.Entry::getValue, 
                (x,y)-> {throw new AssertionError();},
                LinkedHashMap::new
        ));
	
		int sum = 0;
		for (Map.Entry<Character , Integer> item : sortedByValue.entrySet()) {
			sum += item.getValue();
		}
		
		Map<Character, Integer> map = new LinkedHashMap<Character,Integer>();
		int index = 0;
		for (Map.Entry<Character , Integer> item : sortedByValue.entrySet()) {
			if (index < 5) {
				map.put(item.getKey(), (int) (item.getValue().floatValue()/sum*100));
			}
			else {
				break;
			}
			index++;
		}
		HangmanResult result = new HangmanResult();
		result.setMapOfValues(map);
		return result;
	}

	// filtering words that does not have chars in the correct place or does not have them at all 
	private boolean filterCorrectResults(Entry<Character, LetterStatistics> item) {
		for(Map.Entry<Character , List<Integer>> c : result.entrySet()) {
			if ((item.getKey() == c.getKey() && 
					!item.getValue().getPositions().equals(c.getValue())) ||
					item.getValue().getPositions().size() == 0 ) {
				return true;
			}
		}
		return false;
	}

	public List<Word> getListOfWords() {
		return listOfWords;
	}

	public void setListOfWords(List<Word> listOfWords) {
		this.listOfWords = listOfWords;
	}
}

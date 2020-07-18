package com.dictionary.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static java.util.stream.Collectors.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dictionary.model.HangmanResult;
import com.dictionary.model.LetterStatistics;
import com.dictionary.model.TopChoice;
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
		
		Map<Character, Integer> mapOfCounts = getMapOfCounts(listOfWords);
		
		mapOfCounts = filterPrevResults(mapOfCounts);
		
		Map<Character, Integer> sortedByValue = sortByNumberOfOccurances(mapOfCounts);
		
		int sum = getNumberOfLettersLeft(sortedByValue);
		
		HangmanResult result = new HangmanResult();
		result.setListOfTopChoices(getListOfTopChoices(sortedByValue, sum));
		return result;
	}

	private Map<Character, Integer> getMapOfCounts(List<Word> listOfWords) {
		Map<Character , Integer> mapOfCounts = new HashMap<Character , Integer>();

		listOfWords.forEach(s ->  s.getWordStatistics()
				.forEach((key, value) -> mapOfCounts.put(key, 
						mapOfCounts.getOrDefault(key, 0)+value.getCount())));
		
		return mapOfCounts;
	}
	
	private Map<Character, Integer> filterPrevResults(Map<Character, Integer> mapOfCounts) {
		return mapOfCounts.entrySet().stream()
				.filter(s-> !result.containsKey(s.getKey()))
				.collect(toMap(Entry::getKey, Entry::getValue));
	}
	
	private Map<Character, Integer> sortByNumberOfOccurances(Map<Character, Integer> mapOfCounts) {
		return mapOfCounts.entrySet().stream()
		        .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
		        .collect(toMap(Entry::getKey, Entry::getValue,
		                (x,y)-> {throw new AssertionError();},
		                LinkedHashMap::new
		        ));
	}
	
	private int getNumberOfLettersLeft(Map<Character, Integer> sortedByValue) {
		return sortedByValue.entrySet().stream()
				.mapToInt(Entry::getValue)
				.sum();
	}
	
	private List<TopChoice> getListOfTopChoices(Map<Character, Integer> sortedByValue, int sum) {
		return sortedByValue.entrySet().stream()
				.limit(5)
				.map(s-> mapToTopChoices(s.getKey(), s.getValue(), sum))
				.collect(toList());
	}
	
	private TopChoice mapToTopChoices(Character key, Integer value, int sum) {
		return new TopChoice(String.valueOf(key).toUpperCase().charAt(0), 
				String.valueOf((int) (value.floatValue()/sum*100)+"%"));
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

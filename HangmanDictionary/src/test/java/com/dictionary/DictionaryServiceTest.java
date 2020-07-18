package com.dictionary;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dictionary.model.HangmanResult;
import com.dictionary.services.AnalyzingService;
import com.dictionary.services.DictionaryService;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class DictionaryServiceTest {

	private List<String> listOfWords = new ArrayList<String>();
	@Autowired
	private DictionaryService dictService;
	@Autowired
	private AnalyzingService analService;

	@BeforeAll
	void getListOfWords() {
		populateListOfWords();
		dictService.createMapOfWords(listOfWords);
	}
	
	private void populateListOfWords() {
		listOfWords.add("marat");
		listOfWords.add("chair");
		listOfWords.add("table");
		listOfWords.add("more");
		listOfWords.add("game");
		listOfWords.add("hangman");
		listOfWords.add("russia");
		listOfWords.add("united");
		listOfWords.add("canada");
		listOfWords.add("rock");
		listOfWords.add("nightwish");
		listOfWords.add("fess");
		listOfWords.add("toronto");
		listOfWords.add("dina");
		listOfWords.add("magnolia");
		listOfWords.add("shon");
		listOfWords.add("mia");
		listOfWords.add("airplain");
		listOfWords.add("ship");
		listOfWords.add("dog");
		listOfWords.add("khargol");
		listOfWords.add("mother");
		listOfWords.add("father");
		listOfWords.add("population");
		listOfWords.add("dictionary");
		listOfWords.add("test");
		listOfWords.add("case");
		listOfWords.add("cake");
		listOfWords.add("icecream");
		listOfWords.add("maroon");
		listOfWords.add("china");
	}

	@Test
	void startGame() {
		Random rand = new Random();
		int index = rand.nextInt(listOfWords.size());
		String word = listOfWords.get(index);
		
		HangmanResult result = dictService.startGame(word);
		assertNotNull(result);
		assertNotNull(result.getListOfTopChoices());
		assertTrue(result.getListOfTopChoices().size() > 0);
		System.out.println("The top choices are:");
		result.getListOfTopChoices().forEach(System.out::println);
	}

}

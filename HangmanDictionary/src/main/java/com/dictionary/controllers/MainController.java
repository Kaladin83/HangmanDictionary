package com.dictionary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.dictionary.model.HangmanResult;
import com.dictionary.model.HangmanServerResult;
import com.dictionary.services.DictionaryService;

@RestController
@RequestMapping("/")
public class MainController {
	
	@Autowired
	private RestTemplate restTemplate; 
	@Autowired
	private DictionaryService dictService;
	
	private static final String BASE_URL = "http://localhost:8080/";
	private static final String START_GAME_URL = BASE_URL+"startGame";
	private static final String CHECK_LETTER_URL = BASE_URL+"checkLetter?letter=";
	public static String originalWord;
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("startGame")
	public HangmanResult startGame() {
		HangmanResult hangmanResult = new HangmanResult();
		hangmanResult.setReturnCode(-1);
		
		HangmanServerResult result;
		try {
			result = restTemplate.getForObject(START_GAME_URL, HangmanServerResult.class);
			if (result.getReturnCode() == 0) {
				originalWord = result.getOriginalWord();
				dictService.createMapOfWords(result.getListOfWords());
				hangmanResult = dictService.startGame(result.getWord());
			}
			hangmanResult.setResult(result.getWord());
			hangmanResult.setReturnCode(result.getReturnCode());
		}
		catch(Exception e) {
			System.out.println("Exception caught calling server: "+e.getMessage());
		}
		return hangmanResult;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping("checkLetter")
	public HangmanResult checkLetter(@RequestParam char letter) {
		char letter_ = String.valueOf(letter).toLowerCase().charAt(0);
		HangmanResult hangmanResult = new HangmanResult();
		hangmanResult.setReturnCode(-1);
		
		HangmanServerResult result;
		try {
			result = restTemplate.getForObject(CHECK_LETTER_URL+letter_, HangmanServerResult.class);
			if (result.getReturnCode() == 0) {
				hangmanResult = dictService.findMostCommonChars(dictService.getListOfWords(), result.getWord(), result.isFound(), letter_);
				hangmanResult.setReturnCode(0);
			}
			hangmanResult.setResult(result.getWord());
			hangmanResult.setReturnCode(result.getReturnCode());
		}
		catch(Exception e) {
			System.out.println("Exception caught calling server: "+e.getMessage());
		}
		
		return hangmanResult;
	}
}

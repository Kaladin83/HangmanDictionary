package com.dictionary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
	
	@GetMapping("startGame")
	public HangmanResult startGame() {
		HangmanResult hangmanResult = new HangmanResult();
		hangmanResult.setReturnCode(-1);
		
		HangmanServerResult result;
		try {
			result = restTemplate.getForObject(START_GAME_URL, HangmanServerResult.class);
			hangmanResult.setResult(result.getWord());
			hangmanResult.setReturnCode(result.getReturnCode());
			if (result.getReturnCode() == 0) {
				originalWord = result.getOriginalWord();
				dictService.createMapOfWords(result.getListOfWords());
				hangmanResult = dictService.startGame(result.getWord());
			}
		}
		catch(Exception e) {
			System.out.println("Exception caught calling server: "+e.getMessage());
		}
		//callServer(result);
		return hangmanResult;
	}
	
	@GetMapping("checkLetter")
	public HangmanResult checkLetter(@PathVariable char letter) {
		HangmanResult hangmanResult = new HangmanResult();
		hangmanResult.setReturnCode(-1);
		
		HangmanServerResult result;
		try {
			result = restTemplate.getForObject(CHECK_LETTER_URL+letter, HangmanServerResult.class);
			hangmanResult.setResult(result.getWord());
			hangmanResult.setReturnCode(result.getReturnCode());
			if (result.getReturnCode() == 0) {
				hangmanResult = dictService.findMostCommonChars(dictService.getListOfWords(), result.getWord(), result.isFound(), letter);
				hangmanResult.setReturnCode(0);
			}
		}
		catch(Exception e) {
			System.out.println("Exception caught calling server: "+e.getMessage());
		}
		
		return hangmanResult;
	}

//	private void callServer(HangmanServerResult result) {
//		
//		char lastChar = ' ';
//		int itterations = 0;
//		while (result.getReturnCode() == 0) {
//			HangmanResult res;
//			if (itterations == 0) {
//				res = dictService.startGame(result.getWord());
//			}
//			else {
//				res = dictService.findMostCommonChars(dictService.getListOfWords(), result.getWord(), result.isFound(), lastChar);
//			}
//			
//			if (res.getMapOfValues().size() > 0) {
//				int index = 0;
//				for (Map.Entry<Character, Integer> item: res.getMapOfValues().entrySet()) {
//					if (index == 1) {
//						break;
//					}
//					lastChar = item.getKey();
//					result = restTemplate.getForObject("http://localhost:8080/checkLetter?letter="+item.getKey(), HangmanServerResult.class);
//					index++;
//				}
//			}
//			itterations++;
//		}
//	}
}

package com.dictionary.model;

import java.util.List;

public class HangmanServerResult {

	private int returnCode;
	private boolean found;
	private String word;
	private String originalWord;
	private List<String> listOfWords;
	
	public int getReturnCode() {
		return returnCode;
	}
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getOriginalWord() {
		return originalWord;
	}
	public void setOriginalWord(String originalWord) {
		this.originalWord = originalWord;
	}
	public List<String> getListOfWords() {
		return listOfWords;
	}
	public void setListOfWords(List<String> listOfWords) {
		this.listOfWords = listOfWords;
	}
}

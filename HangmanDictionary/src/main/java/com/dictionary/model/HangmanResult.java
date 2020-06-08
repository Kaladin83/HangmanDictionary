package com.dictionary.model;

import java.util.Map;

public class HangmanResult {
	
	private int returnCode;
	private Map<Character, Integer> mapOfValues;
	private String result;
	
	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public Map<Character, Integer> getMapOfValues() {
		return mapOfValues;
	}

	public void setMapOfValues(Map<Character, Integer> mapOfValues) {
		this.mapOfValues = mapOfValues;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

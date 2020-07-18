package com.dictionary.model;

import java.util.List;

public class HangmanResult {
	
	private int returnCode;
	private List<TopChoice> ListOfTopChoices;
	private String result;
	
	public int getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public List<TopChoice> getListOfTopChoices() {
		return ListOfTopChoices;
	}

	public void setListOfTopChoices(List<TopChoice> listOfTopChoices) {
		ListOfTopChoices = listOfTopChoices;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}

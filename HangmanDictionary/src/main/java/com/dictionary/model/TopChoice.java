package com.dictionary.model;

public class TopChoice {
	private char letter;
	private String percent;
	
	public TopChoice() {
	}
	public TopChoice(char letter, String percent) {
		this.letter = letter;
		this.percent = percent;
	}
	public char getLetter() {
		return letter;
	}
	public void setLetter(char letter) {
		this.letter = letter;
	}
	public String getPercent() {
		return percent;
	}
	public void setPercent(String percent) {
		this.percent = percent;
	}
	@Override
	public String toString() {
		return "TopChoice [" + letter + " - " + percent + "]";
	}
}

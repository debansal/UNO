package com.game.uno;

public class Card {

	private Color color;
	private Text text; 
	
	public Card(Color color, Text text) { 
		super(); 
		this.color = color; 
		this.text = text; 
	}
	
	public Color getColor() {
		return color; 
	}
	public void setColor(Color color){
		this.color = color; 
	}
	public Text getText() {
		return text; 
	}

	public void setText(Text text) {
		this.text = text; 
	}
}


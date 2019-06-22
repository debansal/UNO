package com.game.uno;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DrawPile {

	private static List<Card> cards; 
	public void setCards(List<Card> cards){
		this.cards = cards; 
	}
	public static Card draw() throws NoMoreCardException{
		if(cards.size() <= 0){
			//stop game 
			throw new NoMoreCardException(); 
		}
		Card card = cards.remove(cards.size() - 1 ); 
		return card; 
	}
	
	public static Card firstDraw(){
		List<Card> options = cards.stream().filter(c -> (!c.getText().equals(Text.PLUS_4) && !c.getText().equals(Text.WILD)) ).collect(Collectors.toList()); 
		Card card = options.remove(options.size()- 1); 
		cards.remove(card); 
		return card; 
	}
	public void shuffle(){
		Collections.shuffle(cards); 
	}
}
	


package com.game.uno;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player {
	private static Integer count = 1;
	private int id; 
	private String name; 
	private List<Card> cards = new ArrayList<>(); 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Card> getCards() {
		return cards;
	}
	public void setCards(List<Card> cards) {
		this.cards = cards;
	}

	public Player(String name){ 
		this.id = count++; 
		this.name = name; 
	}
	
	private Card throwCard(Card playCard){
		this.cards.remove(playCard); 
		Text actionText = playCard.getText(); 
		if(actionText.equals(Text.PLUS_4) || actionText.equals(Text.WILD)){
			//change color
			int choosenColorNo = -1;
			do{
				System.out.print("Select color : RED(1), GREEN(2), YELLOW(3), BLUE(4)");
				choosenColorNo = GameExecutorService.scanner.nextInt(); 
			}while(choosenColorNo <= 0 && choosenColorNo > 4); 
			
			if(choosenColorNo == 1){
				playCard.setColor(Color.RED); 
			}else if(choosenColorNo == 2){
				playCard.setColor(Color.GREEN);
			}else if(choosenColorNo == 3){
				playCard.setColor(Color.YELLOW); 
			} else{
				playCard.setColor(Color.BLUE); 
			}
		}
		return playCard;
	}
	public void addCards(int numberOfCards) throws NoMoreCardException{
		for(int i= 0; i<numberOfCards; i++){
			this.cards.add(DrawPile.draw()); 
		}
	}
	
	private Card decide(Color actionColor, Text actionText) throws NoMoreCardException { 
		//show user the available options 
		List<Card> options = this.cards.stream().filter(c -> (isThrowable(c, actionColor, actionText))).collect(Collectors.toList());
		//if no option then draw a card from drawPile 
			//keep or throwCard 
		//else throw chosen card 
		if(options == null || options.size() == 0 ){
			return drawCardAndPlay(actionColor, actionText); 
		}else{
			int cardNo = 1;
			for (Card card : options){
				GameExecutorService.printCard(card); 
				System.out.print(" (" + cardNo++ + "),"); 
			}
		
			System.out.println("Pick Up a card (" + cardNo + ")"); 
			int choosenCardNo = -1; 
			do{ 
				System.out.print("Select card from above : ");
				choosenCardNo = GameExecutorService.scanner.nextInt();
			}while(choosenCardNo > cardNo); 
			if(choosenCardNo == cardNo){
				return drawCardAndPlay(actionColor, actionText); 
			}else{ 
				return throwCard(options.get(choosenCardNo-1 )); 
			}
		}
	}
	

	
	private boolean isThrowable(Card card, Color actionColor, Text actionText){
		return ((card.getColor() != null && card.getColor().equals(actionColor)) ||
				(card.getText() != null && 
					card.getText().equals(actionText) ||
					card.getText().equals(Text.PLUS_4) ||
					card.getText().equals(Text.WILD)
			) ||
			card.getColor() == null ||
			card.getText() == null 
			)? true : false; 
	}
			
		
	private Card drawCardAndPlay(Color actionColor, Text actionText) throws NoMoreCardException{
		System.out.println("Pick Up a Card : "); 
		Card card = DrawPile.draw(); 
		GameExecutorService.printCard(card); 
		int input = 0;
		if(isThrowable(card, actionColor, actionText)){ 
			do{
				System.out.print(" || Keep(1) or Throw(2)");
				input = GameExecutorService.scanner.nextInt(); 
			}while (input != 1 && input != 2);
			
			if (input == 1){
				this.cards.add(card); 
				return new Card(actionColor, actionText); 
			}else{
				this.cards.add(card); 
				return this.throwCard(card); 
			}
		}else{
			this.cards.add(card); 
			return new Card(actionColor, actionText); 
		}
	}

	public Card play(Color actionColor, Text actionText) throws NoMoreCardException{
		//check for last played card 
		//any card - number+color
		Card updatedCard = decide(actionColor, actionText); 
		return updatedCard; 
	}

}

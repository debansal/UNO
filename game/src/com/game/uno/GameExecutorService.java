package com.game.uno;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameExecutorService {

	private List<Player> players; 
	private Color actionColor; 
	private Text actionText; 
	private DrawPile drawPile; 
	public static Scanner scanner = new Scanner(System.in);
	
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public Color getActionColor() {
		return actionColor;
	}
	public void setActionColor(Color actionColor) {
		this.actionColor = actionColor;
	}
	public Text getActionText() {
		return actionText;
	}
	public void setActionText(Text actionText) {
		this.actionText = actionText;
	}
	public DrawPile getDrawPile() {
		return this.drawPile;
	}
	public void setDrawPile(DrawPile drawPile) {
		this.drawPile = drawPile;
	}
	public static Scanner getScanner() {
		return scanner;
	}
	public static void setScanner(Scanner scanner) {
		GameExecutorService.scanner = scanner;
	}
	
	private void shuffleCards(){
		this.drawPile.shuffle(); 
	}
	
	private void distributeCards() throws NoMoreCardException{
		//assign 7 cards to players, remaining will be added to drawpile
		for(Player player : players){
			player.addCards(7); 
		}
	}
	
	public void initializeGame() throws NoMoreCardException{
		initializeCards(); 
		initializePlayers(); 
		
		shuffleCards(); 
		distributeCards(); 
		
		int id = 1;
		//print player names 
		for (Player player : players) {
			System.out.println("Player " +id + " : " + player.getName());
			for (Card c : player.getCards()){
				printCard(c); 
				System.out.print(", "); 
			}
			id++;
			System.out.print("\n"); 
		}
		
		//draw top card from pile 
		Card topCard = DrawPile.firstDraw();
		this.actionColor = topCard.getColor();
		this.actionText = topCard.getText(); 
		
		//start with player 1 
		startGame(); 
		scanner.close(); 
	}
	
	//initialize game with 108 cards 
	private void initializeCards(){ 
		this.drawPile = new DrawPile();
		List<Card> cards = new ArrayList<>(); 
		
		// each color have One 0, two 2, 3, 4, 5, 6, 7, 8, 9, rev..., Skip, Plus_2 
		for (Color color : Color.values()){
			for (Text text : Text.values()) {
				if(text == Text.WILD || text == Text.PLUS_4){
					continue; 
				}
				cards.add(new Card(color, text)); 
				if(text != Text.ZERO){
					cards.add(new Card(color, text)); 
				}
			}
		}
		
		cards.add(new Card(null, Text.PLUS_4)); 
		cards.add(new Card(null, Text.PLUS_4)); 
		cards.add(new Card(null, Text.PLUS_4)); 
		cards.add(new Card(null, Text.PLUS_4)); 
		
		cards.add(new Card(null, Text.WILD)); 
		cards.add(new Card(null, Text.WILD)); 
		cards.add(new Card(null, Text.WILD)); 
		cards.add(new Card(null, Text.WILD));
		
		drawPile.setCards(cards); 
		
		return;
	}	

	private void initializePlayers(){
		this.players = new ArrayList<>();
		System.out.print("Number of Players : ");
		int numberOfPlayers = Integer.parseInt(scanner.nextLine()); //scanner.nextInt(); 
		for (int id = 1; id<=numberOfPlayers; id++) {
			System.out.print("Enter Name of Player " + id + " : " ); 
			players.add(new Player(scanner.nextLine())); 
		}
		return; 
	}

	/**
	 * Issue 1
	 *
		for( int id = 1; id<=number0fPlayers; id++) {
		 	System.out.print("Enter Name of Player + id + : );
		 	players.add(new Player(scanner.nextLine())); 
		}
		Input - number0fPlayers =2 
		
		output - Enter Name of Player 1: Enter Name of Player 2: 
		exit for loop 
	
		Question - why it asked for only one scanner input. It should be two 
		Why scanner.nextLine not working in for loop
		 
		this is not the issue of nextline
		it is issue of nextInt, nextInt do not consume \n char and uses it in next input
		this is why when ever we get next input it takes previous \n as input and go for next 

	**/
						
	private void startGame(){
		try{
			boolean flag = true; 
			//ListIterator<Player> = players.listIterator(); 
			//Player player =itr.next(); 
			int currentIndex = 0; 
			Player player = players.get(currentIndex); 
			Boolean isClockWise = true;
			while(flag){
				printPlayerDetails(player);
				System.out.print("\nCurrent Action Card : "); 
				printCard(new Card(this.actionColor, this.actionText)); 
				System.out.println(" ");
				
				Card updateCard = player.play(this.actionColor, this.actionText); 
				if(actionText.equals(Text.REVERSE)){
					isClockWise = !isClockWise; 
				}
				if (player.getCards().size() == 0){
					System.out.println("Player " + player.getName() + " is winner");
					flag = false; 
					continue;
				}else if(player.getCards().size() == 1){
					System.out.println("Last Card.."); 
				}
				
				currentIndex = getNextPlayer(isClockWise, currentIndex); 
				player = players.get(currentIndex); 
				
				this.actionColor = updateCard.getColor();
				this.actionText = updateCard.getText();
				player = playWildCards(currentIndex, player, isClockWise); 
				
				currentIndex = players.indexOf(player); 
				System.out.println("\n********************************************************************************************************** \n");
			}
		}catch(NoMoreCardException e){
			System.out.println("Game Over...No more car,, t, play");
		}
	}
							
	private void printPlayerDetails(Player player) {
		System.out.print("\n********************************************************************************************************** \n");
		System.out.print("Current Player : " + player.getName() + ", Number of cards =  " + player.getCards().size() + "\n");
		for (Card c : player.getCards()){
			printCard(c); 
			System.out.print(", "); 
		}
		
	}
	private Player playWildCards(int currentPlayer, Player player, Boolean isClockWise) throws  NoMoreCardException{
	
		if(actionText.equals(Text.SKIP)){
			int newIndex = getNextPlayer(isClockWise, currentPlayer);
			player = players.get(newIndex); 
		}else if(actionText.equals(Text.PLUS_2)){
			//add +2 for next player 
			player.addCards(2);
			int newIndex = getNextPlayer(isClockWise, currentPlayer); 
			player = players.get(newIndex); 
		}else if(actionText.equals(Text.PLUS_4)){ 
			//check +4 
			player.addCards(4); 
			int newIndex = getNextPlayer(isClockWise, currentPlayer); 
			player = players.get(newIndex); 
		}
	return player; 
	}
	
	private int getNextPlayer(Boolean isClockWise, int currentIndex){
		int totalPlayers = players.size(); 
		if(isClockWise){
			if(currentIndex < totalPlayers- 1){
				return currentIndex+1;
			}else{
				currentIndex = 0;
				return currentIndex;
			}
		}else{
			if(currentIndex > 0){
				return currentIndex - 1;
			}else{
				currentIndex = totalPlayers - 1;
				return currentIndex; 
			}
		}
	}
		
	public static void printCard(Card card){
		if(card.getColor() != null){
			System.out.print(card.getColor().toString() + "_"); 
		}
		if( card.getText() != null){
			System.out.print( card.getText().toString());
		}
	}

}

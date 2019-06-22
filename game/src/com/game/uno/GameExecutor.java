package com.game.uno;

public class GameExecutor {

	public static void main(String args[]){ 
		GameExecutorService executor = new GameExecutorService(); 
		try {
			executor.initializeGame();
		}catch (NoMoreCardException e){
			System.out.println("Game Over...No more cards to play");
		}
	}
}

package dev.lisek.crazybytes.entity;

public interface CrazyEightsPlayer {
    void playCard(Card card);
    void drawCard(Card card);
    void passTurn(int turn);
    int collectPayment();
}


//void startGame();
// face-up first card in deck
//    void endGame();
//    void checkForWinner();
//Deck shuffleDeck();
// Card dealCards(int numCards);
//Player getWinner();

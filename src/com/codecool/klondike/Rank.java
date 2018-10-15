package com.codecool.klondike;

public enum Rank {
    ACE(1, "Ace"),
    TWO(2, "Two"),
    THREE(3, "Three"),
    FOUR(4, "Four"),
    FIVE(5, "Five"),
    SIX(6, "Six"),
    SEVEN(7, "Seven"),
    EIGHT(8, "Eight"),
    NINE(9, "Nine"),
    TEN(10, "Ten"),
    JACK(11, "Jack"),
    QUEEN(12, "Queen"),
    KING(13, "King");




    private int cardValue;



    private String cardName;

    Rank(int cardValue, String cardName){
        this.cardValue=cardValue;
        this.cardName=cardName;
    }

    public int getCardValue() {
        return cardValue;
    }

    public String getCardName() {
        return cardName;
    }



}

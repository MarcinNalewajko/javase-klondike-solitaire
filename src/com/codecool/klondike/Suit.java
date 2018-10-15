package com.codecool.klondike;

public enum Suit {
    DIAMONDS(1,"diamonds"),
    HEARTS(2,"hearts"),
    SPADES(3,"spades"),
    CLUBS(4,"clubs");



    private int cardSuitValue;
    private String cardSuitName;

    Suit (int cardSuitValue, String cardSuitName){
        this.cardSuitValue=cardSuitValue;
        this.cardSuitName=cardSuitName;
    }

    public int getCardSuitValue() {
        return cardSuitValue;
    }

    public String getCardSuitName() {
        return cardSuitName;
    }

}




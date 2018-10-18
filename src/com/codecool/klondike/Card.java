


package com.codecool.klondike;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.*;

import static java.util.Collections.*;

public class Card extends ImageView {

    private Suit suit;
    private Rank rank;


    private boolean faceDown;

    public Image getBackFace() {
        return backFace;
    }

    public Image getFrontFace() {
        return frontFace;
    }

    public static Image getCardBackImage() {
        return cardBackImage;
    }

    private Image backFace;
    private Image frontFace;
    private Pile containingPile;
    private DropShadow dropShadow;



    public static Image cardBackImage;
    private static final Map<String, Image> cardFaceImages = new HashMap<>();
    public static final int WIDTH = 150;
    public static final int HEIGHT = 215;

    public Card(Suit suit, Rank rank, boolean faceDown) {
        this.suit = suit;
        this.rank = rank;
        this.faceDown = faceDown;
        this.dropShadow = new DropShadow(2, Color.gray(0, 0.75));
        backFace = cardBackImage;
        frontFace = cardFaceImages.get(getShortName());
        setImage(faceDown ? backFace : frontFace);
        setEffect(dropShadow);
    }


    public static Map<String, Image> changeCardback(){
        return cardFaceImages;

    };
    public void changeCardback(Card card,Image image){
        card.backFace=image;

    }

    public void changeCardfront(Card card){
        card.frontFace=cardFaceImages.get(getShortName());

    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isFaceDown() {
        return faceDown;
    }

    public String getShortName() {
        return "S" + suit.getCardSuitValue() + "R" + rank.getCardValue();
    }

    public DropShadow getDropShadow() {
        return dropShadow;
    }



    public Pile getContainingPile() {
        return containingPile;
    }

    public void setContainingPile(Pile containingPile) {
        this.containingPile = containingPile;
    }


    public void moveToPile(Pile destPile) {
        this.getContainingPile().getCards().remove(this);
        destPile.addCard(this);
    }


    public void flip() {
        faceDown = !faceDown;
        setImage(faceDown ? backFace : frontFace);
    }


    @Override
    public String toString() {
        return "The " + rank + " of " + suit;
    }


    public static boolean isOppositeColor(Card card1, Card card2) {
        if (card1.suit.getCardSuitValue() <= 2){
            if (card2.suit.getCardSuitValue() > 2){return true;}
            else {return false;}
        }
        else{
            if (card2.suit.getCardSuitValue() <= 2){return true;}
            else {return false;}
        }

    }


    public static boolean isSameSuit(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit();
    }

    public static List<Card> createNewDeck() {
        List<Card> result = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank cardRank: Rank.values()){
                result.add(new Card(suit, cardRank, true));
            }
        }
        Collections.shuffle(result);
        return result;
    }


    public static void loadCardImages(String pathBack, String pathFront) {
        cardBackImage = new Image(pathBack+"card_back.png", WIDTH,HEIGHT, false,true);
        for (Suit suit : Suit.values()) {
            for (Rank cardRank: Rank.values()){
                String cardName = suit.getCardSuitName() + cardRank.getCardValue();
                String cardId = "S" + suit.getCardSuitValue() + "R" + cardRank.getCardValue();

                String imageFileName = pathFront + cardName + ".png";
                cardFaceImages.put(cardId, new Image(imageFileName,WIDTH,HEIGHT, false,true));
            }
        }
    }
}



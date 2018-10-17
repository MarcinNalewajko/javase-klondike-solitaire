package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;

import javax.sound.midi.Soundbank;
import java.util.*;

public class Game extends Pane {

    private List<Card> deck = new ArrayList<>();

    private Pile stockPile;
    private Pile discardPile;
    private List<Pile> foundationPiles = FXCollections.observableArrayList();
    private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<Card> draggedCards = FXCollections.observableArrayList();

    private static double STOCK_GAP = 1;
    private static double FOUNDATION_GAP = 0;
    private static double TABLEAU_GAP = 30;


    private EventHandler<MouseEvent> onMouseClickedHandler = e -> {
        Card card = (Card) e.getSource();
        if (card.getContainingPile().getPileType() == Pile.PileType.STOCK) {
            card.moveToPile(discardPile);
            card.flip();
            card.setMouseTransparent(false);
            System.out.println("Placed " + card + " to the waste.");

        }
    };

    private EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
        refillStockFromDiscard();
    };

    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        Card card = (Card) e.getSource();
        Pile activePile = card.getContainingPile();

        if (card.isFaceDown())
            return;

        if (activePile.getPileType() == Pile.PileType.STOCK)
            return;

        double offsetX = e.getSceneX() - dragStartX;
        double offsetY = e.getSceneY() - dragStartY;
        draggedCards.clear();
        boolean canDrag = false;
        for (Card card1: activePile.getCards()) {
            if (card1 == card) { canDrag = true; }
            if (canDrag) {
                draggedCards.add(card1);
                card1.getDropShadow().setRadius(20);
                card1.getDropShadow().setOffsetX(10);
                card1.getDropShadow().setOffsetY(10);

                card1.toFront();
                card1.setTranslateX(offsetX);
                card1.setTranslateY(offsetY);
            }
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
        if (draggedCards.isEmpty())
            return;
        Card card = (Card) e.getSource();
        //TODO
        Pile pile = getValidIntersectingPile(card, tableauPiles);
        Pile pileUpper = getValidIntersectingPile(card, foundationPiles);
        if (pile != null) {
            handleValidMove(card, pile);

        } else if (pileUpper != null) {
            handleValidMove(card, pileUpper);

            if (isGameWon()) {
                System.out.println("WON WON WON");
            }
        } else {
            draggedCards.forEach(MouseUtil::slideBack);
            draggedCards.clear();
        }
    };


    public boolean isGameWon() {
        //TODO
        for (int it = 0; it < foundationPiles.size(); it++) {
            if ((foundationPiles.get(it).isEmpty()) || (foundationPiles.get(it).getTopCard().getRank() != Rank.KING))  {
                break;
            } else {
                if (it == 3) { return true;}
            }
        }
        return false;
    }

    public Game() {
        deck = Card.createNewDeck();
        initPiles();
        dealCards();
    }

    public void addMouseEventHandlers(Card card) {
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        card.setOnMouseReleased(onMouseReleasedHandler);
        card.setOnMouseClicked(onMouseClickedHandler);
    }

    public void refillStockFromDiscard() {
        //TODO
        List<Card> cardsToRefill=discardPile.getCards();
        Collections.reverse(cardsToRefill);
        stockPile.clear();

        for (Card card: cardsToRefill){
            card.flip();
            stockPile.addCard(card);
        }
        discardPile.clear();
        System.out.println("Stock refilled from discard pile.");
    }

    public boolean isMoveValid(Card card, Pile destPile) {
        //TODO
        Pile.PileType pileType = destPile.getPileType();
        Rank cardRank = card.getRank();
        Card topCard = destPile.getTopCard();
        if (destPile.isEmpty()) {

            if ((pileType == Pile.PileType.FOUNDATION) && (cardRank == Rank.ACE)) {
                return true;
            } else if ((cardRank == Rank.KING) && (pileType == Pile.PileType.TABLEAU)) {
                return true;
            }
        } else {

            if (pileType == Pile.PileType.FOUNDATION) {
                if ((topCard.getRank().getCardValue() == cardRank.getCardValue() - 1) &&
                    ((Card.isSameSuit(card, topCard)))){
                    return true;
                }
            } else {
                if ((topCard.getRank().getCardValue() == cardRank.getCardValue() + 1) &&
                        ((Card.isOppositeColor(card, topCard)))){
                    return true;
                }
            }
        }
        return false;
    }
    private Pile getValidIntersectingPile(Card card, List<Pile> piles) {
        Pile result = null;

        for (Pile pile : piles) {
            if (!pile.equals(card.getContainingPile()) &&
                    isOverPile(card, pile) &&
                    isMoveValid(card, pile))
                result = pile;
        }
        return result;
    }

    private boolean isOverPile(Card card, Pile pile) {
        if (pile.isEmpty())
            return card.getBoundsInParent().intersects(pile.getBoundsInParent());
        else
            return card.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
    }

    private void flipTopOfThePreviousPile(Card card, List<Card> draggedCards) {
        System.out.println("Dragged cards size: " + Integer.toString(draggedCards.size()));

        List<Card> cardsFromPreviousPile = card.getContainingPile().getCards();
        System.out.println("cardsFromPreviousPile size: " + Integer.toString(cardsFromPreviousPile.size()));

        int indexOfACardToFlip = cardsFromPreviousPile.size() - (draggedCards.size() + 1);

        if (indexOfACardToFlip < 0) {indexOfACardToFlip = 0;}
        System.out.println("indexOfACardToFlip: " + Integer.toString(indexOfACardToFlip));

        if ((card.getContainingPile().getPileType() == Pile.PileType.TABLEAU) &&
                (card.getContainingPile().numOfCards() > 1) &&
                (cardsFromPreviousPile.get(indexOfACardToFlip).isFaceDown())) {
            cardsFromPreviousPile.get(indexOfACardToFlip).flip();
        }
    }

    private void handleValidMove(Card card, Pile destPile) {
        String msg = null;
        if (destPile.isEmpty()) {
            if (destPile.getPileType().equals(Pile.PileType.FOUNDATION))
                msg = String.format("Placed %s to the foundation.", card);
            if (destPile.getPileType().equals(Pile.PileType.TABLEAU))
                msg = String.format("Placed %s to a new pile.", card);
        } else {
            msg = String.format("Placed %s to %s.", card, destPile.getTopCard());
        }
        System.out.println(msg);
        // MouseUtil.slideToDest(draggedCards, destPile);       this had to be disabled

        flipTopOfThePreviousPile(card, draggedCards);

        for (Card draggedCard: draggedCards) {
              draggedCard.moveToPile(destPile);
        }
        draggedCards.clear();
    }


    private void initPiles() {
        stockPile = new Pile(Pile.PileType.STOCK, "Stock", STOCK_GAP);
        stockPile.setBlurredBackground();
        stockPile.setLayoutX(95);
        stockPile.setLayoutY(20);
        stockPile.setOnMouseClicked(stockReverseCardsHandler);
        getChildren().add(stockPile);


        discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setBlurredBackground();
        discardPile.setLayoutX(285);
        discardPile.setLayoutY(20);
        getChildren().add(discardPile);

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            foundationPile.setBlurredBackground();
            foundationPile.setLayoutX(610 + i * 180);
            foundationPile.setLayoutY(20);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(Pile.PileType.TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setBlurredBackground();
            tableauPile.setLayoutX(95 + i * 180);
            tableauPile.setLayoutY(275);
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }
    }

    public void dealCards() {

        for(int i = 1; i < tableauPiles.size() + 1; i++) {
            ArrayList<Card> dealPiles = new ArrayList<>(deck.subList(0, i));

            for (int j = 0; j < dealPiles.size(); j++) {
                tableauPiles.get(i - 1).addCard(dealPiles.get(j));

                addMouseEventHandlers(dealPiles.get(j));
                getChildren().add(dealPiles.get(j));
                deck.remove(dealPiles.get(j));

            }
            tableauPiles.get(i-1).getTopCard().flip();

        }


        Iterator<Card> deckIterator = deck.iterator();
        deckIterator.forEachRemaining(card -> {
            stockPile.addCard(card);
            addMouseEventHandlers(card);
            getChildren().add(card);
        });
    }

    public void setTableBackground(Image tableBackground) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

}

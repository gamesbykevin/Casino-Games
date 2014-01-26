package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Hand extends Sprite implements Disposable
{
    //list of cards in our hand
    private List<Card> cards;

    //this will determine how the cards are to be displayed
    private CardDisplay display;
    
    /**
     * How to display the collection of cards
     */
    public enum CardDisplay
    {
        Horizontal, Vertical, None
    }
    
    public Hand()
    {
        //list of cards
        this.cards = new ArrayList<>();
        
        //default to none
        this.display = CardDisplay.None;
    }
    
    /**
     * Determine how the cards are to be displayed
     * @param display Horizontal, Vertical, etc.....
     */
    public void setDisplay(final CardDisplay display)
    {
        this.display = display;
    }
    
    public CardDisplay getDisplay()
    {
        return this.display;
    }
    
    public List<Card> getCards()
    {
        return this.cards;
    }
    
    /**
     * Count the number of cards that have reached their destination
     * @return 
     */
    public int getPlacedCount()
    {
        int count = 0;
        
        for (Card card : getCards())
        {
            //this card has been placed so add to count
            if (card.getPoint().equals(card.getDestination()))
                count++;
        }
        
        return count;
    }
    
    /**
     * Get the number of cards in our collection
     * @return number of cards in our collection
     */
    public int getSize()
    {
        return getCards().size();
    }
    
    /**
     * Does the number of cards equal the parameter limit.
     * @param limit The user specified limit of cards this hand can hold
     * @return True if the total number of cards equals the user specified limit
     */
    public boolean hasLimit(final int limit)
    {
        return (getSize() == limit);
    }
    
    /**
     * Add card to player's hand
     * @param card 
     */
    public void add(final Card card)
    {
        //add card to hand
        getCards().add(card);
    }
    
    /**
     * Checks all the players cards in their hand.
     * @return True if all the card's locations are also at their assigned destinations, false otherwise
     */
    public boolean hasReachedDestination()
    {
        for (Card card : getCards())
        {
            if (card.getPoint().x != card.getDestination().x)
                return false;
            
            if (card.getPoint().y != card.getDestination().y)
                return false;
        }
        
        return true;
    }
    
    /**
     * Are any cards moving?
     * @return True if any of the cards are not at their destination, false otherwise
     */
    public boolean hasActiveCard()
    {
        return (getActiveCard() != null);
    }
    
    /**
     * Locate a card in the players hand that has not reached its destination
     * @return Card where the location is not its destination, if none found null will be returned
     */
    public Card getActiveCard()
    {
        for (Card card : getCards())
        {
            if (card.getPoint().x != card.getDestination().x)
                return card;
            
            if (card.getPoint().y != card.getDestination().y)
                return card;
        }
        
        return null;
    }
    
    /**
     * Move the card towards the destination a x amount of speed
     * @param card Card we want to move towards destination
     * @param velocityX x pixels
     * @param velocityY y pixels
     */
    public void moveCard(final Card card, final int velocityX, final int velocityY)
    {
        //the pixel distance
        int xDifference = (int)(card.getX() - card.getDestination().x);
        int yDifference = (int)(card.getY() - card.getDestination().y);

        if (xDifference < 0)
            xDifference = -xDifference;
        if (yDifference < 0)
            yDifference = -yDifference;

        //if we are soo close set to destination
        if (xDifference < velocityX)
            card.setX(card.getDestination().x);
        if (yDifference < velocityY)
            card.setY(card.getDestination().y);

        //move card towards destination
        if (card.getX() < card.getDestination().x)
            card.setX(card.getX() + velocityX);
        if (card.getY() < card.getDestination().y)
            card.setY(card.getY() + velocityY);
        if (card.getX() > card.getDestination().x)
            card.setX(card.getX() - velocityX);
        if (card.getY() > card.getDestination().y)
            card.setY(card.getY() - velocityY);
    }
    
    /**
     * Move the active card towards the destination.
     * @param velocityX x pixel speed
     * @param velocityY y pixel speed
     */
    public void moveActiveCard(final int velocityX, final int velocityY)
    {
        moveCard(getActiveCard(), velocityX, velocityY);
    }
    
    /**
     * Move all of the cards toward their destination.
     * @param velocityX x pixel speed
     * @param velocityY y pixel speed
     */
    public void moveActiveCards(final int velocityX, final int velocityY)
    {
        for (Card card : getCards())
        {
            moveCard(card, velocityX, velocityY);
        }
    }
    
    /**
     * Get random card from deck
     * @param random Object used to make random decisions
     * @return Get random card
     */
    public Card get(final Random random)
    {
        return get(random.nextInt(getCards().size()));
    }
    
    /**
     * Remove this card from the deck, if this card is in the deck more than once only the first card found is removed
     * @param card 
     */
    public void remove(final Card card)
    {
        remove(card.getSuit(), card.getValue());
    }
    
    /**
     * Remove this card from the deck, if this card is in the deck more than once only the first card found is removed
     * @param suit
     * @param value 
     */
    public void remove(final Card.Suit suit, final Card.Value value)
    {
        //search deck for the card we want to remove
        for (int index = 0; index < getSize(); index++)
        {
            if (getCards().get(index).equals(suit, value))
            {
                //remove card from index
                getCards().remove(index);
                
                //card has been removed so no need to continue
                break;
            }
        }
    }
    
    /**
     * Get the index of the card selected
     * @param point (x, y) coordinate
     * @return Card that contains (x, y) coordinate, if not found -1 is returned
     */
    public int getIndex(final Point point)
    {
        //start at end of index
        for (int index = getSize() - 1; index >= 0; index--)
        {
            if (get(index).getRectangle().contains(point))
                return index;
        }
        
        return -1;
    }
    
    public Card get(final int index)
    {
        return getCards().get(index);
    }
    
    public Card get(final Card card)
    {
        return get(card.getSuit(), card.getValue());
    }
    
    /**
     * Get a card with the matching suit
     * @param suit The suit we are looking for
     * @return Card that has the same parameter suit. If the card is not found null will be returned
     */
    public Card get(final Card.Suit suit)
    {
        for (Card card : getCards())
        {
            if (card.equalsSuit(suit))
                return card;
        }
        
        return null;
    }

    /**
     * Get a card with the matching face value
     * @param value The suit we are looking for
     * @return Card that has the same parameter value. If the card is not found null will be returned
     */
    public Card get(final Card.Value value)
    {
        for (Card card : getCards())
        {
            if (card.equalsValue(value))
                return card;
        }
        
        return null;
    }
    
    /**
     * Get card of the specified suit and face value
     * @param suit
     * @param value
     * @return Card that matched suit and face value. If not found null is returned
     */
    public Card get(final Card.Suit suit, final Card.Value value)
    {
        for (Card card : getCards())
        {
            if (card.equals(suit, value))
                return card;
        }
        
        return null;
    }
    
    /**
     * Mix the cards in the hand at random
     * @param random Object used to make random decisions
     */
    public void shuffle(final Random random)
    {
        //objects used to hold the cards temporary
        Card tmp1, tmp2;
        
        //move each card in the deck to a random location
        for (int index = 0; index < getCards().size(); index++)
        {
            //get the card at the current position
            tmp1 = get(index);

            //get a random position
            int position = random.nextInt(getCards().size());

            //get card from the random location
            tmp2 = get(position);

            //swap the cards
            getCards().set(index,    tmp2);
            getCards().set(position, tmp1);
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        for (Card card : getCards())
        {
            if (card != null)
                card.dispose();
            
            card = null;
        }
        
        cards.clear();
        cards = null;
    }
    
    /**
     * Take into consideration all of the existing cards and return the next x,y location
     * @param width Width of a single card
     * @return Location where next card is to be placed.
     */
    public Point getDestination(final int width)
    {
        //the number of pixels to offset new destination
        final int offset = (int)(getSize() * (width * .3));
        
        switch(getDisplay())
        {
            case Vertical:
                return new Point((int)getX(), (int)getY() + offset);

            case Horizontal:
            case None:
            default:
                return new Point((int)getX() + offset, (int)getY());
        }
    }
    
    /**
     * Draw the hand
     * @param graphics
     * @param image 
     */
    public void render(final Graphics graphics, final Image image)
    {
        //if there is a background image draw it
        if (super.getImage() != null)
            super.draw(graphics);
        
        for (Card card : getCards())
        {
            //draw the card
            card.render(graphics, image);
        }
    }
}
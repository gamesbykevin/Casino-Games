package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.framework.base.Sprite;

import java.awt.Point;

public final class Card extends Sprite
{
    public enum Suit
    {
        Hearts,
        Diamonds,
        Clubs,
        Spades;
    }
    
    public enum Value
    {
        Ace,
        Two,
        Three,
        Four, 
        Five,
        Six,
        Seven,
        Eight,
        Nine,
        Ten,
        Jack,
        Queen,
        King;
    }
    
    /**
     * Can we display the face value of this card or should it be hidden
     */
    public enum State
    {
        Display, Hidden
    }
    
    //what suit does this card come from
    private final Suit suit;
    
    //what is the face value of the card
    private final Value value;
    
    //the x,y coordinate where the card should be
    private final Point destination;
    
    //this is so we can determine which player this card came from
    private long playerId = 0;
    
    public Card(final Suit suit, final Value value)
    {
        this.suit = suit;
        this.value = value;
        
        //the destination will be where we want the card to go
        this.destination = new Point();
        
        //create spritesheet so we can add animations
        super.createSpriteSheet();
    }
    
    public void setPlayerId(final long playerId)
    {
        this.playerId = playerId;
    }
    
    /**
     * Get the id of the player this card came from
     * @return Id of the player, if no player is assigned this card 0 is returned
     */
    public long getPlayerId()
    {
        return this.playerId;
    }
    
    /**
     * Set the destination where the card should go.
     * @param destination 
     */
    public void setDestination(final Point destination)
    {
        setDestination(destination.x, destination.y);
    }
    
    /**
     * Set the destination where the card should go.
     * @param x
     * @param y 
     */
    public void setDestination(final int x, final int y)
    {
        this.destination.setLocation(x, y);
    }
    
    public Point getDestination()
    {
        return this.destination;
    }
    
    /**
     * Determine if we want to display this card or not
     * @param state Display or Hidden
     */
    public void setState(final State state)
    {
        super.getSpriteSheet().setCurrent(state);
    }
    
    /**
     * Get the state of this card. <br>
     * Currently the state will be display or hidden.
     * @return The state of the card.
     */
    public State getState()
    {
        return (State)super.getSpriteSheet().getCurrent();
    }
    
    /**
     * Can we display the card.
     * @return True if the card can be displayed, false otherwise
     */
    public boolean canDisplay()
    {
        return (getState() == State.Display);
    }
    
    /**
     * Get the suit of this card
     * @return The suit
     */
    public Suit getSuit()
    {
        return this.suit;
    }
    
    /**
     * Get the face value of this card
     * @return The face value
     */
    public Value getValue()
    {
        return this.value;
    }
    
    /**
     * Does the card match the specified parameters
     * @param suit
     * @param value
     * @return True if the suit and value are the same, false otherwise
     */
    public boolean equals(final Suit suit, final Value value)
    {
        return (getSuit() == suit && getValue() == value);
    }
    
    /**
     * Does the card match the specified parameters
     * @param card
     * @return True if the suit and value are the same, false otherwise
     */
    public boolean equals(final Card card)
    {
        return equals(card.getSuit(), card.getValue());
    }
}
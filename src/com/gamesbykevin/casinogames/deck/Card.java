package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.casinogames.deck.Hand.CardDisplay;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.Point;
import java.awt.geom.AffineTransform;

public final class Card extends Sprite
{
    //object used to rotate image
    private AffineTransform transform;
    
    private CardDisplay display;
    
    public enum Suit
    {
        Hearts,
        Diamonds,
        Clubs,
        Spades;
        
        //the numeric rank
        private int rank = 0;
        
        public void setRank(final int rank)
        {
            this.rank = rank;
        }
        
        public int getRank()
        {
            return this.rank;
        }
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
        
        //the numeric rank
        private int rank = 0;
        
        public void setRank(final int rank)
        {
            this.rank = rank;
        }
        
        public int getRank()
        {
            return this.rank;
        }
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
        
        //default
        this.display = CardDisplay.None;
        
        //the destination will be where we want the card to go
        this.destination = new Point();
        
        //object that can rotate card image
        this.transform = new AffineTransform();
        
        //create spritesheet so we can add animations
        super.createSpriteSheet();
    }
    
    /**
     * Determine how the cards are to be displayed
     * @param display Horizontal, Vertical, etc.....
     */
    public void setDisplay(final CardDisplay display)
    {
        this.display = display;
    }
    
    private CardDisplay getDisplay()
    {
        return this.display;
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
     * Does the parameter suit equal the suit of this card
     * @param suit The suit
     * @return True if the suit matches, false otherwise
     */
    public boolean equalsSuit(final Suit suit)
    {
        return (getSuit() == suit);
    }
    
    /**
     * Does the parameter value equal the value of this card
     * @param value The face value
     * @return True if the value matches, false otherwise
     */
    public boolean equalsValue(final Value value)
    {
        return (getValue() == value);
    }
    
    /**
     * Does the card match the specified parameters
     * @param suit
     * @param value
     * @return True if the suit and value are the same, false otherwise
     */
    public boolean equals(final Suit suit, final Value value)
    {
        return (equalsSuit(suit) && equalsValue(value));
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
    
    /**
     * Draw the hand
     * @param graphics
     * @param image 
     */
    public void render(final Graphics graphics, final Image image)
    {
        //need to use graphics 2d in order to do rotation
        Graphics2D g2d = (Graphics2D)graphics;
        
        //reset object used to rotate graphics
        transform.setToIdentity();;
            
        switch(getDisplay())
        {
            case Vertical:

                //set rotation accordingly around the anchor coordinates
                transform.setToRotation(Math.toRadians(90), super.getCenter().x, super.getCenter().y);

                //set graphics object to have transformation
                g2d.setTransform(transform);

                //draw card
                super.draw(g2d, image);
                break;

            case Horizontal:
            case None:
            default:

                //draw card
                super.draw(g2d, image);
                break;
        }
        
        //reset rotation etc...
        transform.setToIdentity();
        
        //set to graphics object
        g2d.setTransform(transform);
    }
}
package com.gamesbykevin.casinogames.deck;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.Card.*;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public abstract class Deck extends Sprite implements Disposable
{
    //all the cards in the deck are located here
    private Hand hand;
    
    //this will contain a list of all the mappings for each card and hidden top
    private List<Mapping> mappings;
    
    //the dimensions of each card on the image so we can map the image(s)
    private final int sourceWidth, sourceHeight;
    
    //what is the width/height ratio
    private final float ratio;
    
    //some sprite sheets may have empty pixels between each card
    private int pixelSpacing = 0;
    
    protected Deck(final int sourceWidth, final int sourceHeight)
    {
        //create new hand
        this.hand = new Hand();
        
        //the original dimensions of each card on the image
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        
        //calculate the width/height ratio
        this.ratio = (float)(sourceHeight / sourceWidth);
        
        //this is where we will keep all coordinates for the card animations
        this.mappings = new ArrayList<>();
    }
    
    /**
     * Get the width/height ratio
     * @return Ratio
     */
    public float getRatio()
    {
        return this.ratio;
    }
    
    protected void setPixelSpacing(final int pixelSpacing)
    {
        this.pixelSpacing = pixelSpacing;
    }
    
    @Override
    public void dispose()
    {
        hand.dispose();
        hand = null;
        
        for (Mapping mapping : mappings)
        {
            if (mapping != null)
                mapping = null;
        }
        
        mappings.clear();
        mappings = null;
    }
    
    /**
     * Get the width of each image on the sprite sheet
     * @return The width of each image on the sprite sheet
     */
    public int getSourceWidth()
    {
        return this.sourceWidth;
    }
    
    /**
     * Get the height of each image on the sprite sheet
     * @return The height of each image on the sprite sheet
     */
    public int getSourceHeight()
    {
        return this.sourceHeight;
    }
    
    /**
     * Each child of deck will need to map out the coordinates for each card
     */
    protected abstract void map();
    
    /**
     * Get the object containing all of the cards in our hand
     * @return Self Explanatory
     */
    public Hand getHand()
    {
        return this.hand;
    }
    
    /**
     * Add card to the deck
     * @param suit The suit of the card
     * @param value The face value of the card
     */
    public void add(final Suit suit, final Value value)
    {
        //create new card
        final Card card = new Card(suit, value);
        
        //add face value animation to sprite sheet
        card.getSpriteSheet().add(new Animation(getMapping(card).getCoordinate(), 0), State.Display);
        
        //add hidden animation
        card.getSpriteSheet().add(new Animation(getMapping(null, null).getCoordinate(), 0), State.Hidden);
        
        //default the cards to be hidden
        card.getSpriteSheet().setCurrent(State.Hidden);
    
        //set the location/destination of the cards
        card.setLocation(getX(), getY());
        card.setDestination((int)getX(), (int)getY());
        
        //add card to list
        getHand().add(card);
    }
        
    private Mapping getMapping(final Card card)
    {
        return getMapping(card.getSuit(), card.getValue());
    }

    private Mapping getMapping(final Suit suit, final Value value)
    {
        for (Mapping mapping : mappings)
        {
            if (mapping.getSuit() == suit && mapping.getValue() == value)
                return mapping;
        }
        
        return null;
    }
    
    /**
     * Make sure all card mappings have been set
     * @throws Exception If one card is missing mapping throw exception.
     */
    protected void verifyMapping() throws Exception
    {
        for (Suit suit : Suit.values())
        {
            for (Value value : Value.values())
            {
                //if mapping does not exist throw exception
                if (getMapping(suit, value) == null)
                    throw new Exception("Card = " + suit.toString() + ":" + value.toString() + " is not mapped.");
            }
        }
        
        if (getMapping(null, null) == null)
            throw new Exception("Hidden card is not mapped.");
    }
    
    /**
     * This class will keep track of the coordinates for each card
     */
    private class Mapping
    {
        private final Suit suit;
        private final Value value;
        private final Rectangle coordinate;
        
        private Mapping(final Suit suit, final Value value, final Rectangle coordinate)
        {
            this.suit = suit;
            this.value = value;
            this.coordinate = coordinate;
        }
        
        private Suit getSuit()
        {
            return this.suit;
        }
        
        private Value getValue()
        {
            return this.value;
        }
        
        private Rectangle getCoordinate()
        {
            return this.coordinate;
        }
    }
    
    /**
     * Add mapping of card to list
     * @param suit Suit of card
     * @param value Face value of card
     * @param column Where are card coordinates located.
     * @param row Where are card coordinates located.
     */
    protected void addMapping(final Suit suit, final Value value, final int column, final int row)
    {
        mappings.add(new Mapping(suit, value, getCoordinates(column, row)));
    }
    
    /**
     * Get the x, y, width, height coordinates from the source image.<br>
     * @return Depending on the column, row location and source width, source height the rectangle will vary.
     */
    private Rectangle getCoordinates(final int column, final int row)
    {
        final int x = (column * getSourceWidth())  + (column * pixelSpacing);
        final int y = (row    * getSourceHeight()) + (row    * pixelSpacing);
        
        return new Rectangle(x, y, getSourceWidth(), getSourceHeight());
    }
    
    /**
     * Draw the cards in the deck.
     * @param graphics 
     */
    public void render(final Graphics graphics)
    {
        getHand().render(graphics, getImage());
    }
}
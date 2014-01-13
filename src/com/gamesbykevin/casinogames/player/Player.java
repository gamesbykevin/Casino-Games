package com.gamesbykevin.casinogames.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Player extends Sprite implements Disposable
{
    //player's collection of cards
    private Hand hand;
    
    //is this player controlled by a human
    private boolean human = false;
    
    //unique number to identify each player
    private final long id;
    
    protected Player()
    {
        hand = new Hand();
        
        //set the unique id
        id  = System.nanoTime();
    }
    
    /**
     * Get the id assigned to this player
     * @return Id each player will have its own unique id
     */
    public long getId()
    {
        return id;
    }
    
    /**
     * Mark this person as human/non-human
     * @param human True if human, false otherwise
     */
    public void setHuman(final boolean human)
    {
        this.human = human;
    }
    
    /**
     * Is this player human
     * @return True if human, false otherwise
     */
    public boolean isHuman()
    {
        return this.human;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        hand.dispose();
        hand = null;
    }
    
    /**
     * Gets the current hand of cards the player has.
     * @return List of cards
     */
    public Hand getHand()
    {
        return this.hand;
    }
    
    /**
     * Add this card to the players hand
     * @param card The card we want to add
     */
    public void addHand(final Card card)
    {
        //assign player id so we know who the card belongs to
        card.setPlayerId(getId());
        
        //set the display for the card
        card.setDisplay(getHand().getDisplay());
        
        getHand().add(card);
    }
    
    /**
     * Need to override method to handle updates
     * @param engine 
     */
    public abstract void update(final Engine engine);
    
    /**
     * Need to override method to render the cards
     * @param graphics
     * @param image 
     */
    public abstract void render(final Graphics graphics, final Image image);
}
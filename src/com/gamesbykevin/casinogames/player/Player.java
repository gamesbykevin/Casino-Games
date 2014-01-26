package com.gamesbykevin.casinogames.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.framework.input.Mouse;

import java.awt.Graphics;
import java.awt.Image;

public abstract class Player extends Sprite implements Disposable
{
    //the speed at which the cards move while placing
    protected static final int PLACE_PIXEL_SPEED_X = 3;
    protected static final int PLACE_PIXEL_SPEED_Y = 3;
    
    //player's collection of cards
    private Hand hand;
    
    //is this player controlled by a human
    private boolean human = false;
    
    //does this player have a turn, default to false
    private boolean turn = false;
    
    //unique number to identify each player
    private final long id;
    
    //the name of our player
    private final String name;
    
    //the index of our selected card
    private int index = -1;
    
    protected Player(final String name)
    {
        //set the name of this player
        this.name = name;
        
        //create this player's hand
        this.hand = new Hand();
        
        //set the unique id
        this.id  = System.nanoTime();
    }
    
    /**
     * Get the player's name
     * @return The name assigned to the player
     */
    public String getName()
    {
        return this.name;
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
     * Switch the turn, if turn is true then it will be false and vice versa
     */
    public void switchTurn()
    {
        this.turn = !this.turn;
    }
    
    /**
     * Does this player have a turn
     * @return True if they have a turn, false otherwise
     */
    public boolean hasTurn()
    {
        return this.turn;
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
        
        //add the card to the hand
        getHand().add(card);
    }
    
    protected void update(final Engine engine)
    {
        //is the player human
        if (isHuman())
        {
            //is there a card selected
            if (hasCardSelected())
            {
                //user dragged mouse so move card with it
                if (engine.getMouse().isMouseDragged())
                {
                    final int x = (int)(engine.getMouse().getLocation().x - (getHand().get(getCardSelectedIndex()).getWidth() / 2));
                    final int y = (int)(engine.getMouse().getLocation().y - (getHand().get(getCardSelectedIndex()).getHeight() / 2));

                    getHand().get(getCardSelectedIndex()).setLocation(x, y);
                }
            }
            else
            {
                //if we have an active card move it
                if (getHand().hasActiveCard())
                {
                    getHand().moveActiveCard(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
                }
                else
                {
                    //has the mouse been pressed
                    if (engine.getMouse().isMousePressed())
                    {
                        //set the selected card based on the mouse location
                        setCardSelected(getHand().getIndex(engine.getMouse().getLocation()));
                    }
                }
            }
        }
        else
        {
            /*
            //if we have an active card move it
            if (getHand().hasActiveCard())
            {
                getHand().moveActiveCard(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
            }
            */ 
        }
    }
    
    /**
     * Reset the mouse events and card selection
     * @param mouse Object used for mouse input
     */
    protected void resetSelection(final Mouse mouse)
    {
        //we are no longer selecting a card
        resetCardSelected();

        //reset mouse events
        mouse.reset();
    }
    
    /**
     * Add card to the destination parameter and remove card from our hand
     * @param destination The destination
     * @param card The card we want to place
     */
    protected void placeCard(final Hand destination, final Card card)
    {
        //mark the location of the card as the destination
        card.setDestination(card.getPoint());

        //add card to destination
        destination.add(card);

        //remove card from the players hand
        getHand().remove(card);
    }
    
    protected int getCardSelectedIndex()
    {
        return this.index;
    }
    
    protected void resetCardSelected()
    {
        setCardSelected(-1);
    }
    
    protected void setCardSelected(final int index)
    {
        this.index = index;
    }
    
    /**
     * Does this player have a card selected
     * @return 
     */
    public boolean hasCardSelected()
    {
        return (index >= 0);
    }
    
    /**
     * Need to override method to render the cards
     * @param graphics
     * @param image 
     */
    public abstract void render(final Graphics graphics, final Image image);
}
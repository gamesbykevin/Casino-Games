package com.gamesbykevin.casinogames.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.framework.input.Mouse;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class Player extends Sprite implements Disposable
{
    //the speed for the movement
    private int pixelMoveX = 1, pixelMoveY = 1;
    
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
    
    //do we create a new status image
    private boolean resetStatusImage = true;
    
    //our status image
    private BufferedImage statusImage;
    
    //our graphics object
    private Graphics2D statusGraphics;
    
    //the player's score
    private int score = 0;
    
    protected Player(final String name)
    {
        //set the name of this player
        this.name = name;
        
        //create this player's hand
        this.hand = new Hand();
        
        //set the unique id
        this.id  = System.nanoTime();
    }
    
    public int getScore()
    {
        return this.score;
    }
    
    public void setScore(final int score)
    {
        this.score = score;
    }
    
    /**
     * Remove the display image
     */
    public void resetStatusImage()
    {
        if (statusImage != null)
        {
            //release image resources
            statusImage.flush();
        }
        
        //write to image again
        this.resetStatusImage = true;
    }
    
    protected void createStatusImage(final int width, final int height)
    {
        this.statusImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    protected BufferedImage getStatusImage()
    {
        return this.statusImage;
    }
    
    protected Graphics2D getStatusImageGraphics()
    {
        statusGraphics = this.getStatusImage().createGraphics();
        
        return this.statusGraphics;
    }
    
    protected boolean hasResetStatusImageFlag()
    {
        return this.resetStatusImage;
    }
    
    protected void switchResetStatusImageFlag()
    {
        this.resetStatusImage = !this.resetStatusImage;
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
        
        if (this.statusImage != null)
            this.statusImage.flush();
        
        this.statusImage = null;
        
        if (this.statusGraphics != null)
            this.statusGraphics.dispose();
        
        this.statusGraphics = null;
    }
    
    public void setPixelSpeed(final int x, final int y)
    {
        this.pixelMoveX = x;
        this.pixelMoveY = y;
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
                    getHand().moveActiveCard(pixelMoveX, pixelMoveY);
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
    
    /**
     * Set the index of the card we have selected
     * @param index Index where card is located
     */
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
    
    protected void drawInfo(final Graphics2D g2d, final String info, final double x, final double y)
    {
        g2d.drawString(info, (int)x, (int)y);
    }
}
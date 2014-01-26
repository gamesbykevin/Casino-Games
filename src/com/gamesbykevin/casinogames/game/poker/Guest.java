package com.gamesbykevin.casinogames.game.poker;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.player.IPlayer;
import com.gamesbykevin.casinogames.player.Player;
import com.gamesbykevin.casinogames.resources.GameAudio;

import com.gamesbykevin.framework.input.Mouse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Guest extends Player implements IPlayer
{
    //the speed at which the cards move while placing
    protected static final int PLACE_PIXEL_SPEED_X = 3;
    protected static final int PLACE_PIXEL_SPEED_Y = 3;
    
    private enum Position
    {
        North, South, East, West
    }
    
    /**
     * Logic used to play the game
     */
    private enum Logic
    {
        
    }
    
    public Guest(final String name)
    {
        super(name);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void update(final Engine engine)
    {
        //if the player is human check for mouse input
        if (isHuman())
        {
            super.update(engine);
            
            if (hasCardSelected())
            {
                //if user has let go of mouse
                if (engine.getMouse().isMouseReleased())
                {
                    //the active card that is not at its destination
                    final Card card = getHand().getActiveCard();
                    
                    //reset card selection and mouse events
                    resetSelection(engine.getMouse());
                }
            }
        }
        else
        {
            //make sure we haven't picked a card yet
            if (!getHand().hasActiveCard())
            {
            }
            else
            {
                //this is the active card
                final Card card = getHand().getActiveCard();
                
                //we have an active card so move it
                getHand().moveActiveCard(PLACE_PIXEL_SPEED_X, PLACE_PIXEL_SPEED_Y);
                
                //if we had an active card but no longer do we can now place it
                if (!getHand().hasActiveCard())
                {
                    //play sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard);
                    
                    //place card accordingly
                    placeCard(engine.getManager().getCardGame().getCardDestinations().get(0), card);

                    //no longer turn so change turn
                    engine.getManager().getCardGame().changeTurn();
                }
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics, final Image image)
    {
        //draw the player's hand
        getHand().render(graphics, image);
        
        //draw dislay info
        drawDisplay(graphics);
    }
    
    private void drawDisplay(final Graphics graphics)
    {
        
    }
}
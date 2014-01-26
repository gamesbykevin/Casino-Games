package com.gamesbykevin.casinogames.manager;

import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.spades.Spades;
import com.gamesbykevin.casinogames.game.poker.Poker;
import com.gamesbykevin.framework.menu.Menu;

import com.gamesbykevin.casinogames.deck.*;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.*;
import com.gamesbykevin.casinogames.menu.CustomMenu.*;
import com.gamesbykevin.casinogames.menu.option.*;
import com.gamesbykevin.casinogames.resources.*;
import com.gamesbykevin.casinogames.resources.GameImage.Keys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //if we are playing spades
    private Spades spades;
    
    //if we are playing 5,7 card poker
    private Poker poker;
    
    private final Mode.Types type;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //get the menu object
        final Menu menu = engine.getMenu();
        
        //get the index of the game we want to play
        final int gameIndex = menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode);
        
        //the type of game we want to play
        this.type = Mode.Types.values()[gameIndex];
        
        switch(type)
        {
            case Spades:
                
                //create new spades game
                spades = new Spades(engine);
                break;
                
            case Poker:
                
                //create new poker game
                poker = new Poker(engine);
                break;
        }
        
        //store the size of the screen
        //screen = new Rectangle(engine.getMain().getScreen());
    }
    
    /**
     * Get our card game object
     * @return CardGame object, if not found null is returned
     */
    public CardGame getCardGame()
    {
        switch(type)
        {
            case Spades:
                return this.spades;
                
            case Poker:
                return this.poker;
        }
        
        return null;
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        switch(type)
        {
            case Spades:
                spades.dispose();
                spades = null;
                break;
                
            case Poker:
                poker.dispose();
                poker = null;
                break;
        }
    }
    
    /**
     * Update all application elements
     * 
     * @param engine Our main game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        switch(type)
        {
            case Spades:
                spades.update(engine);
                break;
                
            case Poker:
                poker.update(engine);
                break;
                
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        switch(type)
        {
            case Spades:
                spades.render(graphics);
                break;
                
            case Poker:
                poker.render(graphics);
                break;
                
        }
    }
}
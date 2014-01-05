package com.gamesbykevin.casinogames.manager;

import com.gamesbykevin.casinogames.game.spades.Spades;
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
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //create new spades game
        spades = new Spades(engine);
        
        //store the size of the screen
        //screen = new Rectangle(engine.getMain().getScreen());
        
        //get the menu object
        final Menu menu = engine.getMenu();
        
        //get the index
        final int modeIndex = menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        
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
        if (spades != null)
            spades.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        if (spades != null)
            spades.render(graphics);
    }
}
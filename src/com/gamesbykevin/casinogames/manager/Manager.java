package com.gamesbykevin.casinogames.manager;

import com.gamesbykevin.framework.menu.Menu;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.menu.CustomMenu.*;
import com.gamesbykevin.casinogames.menu.option.*;
import com.gamesbykevin.casinogames.resources.*;
import com.gamesbykevin.casinogames.resources.GameImage.Keys;
import com.gamesbykevin.casinogames.shared.IElement;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Random;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IElement
{
    //seed used to generate random numbers
    private final long seed = System.nanoTime();
    
    //random number generator object
    private Random random = new Random(seed);
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        /*
        //store the size of the screen
        screen = new Rectangle(engine.getMain().getScreen());
        
        //get the background image
        background = engine.getResources().getGameImage(Keys.Background);
        
        //get the menu object
        final Menu menu = engine.getMenu();
        
        //get the index 
        final int modeIndex = menu.getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode);
        */
        
        System.out.println("Seed - " + seed);
    }
    
    /**
     * Get our object used to make random decisions
     * @return Random
     */
    public Random getRandom()
    {
        return this.random;
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        random = null;
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
        
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        
    }
}
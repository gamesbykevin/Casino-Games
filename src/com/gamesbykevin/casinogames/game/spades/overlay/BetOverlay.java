package com.gamesbykevin.casinogames.game.spades.overlay;

import com.gamesbykevin.framework.menu.Overlay;
import com.gamesbykevin.framework.resources.Disposable;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class BetOverlay extends Overlay implements Disposable
{
    public enum Keys
    {
        NIL, ONE, TWO, THREE, FOUR, 
        FIVE, SIX, SEVEN, EIGHT,
        NINE, TEN, ELEVEN, TWELVE, THIRTEEN
    }
    
    //the current key
    private Keys key;
    
    private static final int WIDTH = 65;
    private static final int HEIGHT = 69;
    
    public BetOverlay()
    {
        super();
        
        final int startX = 138;
        final int difference = WIDTH + 10;
        
        final int firstY = 110;
        
        //add boundaries
        add(Keys.NIL,       new Rectangle(startX + (difference * 0), firstY, WIDTH, HEIGHT));
        add(Keys.ONE,       new Rectangle(startX + (difference * 1), firstY, WIDTH, HEIGHT));
        add(Keys.TWO,       new Rectangle(startX + (difference * 2), firstY, WIDTH, HEIGHT));
        add(Keys.THREE,     new Rectangle(startX + (difference * 3), firstY, WIDTH, HEIGHT));
        add(Keys.FOUR,      new Rectangle(startX + (difference * 4), firstY, WIDTH, HEIGHT));
        add(Keys.FIVE,      new Rectangle(startX + (difference * 5), firstY, WIDTH, HEIGHT));
        add(Keys.SIX,       new Rectangle(startX + (difference * 6), firstY, WIDTH, HEIGHT));
        
        final int secondY = 185;
        
        add(Keys.SEVEN,     new Rectangle(startX + (difference * 0), secondY, WIDTH, HEIGHT));
        add(Keys.EIGHT,     new Rectangle(startX + (difference * 1), secondY, WIDTH, HEIGHT));
        add(Keys.NINE,      new Rectangle(startX + (difference * 2), secondY, WIDTH, HEIGHT));
        add(Keys.TEN,       new Rectangle(startX + (difference * 3), secondY, WIDTH, HEIGHT));
        add(Keys.ELEVEN,    new Rectangle(startX + (difference * 4), secondY, WIDTH, HEIGHT));
        add(Keys.TWELVE,    new Rectangle(startX + (difference * 5), secondY, WIDTH, HEIGHT));
        add(Keys.THIRTEEN,  new Rectangle(startX + (difference * 6), secondY, WIDTH, HEIGHT));
    }
    
    /**
     * Reset the bet being made
     */
    public void reset()
    {
        this.key = null;
    }
    
    /**
     * Set the key based on the location
     * @param point 
     */
    public void setKey(final Point point)
    {
        key = (Keys)get(point);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    public void render(final Graphics graphics)
    {
        super.draw(graphics);
        
        //highlight boundary mouse is hovering over
        if (key != null)
        {
            Rectangle r = super.get(key);
            
            if (r != null)
            {
                graphics.setColor(Color.RED);
                graphics.drawRoundRect(r.x, r.y, r.width, r.height, 20, 20);
            }
        }
    }
}
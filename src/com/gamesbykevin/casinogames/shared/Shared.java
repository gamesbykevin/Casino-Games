package com.gamesbykevin.casinogames.shared;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * This Shared class will have shared objects
 * 
 * @author GOD
 */
public class Shared 
{
    //these dimensions is the size of the game window the user will see
    public static final int INITIAL_WIDTH  = 775;
    public static final int INITIAL_HEIGHT = 480;
    
    //the game is originally programmed for these dimensions
    public static final int ORIGINAL_WIDTH  = 775;
    public static final int ORIGINAL_HEIGHT = 480;
    
    //show UPS/FPS counters
    public static final boolean DEBUG = false;
    
    //how many updates per second, controls speed of game
    public static final int DEFAULT_UPS = 90;
    
    //how many frames per second, controls how many images are rendered
    public static final int DEFAULT_FPS = 30;
    
    //what is the name of our game
    public static final String GAME_NAME = "casinogames";
    
    //blank cursor created here to hide the mouse cursor
    public static final Cursor CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
}
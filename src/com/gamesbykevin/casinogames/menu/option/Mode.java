package com.gamesbykevin.casinogames.menu.option;

import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.framework.resources.Audio;

/**
 * The setup of this specific option
 * @author GOD
 */
public final class Mode extends Option
{
    private static final String TITLE = "Game: ";
    
    public enum Types
    {
        //Play Spades
        Spades, 
        
        //Play Poker
        Poker,
    }
    
    public Mode(final Audio audio)
    {
        super(TITLE);
        
        for (Types types : Types.values())
        {
            super.add(types.toString(), audio);
        }
        
        //set default
        super.setIndex(1);
    }
}
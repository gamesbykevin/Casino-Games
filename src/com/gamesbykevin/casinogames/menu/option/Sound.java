package com.gamesbykevin.casinogames.menu.option;

import com.gamesbykevin.casinogames.menu.CustomMenu;
import com.gamesbykevin.framework.menu.Option;

import com.gamesbykevin.framework.resources.Audio;

/**
 * The setup of this specific option
 * @author GOD
 */
public final class Sound extends Option
{
    private static final String TITLE = "Sound: ";
    
    public Sound(final Audio audio)
    {
        super(TITLE);
        
        for (CustomMenu.Toggle toggle : CustomMenu.Toggle.values())
        {
            super.add(toggle.toString(), audio);
        }
        
        //default to on
        super.setIndex(1);
    }
}
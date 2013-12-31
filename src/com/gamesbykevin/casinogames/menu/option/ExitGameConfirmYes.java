package com.gamesbykevin.casinogames.menu.option;

import com.gamesbykevin.casinogames.menu.CustomMenu;
import com.gamesbykevin.framework.menu.Option;

/**
 * The setup of this specific option
 * @author GOD
 */
public final class ExitGameConfirmYes extends Option
{
    private static final String TITLE = "Yes";
    
    public ExitGameConfirmYes()
    {
        //when this option is selected it will go to another layer
        super(CustomMenu.LayerKey.MainTitle);
        
        super.add(TITLE, null);
    }
}
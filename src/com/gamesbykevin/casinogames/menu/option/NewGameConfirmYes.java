package com.gamesbykevin.casinogames.menu.option;

import com.gamesbykevin.casinogames.menu.CustomMenu;
import com.gamesbykevin.framework.menu.Option;

/**
 * The setup of this specific option
 * @author GOD
 */
public final class NewGameConfirmYes extends Option
{
    private static final String TITLE = "Yes";
    
    public NewGameConfirmYes()
    {
        //when this option is selected it will go to another layer
        super(CustomMenu.LayerKey.NewGameConfirmed);
        
        super.add(TITLE, null);
    }
}
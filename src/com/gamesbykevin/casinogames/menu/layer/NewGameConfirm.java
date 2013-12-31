package com.gamesbykevin.casinogames.menu.layer;

import com.gamesbykevin.framework.menu.Layer;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.menu.CustomMenu;
import com.gamesbykevin.casinogames.menu.option.*;

public final class NewGameConfirm extends Layer implements LayerRules
{
    public NewGameConfirm(final Engine engine) throws Exception
    {
        //the layer will have the given transition and screen size
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        //this layer will have a title at the top
        super.setTitle("Confirm New");
        
        //should we force the user to view this layer
        super.setForce(false);
        
        //when the layer is complete should we transition to the next or pause
        super.setPause(true);
        
        //since there are options how big should the container be
        super.setOptionContainerRatio(RATIO);
        
        //setup options here
        super.add(CustomMenu.OptionKey.NewGameConfim,   new NewGameConfirmYes());
        super.add(CustomMenu.OptionKey.NewGameDeny,     new NewGameConfirmNo());
    }
}
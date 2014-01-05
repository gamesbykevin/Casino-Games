package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.player.*;

import java.awt.Graphics;
import java.awt.Image;

public final class Guest extends Player implements IPlayer
{
    //the index of our selected card
    private int index = -1;
    
    public Guest()
    {
        super();
    }
    
    @Override
    public void update(final Engine engine)
    {
        if (hasCardSelected())
        {
            //if user has let go of mouse
            if (engine.getMouse().isMouseReleased())
            {
                //we are no longer selecting a card
                resetCardSelected();

                //reset mouse events
                engine.getMouse().reset();
            }

            //user dragged mouse so move card with it
            if (engine.getMouse().isMouseDragged())
            {
                final int x = (int)(engine.getMouse().getLocation().x - (getHand().get(getCardSelectedIndex()).getWidth() / 2));
                final int y = (int)(engine.getMouse().getLocation().y - (getHand().get(getCardSelectedIndex()).getHeight() / 2));

                getHand().get(getCardSelectedIndex()).setLocation(x, y);
            }
        }
        else
        {
            //has the mouse been pressed
            if (engine.getMouse().isMousePressed())
            {
                //set the selected card based on the mouse location
                setCardSelected(super.getHand().getIndex(engine.getMouse().getLocation()));
            }
        }
    }
    
    private int getCardSelectedIndex()
    {
        return this.index;
    }
    
    private void resetCardSelected()
    {
        setCardSelected(-1);
    }
    
    private void setCardSelected(final int index)
    {
        this.index = index;
    }
    
    /**
     * Does this player have a card selected
     * @return 
     */
    public boolean hasCardSelected()
    {
        return (index >= 0);
    }
    
    @Override
    public void render(final Graphics graphics, final Image image)
    {
        //draw the player's hand
        getHand().render(graphics, image);
    }
}

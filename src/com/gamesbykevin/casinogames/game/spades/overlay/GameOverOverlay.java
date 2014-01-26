package com.gamesbykevin.casinogames.game.spades.overlay;

import com.gamesbykevin.framework.menu.Overlay;
import com.gamesbykevin.framework.resources.Disposable;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameOverOverlay extends Overlay implements Disposable
{
    private String winner = null;
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    public void setWinnerName(final String winner)
    {
        this.winner = winner;
    }
    
    public String getWinnerName()
    {
        return this.winner;
    }
    
    public boolean hasWinnerName()
    {
        return (getWinnerName() != null);
    }
    
    public void render(final Graphics graphics)
    {
        super.draw(graphics);
        
        if (hasWinnerName())
        {
            graphics.setColor(Color.BLACK);
            graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, 48f));
            graphics.drawString(getWinnerName(), 225, 245);
            graphics.drawString("Hit \"Esc\" for menu.", 225, 245 + graphics.getFontMetrics().getHeight());
        }
    }
}
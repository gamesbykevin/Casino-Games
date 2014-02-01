package com.gamesbykevin.casinogames.game.crazyeights.overlay;

import com.gamesbykevin.framework.menu.Overlay;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ScoreOverlay extends Overlay implements Disposable
{
    //list of all the player's scores
    private List<ScoreDisplay> players;
    
    //our text display image
    private BufferedImage image;
    
    public ScoreOverlay()
    {
        this.players = new ArrayList<>();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        players.clear();
        players = null;
    }
    
    /**
     * Have we at least added at least 1 player stat
     * @return True for yes, otherwise false
     */
    public boolean hasPlayerStats()
    {
        return (players.size() > 0);
    }
    
    public void reset()
    {
        //remove all elements
        players.clear();
        
        //image has to be reset as well
        this.image = null;
    }
    
    /**
     * Add stats for a player to be displayed
     * @param name Player's name
     * @param score The score
     */
    public void addPlayerStat(final String name, final int score)
    {
        players.add(new ScoreDisplay(name, score));
        
        //sort score by lowest score first
        for (int index1 = 0; index1 < players.size(); index1++)
        {
            for (int index2 = 1; index2 < players.size(); index2++)
            {
                //if the previous score is greater than the current
                if(players.get(index2 - 1).score > players.get(index2).score)
                {
                    ScoreDisplay tmp = players.get(index2 - 1);
                    players.set(index2 - 1, players.get(index2));
                    players.set(index2, tmp);
                }
            }
        }
    }
    
    private class ScoreDisplay
    {
        private final String name;
        private final int score;
        
        private ScoreDisplay(final String name, final int score)
        {
            this.name  = name;
            this.score = score;
        }
    }
    
    public void render(final Graphics graphics)
    {
        //draw background image
        super.draw(graphics);
        
        //if image hasn't been created yet, do so now
        if (this.image == null)
        {
            //create new image of specified dimensions and transparent background
            this.image = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_INT_ARGB);
            
            //graphics object to write to image
            Graphics tmp = this.image.createGraphics();
            
            int startY = 155;

            for (ScoreDisplay info : players)
            {
                //set font size
                tmp.setFont(graphics.getFont().deriveFont(Font.BOLD, 24f));

                tmp.setColor(Color.BLACK);
                tmp.drawString(info.name      , 190, startY);
                tmp.drawString(info.score + "", 395, startY);

                startY += 50;
            }
        }
        
        super.draw(graphics, this.image);
    }
}
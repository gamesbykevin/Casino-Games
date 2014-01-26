package com.gamesbykevin.casinogames.game.spades.overlay;

import com.gamesbykevin.framework.menu.Overlay;
import com.gamesbykevin.framework.resources.Disposable;
import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ScoreOverlay extends Overlay implements Disposable
{
    private List<ScoreDisplay> players;
    
    //dark green color
    private static final Color DARK_GREEN = new Color(0, 100, 0);
    
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
     * Remove all stats
     */
    public void reset()
    {
        players.clear();
    }
    
    /**
     * Have we at least added at least 1 player stat
     * @return True for yes, otherwise false
     */
    public boolean hasPlayerStats()
    {
        return (players.size() > 0);
    }
    
    /**
     * Add stats for a player to be displayed
     * @param name Player's name
     * @param previousScore Their previous score
     * @param bet How many books they bet
     * @param win How many they actually won
     * @param bonus The bonus score added for this round
     * @param finalScore The overall final score after scoring
     */
    public void addPlayerStat(final String name, final int previousScore, final int bet, final int win, final int bonus, final int finalScore)
    {
        players.add(new ScoreDisplay(name, previousScore, bet, win, bonus, finalScore));
    }
    
    private class ScoreDisplay
    {
        private final String name;
        private final int previousScore, bet, win, bonus, finalScore;
        
        private ScoreDisplay(final String name, final int previousScore, final int bet, final int win, final int bonus, final int finalScore)
        {
            this.name = name;
            this.previousScore = previousScore;
            this.bet = bet;
            this.win = win;
            this.bonus = bonus;
            this.finalScore = finalScore;
        }
    }
    
    public void render(final Graphics graphics)
    {
        //draw image
        super.draw(graphics);
        
        int startY = 155;
        
        for (ScoreDisplay info : players)
        {
            //set font size
            graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, 24f));
            
            graphics.setColor(Color.BLACK);
            graphics.drawString(info.name,                50, startY);
            graphics.drawString(info.previousScore + "", 200, startY);
            graphics.drawString(info.bet + "",           320, startY);
            
            //default red color
            graphics.setColor(Color.RED);
            
            //if the wins exceed the bet
            if (info.win >= info.bet)
            {
                //NIL bid
                if (info.bet == 0)
                {
                    //make sure the bet and wins are both 0 to show green color
                    if (info.win == info.bet)
                        graphics.setColor(DARK_GREEN);
                }
                else
                {
                    //we met our goal so show green color
                    graphics.setColor(DARK_GREEN);
                }
            }
            
            if (info.win > info.bet || info.bet == 0 && info.win == info.bet)
                graphics.setColor(DARK_GREEN);
            
            //the wins/bonus/final-score will have the same color
            graphics.drawString(info.win + "",           400, startY);
            graphics.drawString(info.bonus + "",         495, startY);
            graphics.drawString(info.finalScore + "",    600, startY);
            
            startY += 50;
        }
    }
}
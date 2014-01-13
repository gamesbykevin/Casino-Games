package com.gamesbykevin.casinogames.game;

import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Graphics;
import java.util.Random;

public interface ICardGame extends Disposable
{
    /**
     * This method will assign a rank to each card's suit and face value.<br>
     * This will make it easier to sort or determine victory.
     */
    public void assignRank() throws Exception;
    
    /**
     * Each card game we will need to create the deck
     */
    public void createDeck() throws Exception;
    
    /**
     * Here is where the cards will be shuffled
     * @param random Object used to make random decisions
     */
    public void shuffleDeck(final Random random);
    
    /**
     * Update our game element accordingly
     * @param engine The Engine containing resources if needed
     * @throws Exception 
     */
    public void update(final Engine engine) throws Exception;
    
    /**
     * Determine the width/height for each card when displayed
     */
    public void setCardSize();
    
    
    /**
     * Determine how the cards will be dealt out to the player(s)
     */
    public void deal(final Random random);
    
    /**
     * Here we will determine whether the round/game has been completed
     */
    public void checkFinish();
    
    /**
     * Contains logic to draw the game
     * @param graphics 
     */
    public void render(final Graphics graphics);
}
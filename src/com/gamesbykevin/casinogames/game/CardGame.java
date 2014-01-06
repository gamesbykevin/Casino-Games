package com.gamesbykevin.casinogames.game;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.*;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.player.Player;
import com.gamesbykevin.casinogames.resources.GameImage.Keys;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public abstract class CardGame extends Sprite implements Disposable
{
    //our sprite sheet containing all card images
    private Image image;
    
    //deck of cards
    private Deck deck;
    
    //store the type of deck that will be used
    private final Keys type;
    
    //should we deal cards
    private boolean deal = true;
    
    //the player that has the current turn
    private int turnIndex = 0;
    
    //our object containing all players
    private List<Player> players;
    
    //this is a place holder for cards (example the central place where all players place their cards when playing spades)
    private List<Hand> cardDestinations;
    
    /**
     * Create a new card game
     * @param engine Our main game engine
     * @param players Number of players in this game
     * @throws Exception 
     */
    public CardGame(final Engine engine) throws Exception
    {
        //choose random key that will determine which deck is used
        type = Keys.values()[engine.getRandom().nextInt(Keys.values().length)];
        
        //store our image with all of the cards
        createDeck(engine.getResources().getGameImage(type));
        
        //create our player list
        this.players = new ArrayList<>();
        
        //create our card destination(s) list
        this.cardDestinations = new ArrayList<>();
    }
    
    public List<Hand> getCardDestinations()
    {
        return this.cardDestinations;
    }
    
    /**
     * Get the player that has been marked by the current turn index
     * @return Player marked as current
     */
    public Player getPlayer()
    {
        return getPlayer(getTurnIndex());
    }
    
    public Player getPlayer(final int index)
    {
        return players.get(index);
    }
    
    public List<Player> getPlayers()
    {
        return this.players;
    }
    
    /**
     * Get the index of the player who is currently being dealt
     * @return location in collection of List player(s)
     */
    private int getTurnIndex()
    {
        return this.turnIndex;
    }
    
    public void setTurnIndex(final int turnIndex)
    {
        this.turnIndex = turnIndex;
    }
    
    /**
     * Determine who is the next player to select going from one index to the next.
     * Once we reach the end we restart back at 0.
     */
    public void changeTurnIndex()
    {
        if (turnIndex < players.size() - 1)
        {
            setTurnIndex(getTurnIndex() + 1);
        }
        else
        {
            setTurnIndex(0);
        }
    }
    
    private void createDeck(final Image image) throws Exception
    {
        switch (type)
        {
            case Deck1:
                this.deck = new CustomDeck1();
                break;
                
            case Deck2:
                this.deck = new CustomDeck2();
                break;
                
            case Deck3:
                this.deck = new CustomDeck3();
                break;
                
            case Deck4:
                this.deck = new CustomDeck4();
                break;
                
            case Deck5:
                this.deck = new CustomDeck5();
                break;
                
            case Deck6:
                this.deck = new CustomDeck6();
                break;
                
            case Deck7:
                this.deck = new CustomDeck7();
                break;
                
            case Deck8:
                this.deck = new CustomDeck8();
                break;
                
            default:
                throw new Exception("Deck not found");
        }
        
        //store the card sprite sheet
        this.deck.setImage(image);
    }
    
    /**
     * Set the display width, height for the cards in the deck.<br>
     * We only need to pass the width because the height will be calculated by the ratio.
     * @param width Desired width of the cards
     */
    protected void setCardDimensions(final int width)
    {
        for (Card card : getDeck().getHand().getCards())
        {
            card.setDimensions(width, width * getDeck().getRatio());
        }
    }
    
    protected Deck getDeck()
    {
        return this.deck;
    }
    
    /**
     * Do we continue to deal cards.
     * @param deal True if we are to deal cards, otherwise false.
     */
    protected void setDeal(final boolean deal)
    {
        this.deal = deal;
    }
    
    protected boolean hasDeal()
    {
        return this.deal;
    }
    
    @Override
    public void dispose()
    {
        image.flush();
        image = null;
        
        deck.dispose();
        deck = null;
        
        for (Player player : getPlayers())
        {
            if (player != null)
                player.dispose();
            
            player = null;
        }
        
        this.players.clear();
        this.players = null;
        
        for (Hand hand : getCardDestinations())
        {
            if (hand != null)
                hand.dispose();
            
            hand = null;
        }
        
        this.cardDestinations.clear();
        this.cardDestinations = null;
    }
    
    public void render(final Graphics graphics)
    {
        //draw the deck first
        getDeck().render(graphics);
        
        //now draw all the players hands
        for (Player player : getPlayers())
        {
            if (player != null)
            {
                //draw the players hand
                player.render(graphics, getDeck().getImage());
            }
        }
        
        //finally draw the destination(s)
        for (Hand hand : getCardDestinations())
        {
            hand.render(graphics, image);
        }
    }
}
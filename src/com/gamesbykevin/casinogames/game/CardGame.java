package com.gamesbykevin.casinogames.game;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.casinogames.deck.*;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.menu.CustomMenu.LayerKey;
import com.gamesbykevin.casinogames.menu.CustomMenu.OptionKey;
import com.gamesbykevin.casinogames.player.Player;
import com.gamesbykevin.casinogames.resources.GameImage.Keys;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class CardGame extends Sprite implements Disposable
{
    //our sprite sheet containing all card images
    private Image image;
    
    //deck of cards
    private Deck deck;
    
    //store the type of deck that will be used
    private final Keys type;
    
    //this will determine where we are in the entire process
    private Object step;
    
    //the player that has the current turn
    //private int turnIndex = 0;
    
    //our object containing all players
    private List<Object> players;
    
    //this is a place holder for cards (example the central place where all players place their cards when playing spades)
    private LinkedHashMap<Object, Hand> cardDestinations;
    
    /**
     * Create a new card game
     * @param engine Our main game engine
     * @param players Number of players in this game
     * @throws Exception 
     */
    public CardGame(final Engine engine) throws Exception
    {
        final int deckSelection = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.DeckSelection);
        
        switch(deckSelection)
        {
            case 0:
                type = Keys.Deck1;
                break;
                
            case 1:
                type = Keys.Deck2;
                break;
                
            case 2:
                type = Keys.Deck3;
                break;
                
            case 3:
                type = Keys.Deck4;
                break;
                
            case 4:
                type = Keys.Deck5;
                break;
                
            case 5:
                type = Keys.Deck6;
                break;

            default:
                
                //add all deck image possibilities
                final List<Keys> keys = new ArrayList<>();
                keys.add(Keys.Deck1);
                keys.add(Keys.Deck2);
                keys.add(Keys.Deck3);
                keys.add(Keys.Deck4);
                keys.add(Keys.Deck5);
                keys.add(Keys.Deck6);
                
                //choose random key that will determine which deck is used
                type = keys.get(engine.getRandom().nextInt(keys.size()));
                break;
        }
        
        createDeck(engine.getResources().getGameImage(type));
        
        //create our player list
        this.players = new ArrayList<>();
        
        //create our card destination(s) list
        this.cardDestinations = new LinkedHashMap<Object, Hand>();
    }
    
    public void setStep(final Object step)
    {
        this.step = step;
    }
    
    public Object getStep()
    {
        return this.step;
    }
    
    public Hand getCardDestination(final Object key)
    {
        return this.cardDestinations.get(key);
    }
    
    public void addCardDestination(final Object key, final Hand hand)
    {
        this.cardDestinations.put(key, hand);
    }
    
    /**
     * Get the first player in the list that has a turn
     * @return Player that has turn, if none were found null is returned.
     */
    public Object getPlayer()
    {
        for (int index = 0; index < getPlayers().size(); index++)
        {
            if (((Player)getPlayer(index)).hasTurn())
                return getPlayers().get(index);
        }
        
        return null;
    }
    
    public Object getPlayer(final int index)
    {
        return (Player)players.get(index);
    }
    
    public List<Object> getPlayers()
    {
        return this.players;
    }
    
    public void setTurn(final Object player)
    {
        setTurn(((Player)player).getId());
    }
    
    /**
     * Sets the player with the matching playerId to have a turn
     * @param playerId Unique id of player
     */
    public void setTurn(final long playerId)
    {
        for (int index = 0; index < players.size(); index++)
        {
            Player player = (Player)getPlayer(index);
            
            if (player.getId() == playerId && !player.hasTurn())
            {
                player.switchTurn();
                break;
            }
        }
    }
    
    /**
     * Set all turns for each player to false
     */
    public void resetTurns()
    {
        for (int index = 0; index < players.size(); index++)
        {
            Player player = (Player)getPlayer(index);
            
            //if this is the player that has a turn
            if (player.hasTurn())
            {
                //switch turn off
                player.switchTurn();
            }
        }
    }
    
    /**
     * Locate the first player to have a turn, then set their turn to false.<br> 
     * Then give the next player in the list a turn.<br>
     * If the player is the last one then the turn will be the first player in the list.
     */
    public void changeTurn()
    {
        for (int index = 0; index < players.size(); index++)
        {
            Player player = (Player)getPlayer(index);
            
            //if this is the player that has a turn
            if (player.hasTurn())
            {
                //need to create new status image
                player.resetStatusImage();
                
                //switch turn
                player.switchTurn();
                
                Player nextPlayer;
                
                if (index == players.size() - 1)
                {
                    nextPlayer = ((Player)getPlayer(0));
                }
                else
                {
                    nextPlayer = ((Player)getPlayer(index + 1));
                }
                
                //need to create new status image
                nextPlayer.resetStatusImage();
                
                nextPlayer.switchTurn();
                
                break;
            }
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
    
    public Deck getDeck()
    {
        return this.deck;
    }
    
    @Override
    public void dispose()
    {
        if (image != null)
            image.flush();
        
        image = null;
        
        if (deck != null)
            deck.dispose();
        
        deck = null;
        
        players.clear();
        players = null;
        
        for (Hand hand : cardDestinations.values())
        {
            if (hand != null)
                hand.dispose();
            
            hand = null;
        }
        
        cardDestinations.clear();
        cardDestinations = null;
    }
    
    public void render(final Graphics graphics)
    {
        //finally draw the destination(s)
        for (Hand hand : cardDestinations.values())
        {
            hand.render(graphics, getDeck().getImage());
        }
        
        //draw the deck
        getDeck().render(graphics);
        
        for (Object player : getPlayers())
        {
            if (player != null)
            {
                //draw the players hand
                ((Player)player).render(graphics, getDeck().getImage());
            }
        }
    }
}
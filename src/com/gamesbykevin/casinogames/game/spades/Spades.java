package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.deck.Hand.CardDisplay;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.ICardGame;
import com.gamesbykevin.casinogames.game.spades.Guest;
import com.gamesbykevin.casinogames.player.Player;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Spades extends CardGame implements ICardGame
{
    //we only need the width because we calculate the height by the ratio
    private static final int CARD_WIDTH = 80;
    
    //the speed at which the cards can move
    private static final int PIXEL_SPEED_X = 15;
    private static final int PIXEL_SPEED_Y = 15;
    
    //where the deck will be located
    private static final Point DECK_LOCATION = new Point(260, 210);
    
    //each player is dealt x cards
    private static final int HAND_LIMIT = 13;
    
    public Spades(final Engine engine) throws Exception
    {
        super(engine);
        
        //add place for players to put their cards when playing
        super.getCardDestinations().add(new Hand());
        
        
        //set area where cards can be placed
        super.getCardDestinations().get(0).setLocation(250, 200);
        super.getCardDestinations().get(0).setDimensions(100, 100);
        
        //add our 4 players
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        
        //create card deck
        reset(engine.getRandom());
        
        //player 1, south and this will be our human
        super.getPlayer(0).getHand().setLocation(120, 400);
        super.getPlayer(0).getHand().setDisplay(CardDisplay.Horizontal);
        super.getPlayer(0).setHuman(true);
        
        //player 2, west
        super.getPlayer(1).getHand().setLocation(25, 100);
        super.getPlayer(1).getHand().setDisplay(CardDisplay.Vertical);
        
        //player 3, north
        super.getPlayer(2).getHand().setLocation(120, 20);
        super.getPlayer(2).getHand().setDisplay(CardDisplay.Horizontal);
        
        //player 4, east
        super.getPlayer(3).getHand().setLocation(500, 100);
        super.getPlayer(3).getHand().setDisplay(CardDisplay.Vertical);
    }
    
    /**
     * Create/Shuffle new deck.<br>
     * Do not reset the players just the card deck.
     * @param random Object used to make random decisions
     * @throws Exception 
     */
    private void reset(final Random random) throws Exception
    {
        //create deck
        createDeck();
        
        //shuffle it twice
        shuffleDeck(random);
        shuffleDeck(random);
        
        //set the dimensions for display purposes
        setCardSize();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    @Override
    public void createDeck() throws Exception
    {
        //set the location of the deck
        super.getDeck().setLocation(DECK_LOCATION);
        
        //create deck
        for (Card.Suit suit : Card.Suit.values())
        {
            for (Card.Value value : Card.Value.values())
            {
                //add card to deck
                super.getDeck().add(suit, value);
            }
        }
    }
    
    @Override
    public void shuffleDeck(final Random random)
    {
        //shuffle the deck
        super.getDeck().getHand().shuffle(random);
    }
    
    @Override
    public void setCardSize()
    {
        //set the size of the cards when displayed
        super.setCardDimensions(CARD_WIDTH);
        //super.setCardDimensions(super.getDeck().getSourceWidth());
    }
    
    @Override
    public void deal(final Random random)
    {
        //get the current player we are dealing to
        Player player = getPlayer();
        
        //if the player has an active card that has not yet reached the destination
        if (player.getHand().hasActiveCard())
        {
            player.getHand().moveActiveCard(PIXEL_SPEED_X, PIXEL_SPEED_Y);
            
            //if the card has now been placed
            if (!player.getHand().hasActiveCard())
                changeTurnIndex();
        }
        else
        {
            //this player has already been dealt the required amount of cards
            if (player.getHand().hasLimit(HAND_LIMIT))
            {
                changeTurnIndex();
            }
            else
            {
                //this player has not reached the limit yet so grab a card from the deck
                final Card card = getDeck().getHand().get(random);
                
                //mark the card as hidden
                card.getSpriteSheet().setCurrent(Card.State.Hidden);
                
                //if our player is human we will display the cards
                if (player.isHuman())
                    card.getSpriteSheet().setCurrent(Card.State.Display);

                //set the card to be placed where the player is
                card.setDestination(player.getHand().getDestination(CARD_WIDTH));

                //mark the card as belonging to the player
                card.setPlayerId(player.getId());
                
                //add card to player hand
                player.getHand().add(card);

                //remove card from deck
                getDeck().getHand().remove(card);
            }
        }
        
        //check if we are finished dealing cards to every player
        for (Player tmp : getPlayers())
        {
            //if the player reached the limit we are not finished dealing yet
            if (!tmp.getHand().hasLimit(HAND_LIMIT) || tmp.getHand().hasActiveCard())
            {
                //we are not finished yet
                super.setDeal(true);
                
                //don't continue
                return;
            }
        }
        
        //for now the first player will have the first turn
        super.setTurnIndex(0);
        
        //we made it to this point so dealing is finished
        super.setDeal(false);
    }
    
    @Override
    public void checkFinish()
    {
        
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if we are to deal cards
        if (hasDeal())
        {
            //deal cards out to players
            deal(engine.getRandom());
        }
        else
        {
            //get the player who has the current turn
            Guest guest = ((Guest)getPlayer());
            
            //does the player have a card selected
            boolean selected = guest.hasCardSelected();
            
            //update player
            guest.update(engine);
            
            //if the player does not have a card selected and previously did so lets see where it needs to be placed
            if (!guest.hasCardSelected() && selected)
            {
                final Card card = guest.getHand().getActiveCard();

                //if the active card equals the place card area then we can place the card
                if (super.getCardDestinations().get(0).getRectangle().contains(card.getCenter()))
                {
                    //mark the location of the card as the destination
                    card.setDestination(card.getPoint());
                    
                    //add card to destination
                    super.getCardDestinations().get(0).add(card);
                    
                    //remove card from the players hand
                    guest.getHand().remove(card);
                    
                    //no longer turn so change
                    super.changeTurnIndex();
                }
            }
            else
            {
                if (guest.getHand().hasActiveCard())
                    guest.getHand().moveActiveCard(PIXEL_SPEED_X, PIXEL_SPEED_Y);
            }
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw deck and player cards
        super.render(graphics);
    }
}
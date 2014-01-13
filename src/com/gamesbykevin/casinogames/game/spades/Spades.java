package com.gamesbykevin.casinogames.game.spades;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.deck.Hand.CardDisplay;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.ICardGame;
import com.gamesbykevin.casinogames.player.Player;
import com.gamesbykevin.casinogames.resources.GameImage;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Spades extends CardGame implements ICardGame
{
    //we only need the width because we calculate the height by the ratio
    private static final int CARD_WIDTH = 100;
    
    //the speed at which the cards move while dealing
    private static final int DEAL_PIXEL_SPEED_X = 100;
    private static final int DEAL_PIXEL_SPEED_Y = 100;
    
    //where the deck will be located
    private static final Point DECK_LOCATION = new Point(350, 195);
    
    //each player is dealt x cards
    private static final int HAND_LIMIT = 13;
    
    public Spades(final Engine engine) throws Exception
    {
        super(engine);
        
        //create area for players to place their cards
        final Hand destination = new Hand();
        
        //set area where cards can be placed
        destination.setLocation(250, 175);
        destination.setDimensions(300, 150);
        
        //set background image for destination
        destination.setImage(engine.getResources().getGameImage(GameImage.Keys.CardPlaceHolder));
        
        //add all background image possibilities
        final List<GameImage.Keys> keys = new ArrayList<>();
        keys.add(GameImage.Keys.Background1);
        keys.add(GameImage.Keys.Background2);
        keys.add(GameImage.Keys.Background3);
        keys.add(GameImage.Keys.Background4);
        keys.add(GameImage.Keys.Background5);
        keys.add(GameImage.Keys.Background6);
        
        //set the background image
        super.setImage(engine.getResources().getGameImage(keys.get(engine.getRandom().nextInt(keys.size()))));
        super.setDimensions(super.getImage().getWidth(null), super.getImage().getHeight(null));
        
        //add place for players to put their cards when playing
        super.getCardDestinations().add(destination);
        
        //add our 4 players
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        super.getPlayers().add(new Guest());
        
        //create card deck
        reset(engine.getRandom());
        
        //player 1, south and this will be our human
        super.getPlayer(0).getHand().setLocation(170, 375);
        super.getPlayer(0).getHand().setDisplay(CardDisplay.Horizontal);
        super.getPlayer(0).setHuman(true);
        
        //player 2, west
        super.getPlayer(1).getHand().setLocation(25, 25);
        super.getPlayer(1).getHand().setDisplay(CardDisplay.Vertical);
        
        //player 3, north
        super.getPlayer(2).getHand().setLocation(170, 20);
        super.getPlayer(2).getHand().setDisplay(CardDisplay.Horizontal);
        
        //player 4, east
        super.getPlayer(3).getHand().setLocation(675, 25);
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
    public void assignRank() throws Exception
    {
        for (Card card : getDeck().getHand().getCards())
        {
            switch (card.getSuit())
            {
                case Hearts:
                case Diamonds:
                case Clubs:
                    card.getSuit().setRank(0);
                    break;
                    
                //since we are playing spades the spades suit has the highest rank
                case Spades:
                    card.getSuit().setRank(1);
                    break;
                    
                default:
                    throw new Exception("Suit not found");
            }
            
            switch (card.getValue())
            {
                case Two:
                    card.getValue().setRank(0);
                    break;
                        
                case Three:
                    card.getValue().setRank(1);
                    break;
                        
                case Four:
                    card.getValue().setRank(2);
                    break;
                        
                case Five:
                    card.getValue().setRank(3);
                    break;
                        
                case Six:
                    card.getValue().setRank(4);
                    break;
                        
                case Seven:
                    card.getValue().setRank(5);
                    break;
                        
                case Eight:
                    card.getValue().setRank(6);
                    break;
                        
                case Nine:
                    card.getValue().setRank(7);
                    break;
                        
                case Ten:
                    card.getValue().setRank(8);
                    break;
                        
                case Jack:
                    card.getValue().setRank(9);
                    break;
                        
                case Queen:
                    card.getValue().setRank(10);
                    break;
                        
                case King:
                    card.getValue().setRank(11);
                    break;
                    
                case Ace:
                    card.getValue().setRank(12);
                    break;
                    
                default:
                    throw new Exception("Value not found");
            }
        }
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
        
        //now that deck is created assign rank to each card
        assignRank();
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
            player.getHand().moveActiveCard(DEAL_PIXEL_SPEED_X, DEAL_PIXEL_SPEED_Y);
            
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
                
                //set the display for the card
                card.setDisplay(player.getHand().getDisplay());
                
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
            //deal has finished next is to place bets
            
            
            
            //once bets are placed we can start game
            
            
            //after game finishes determine if there is a winner
            
            
            //if no winner a new deck needs to be created and continue until winning score is reached
            
            
            
            //update the player who has the current turn
            ((Guest)getPlayer()).update(engine);
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw the background
        super.draw(graphics);
        
        //draw deck and player cards
        super.render(graphics);
    }
}
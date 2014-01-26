package com.gamesbykevin.casinogames.game.poker;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.ICardGame;
import com.gamesbykevin.casinogames.game.spades.overlay.BetOverlay;
import com.gamesbykevin.casinogames.game.spades.overlay.GameOverOverlay;
import com.gamesbykevin.casinogames.game.spades.overlay.ScoreOverlay;
import com.gamesbykevin.casinogames.resources.GameAudio;
import com.gamesbykevin.casinogames.resources.GameImage;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Poker extends CardGame implements ICardGame
{
    //we only need the width because we calculate the height by the ratio
    private static final int CARD_WIDTH = 125;
    
    //the speed at which the cards move while dealing
    private static final int DEAL_PIXEL_SPEED_X = 50;
    private static final int DEAL_PIXEL_SPEED_Y = 50;
    
    //where the deck will be located
    private static final Point DECK_LOCATION = new Point(350, 195);
    
    //each player is dealt x cards
    private static final int HAND_LIMIT = 13;
    
    //steps to follow in this game
    private enum Steps
    {
        //deal cards to players
        Deal,
        
        //players now place their bet
        PlaceBet,
        
        //we play 1 hand
        Play,
        
        //check who won the hand
        CheckWin,
        
        //fly the cards towards the winner
        AnimateWin,
        
        //check if all cards have been played and then compare wins to bet
        CheckFinish,
        
        //display score and overall of each player
        DisplayScore,
        
        //check if the game has ended yet
        CheckGameOver,
        
        //here the game is officially over
        GameOver
    }
    
    public Poker(final Engine engine) throws Exception
    {
        super(engine);
        
        //play sound effect repeatively
        engine.getResources().playGameAudio(GameAudio.Keys.PlaceCard, true);
        
        //set the current step
        super.setStep(Poker.Steps.Deal);
        
        //create area for players to place their cards
        final Hand destination = new Hand();
        
        //set area where cards can be placed
        destination.setLocation(300, 175);
        destination.setDimensions(200, 150);
        
        //set background image for destination
        destination.setImage(engine.getResources().getGameImage(GameImage.Keys.CardPlaceHolder));
        
        /*
        //create bet overlay and set image/location/dimensions
        betOverlay = new BetOverlay();
        betOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesBetOverlay));
        betOverlay.setLocation(0, 0);
        betOverlay.setDimensions(engine.getMain().getScreen());
        
        //create score overlay and set image/location/dimensions
        scoreOverlay = new ScoreOverlay();
        scoreOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesScoreOverlay));
        scoreOverlay.setLocation(0, 0);
        scoreOverlay.setDimensions(engine.getMain().getScreen());
        
        //create game over screen
        gameOverOverlay = new GameOverOverlay();
        gameOverOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.SpadesGameOverOverlay));
        gameOverOverlay.setLocation(0, 0);
        gameOverOverlay.setDimensions(engine.getMain().getScreen());
        */
        //add all background image possibilities so we can pick a random one
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
        //super.getPlayers().add(new Guest("Human",    Guest.Position.South));
        //super.getPlayers().add(new Guest("Player 2", Guest.Position.West));
        //super.getPlayers().add(new Guest("Player 3", Guest.Position.North));
        //super.getPlayers().add(new Guest("Player 4", Guest.Position.East));
        
        //create card deck
        reset(engine.getRandom());
        
        //player 1, south and this will be our human
        Guest player1 = (Guest)super.getPlayer(0);
        player1.getHand().setLocation(135, 365);
        player1.getHand().setDisplay(Hand.CardDisplay.Horizontal);
        player1.setHuman(true);
        player1.setImage(engine.getResources().getGameImage(GameImage.Keys.HorizontalDisplay));
        player1.setLocation(250, 435);
        player1.setDimensions(298, 70);
        
        //player 2, west
        Guest player2 = (Guest)super.getPlayer(1);
        player2.getHand().setLocation(5, 0);
        player2.getHand().setDisplay(Hand.CardDisplay.Vertical);
        player2.setImage(engine.getResources().getGameImage(GameImage.Keys.VerticalDisplay));
        player2.setLocation(5, 55);
        player2.setDimensions(57, 297);
        
        //player 3, north
        Guest player3 = (Guest)super.getPlayer(2);
        player3.getHand().setLocation(135, 10);
        player3.getHand().setDisplay(Hand.CardDisplay.Horizontal);
        player3.setImage(engine.getResources().getGameImage(GameImage.Keys.HorizontalDisplay));
        player3.setLocation(250, 0);
        player3.setDimensions(298, 70);
        
        //player 4, east
        Guest player4 = (Guest)super.getPlayer(3);
        player4.getHand().setLocation(675, 0);
        player4.getHand().setDisplay(Hand.CardDisplay.Vertical);
        player4.setImage(engine.getResources().getGameImage(GameImage.Keys.VerticalDisplay));
        player4.setLocation(740, 55);
        player4.setDimensions(57, 297);
        
        //reset all turns
        super.resetTurns();
        
        //for now the first player will have the first turn
        super.setTurn(player1.getId());
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
                    card.getSuit().setRank(1);
                    break;
                    
                //since we are playing spades the spades suit has the highest rank
                case Spades:
                    card.getSuit().setRank(2);
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
    }
    
    @Override
    public void deal(final Engine engine)
    {
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        switch((Poker.Steps)getStep())
        {
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
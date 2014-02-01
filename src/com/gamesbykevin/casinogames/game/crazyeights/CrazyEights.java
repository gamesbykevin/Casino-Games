package com.gamesbykevin.casinogames.game.crazyeights;

import com.gamesbykevin.casinogames.deck.Card;
import com.gamesbykevin.casinogames.deck.Hand;
import com.gamesbykevin.casinogames.engine.Engine;
import com.gamesbykevin.casinogames.game.CardGame;
import com.gamesbykevin.casinogames.game.ICardGame;
import com.gamesbykevin.casinogames.game.crazyeights.overlay.ScoreOverlay;
import com.gamesbykevin.casinogames.resources.GameAudio;
import com.gamesbykevin.casinogames.resources.GameImage;
import com.gamesbykevin.casinogames.resources.Resources;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Here is the engine for Crazy Eights
 * @author GOD
 */
public class CrazyEights extends CardGame implements ICardGame
{
    //we only need the width because we calculate the height by the ratio
    protected static final int CARD_WIDTH = 125;
    
    //the speed at which the cards move while dealing
    private static final int DEAL_PIXEL_SPEED_X = 30;
    private static final int DEAL_PIXEL_SPEED_Y = 30;
    
    //where the deck will be located
    private static final Point DECK_LOCATION = new Point(225, 195);
    
    //each player is dealt x cards
    private static final int HAND_LIMIT = 7;
    
    //this is the wild card
    protected static final Card.Value WILDCARD = Card.Value.Eight;
    
    //the overlay for displaying the score
    private ScoreOverlay scoreOverlay;
    
    protected enum Destinations
    {
        DeckSource,
        CardPlace
    }
    
    //steps to follow in this game
    private enum Steps
    {
        //deal cards to players
        Deal,
        
        //draw initial card onto destination
        DrawInitialCard,
        
        //play the game
        Play,
        
        //check if the game has ended yet
        CheckGameOver,
        
        //if the game is over we will calculate score to determine 1st, 2nd, 3rd place etc....
        CalculateScore,
        
        //display score overall for each player
        DisplayScore,
        
        //the last step no going back here
        Finish,
    }
    
    public CrazyEights(final Engine engine) throws Exception
    {
        super(engine);
        
        //create where cards are dealt
        final Hand deckSourceDestination = new Hand();
        
        //set area where cards can be placed
        deckSourceDestination.setLocation(200, 175);
        deckSourceDestination.setDimensions(175, 160);
        
        //set background image for destination
        deckSourceDestination.setImage(engine.getResources().getGameImage(GameImage.Keys.CardPlaceHolder));
        
        //add place for players to put their cards when playing
        super.addCardDestination(Destinations.DeckSource, deckSourceDestination);
        
        //create area for players to place their cards
        final Hand deckPlacedDestination = new Hand();
        
        //set area where cards can be placed
        deckPlacedDestination.setLocation(400, 175);
        deckPlacedDestination.setDimensions(175, 160);
        
        //set background image for destination
        deckPlacedDestination.setImage(engine.getResources().getGameImage(GameImage.Keys.CardPlaceHolder));
        
        //add destination
        super.addCardDestination(Destinations.CardPlace, deckPlacedDestination);
        
        //create score overlay and set image/location/dimensions
        scoreOverlay = new ScoreOverlay();
        scoreOverlay.setImage(engine.getResources().getGameImage(GameImage.Keys.CrazyEightsScoreOverlay));
        scoreOverlay.setLocation(0, 0);
        scoreOverlay.setDimensions(engine.getMain().getScreen());
        
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
        
        //add our players
        super.getPlayers().add(new Guest("Human")   );
        super.getPlayers().add(new Guest("Player 2"));
        super.getPlayers().add(new Guest("Player 3"));
        super.getPlayers().add(new Guest("Player 4"));
        
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
        player2.getHand().setLocation(5, 10);
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
        player4.getHand().setLocation(675, 10);
        player4.getHand().setDisplay(Hand.CardDisplay.Vertical);
        player4.setImage(engine.getResources().getGameImage(GameImage.Keys.VerticalDisplay));
        player4.setLocation(740, 55);
        player4.setDimensions(57, 297);
        
        //reset game
        reset(engine.getRandom(), engine.getResources());
    }
    
    /**
     * Create/Shuffle new deck.<br>
     * Do not reset the players just the card deck.
     * @param random Object used to make random decisions
     * @throws Exception 
     */
    private void reset(final Random random, final Resources resources) throws Exception
    {
        //play sound effect repeatively
        resources.playGameAudio(GameAudio.Keys.PlaceCard, true);
        
        //set the first step
        super.setStep(CrazyEights.Steps.Deal);
        
        //if deck already exists reset deck
        if (getDeck() != null)
            getDeck().getHand().getCards().clear();
        
        //reset all turns
        super.resetTurns();
        
        //random player will start first
        super.setTurn(getPlayer(random.nextInt(super.getPlayers().size())));
        
        //remove all cards from each player
        for (Object obj : getPlayers())
        {
            //get the player
            Guest player = ((Guest)obj);
            
            //remove all cards in hand
            player.getHand().getCards().clear();
            
            //reset score
            player.setScore(0);
        }
        
        //remove all cards from all destinations
        for (Destinations key : Destinations.values())
        {
            super.getCardDestination(key).getCards().clear();
        }
        
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
        
        scoreOverlay.dispose();
        scoreOverlay = null;
    }
    
    @Override
    public void assignRank() throws Exception
    {
        //here the rank will be the score for each card
        for (Card card : getDeck().getHand().getCards())
        {
            switch (card.getValue())
            {
                case Ace:
                    card.getValue().setRank(1);
                    break;
                    
                case Two:
                    card.getValue().setRank(2);
                    break;
                    
                case Three:
                    card.getValue().setRank(3);
                    break;
                    
                case Four:
                    card.getValue().setRank(4);
                    break;
                    
                case Five:
                    card.getValue().setRank(5);
                    break;
                    
                case Six:
                    card.getValue().setRank(6);
                    break;
                    
                case Seven:
                    card.getValue().setRank(7);
                    break;
                    
                case Eight:
                    card.getValue().setRank(50);
                    break;
                    
                case Nine:
                    card.getValue().setRank(9);
                    break;
                    
                case Ten:
                case Jack:
                case Queen:
                case King:
                    card.getValue().setRank(10);
                    break;
                    
                default:
                    throw new Exception("Value needs to be set here");
            }
        }
    }
    
    @Override
    public void createDeck() throws Exception
    {
        //set the location of the deck
        super.getDeck().setLocation(DECK_LOCATION);
        
        //create 2 decks
        for (int count=0; count < 2; count++)
        {
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
        //get the current player we are dealing to
        Guest player = (Guest)getPlayer();
        
        //if the player has an active card that has not yet reached the destination
        if (player.getHand().hasActiveCard())
        {
            player.getHand().moveActiveCard(DEAL_PIXEL_SPEED_X, DEAL_PIXEL_SPEED_Y);
            
            //if the card has now been placed
            if (!player.getHand().hasActiveCard())
            {
                //switch to next players turn
                changeTurn();
            }
        }
        else
        {
            //this player has already been dealt the required amount of cards
            if (player.getHand().hasLimit(HAND_LIMIT))
            {
                changeTurn();
            }
            else
            {
                //this player has not reached the limit yet so grab a card from the deck
                final Card card = getDeck().getHand().get(engine.getRandom());
                
                //mark the card as hidden
                card.setState(Card.State.Hidden);
                
                //if our player is human we will display the cards
                if (player.isHuman())
                    card.setState(Card.State.Display);

                //determine the position of the card
                final int size = player.getHand().getSize();
                
                //set the card to be placed where the player is
                card.setDestination(player.getHand().getDestination(size, CARD_WIDTH));
                
                //add card to player hand
                player.addHand(card);

                //remove card from deck
                getDeck().getHand().remove(card);
            }
        }
        
        //check if we are finished dealing cards to every player
        for (Object obj : getPlayers())
        {
            Guest tmp = (Guest)obj;
            
            //if the player reached the limit we are not finished dealing yet
            if (!tmp.getHand().hasLimit(HAND_LIMIT) || tmp.getHand().hasActiveCard())
                return;
        }
        
        //stop sound effect
        engine.getResources().stopGameAudio(GameAudio.Keys.PlaceCard);
        
        //we are done dealing, next step
        super.setStep(Steps.DrawInitialCard);
    }
    
    /**
     * Take card from top of deck and place in initial place
     */
    private void drawInitialCard()
    {
        //get the destination where the cards are to be placed
        final Hand destination = super.getCardDestination(Destinations.CardPlace);
        
        //if there are 0 cards in our destination
        if (destination.getSize() == 0)
        {
            //get the number of cards in the hand
            int size = getDeck().getHand().getSize() - 1;
            
            //continue until a card that is not an eight is found
            while(true)
            {
                //if the card is an eight go to next one
                if (getDeck().getHand().get(size).getValue() == WILDCARD)
                {
                    size--;
                }
                else
                {
                    break;
                }
            }
            
            //get our card that we will add to the other destination
            final Card card = getDeck().getHand().get(size);
            
            //show card to display
            card.setState(Card.State.Display);
            
            //set the new destination
            card.setDestination(destination.getCenter().x - (card.getWidth() / 2), destination.getCenter().y - (card.getHeight() / 2));
            
            //add card to player hand
            destination.add(card);

            //remove card from deck
            getDeck().getHand().remove(card);
        }
        else
        {
            if (destination.hasActiveCard())
            {
                destination.moveActiveCard(Guest.PLACE_PIXEL_SPEED_X, Guest.PLACE_PIXEL_SPEED_Y);
            }
            else
            {
                super.setStep(Steps.Play);
            }
        }
    }
    
    /**
     * Determine if the game has ended
     */
    private void checkGameOver()
    {
        boolean gameOver = false;
        
        //check if any of the players have used all of their cards
        for (Object obj : getPlayers())
        {
            Guest player = (Guest)obj;
            
            //if player has used all of their cards then the game is over
            if (player.getHand().getSize() == 0)
            {
                gameOver = true;
                break;
            }
        }
        
        //if the game is still not over
        if (!gameOver)
        {
            //if the deck is empty then the game has ended
            if (super.getDeck().getHand().getSize() == 0)
                gameOver = true;
        }
        
        if (!gameOver)
        {
            //the game is not over so go back to play step
            super.setStep(Steps.Play);
        }
        else
        {
            //game ended now determine each player's score
            super.setStep(Steps.CalculateScore);
        }
    }
    
    /**
     * Calculate each players score and determine who came in 1st, 2nd, etc...
     */
    private void calculateScore()
    {
        //reset score elements and image
        scoreOverlay.reset(); 
            
        //calculate for every player
        for (Object obj : getPlayers())
        {
            Guest player = (Guest)obj;
            
            //calculate the score
            player.calculateScore();
        }
        
        super.setStep(Steps.DisplayScore);
    }
    
    /**
     * Setup the display score overlay
     */
    private void displayScore()
    {
        //reset elements
        scoreOverlay.reset();
        
        for (Object obj : getPlayers())
        {
            Guest player = (Guest)obj;
            
            scoreOverlay.addPlayerStat(player.getName(), player.getScore());
        }
        
        //go to last step
        super.setStep(Steps.Finish);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        switch((Steps)getStep())
        {
            case Deal:
                
                //deal cards
                deal(engine);
                break;
                
            case DrawInitialCard:
                
                //put initial card
                drawInitialCard();
                break;
                
            case Play:
                
                Guest player = (Guest)getPlayer();
                //update the current player that has a turn
                player.update(engine);
                
                //if the player has finished their turn
                if (!player.hasTurn())
                {
                    //now check to see if the game is over
                    super.setStep(Steps.CheckGameOver);
                }
                break;
                
            case CheckGameOver:
                
                //check if the game has ended
                checkGameOver();
                break;
                
            case CalculateScore:
                
                //determine the places of all players
                calculateScore();
                break;
                
            case DisplayScore:
                
                //create the display score overlay
                displayScore();
                
                //play sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.ShowScoreSound);
                
                //reset mouse events
                engine.getMouse().reset();
                break;
                
            case Finish:
                
                //if mouse is pressed reset game
                if (engine.getMouse().isMousePressed())
                {
                    //reset mouse events
                    engine.getMouse().reset();
                    
                    //reset game
                    reset(engine.getRandom(), engine.getResources());
                }
                
                break;
        }
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw the background
        super.draw(graphics);
        
        //draw deck and player cards
        super.render(graphics);
        
        //if on the last step draw the game over score overlay
        if ((Steps)super.getStep() == Steps.Finish)
        {
            //draw score
            this.scoreOverlay.render(graphics);
        }
    }
}